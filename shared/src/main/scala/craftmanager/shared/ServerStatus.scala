package craftmanager.shared

sealed trait ServerStatus
case object Unknown extends ServerStatus // Unable to determine server status
case object Running extends ServerStatus // The server is running properly
case object Stopped extends ServerStatus // The server is not running
case object Starting extends ServerStatus // The server is starting up
case object Stopping extends ServerStatus // The server is shutting down
case object Frozen extends ServerStatus // The server is running but unresponsive

object ServerStatus {
  def apply(value: Int): ServerStatus = value match {
    case 0 => Unknown
    case 10 => Running
    case 11 => Stopped
    case 12 => Frozen
    case 20 => Starting
    case 21 => Stopping
    case _ => Unknown
  }
}