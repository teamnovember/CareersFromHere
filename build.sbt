name := "november"

version := "1.0"

lazy val `november` = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq( javaJdbc , javaEbean , cache , javaWs, "mysql" % "mysql-connector-java" % "5.1.34")



unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )

