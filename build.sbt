name := "akka-crawler-example-scala"

version := "1.0"

scalaVersion := "2.9.2"

retrieveManaged := true

libraryDependencies += "org.apache.lucene" % "lucene-core" % "3.6.1"

libraryDependencies += "com.typesafe.akka" % "akka-actor_2.10.0-RC5" % "2.1.0-RC6"

libraryDependencies += "org.slf4j" % "slf4j-api" % "1.6.4"

libraryDependencies += "org.htmlparser" % "htmlparser" % "2.1"

libraryDependencies += "org.perf4j" % "perf4j" % "0.9.16"

libraryDependencies += "org.slf4j" % "slf4j-simple" % "1.6.4"

libraryDependencies += "junit" % "junit" % "4.10"
