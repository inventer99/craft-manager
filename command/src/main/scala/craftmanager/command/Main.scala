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
import com.typesafe.config.{Config, ConfigFactory}
import com.typesafe.scalalogging.Logger

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt
import scala.util.control.NonFatal

object BotBehavior {
  private val logger = Logger(BotBehavior.getClass)

  sealed trait Command

  def apply(config: Config, settings: GatewaySettings): Behavior[Command] =
    Behaviors.setup[Command] { context =>
      implicit val system: ActorSystem[Nothing] = context.system
      implicit val timeout: Timeout = 30.seconds

      val events =
        Events.create(
          ignoredEvents = Seq(
            classOf[GatewayEvent.PresenceUpdate],
            classOf[GatewayEvent.TypingStart]
          ),
          cacheTypeRegistry = CacheTypeRegistry.noPresences
        )

      val websocketGatewayUri: Uri =
        try {
          Await.result(DiscordShard.fetchWsGateway, timeout.duration)
        } catch {
          case NonFatal(e) =>
            logger.error("Could not connect to Discord Gateway", e)
            throw e;
        }

      val shard       = context.spawn(DiscordShard(websocketGatewayUri, settings, events), "DiscordShard")
      val ratelimiter = context.spawn(Ratelimiter(), "Ratelimiter")

      val requests: Requests =
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

      shard ! DiscordShard.StartShard
      logger.info("Ready!")

      Behaviors.receiveMessage { _ =>
        Behaviors.same
      }
    }
}

object Main {
  private val logger = Logger(Main.getClass)

  def main(args: Array[String]): Unit = {
    val config = ConfigFactory.load()

    val token = sys.env.get("DISCORD_TOKEN") match {
      case Some(token) => token
      case None =>
        logger.error("No Discord auth token provided. (Env DISCORD_TOKEN)")
        return
    }

    val settings = GatewaySettings(token = token)
    ActorSystem(BotBehavior(config, settings), "CraftManager", config)
  }
}
