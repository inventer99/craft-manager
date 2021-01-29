package craftmanager.command

import ackcord.Requests
import ackcord.commands.{CommandController, NamedCommand, NamedDescribedCommand}
import ackcord.syntax.TextChannelSyntax
import akka.NotUsed
import akka.actor.typed.scaladsl.AskPattern._
import akka.actor.typed.{ActorRef, ActorSystem}
import akka.util.Timeout

class ProcessCommands(requests: Requests)(implicit timeout: Timeout, system: ActorSystem[Nothing]) extends CommandController(requests) {
  lazy val commands = Seq(start, stop, status)

  val start: NamedDescribedCommand[NotUsed] = Command
    .named(craftManagerSymbols, Seq("start"))
    .described("Start", "Starts the named server")
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

  val stop: NamedDescribedCommand[NotUsed] = Command
    .named(craftManagerSymbols, Seq("stop"))
    .described("Stop", "Stops the named server")
    .withRequest(_.textChannel.sendMessage("Not Implemented"))

  val status: NamedDescribedCommand[NotUsed] = Command
    .named(craftManagerSymbols, Seq("status"))
    .described("Status", "Displays the status of the named server")
    .withRequest(_.textChannel.sendMessage("Not Implemented"))
}
