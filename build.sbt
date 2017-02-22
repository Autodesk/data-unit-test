import de.johoop.testngplugin.TestNGPlugin._

name := "validation-framework"

organization := "com.autodesk"

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.1"

resolvers := Seq(
  "Artifactory" at "https://art-bobcat.autodesk.com/artifactory"
)

// Do not append Scala versions to the generated artifacts
crossPaths := false

packSettings

// This forbids including Scala related libraries into the dependency
autoScalaLibrary := false

retrieveManaged := true

jacoco.settings

testNGSettings

testNGSuites := Seq(((resourceDirectory in Test).value / "testframework.xml").absolutePath)

testNGVersion := "6.11"

libraryDependencies ++= Seq(
  "commons-dbcp" % "commons-dbcp" % "1.4",
  "log4j" % "log4j" % "1.2.17" ,
  "org.apache.hive" % "hive-jdbc" % "1.2.0",
  "org.testng" % "testng" % "6.11",
  "org.json" % "json" % "20160810",
  "org.apache.commons" % "commons-csv" % "1.4",
  "org.yaml" % "snakeyaml" % "1.17",
  "org.apache.hadoop" % "hadoop-common" % "2.6.0",
  "org.powermock" % "powermock-api-mockito" % "1.7.0RC4",
  "org.powermock" % "powermock-module-testng" % "1.7.0RC4"
) 

artifact in (Compile, assembly) := {
 val art = (artifact in (Compile, assembly)).value
 art.copy(`classifier` = Some("assembly"))
}

addArtifact(artifact in (Compile, assembly), assembly)

assemblyMergeStrategy in assembly := {
  case PathList("org", "apache", xs @ _*) => MergeStrategy.last
  case PathList("javax", "transaction", xs @ _*) => MergeStrategy.last
  case "plugin.xml" => MergeStrategy.last
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}

mainClass in assembly := Some("com.autodesk.adp.validation_framework.TestRunner")

unmanagedResourceDirectories in Compile := Seq(baseDirectory.value / "conf")
includeFilter in unmanagedResources := "log4j.properties"

credentials += Credentials(new File(System.getenv("FILE")))

publishTo <<= version { v: String =>
  val bobcat = "https://art-bobcat.autodesk.com/artifactory/"
  if (v.trim.endsWith("SNAPSHOT")) Some("Artifactory Realm" at bobcat + "team-adp-sbt")
  else Some("Artifactory Realm" at bobcat + "autodesk-libs-release-ivy")
}
