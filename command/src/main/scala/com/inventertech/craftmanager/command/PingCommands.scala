package com.inventertech.craftmanager.command

import ackcord.Requests
import ackcord.commands.{CommandController, NamedCommand}
import ackcord.syntax.TextChannelSyntax
import akka.NotUsed

class PingCommands(requests: Requests) extends CommandController(requests) {
  val ping: NamedCommand[NotUsed] = Command
    .named(craftManagerSymbols, Seq("ping"))
    .withRequest(_.textChannel.sendMessage("pong"))
}
