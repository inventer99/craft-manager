package craftmanager.minecraft.setup

import akka.actor.typed.scaladsl.ActorContext
import com.typesafe.config.Config
import craftmanager.shared.setup.Setup

class MinecraftSetup extends Setup {
  override def spawn(context: ActorContext[Nothing], config: Config): Unit = ()
}
