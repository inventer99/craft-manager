package com.inventertech.craftmanager.command

import ackcord.Requests
import ackcord.commands.{CommandController, NamedCommand}
import ackcord.syntax.TextChannelSyntax
import akka.NotUsed
import akka.actor.typed.scaladsl.AskPattern._
import akka.actor.typed.{ActorRef, ActorSystem}
import akka.util.Timeout
import com.inventertech.craftmanager.shared.ServerProcess

class ProcessCommands(requests: Requests)(implicit timeout: Timeout, system: ActorSystem[Nothing]) extends CommandController(requests) {
  val start: NamedCommand[NotUsed] = Command
    .named(craftManagerSymbols, Seq("start"))
    .withRequest(_.textChannel.sendMessage("Not Implemented"))
//    .withSideEffects { m =>
//      val serverProcess
//      for (
//        result <- serverProcess ? (ref => ServerProcess.Start(ref))
//      ) yield result match {
//        case ServerProcess.RespondSuccess() =>
//          m.textChannel.sendMessage("Started Server")
//        case ServerProcess.RespondFailure(reason) =>
//          m.textChannel.sendMessage(s"Unable to start server: $reason")
//      }
//    }

  val stop: NamedCommand[NotUsed] = Command
    .named(craftManagerSymbols, Seq("stop"))
    .withRequest(_.textChannel.sendMessage("Not Implemented"))

  val status: NamedCommand[NotUsed] = Command
    .named(craftManagerSymbols, Seq("status"))
    .withRequest(_.textChannel.sendMessage("Not Implemented"))
}
