name := "taurus"

version := "1.0"

scalaVersion := "2.11.11"

lazy val root = (project in (file("."))).enablePlugins(PlayScala)

libraryDependencies += guice
libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.44"
libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick" % "3.2.1",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.2.1"
)
libraryDependencies += "com.typesafe.play" %% "play-slick" % "3.0.3"
libraryDependencies += "com.typesafe.play" %% "play-slick-evolutions" % "3.0.3"
libraryDependencies += "com.typesafe.play" %% "play-json" % "2.6.8"

//=============hadoop=========

libraryDependencies += "org.apache.spark" %% "spark-core" % "2.2.1"
libraryDependencies += "org.apache.spark" %% "spark-sql" % "2.2.1"
libraryDependencies += "org.apache.spark" %% "spark-streaming" % "2.2.1" % "provided"
libraryDependencies += "org.apache.hadoop" % "hadoop-client" % "2.7.4"
libraryDependencies += "org.apache.hadoop" % "hadoop-hdfs" % "2.7.4"
libraryDependencies += "org.apache.hbase" % "hbase-client" % "1.0.1.1"
libraryDependencies += "org.apache.hbase" % "hbase-server" % "1.0.1.1"


//============hadoop============
libraryDependencies += "org.scala-sbt" %% "compiler-bridge" % "1.0.5" % Test

dependencyOverrides ++= Seq(
  "org.webjars" % "webjars-locator-core" % "0.33",
  "org.codehaus.plexus" % "plexus-utils" % "3.0.17",
  "com.google.guava" % "guava" % "23.0"
)

