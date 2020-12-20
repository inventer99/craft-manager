package com.inventertech.craftmanager.command

import ackcord.Requests
import ackcord.commands.{CommandController, NamedCommand}
import ackcord.syntax.TextChannelSyntax
import akka.NotUsed

class ProcessCommands(requests: Requests) extends CommandController(requests) {
  val start: NamedCommand[NotUsed] = Command
    .named(craftManagerSymbols, Seq("start"))
    .withRequest(_.textChannel.sendMessage("Not Implemented"))

  val stop: NamedCommand[NotUsed] = Command
    .named(craftManagerSymbols, Seq("stop"))
    .withRequest(_.textChannel.sendMessage("Not Implemented"))

  val status: NamedCommand[NotUsed] = Command
    .named(craftManagerSymbols, Seq("status"))
    .withRequest(_.textChannel.sendMessage("Not Implemented"))
}
