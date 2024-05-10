name := "StreamingWC"

ThisBuild / version := "0.0.1"
ThisBuild / scalaVersion := "2.12.10"
autoScalaLibrary := false

val sparkVersion = "3.0.0"

val sparkDependencies = Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-sql" % sparkVersion
)
libraryDependencies ++= sparkDependencies


