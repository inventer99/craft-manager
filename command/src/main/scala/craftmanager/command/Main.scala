package craftmanager.command

import ackcord.cachehandlers.CacheTypeRegistry
import ackcord.commands.CommandConnector
import ackcord.gateway.{GatewayEvent, GatewaySettings}
import ackcord.requests.{BotAuthentication, Ratelimiter, Requests}
import ackcord.{DiscordShard, _}
import akka.actor.typed._
import akka.actor.typed.scaladsl._
import akka.http.scaladsl.model.Uri
import akka.stream.{KillSwitches, SharedKillSwitch}
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.StrictLogging
import org.slf4j.LoggerFactory

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt
import scala.util.control.NonFatal

object Main {
  private val log = LoggerFactory.getLogger(Main.getClass)

  def main(args: Array[String]): Unit = {
    val token = sys.env.get("DISCORD_TOKEN") match {
      case Some(token) => token
      case None =>
        log.error("No Discord auth token provided. (Env DISCORD_TOKEN)")
        return
    }

    val settings = GatewaySettings(token = token)
    ActorSystem(CraftManager(settings), "CraftManager", ConfigFactory.load())
  }
}

object CraftManager {
  sealed trait Command

  def apply(settings: GatewaySettings): Behavior[Command] =
    Behaviors.setup[Command](ctx => new CraftManager(ctx, settings))
}

class CraftManager(context: ActorContext[CraftManager.Command], settings: GatewaySettings) extends AbstractBehavior[CraftManager.Command](context) with StrictLogging {
  implicit val system: ActorSystem[Nothing] = context.system
  implicit val timeout: Timeout = 30.seconds

  private val events =
    Events.create(
      ignoredEvents = Seq(
        classOf[GatewayEvent.PresenceUpdate],
        classOf[GatewayEvent.TypingStart]
      ),
      cacheTypeRegistry = CacheTypeRegistry.noPresences
    )

  private val shard       = context.spawn(DiscordShard(websocketGatewayUri, settings, events), "DiscordShard")
  private val ratelimiter = context.spawn(Ratelimiter(), "Ratelimiter")

  private val requests: Requests =
    new Requests(
      BotAuthentication(settings.token),
      ratelimiter,
      relativeTime = true
    )

  val killSwitch: SharedKillSwitch =
    KillSwitches.shared("Commands")

  val commandConnector: CommandConnector =
    new CommandConnector(
      events.subscribeAPI
        .collectType[APIMessage.MessageCreate]
        .map(m => m.message -> m.cache.current)
        .via(killSwitch.flow),
      requests,
      requests.parallelism
    )

  registerCommands()

  shard ! DiscordShard.StartShard
  logger.info("Ready!")

  override def onMessage(msg: CraftManager.Command): Behavior[CraftManager.Command] = this

  private def websocketGatewayUri: Uri =
    try {
      Await.result(DiscordShard.fetchWsGateway, timeout.duration)
    } catch {
      case NonFatal(e) =>
        logger.error("Could not connect to Discord Gateway", e)
        throw e;
    }

  private def registerCommands(): Unit = {
    logger.info("Registering commands")

    val helpCommand     = new CraftManagerHelpCommand(requests)
    val testCommands    = new TestCommands(requests)
    val processCommands = new ProcessCommands(requests)

    commandConnector.bulkRunNamedWithHelp(
      helpCommand,
      testCommands.commands ++
      processCommands.commands
      : _*
    )
    logger.info("Commands registered")
  }
}