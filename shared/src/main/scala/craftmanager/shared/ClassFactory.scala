package craftmanager.shared

import craftmanager.shared.setup.Setup
import scala.util.Try

object ClassFactory {
  def getSetupInstance(className: String): Try[Setup] =
    instantiate[Setup](className)

  private def instantiate[T](className: String): Try[T] = Try {
    Class.forName(className).newInstance().asInstanceOf[T]
  }
}
