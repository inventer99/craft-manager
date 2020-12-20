package com.inventertech.craftmanager.shared

import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors}

object ServerProcess {
  def apply(serverName: String): Behavior[Command] =
    Behaviors.setup(context => new ServerProcess(context, serverName))

  sealed trait ServerStatus
  case object Starting extends ServerStatus // The server is starting up
  case object Stopped extends ServerStatus // The server is not running
  case object Frozen extends ServerStatus // The server is running but unresponsive
  case object Running extends ServerStatus // The server is running properly
  case object Unknown extends ServerStatus // Unable to determine server statusdd

  sealed trait Command
  final case class Start(replyTo: ActorRef[Response]) extends Command
  final case class Stop(force: Boolean, replyTo: ActorRef[Response]) extends Command
  final case class GetStatus(replyTo: ActorRef[RespondStatus]) extends Command

  sealed trait Response
  final case class RespondSuccess() extends Response
  final case class RespondFailure(reason: String) extends Response
  final case class RespondStatus(status: ServerStatus)
}

class ServerProcess(context: ActorContext[ServerProcess.Command], serverName: String)
  extends AbstractBehavior[ServerProcess.Command](context) {
  import ServerProcess._

  override def onMessage(message: Command): Behavior[Command] =
    message match {
      case Start(replyTo) =>
        replyTo ! RespondFailure("Not Implemented")
        this
      case Stop(force, replyTo) =>
        replyTo ! RespondFailure("Not Implemented")
        this
      case GetStatus(replyTo) =>
        replyTo ! RespondStatus(Unknown)
        this
    }
}
