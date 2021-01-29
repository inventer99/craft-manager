package craftmanager.server

import com.typesafe.config.Config

import scala.concurrent.Future

class ProcessService(config: Config) {
  private val wd = os.root / config.getString("craftmanager.directory")
  private val env = Map("SERVER_DIRECTORY" -> wd.toString)

  def start(): Future[Unit] =
    invokeService()

  def stop(force: Boolean): Future[Unit] =
    invokeService()

  def restart(): Future[Unit] =
    invokeService()

  def status(): Future[Unit] =
    invokeService()

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
