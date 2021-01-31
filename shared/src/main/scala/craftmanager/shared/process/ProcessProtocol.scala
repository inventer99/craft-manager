package craftmanager.shared.process

import akka.actor.typed.ActorRef
import craftmanager.shared.Serializable

object ProcessProtocol {
  sealed trait Command
  final case class StartProcess(replyTo: ActorRef[StartResponse]) extends Command with Serializable
  final case class StopProcess(force: Boolean, replyTo: ActorRef[StopResponse]) extends Command with Serializable
  final case class RestartProcess(replyTo: ActorRef[RestartResponse]) extends Command with Serializable
  final case class InquireProcessStatus(replyTo: ActorRef[StatusResponse]) extends Command with Serializable

  sealed trait StartResponse
  final case class StartProcessSucceeded() extends StartResponse with Serializable
  final case class StartProcessFailed(reason: String) extends StartResponse with Serializable

  sealed trait StopResponse
  final case class StopProcessSucceeded() extends StopResponse with Serializable
  final case class StopProcessFailed(reason: String) extends StopResponse with Serializable

  sealed trait RestartResponse
  final case class RestartProcessSucceeded() extends RestartResponse with Serializable
  final case class RestartProcessFailed(reason: String) extends RestartResponse with Serializable

  sealed trait StatusResponse
  final case class InquireProcessStatusSucceeded(status: ProcessStatus) extends StatusResponse with Serializable
  final case class InquireProcessStatusFailed(reason: String) extends StatusResponse with Serializable
}
