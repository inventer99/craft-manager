package craftmanager.server

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import com.typesafe.config.ConfigFactory

import scala.concurrent.ExecutionContext

object Main {
  implicit val system: ActorSystem[Nothing] = ActorSystem(Behaviors.ignore, "CraftManager")
  implicit val executionContext: ExecutionContext = system.executionContext

  def main(args: Array[String]): Unit = {
    val config = ConfigFactory.load()

    system.systemActorOf(Process(), "Process")
  }
}
