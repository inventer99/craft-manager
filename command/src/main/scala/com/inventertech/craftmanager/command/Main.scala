package com.inventertech.craftmanager.command

import ackcord.{APIMessage, ClientSettings}
import org.slf4j.LoggerFactory

//import akka.actor.typed.ActorSystem

object Main {
  private val log = LoggerFactory.getLogger(Main.getClass)

  def main(args: Array[String]): Unit = {
    val token = sys.env.get("DISCORD_TOKEN") match {
      case Some(token) => token
      case None =>
        log.error("No Discord auth token provided. (Env DISCORD_TOKEN)")
        return
    }

    val settings = ClientSettings(token)
    import settings.executionContext

    settings
      .createClient()
      .foreach { client =>
        client.onEventSideEffectsIgnore {
          case APIMessage.Ready(_) =>
            log.info("Registering commands")
            val pingCommands = new PingCommands(client.requests)
            val processCommands = new ProcessCommands(client.requests)
            client.commands.bulkRunNamed(
              pingCommands.ping,
              processCommands.start,
              processCommands.stop,
              processCommands.status
            )
            log.info("Ready")
        }

        client.login()
      }
  }
}