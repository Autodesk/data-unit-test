name := "validation-framework"

version := "1.0"

scalaVersion := "2.11.1"

resolvers := Seq(
  "Nexus Public" at "http://a360nexus.autodesk.com/nexus/content/groups/public"
)

// Do not append Scala versions to the generated artifacts
crossPaths := false

packSettings

// This forbids including Scala related libraries into the dependency
autoScalaLibrary := false

retrieveManaged := true

libraryDependencies ++= Seq(
  "commons-dbcp" % "commons-dbcp" % "1.4",
  "log4j" % "log4j" % "1.2.17" ,
  "org.apache.hive" % "hive-jdbc" % "1.2.0",
  "org.testng" % "testng" % "6.9.13.6",
  "org.json" % "json" % "20160810",
  "org.apache.commons" % "commons-csv" % "1.4",
  "org.yaml" % "snakeyaml" % "1.17"
) 
