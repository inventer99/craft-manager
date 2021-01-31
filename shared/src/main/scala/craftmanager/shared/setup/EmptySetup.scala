package craftmanager.shared.setup

import akka.actor.typed.scaladsl.ActorContext
import com.typesafe.config.Config

class EmptySetup extends Setup {
  override def spawn(context: ActorContext[Nothing], config: Config): Unit = ()
}
