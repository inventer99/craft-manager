package craftmanager.server

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import org.slf4j.LoggerFactory

import scala.concurrent.ExecutionContext

object Main {
  private val log = LoggerFactory.getLogger(Main.getClass)

  implicit val system: ActorSystem[Nothing] = ActorSystem(Behaviors.ignore, "CraftManager")
  implicit val executionContext: ExecutionContext = system.executionContext

  def main(args: Array[String]): Unit = {
    val name = sys.env.get("NAME") match {
      case Some(value) => value
      case None =>
        log.error("No name provided. (Env: NAME)")
        return
    }

    //        val minecraftIp = sys.env.get("MINECRAFT_IP") match {
    //            case Some(value) => value
    //            case None =>
    //                log.error("No Minecraft IP provided. (Env: MINECRAFT_IP)")
    //                return
    //        }
    //
    //        val minecraftRconPort = sys.env.get("MINECRAFT_RCON_PORT") match {
    //            case Some(value) => value
    //            case None =>
    //                log.error("No Minecraft RCON port provided. (Env: MINECRAFT_RCON_PORT)")
    //                return
    //        }

    system.systemActorOf(ServerProcess(name), "ServerProcess")
  }
}
