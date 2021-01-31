import sbt._

object Dependencies {
  val akkaVersion = "2.6.6"
  val ackcordVersion = "0.17.1"
  val log4jVersion = "2.14.0"

  // Akka
  val akkaActor = "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion
  val akkaStream = "com.typesafe.akka" %% "akka-stream-typed" % akkaVersion
  val akkaCluster = "com.typesafe.akka" %% "akka-cluster-typed" % akkaVersion
  val akkaSerializationJackson = "com.typesafe.akka" %% "akka-serialization-jackson" % akkaVersion

  // Discord
  val ackcordCore = "net.katsstuff" %% "ackcord-core" % ackcordVersion
  val ackcordRequests = "net.katsstuff" %% "ackcord-requests" % ackcordVersion
  val ackcordGateway = "net.katsstuff" %% "ackcord-gateway" % ackcordVersion
  val ackcordCommands = "net.katsstuff" %% "ackcord-commands" % ackcordVersion

  // Logging
  val akkaSlf4j = "com.typesafe.akka" %% "akka-slf4j" % akkaVersion
  val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"
  val log4jApi  = "org.apache.logging.log4j" % "log4j-api" % log4jVersion
  val log4jCore = "org.apache.logging.log4j" % "log4j-core" % log4jVersion
  val log4jSlf4jImpl = "org.apache.logging.log4j" % "log4j-slf4j-impl" % log4jVersion

  // Utils
  val osLib = "com.lihaoyi" %% "os-lib" % "0.7.1"

  // Minecraft
  val rcon = "com.github.MrGraversen" % "minecraft-rcon" % "0.0.3"

  val akkaDeps = Seq(
    akkaActor,
    akkaStream,
    akkaCluster,
    akkaSerializationJackson
  )

  val ackcordDeps = Seq(
    ackcordCore,
    ackcordRequests,
    ackcordGateway,
    ackcordCommands
  )

  val loggingDeps = Seq(
    akkaSlf4j,
    scalaLogging,
    log4jApi,
    log4jCore,
    log4jSlf4jImpl
  )

  val utilDeps = Seq(
    osLib
  )

  val minecraftDeps = Seq(
    rcon
  )

  val commonDeps = akkaDeps ++ loggingDeps ++ utilDeps

  val excludedDeps = Seq(
    ExclusionRule(organization = "ch.qos.logback")
  )
}
