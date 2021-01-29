package craftmanager.command

import ackcord.Requests
import ackcord.commands.{CommandController, NamedDescribedCommand}
import ackcord.syntax.TextChannelSyntax
import akka.NotUsed

class TestCommands(requests: Requests) extends CommandController(requests) with Commands {
  lazy val commands = Seq(ping, list)

  val ping: NamedDescribedCommand[NotUsed] = Command
    .named(craftManagerSymbols, Seq("ping"))
    .described("Ping", "Pong.")
    .withRequest(_.textChannel.sendMessage("pong"))

  val list: NamedDescribedCommand[NotUsed] = Command
    .named(craftManagerSymbols, Seq("list"))
    .described("List", "Lists all known servers")
    .withRequest(_.textChannel.sendMessage(
      s"""
      | • MCEternal
      | • VanillaMC
      | • Terraria
      | • GMod
      |""".stripMargin
    ))
}
