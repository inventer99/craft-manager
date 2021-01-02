import Dependencies._

lazy val commonSettings = Seq(
  javacOptions ++= Seq("-source", "1.8", "-target", "1.8", "-Xlint"),
  scalaVersion := "2.13.4",
  organization := "com.inventertech",
  version := "0.1.0-SNAPSHOT",
  resolvers -= Resolver.DefaultMavenRepository,
  resolvers ++= Seq(
    Resolver.JCenterRepository,
    "jitpack.io" at "https://jitpack.io"
  ),
  libraryDependencies ++= commonDeps
)

lazy val shared = (project in file("shared"))
  .settings(
    commonSettings,
    name := "shared"
  )

lazy val command = (project in file("command"))
  .settings(
    commonSettings,
    name := "command",
    libraryDependencies ++= ackcordDeps,
    mainClass := Some("craftmanager.command.Main")
  )
  .dependsOn(shared)

lazy val server = (project in file("server"))
  .settings(
    commonSettings,
    name := "server",
    mainClass := Some("craftmanager.server.Main")
  )
  .dependsOn(shared)

lazy val minecraft = (project in file("minecraft"))
  .settings(
    commonSettings,
    name := "minecraft",
    libraryDependencies ++= minecraftDeps
  )
  .dependsOn(shared, server)

lazy val root = (project in file("."))
  .aggregate(
    shared,
    command,
    server,
    minecraft
  )
