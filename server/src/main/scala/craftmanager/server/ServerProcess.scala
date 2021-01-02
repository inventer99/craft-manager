package craftmanager.server

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import craftmanager.shared.ServerProtocol._

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

object ServerProcess {

  def apply(serverName: String): Behavior[Command] =
    Behaviors.setup { context =>
      val server: Server = ServerFactory.getServer()

      serverProcess(server, serverName)(context.executionContext)
    }

  private def serverProcess(server: Server, serverName: String)(implicit ec: ExecutionContext): Behavior[Command] =
    Behaviors.receiveMessage[Command] { message =>
      if (message.forServer == serverName || message.forServer == null) {
        message match {
          case StartServer(_, replyTo) =>
            server.start()
            replyTo ! FailureResponse("Not Implemented")
          case StopServer(_, force, replyTo) =>
            replyTo ! FailureResponse("Not Implemented")
          case GetServerStatus(_, replyTo) =>
            server.status()
              .onComplete {
                case Success(status) =>
                  replyTo ! StatusResponse(status)
                case Failure(exception) =>
                  replyTo ! FailureResponse(exception.getMessage)
              }
        }
      }
      Behaviors.same
    }
}
