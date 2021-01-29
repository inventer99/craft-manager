//package craftmanager.server
//
//import craftmanager.shared.ServerConfig
//
//object ServerFactory {
//  def getServerInstance(config: ServerConfig): Server = {
//    Class.forName(config.klass).newInstance().asInstanceOf[Server]
//  }
//}
