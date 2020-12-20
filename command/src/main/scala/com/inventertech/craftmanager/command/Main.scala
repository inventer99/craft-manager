package com.inventertech.craftmanager.command

import ackcord._
import ackcord.cachehandlers.CacheTypeRegistry
import ackcord.commands.CommandConnector
import ackcord.gateway.{GatewayEvent, GatewaySettings}
import ackcord.requests.{BotAuthentication, Ratelimiter, Requests}
import akka.actor.typed._
import akka.actor.typed.scaladsl._
import akka.stream.{KillSwitches, SharedKillSwitch}
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory

import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, ExecutionContext}
import scala.util.control.NonFatal

object Main {
  private val log = LoggerFactory.getLogger(Main.getClass)

  implicit val system: ActorSystem[Nothing] = ActorSystem(Behaviors.ignore, "CraftManager")
  implicit val executionContext: ExecutionContext = system.executionContext

  def main(args: Array[String]): Unit = {
    val token = sys.env.get("DISCORD_TOKEN") match {
      case Some(token) => token
      case None =>
        log.error("No Discord auth token provided. (Env DISCORD_TOKEN)")
        return
    }

    val settings = GatewaySettings(token = token)
    ActorSystem(Behaviors.setup[CraftManager.Command](ctx => new CraftManager(ctx, settings)), "CraftManager", ConfigFactory.load())
  }
}

object CraftManager {
  private val log = LoggerFactory.getLogger(CraftManager.getClass)

  sealed trait Command
}

class CraftManager(context: ActorContext[CraftManager.Command], settings: GatewaySettings) extends AbstractBehavior[CraftManager.Command](context) {
  import CraftManager._

  implicit val system: ActorSystem[Nothing] = context.system

  private val events = Events.create(
    ignoredEvents = Seq(
      classOf[GatewayEvent.PresenceUpdate],
      classOf[GatewayEvent.TypingStart]
    ),
    cacheTypeRegistry = CacheTypeRegistry.noPresences
  )

  private val wsUri =
    try {
      Await.result(DiscordShard.fetchWsGateway, 30.seconds)
    } catch {
      case NonFatal(e) =>
        log.error("Could not connect to Discord")
        throw e
    }

  private val shard       = context.spawn(DiscordShard(wsUri, settings, events), "DiscordShard")
  private val ratelimiter = context.spawn(Ratelimiter(), "Ratelimiter")

  private val requests: Requests =
    new Requests(
      BotAuthentication(settings.token),
      ratelimiter,
      relativeTime = true
    )

  val killSwitch: SharedKillSwitch = KillSwitches.shared("Commands")

  val commandConnector = new CommandConnector(
    events.subscribeAPI
      .collectType[APIMessage.MessageCreate]
      .map(m => m.message -> m.cache.current)
      .via(killSwitch.flow),
    requests,
    requests.parallelism
  )

  log.info("Registering commands")

  implicit val timeout: Timeout = 30.seconds

  val pingCommands = new PingCommands(requests)
  val processCommands = new ProcessCommands(requests)

  commandConnector.bulkRunNamed(
    pingCommands.ping,
    processCommands.start,
    processCommands.stop,
    processCommands.status
  )
  log.info("Registering commands DONE")

  shard ! DiscordShard.StartShard
  log.info("Ready!")

  override def onMessage(msg: CraftManager.Command): Behavior[CraftManager.Command] = this
}