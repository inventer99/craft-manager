import sbt._

object Dependencies {
  lazy val akkaVersion = "2.6.6"
  lazy val ackcordVersion = "0.17.1"

  val akkaActor = "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion
  val akkaStream = "com.typesafe.akka" %% "akka-stream-typed" % akkaVersion
  val akkaCluster = "com.typesafe.akka" %% "akka-cluster-typed" % akkaVersion

  val ackcordCore = "net.katsstuff" %% "ackcord-core" % ackcordVersion
  val ackcordRequests = "net.katsstuff" %% "ackcord-requests" % ackcordVersion
  val ackcordGateway = "net.katsstuff" %% "ackcord-gateway" % ackcordVersion
  val ackcordCommands = "net.katsstuff" %% "ackcord-commands" % ackcordVersion

  val akkaSlf4j = "com.typesafe.akka" %% "akka-slf4j" % akkaVersion
  val slf4jSimple = "org.slf4j" % "slf4j-simple" % "2.0.0-alpha1"

  val rcon = "com.github.MrGraversen" % "minecraft-rcon" % "0.0.3"

  val akkaDeps = Seq(
    akkaActor,
    akkaStream,
    akkaCluster
  )

  val ackcordDeps = Seq(
    ackcordCore,
    ackcordRequests,
    ackcordGateway,
    ackcordCommands
  )

  val loggingDeps = Seq(
    akkaSlf4j,
    slf4jSimple
  )

  val minecraftDeps = Seq(
    rcon
  )

  val commonDeps = akkaDeps ++ loggingDeps
}
