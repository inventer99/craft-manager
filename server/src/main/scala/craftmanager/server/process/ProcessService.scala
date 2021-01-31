package craftmanager.server.process

import com.typesafe.config.Config
import craftmanager.shared.ConfigPaths
import craftmanager.shared.process.{ProcessStatus, Stopped}

import scala.concurrent.{ExecutionContext, Future}

class ProcessService(config: Config)(implicit ex: ExecutionContext) {
  private val wd = os.root / config.getString(ConfigPaths.directory)
  private val env = Map("SERVER_DIRECTORY" -> wd.toString)

  def start(): Future[Unit] =
//    invokeService()
    Future.successful()

  def stop(force: Boolean): Future[Unit] =
//    invokeService()
    Future.successful()

  def restart(): Future[Unit] =
//    invokeService()
    Future.successful()

  def status(): Future[ProcessStatus] =
//    invokeService()
    Future.successful(Stopped)

  private def invokeService(): Future[Unit] = {
    val result = os
      .proc("service", "name", "command")
      .call(env = env)
    if(result.exitCode != 0)
      Future.successful()
    else
      Future.failed(new RuntimeException(result.err.toString))
  }
}
