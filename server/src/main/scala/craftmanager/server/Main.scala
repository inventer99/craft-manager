package craftmanager.server

import akka.actor.typed.javadsl.AbstractBehavior
import akka.actor.typed.receptionist.{Receptionist, ServiceKey}
import akka.actor.typed.{ActorSystem, Behavior}
import akka.actor.typed.scaladsl.{ActorContext, Behaviors}
import com.typesafe.config.{Config, ConfigFactory}
import com.typesafe.scalalogging.Logger
import craftmanager.server.process.Process
import craftmanager.shared.{ClassFactory, ConfigPaths}
import craftmanager.shared.process.ProcessProtocol
import craftmanager.shared.setup.EmptySetup

import scala.util.{Failure, Success}

object ServerBehavior {
  private val logger = Logger(ServerBehavior.getClass)

  def apply(config: Config): Behavior[Nothing] =
    Behaviors.setup[Nothing] { context =>
      val name = config.getString(ConfigPaths.name)
      //        logger.info(s"Setting up Craft Manager server for \"$name\".")

      // Spawn default server actors
      val process = context.spawn(Process(config), s"${name}Process")
      context.system.receptionist ! Receptionist.register(
        ServiceKey[ProcessProtocol.Command](s"${name}Process"),
        process)
      logger.debug("Spawned process actor.")

      // Spawn server specific actors
      val setupClass = config.getString(ConfigPaths.setupClass)
      val setup = ClassFactory.getSetupInstance(setupClass) match {
        case Success(setup) => setup
        case Failure(exception) =>
          //            logger.error(s"Failed to instantiate setup class \"$setupClass\". Using EmptySetup.", exception)
          new EmptySetup
      }
      setup.spawn(context, config)

      Behaviors.empty
    }
}

object Main {
  def main(args: Array[String]): Unit = {
    val config = ConfigFactory.load()

    ActorSystem[Nothing](ServerBehavior(config), "CraftManager", config)
  }
}
