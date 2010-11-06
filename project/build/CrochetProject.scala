import sbt._
import Process._

class CrochetProject(info: ProjectInfo) extends DefaultProject(info) {

	// HttpClient
	lazy val com_http_client = "commons-httpclient" % "commons-httpclient" % "3.1"
	
	// Jetty
	lazy val jetty = "org.mortbay.jetty" % "jetty" % "6.1.9"
	
	// Testing facilities
	lazy val specs = "specs" % "specs" % "2.8.0-1.6.5" from "http://code.google.com/p/specs/downloads/detail?name=specs_2.8.0-1.6.5.jar"
	
}