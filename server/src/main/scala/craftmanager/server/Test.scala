package craftmanager.server

import akka.actor.typed.scaladsl.AskPattern.Askable
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, ActorSystem, Behavior, Scheduler}
import akka.util.Timeout
import org.slf4j.LoggerFactory

import scala.concurrent.duration.DurationInt

object Process {
  sealed trait Command
  final case class Start(replyTo: ActorRef[Response]) extends Command
  final case class Stop(replyTo: ActorRef[Response]) extends Command

  sealed trait Response
  final case class Success() extends Response
  final case class Failure(error: Any) extends Response

  def apply(): Behavior[Command] =
    Behaviors.receive { (context, message) =>
      message match {
        case Start(replyTo) =>
          replyTo ! Success()
          Behaviors.same
        case Stop(replyTo) =>
          replyTo ! Success()
          Behaviors.same
      }
    }
}

object CraftManagerServer {
  private val log = LoggerFactory.getLogger(CraftManagerServer.getClass)

  final case class Log(message: String)

  def apply(): Behavior[Log] =
    Behaviors.setup { context =>
      import context.executionContext
      implicit val scheduler: Scheduler = context.system.scheduler
      implicit val timeout: Timeout = 3.seconds

      val process = context.spawn(Process(), "Process")

      for(
        result <- process ? (replyTo => Process.Start(replyTo))
      ) yield result match {
        case Process.Success() => Log("Started process")
        case Process.Failure(error) => Log(s"Failed to start process: $error")
      }

      for(
        result <- process ? (replyTo => Process.Stop(replyTo))
      ) yield result match {
        case Process.Success() => Log("Stopped process")
        case Process.Failure(error) => Log(s"Failed to stop process: $error")
      }

      Behaviors.receiveMessage {
        case Log(message) =>
          log.info(message)
          Behaviors.same
      }
    }
}

object Test {
  def main(args: Array[String]): Unit = {
    ActorSystem(CraftManagerServer(), "CraftManager")
  }
}
