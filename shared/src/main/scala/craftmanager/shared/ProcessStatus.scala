package craftmanager.shared

sealed trait ProcessStatus
case object Unknown extends ProcessStatus // Unable to determine server status
case object Running extends ProcessStatus // The server process is running properly
case object Stopped extends ProcessStatus // The server process is not running
case object Starting extends ProcessStatus // The server process is starting up
case object Stopping extends ProcessStatus // The server process is shutting down
case object Frozen extends ProcessStatus // The server process is running but unresponsive

object ProcessStatus {
  def apply(value: Int): ProcessStatus = value match {
    case 0 => Unknown
    case 10 => Running
    case 11 => Stopped
    case 12 => Frozen
    case 20 => Starting
    case 21 => Stopping
    case _ => Unknown
  }
}