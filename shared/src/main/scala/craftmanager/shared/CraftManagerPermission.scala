package craftmanager.shared

sealed trait CraftManagerPermission
case object CmAdmin extends CraftManagerPermission
