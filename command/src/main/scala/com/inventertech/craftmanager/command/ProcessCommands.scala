package com.inventertech.craftmanager.command

import ackcord.{OptFuture, Requests}
import ackcord.commands.{CommandController, NamedCommand}
import ackcord.syntax.TextChannelSyntax
import akka.NotUsed
import akka.actor.typed.{ActorRef, ActorSystem, Props, SpawnProtocol}
import akka.actor.typed.scaladsl.AskPattern._
import akka.util.Timeout
import com.inventertech.craftmanager.shared.ServerProcess

import scala.concurrent.duration.DurationInt

class ProcessCommands(requests: Requests) extends CommandController(requests) {
  val start: NamedCommand[NotUsed] = Command
    .named(craftManagerSymbols, Seq("start"))
    .asyncOptRequest(m => {
      implicit val timeout: Timeout = 5.seconds
      implicit val system: ActorSystem[_] = requests.system
      implicit val executionContext = system.executionContext
      val process: ActorRef[ServerProcess.Command] = system ? (SpawnProtocol.Spawn(behavior = ServerProcess("Test"), name = "Start Server", props = Props.empty, _))
      OptFuture.fromFuture(for (
        result <- process ? (ref => ServerProcess.Start(ref))
      ) yield result match {
        case ServerProcess.RespondSuccess() =>
          m.textChannel.sendMessage("Started Server")
        case ServerProcess.RespondFailure(reason) =>
          m.textChannel.sendMessage(s"Unable to start server: $reason")
      })
    })

  val stop: NamedCommand[NotUsed] = Command
    .named(craftManagerSymbols, Seq("stop"))
    .withRequest(_.textChannel.sendMessage("Not Implemented"))

  val status: NamedCommand[NotUsed] = Command
    .named(craftManagerSymbols, Seq("status"))
    .withRequest(_.textChannel.sendMessage("Not Implemented"))
}
