name := "AoC-2019"

version := "0.1"

scalaVersion := "2.13.3"

libraryDependencies ++= Seq(
  "com.beachape"  %% "enumeratum" % "1.6.1",
  "org.scalatest" %% "scalatest"  % "3.2.2" % Test
)

scalacOptions --= Seq(
  "-Xfatal-warnings"
)
