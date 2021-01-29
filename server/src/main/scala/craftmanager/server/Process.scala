package craftmanager.server

import akka.actor.typed.Behavior
import akka.actor.typed.receptionist.{Receptionist, ServiceKey}
import akka.actor.typed.scaladsl.Behaviors
import craftmanager.shared.Stopped

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

object Process {
  import craftmanager.shared.ProcessProtocol._

  val ProcessServiceKey: ServiceKey[Command] = ServiceKey("Process")

  def apply(): Behavior[Command] =
    Behaviors.setup { context =>
      context.system.receptionist ! Receptionist.register(ProcessServiceKey, context.self)
//      processHandler()
    }

  private def processHandler(processService: ProcessService)(implicit ec: ExecutionContext): Behavior[Command] =
    Behaviors.receiveMessage[Command] {
      case StartProcess(replyTo) =>
        processService.start().onComplete {
          case Success(_) =>
            replyTo ! StartProcessSucceeded()
          case Failure(error) =>
            replyTo ! StartProcessFailed(error.getMessage)
        }
        Behaviors.same
      case StopProcess(force, replyTo) =>
        processService.stop(force).onComplete {
          case Success(_) =>
            replyTo ! StopProcessSucceeded()
          case Failure(error) =>
            replyTo ! StopProcessFailed(error.getMessage)
        }
        Behaviors.same
      case RestartProcess(replyTo) =>
        processService.restart().onComplete {
          case Success(_) =>
            replyTo ! RestartProcessSucceeded()
          case Failure(error) =>
            replyTo ! RestartProcessFailed(error.getMessage)
        }
        Behaviors.same
      case InquireProcessStatus(replyTo) =>
        processService.status().onComplete {
          case Success(_) =>
            replyTo ! InquireProcessStatusSucceeded(Stopped)
          case Failure(error) =>
            replyTo ! InquireProcessStatusFailed(error.getMessage)
        }
        Behaviors.same
    }
}
