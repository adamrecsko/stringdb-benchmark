name := "stringdb-benchmark"

version := "1.0-SNAPSHOT"

resolvers ++= Seq(
  "anormcypher" at "http://repo.anormcypher.org/",
  "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases/"
)

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  "mysql" % "mysql-connector-java" % "5.1.21",
  "org.anormcypher" %% "anormcypher" % "0.4.4"
)     

play.Project.playScalaSettings
