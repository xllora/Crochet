import sbt._
import Process._

class CrochetProject(info: ProjectInfo) extends DefaultProject(info) {

	// HttpClient
	lazy val com_http_client = "commons-httpclient" % "commons-httpclient" % "3.1"
	
	// Jetty
	lazy val jetty = "org.mortbay.jetty" % "jetty" % "6.1.9"
	
	// Testing facilities
	lazy val scalatest = "org.scalatest" % "scalatest" % "1.0"
	lazy val scalacheck = "org.scala-tools.testing" % "scalacheck_2.7.7" % "1.6"
	lazy val specs = "org.scala-tools.testing" % "specs" % "1.6.5" from "http://specs.googlecode.com/files/specs_2.8.0-1.6.5.jar"
	
}