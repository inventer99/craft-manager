package craftmanager.minecraft

import craftmanager.server.Server
import craftmanager.shared.ServerStatus

import java.io.File
import scala.concurrent.{ExecutionContext, Future, blocking}
import scala.sys.process._

class MinecraftServer(implicit ec: ExecutionContext) extends Server {
  override def start(): Future[Unit] = Future {
    blocking {
      Process("server_start.sh", new File(""), "SERVER_DIRECTORY" -> "").!
    }
  }

  override def stop(): Future[Unit] = Future {
    blocking {
      Process("server_stop.sh", new File(""), "SERVER_DIRECTORY" -> "").!
    }
  }
  override def status(): Future[ServerStatus] = Future {
    blocking {
      Process("server_status.sh", new File(""), "SERVER_DIRECTORY" -> "").!
    }
  }.map(ServerStatus(_))
}
