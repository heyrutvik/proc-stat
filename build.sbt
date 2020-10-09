import Dependencies._

ThisBuild / scalaVersion     := "2.13.3"
ThisBuild / version          := "0.0.1"
ThisBuild / organization     := "io.github.heyrutvik"

val AkkaVersion = "2.6.9"
val AkkaHttpVersion = "10.2.1"
val AkkaKafka = "2.0.5"

lazy val commonDependencies = Seq(
  scalaTest % Test,
  "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
  "com.typesafe.akka" %% "akka-stream-kafka" % AkkaKafka,
  "com.typesafe.scala-logging" %% "scala-logging"   % "3.9.2"
)

lazy val root = (project in file("."))
  .settings(
    name := "proc-stat"
  )
  .aggregate(service, reader)

lazy val service = project
  .settings(
    name := "proc-stat-service",
    libraryDependencies ++= commonDependencies ++ Seq(
      "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion,
    )
  )

lazy val reader = project
  .settings(
    name := "proc-stat-reader",
    libraryDependencies ++= commonDependencies ++  Seq(
      "io.spray" %% "spray-json" % "1.3.5"
    )
  )

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
