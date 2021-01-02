package craftmanager.server

import craftmanager.shared.ServerStatus

import scala.concurrent.Future

trait Server {
  def start(): Future[Unit]
  def stop(): Future[Unit]
  def status(): Future[ServerStatus]
}
