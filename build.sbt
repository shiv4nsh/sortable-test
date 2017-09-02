name := "sortableTest"

version := "1.0"

organization := "foobar"

scalaVersion := "2.11.8"

val sparkVersion = "2.1.1"

libraryDependencies ++= Seq(
  "org.apache.spark" % "spark-core_2.11" % sparkVersion,
  "org.apache.spark" % "spark-sql_2.11" % sparkVersion,
  "org.json4s" %% "json4s-native" % "3.5.3",
  "com.typesafe" % "config" % "1.3.1"
)

resolvers += "Maven Central Server" at "http://repo1.maven.org/maven2"
resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
resolvers += Resolver.mavenLocal
resolvers += Resolver.typesafeIvyRepo("releases")
resolvers += "Typesafe Releases" at "https://repo.typesafe.com/typesafe/releases/"


    