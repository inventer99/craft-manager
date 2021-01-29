package craftmanager.shared

import akka.actor.typed.ActorRef

object ProcessProtocol {
  sealed trait Command
  final case class StartProcess(replyTo: ActorRef[StartResponse]) extends Command
  final case class StopProcess(force: Boolean, replyTo: ActorRef[StopResponse]) extends Command
  final case class RestartProcess(replyTo: ActorRef[RestartResponse]) extends Command
  final case class InquireProcessStatus(replyTo: ActorRef[StatusResponse]) extends Command

  sealed trait StartResponse
  final case class StartProcessSucceeded() extends StartResponse
  final case class StartProcessFailed(reason: String) extends StartResponse

  sealed trait StopResponse
  final case class StopProcessSucceeded() extends StopResponse
  final case class StopProcessFailed(reason: String) extends StopResponse

  sealed trait RestartResponse
  final case class RestartProcessSucceeded() extends RestartResponse
  final case class RestartProcessFailed(reason: String) extends RestartResponse

  sealed trait StatusResponse
  final case class InquireProcessStatusSucceeded(status: ProcessStatus) extends StatusResponse
  final case class InquireProcessStatusFailed(reason: String) extends StatusResponse
}
