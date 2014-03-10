name := "The Akka Lab"

version := "0.1"

scalaVersion := "2.10.3"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.2.3",
  "com.typesafe.akka" %% "akka-testkit" % "2.2.3",
  "org.scalatest" %% "scalatest" % "2.0" % "test",
  "junit" % "junit" % "4.10" % "test",
  "org.mockito" % "mockito-all" % "1.8.1" % "test"
)