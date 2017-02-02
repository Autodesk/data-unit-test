name := "validation-framework"

organization := "com.autodesk"

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
  "org.yaml" % "snakeyaml" % "1.17",
  "org.apache.hadoop" % "hadoop-common" % "2.6.0"
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

publishTo <<= version { v: String =>
  val nexus = "http://a360nexus.autodesk.com/nexus/"
  if (v.trim.endsWith("SNAPSHOT")) Some("Snapshots" at nexus + "content/repositories/snapshots")
  else Some("Release" at nexus + "content/repositories/releases")
}

credentials += Credentials("Sonatype Nexus Repository Manager", "a360nexus.autodesk.com", "admin", sys.props.getOrElse("nexus_admin_pw", "password"))

