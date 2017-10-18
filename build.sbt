lazy val commonSettings = Seq(
  scalaVersion := "2.12.3",
  assemblyMergeStrategy in assembly := {
    case PathList("javax", "servlet", xs @ _*) => MergeStrategy.first
    case PathList(ps @ _*) if ps.last endsWith ".properties" => MergeStrategy.first
    case PathList(ps @ _*) if ps.last endsWith ".xml" => MergeStrategy.first
    case PathList(ps @ _*) if ps.last endsWith ".types" => MergeStrategy.first
    case PathList(ps @ _*) if ps.last endsWith ".class" => MergeStrategy.first
    case "application.conf" => MergeStrategy.concat
    case "unwanted.txt" => MergeStrategy.discard
    case "BUILD" => MergeStrategy.discard
    case x =>
      val oldStrategy = (assemblyMergeStrategy in assembly).value
      oldStrategy(x)
  }
)

lazy val root = (project in file("."))
  .settings(name := "common-bidder")
  .aggregate(bid, logic, common)

lazy val bid = (project in file("bid"))
  .settings(commonSettings)
  .settings(libraryDependencies ++= Seq(
    "com.twitter" %% "finagle-http" % "7.1.0",
    "com.twitter" %% "twitter-server" % "1.32.0"
  ))
  .dependsOn(common)

lazy val logic = (project in file("logic"))
  .settings(commonSettings)
  .settings(libraryDependencies ++= Seq(
    "com.twitter" %% "finagle-http" % "7.1.0",
    "com.twitter" %% "twitter-server" % "1.32.0"
  ))
  .dependsOn(common)

lazy val common = (project in file("common"))
  .settings(commonSettings)
  .settings(libraryDependencies ++= Seq(
    "org.apache.avro" % "avro" % "1.8.2",
    "org.apache.avro" % "avro-ipc" % "1.8.2",
    "com.sksamuel.avro4s" %% "avro4s-core" % "1.7.0"
  ))