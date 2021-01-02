package craftmanager.shared

import akka.actor.typed.ActorRef

object ServerProtocol {
  sealed class Command(val forServer: String)
  final case class StartServer(override val forServer: String, replyTo: ActorRef[Response]) extends Command(forServer)
  final case class StopServer(override val forServer: String, force: Boolean, replyTo: ActorRef[Response]) extends Command(forServer)
  final case class GetServerStatus(override val forServer: String, replyTo: ActorRef[Response]) extends Command(forServer)

  sealed trait Response
  final case class SuccessResponse() extends Response
  final case class FailureResponse(reason: String) extends Response
  final case class StatusResponse(status: ServerStatus) extends Response
}
