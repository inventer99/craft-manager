package craftmanager.command

import ackcord.commands.NamedDescribedCommand

trait Commands {
  def commands: Seq[NamedDescribedCommand[_]]
}
