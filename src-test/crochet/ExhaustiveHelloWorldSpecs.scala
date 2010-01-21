package crochet

import scala.concurrent.ops.spawn
import org.specs.Specification
import java.io.StringReader
import scala.xml.XML
import crochet._
import crochet.net.utils._
import javax.servlet.http.HttpServletResponse


/**
 * A basic simple specs for a Hello World! example
 *
 * @author Xavier Llora
 * @date Jan 9, 2010 at 9:12:58 PM
 *
 */

object ExhaustiveHelloWorldSpecs extends Specification {
  
  protected val TEST_PORT = 8080

  protected val helloWordXHTML = <html>
    <head>
      <title>Hello World!</title>
    </head>
    <body>
      <b>Hello World!</b>
    </body>
  </html>

  protected val helloWorldText = "Hello World!"

  protected val csHelloWorld = new CrochetServlet {

    //
    // GET tests
    //
    get("/msg") {helloWordXHTML}
    get("/msg/html", "text/html") {helloWordXHTML}
    get("/msg/text", {header("Host") == "localhost:" + TEST_PORT}) {helloWorldText}
    get("/msg/text/auth", (uri:String,path:Option[String])=>path==None) { helloWordXHTML }
    get("/msg/text/2", "text/plain", {header("Host") == "localhost:" + TEST_PORT}) {helloWorldText}
    get("/msg/text/auth/2", "text/plain", (uri:String,path:Option[String])=>path==None) { helloWorldText }
    get("/msg/html/2", {header("Host") == "localhost:" + TEST_PORT}, (uri:String,path:Option[String])=>path==None) {helloWordXHTML}
    get("/msg/text/all", "text/plain", {header("Host") == "localhost:" + TEST_PORT}, (uri:String,path:Option[String])=>path==None) {helloWorldText}

    get("^/re/msg$".r) {helloWordXHTML}
    get("^/re/msg/html$".r, "text/html") {helloWordXHTML}
    get("^/re/msg/text$".r, {header("Host") == "localhost:" + TEST_PORT}) {helloWorldText}
    get("^/re/msg/text/auth$".r, (uri:String,path:Option[String])=>path==None) { helloWordXHTML }
    get("^/re/msg/text/2$".r, "text/plain", {header("Host") == "localhost:" + TEST_PORT}) {helloWorldText}
    get("^/re/msg/text/auth/2$".r, "text/plain", (uri:String,path:Option[String])=>path==None) { helloWorldText }
    get("^/re/msg/html/2$".r, {header("Host") == "localhost:" + TEST_PORT}, (uri:String,path:Option[String])=>path==None) {helloWordXHTML}
    get("^/re/msg/text/all$".r, "text/plain", {header("Host") == "localhost:" + TEST_PORT}, (uri:String,path:Option[String])=>path==None) {helloWorldText}
    get("^/re/echo/([0-9]+)$".r, "text/plain", {elements.size == 1}) {elements(0)}

    //
    // POST tests
    //
    post("/msg") {helloWordXHTML}
    post("/msg/html", "text/html") {helloWordXHTML}
    post("/msg/text", {header("Host") == "localhost:" + TEST_PORT}) {helloWorldText}
    post("/msg/text/auth", (uri:String,path:Option[String])=>path==None) { helloWordXHTML }
    post("/msg/text/2", "text/plain", {header("Host") == "localhost:" + TEST_PORT}) {helloWorldText}
    post("/msg/text/auth/2", "text/plain", (uri:String,path:Option[String])=>path==None) { helloWorldText }
    post("/msg/html/2", {header("Host") == "localhost:" + TEST_PORT}, (uri:String,path:Option[String])=>path==None) {helloWordXHTML}
    post("/msg/text/all", "text/plain", {header("Host") == "localhost:" + TEST_PORT}, (uri:String,path:Option[String])=>path==None) {helloWorldText}

    post("^/re/msg$".r) {helloWordXHTML}
    post("^/re/msg/html$".r, "text/html") {helloWordXHTML}
    post("^/re/msg/text$".r, {header("Host") == "localhost:" + TEST_PORT}) {helloWorldText}
    post("^/re/msg/text/auth$".r, (uri:String,path:Option[String])=>path==None) { helloWordXHTML }
    post("^/re/msg/text/2$".r, "text/plain", {header("Host") == "localhost:" + TEST_PORT}) {helloWorldText}
    post("^/re/msg/text/auth/2$".r, "text/plain", (uri:String,path:Option[String])=>path==None) { helloWorldText }
    post("^/re/msg/html/2$".r, {header("Host") == "localhost:" + TEST_PORT}, (uri:String,path:Option[String])=>path==None) {helloWordXHTML}
    post("^/re/msg/text/all$".r, "text/plain", {header("Host") == "localhost:" + TEST_PORT}, (uri:String,path:Option[String])=>path==None) {helloWorldText}
    post("^/re/echo/([0-9]+)$".r, "text/plain", {elements.size == 1}) {elements(0)}

    //
    // PUT tests
    //
    put("/msg") {helloWordXHTML}
    put("/msg/html", "text/html") {helloWordXHTML}
    put("/msg/text", {header("Host") == "localhost:" + TEST_PORT}) {helloWorldText}
    put("/msg/text/auth", (uri:String,path:Option[String])=>path==None) { helloWordXHTML }
    put("/msg/text/2", "text/plain", {header("Host") == "localhost:" + TEST_PORT}) {helloWorldText}
    put("/msg/text/auth/2", "text/plain", (uri:String,path:Option[String])=>path==None) { helloWorldText }
    put("/msg/html/2", {header("Host") == "localhost:" + TEST_PORT}, (uri:String,path:Option[String])=>path==None) {helloWordXHTML}
    put("/msg/text/all", "text/plain", {header("Host") == "localhost:" + TEST_PORT}, (uri:String,path:Option[String])=>path==None) {helloWorldText}

    put("^/re/msg$".r) {helloWordXHTML}
    put("^/re/msg/html$".r, "text/html") {helloWordXHTML}
    put("^/re/msg/text$".r, {header("Host") == "localhost:" + TEST_PORT}) {helloWorldText}
    put("^/re/msg/text/auth$".r, (uri:String,path:Option[String])=>path==None) { helloWordXHTML }
    put("^/re/msg/text/2$".r, "text/plain", {header("Host") == "localhost:" + TEST_PORT}) {helloWorldText}
    put("^/re/msg/text/auth/2$".r, "text/plain", (uri:String,path:Option[String])=>path==None) { helloWorldText }
    put("^/re/msg/html/2$".r, {header("Host") == "localhost:" + TEST_PORT}, (uri:String,path:Option[String])=>path==None) {helloWordXHTML}
    put("^/re/msg/text/all$".r, "text/plain", {header("Host") == "localhost:" + TEST_PORT}, (uri:String,path:Option[String])=>path==None) {helloWorldText}
    put("^/re/echo/([0-9]+)$".r, "text/plain", {elements.size == 1}) {elements(0)}

    //
    // DELETE tests
    //
    delete("/msg") {helloWordXHTML}
    delete("/msg/html", "text/html") {helloWordXHTML}
    delete("/msg/text", {header("Host") == "localhost:" + TEST_PORT}) {helloWorldText}
    delete("/msg/text/auth", (uri:String,path:Option[String])=>path==None) { helloWordXHTML }
    delete("/msg/text/2", "text/plain", {header("Host") == "localhost:" + TEST_PORT}) {helloWorldText}
    delete("/msg/text/auth/2", "text/plain", (uri:String,path:Option[String])=>path==None) { helloWorldText }
    delete("/msg/html/2", {header("Host") == "localhost:" + TEST_PORT}, (uri:String,path:Option[String])=>path==None) {helloWordXHTML}
    delete("/msg/text/all", "text/plain", {header("Host") == "localhost:" + TEST_PORT}, (uri:String,path:Option[String])=>path==None) {helloWorldText}

    delete("^/re/msg$".r) {helloWordXHTML}
    delete("^/re/msg/html$".r, "text/html") {helloWordXHTML}
    delete("^/re/msg/text$".r, {header("Host") == "localhost:" + TEST_PORT}) {helloWorldText}
    delete("^/re/msg/text/auth$".r, (uri:String,path:Option[String])=>path==None) { helloWordXHTML }
    delete("^/re/msg/text/2$".r, "text/plain", {header("Host") == "localhost:" + TEST_PORT}) {helloWorldText}
    delete("^/re/msg/text/auth/2$".r, "text/plain", (uri:String,path:Option[String])=>path==None) { helloWorldText }
    delete("^/re/msg/html/2$".r, {header("Host") == "localhost:" + TEST_PORT}, (uri:String,path:Option[String])=>path==None) {helloWordXHTML}
    delete("^/re/msg/text/all$".r, "text/plain", {header("Host") == "localhost:" + TEST_PORT}, (uri:String,path:Option[String])=>path==None) {helloWorldText}
    delete("^/re/echo/([0-9]+)$".r, "text/plain", {elements.size == 1}) {elements(0)}

    //
    // HEAD tests
    //
    head("/msg")
    head("/msg/html", "text/html")
    head("/msg/text", {header("Host") == "localhost:" + TEST_PORT})
    head("/msg/text/auth", (uri:String,path:Option[String])=>path==None)
    head("/msg/text/2", "text/plain", {header("Host") == "localhost:" + TEST_PORT})
    head("/msg/text/auth/2", "text/plain", (uri:String,path:Option[String])=>path==None)
    head("/msg/html/2", {header("Host") == "localhost:" + TEST_PORT}, (uri:String,path:Option[String])=>path==None)
    head("/msg/text/all", "text/plain", {header("Host") == "localhost:" + TEST_PORT}, (uri:String,path:Option[String])=>path==None)

    head("^/re/msg$".r)
    head("^/re/msg/html$".r, "text/html")
    head("^/re/msg/text$".r, {header("Host") == "localhost:" + TEST_PORT})
    head("^/re/msg/text/auth$".r, (uri:String,path:Option[String])=>path==None)
    head("^/re/msg/text/2$".r, "text/plain", {header("Host") == "localhost:" + TEST_PORT})
    head("^/re/msg/text/auth/2$".r, "text/plain", (uri:String,path:Option[String])=>path==None)
    head("^/re/msg/html/2$".r, {header("Host") == "localhost:" + TEST_PORT}, (uri:String,path:Option[String])=>path==None)
    head("^/re/msg/text/all$".r, "text/plain", {header("Host") == "localhost:" + TEST_PORT}, (uri:String,path:Option[String])=>path==None)
    head("^/re/echo/([0-9]+)$".r, "text/plain", {elements.size == 1})

    //
    // OPTIONS tests
    //
    options("/msg") {helloWordXHTML}
    options("/msg/html", "text/html") {helloWordXHTML}
    options("/msg/text", {header("Host") == "localhost:" + TEST_PORT}) {helloWorldText}
    options("/msg/text/auth", (uri:String,path:Option[String])=>path==None) { helloWordXHTML }
    options("/msg/text/2", "text/plain", {header("Host") == "localhost:" + TEST_PORT}) {helloWorldText}
    options("/msg/text/auth/2", "text/plain", (uri:String,path:Option[String])=>path==None) { helloWorldText }
    options("/msg/html/2", {header("Host") == "localhost:" + TEST_PORT}, (uri:String,path:Option[String])=>path==None) {helloWordXHTML}
    options("/msg/text/all", "text/plain", {header("Host") == "localhost:" + TEST_PORT}, (uri:String,path:Option[String])=>path==None) {helloWorldText}

    options("^/re/msg$".r) {helloWordXHTML}
    options("^/re/msg/html$".r, "text/html") {helloWordXHTML}
    options("^/re/msg/text$".r, {header("Host") == "localhost:" + TEST_PORT}) {helloWorldText}
    options("^/re/msg/text/auth$".r, (uri:String,path:Option[String])=>path==None) { helloWordXHTML }
    options("^/re/msg/text/2$".r, "text/plain", {header("Host") == "localhost:" + TEST_PORT}) {helloWorldText}
    options("^/re/msg/text/auth/2$".r, "text/plain", (uri:String,path:Option[String])=>path==None) { helloWorldText }
    options("^/re/msg/html/2$".r, {header("Host") == "localhost:" + TEST_PORT}, (uri:String,path:Option[String])=>path==None) {helloWordXHTML}
    options("^/re/msg/text/all$".r, "text/plain", {header("Host") == "localhost:" + TEST_PORT}, (uri:String,path:Option[String])=>path==None) {helloWorldText}
    options("^/re/echo/([0-9]+)$".r, "text/plain", {elements.size == 1}) {elements(0)}

    //
    // TRACE tests
    //
    trace("/msg") {helloWordXHTML}
    trace("/msg/html", "text/html") {helloWordXHTML}
    trace("/msg/text", {header("Host") == "localhost:" + TEST_PORT}) {helloWorldText}
    trace("/msg/text/auth", (uri:String,path:Option[String])=>path==None) { helloWordXHTML }
    trace("/msg/text/2", "text/plain", {header("Host") == "localhost:" + TEST_PORT}) {helloWorldText}
    trace("/msg/text/auth/2", "text/plain", (uri:String,path:Option[String])=>path==None) { helloWorldText }
    trace("/msg/html/2", {header("Host") == "localhost:" + TEST_PORT}, (uri:String,path:Option[String])=>path==None) {helloWordXHTML}
    trace("/msg/text/all", "text/plain", {header("Host") == "localhost:" + TEST_PORT}, (uri:String,path:Option[String])=>path==None) {helloWorldText}

    trace("^/re/msg$".r) {helloWordXHTML}
    trace("^/re/msg/html$".r, "text/html") {helloWordXHTML}
    trace("^/re/msg/text$".r, {header("Host") == "localhost:" + TEST_PORT}) {helloWorldText}
    trace("^/re/msg/text/auth$".r, (uri:String,path:Option[String])=>path==None) { helloWordXHTML }
    trace("^/re/msg/text/2$".r, "text/plain", {header("Host") == "localhost:" + TEST_PORT}) {helloWorldText}
    trace("^/re/msg/text/auth/2$".r, "text/plain", (uri:String,path:Option[String])=>path==None) { helloWorldText }
    trace("^/re/msg/html/2$".r, {header("Host") == "localhost:" + TEST_PORT}, (uri:String,path:Option[String])=>path==None) {helloWordXHTML}
    trace("^/re/msg/text/all$".r, "text/plain", {header("Host") == "localhost:" + TEST_PORT}, (uri:String,path:Option[String])=>path==None) {helloWorldText}
    trace("^/re/echo/([0-9]+)$".r, "text/plain", {elements.size == 1}) {elements(0)}

  }

  "A simple Hello World! example " should {

    "Return Hello World!" in {

      val client = HttpClient("http", "localhost", TEST_PORT)
      val server = CrochetServer(TEST_PORT, csHelloWorld)
      server.start

      //
      // Basic GET tests for fixed paths
      //
      XML.load(new StringReader(client.get("/msg")._2)) must be equalTo (helloWordXHTML)
      XML.load(new StringReader(client.get("/msg/html")._2)) must be equalTo (helloWordXHTML)
      client.get("/msg/text")._2 must be equalTo (helloWorldText)
      XML.load(new StringReader(client.get("/msg/text/auth")._2)) must be equalTo (helloWordXHTML)
      client.get("/msg/text/2")._2 must be equalTo (helloWorldText)
      client.get("/msg/text/auth/2")._2 must be equalTo (helloWorldText)
      XML.load(new StringReader(client.get("/msg/html/2")._2)) must be equalTo (helloWordXHTML)
      client.get("/msg/text/all")._2 must be equalTo (helloWorldText)
      //
      // Basic tests for regex paths
      //
      XML.load(new StringReader(client.get("/re/msg")._2)) must be equalTo (helloWordXHTML)
      XML.load(new StringReader(client.get("/re/msg/html")._2)) must be equalTo (helloWordXHTML)
      client.get("/re/msg/text")._2 must be equalTo (helloWorldText)
      XML.load(new StringReader(client.get("/re/msg/text/auth")._2)) must be equalTo (helloWordXHTML)
      client.get("/re/msg/text/2")._2 must be equalTo (helloWorldText)
      client.get("/re/msg/text/auth/2")._2 must be equalTo (helloWorldText)
      XML.load(new StringReader(client.get("/re/msg/html/2")._2)) must be equalTo (helloWordXHTML)
      client.get("/re/msg/text/all")._2 must be equalTo (helloWorldText)
      client.get("/re/echo/123")._2 must be equalTo ("123")

      //
      // Basic POST tests for fixed paths
      //
      XML.load(new StringReader(client.post("/msg")._2)) must be equalTo (helloWordXHTML)
      XML.load(new StringReader(client.post("/msg/html")._2)) must be equalTo (helloWordXHTML)
      client.post("/msg/text")._2 must be equalTo (helloWorldText)
      XML.load(new StringReader(client.post("/msg/text/auth")._2)) must be equalTo (helloWordXHTML)
      client.post("/msg/text/2")._2 must be equalTo (helloWorldText)
      client.post("/msg/text/auth/2")._2 must be equalTo (helloWorldText)
      XML.load(new StringReader(client.post("/msg/html/2")._2)) must be equalTo (helloWordXHTML)
      client.post("/msg/text/all")._2 must be equalTo (helloWorldText)
      //
      // Basic tests for regex paths
      //
      XML.load(new StringReader(client.post("/re/msg")._2)) must be equalTo (helloWordXHTML)
      XML.load(new StringReader(client.post("/re/msg/html")._2)) must be equalTo (helloWordXHTML)
      client.post("/re/msg/text")._2 must be equalTo (helloWorldText)
      XML.load(new StringReader(client.post("/re/msg/text/auth")._2)) must be equalTo (helloWordXHTML)
      client.post("/re/msg/text/2")._2 must be equalTo (helloWorldText)
      client.post("/re/msg/text/auth/2")._2 must be equalTo (helloWorldText)
      XML.load(new StringReader(client.post("/re/msg/html/2")._2)) must be equalTo (helloWordXHTML)
      client.post("/re/msg/text/all")._2 must be equalTo (helloWorldText)
      client.post("/re/echo/123")._2 must be equalTo ("123")

      //
      // Basic PUT tests for fixed paths
      //
      XML.load(new StringReader(client.put("/msg")._2)) must be equalTo (helloWordXHTML)
      XML.load(new StringReader(client.put("/msg/html")._2)) must be equalTo (helloWordXHTML)
      client.put("/msg/text")._2 must be equalTo (helloWorldText)
      XML.load(new StringReader(client.put("/msg/text/auth")._2)) must be equalTo (helloWordXHTML)
      client.put("/msg/text/2")._2 must be equalTo (helloWorldText)
      client.put("/msg/text/auth/2")._2 must be equalTo (helloWorldText)
      XML.load(new StringReader(client.put("/msg/html/2")._2)) must be equalTo (helloWordXHTML)
      client.put("/msg/text/all")._2 must be equalTo (helloWorldText)
      //
      // Basic tests for regex paths
      //
      XML.load(new StringReader(client.put("/re/msg")._2)) must be equalTo (helloWordXHTML)
      XML.load(new StringReader(client.put("/re/msg/html")._2)) must be equalTo (helloWordXHTML)
      client.put("/re/msg/text")._2 must be equalTo (helloWorldText)
      XML.load(new StringReader(client.put("/re/msg/text/auth")._2)) must be equalTo (helloWordXHTML)
      client.put("/re/msg/text/2")._2 must be equalTo (helloWorldText)
      client.put("/re/msg/text/auth/2")._2 must be equalTo (helloWorldText)
      XML.load(new StringReader(client.put("/re/msg/html/2")._2)) must be equalTo (helloWordXHTML)
      client.put("/re/msg/text/all")._2 must be equalTo (helloWorldText)
      client.put("/re/echo/123")._2 must be equalTo ("123")

      //
      // Basic DELETE tests for fixed paths
      //
      XML.load(new StringReader(client.delete("/msg")._2)) must be equalTo (helloWordXHTML)
      XML.load(new StringReader(client.delete("/msg/html")._2)) must be equalTo (helloWordXHTML)
      client.delete("/msg/text")._2 must be equalTo (helloWorldText)
      XML.load(new StringReader(client.delete("/msg/text/auth")._2)) must be equalTo (helloWordXHTML)
      client.delete("/msg/text/2")._2 must be equalTo (helloWorldText)
      client.delete("/msg/text/auth/2")._2 must be equalTo (helloWorldText)
      XML.load(new StringReader(client.delete("/msg/html/2")._2)) must be equalTo (helloWordXHTML)
      client.delete("/msg/text/all")._2 must be equalTo (helloWorldText)
      //
      // Basic tests for regex paths
      //
      XML.load(new StringReader(client.delete("/re/msg")._2)) must be equalTo (helloWordXHTML)
      XML.load(new StringReader(client.delete("/re/msg/html")._2)) must be equalTo (helloWordXHTML)
      client.delete("/re/msg/text")._2 must be equalTo (helloWorldText)
      XML.load(new StringReader(client.delete("/re/msg/text/auth")._2)) must be equalTo (helloWordXHTML)
      client.delete("/re/msg/text/2")._2 must be equalTo (helloWorldText)
      client.delete("/re/msg/text/auth/2")._2 must be equalTo (helloWorldText)
      XML.load(new StringReader(client.delete("/re/msg/html/2")._2)) must be equalTo (helloWordXHTML)
      client.delete("/re/msg/text/all")._2 must be equalTo (helloWorldText)
      client.delete("/re/echo/123")._2 must be equalTo ("123")

      //
      // Basic HEAD tests for fixed paths
      //
      client.head("/msg") must be equalTo (HttpServletResponse.SC_OK)
      client.head("/msg/html") must be equalTo (HttpServletResponse.SC_OK)
      client.head("/msg/text")must be equalTo (HttpServletResponse.SC_OK)
      client.head("/msg/text/auth") must be equalTo (HttpServletResponse.SC_OK)
      client.head("/msg/text/2") must be equalTo (HttpServletResponse.SC_OK)
      client.head("/msg/text/auth/2") must be equalTo (HttpServletResponse.SC_OK)
      client.head("/msg/html/2") must be equalTo (HttpServletResponse.SC_OK)
      client.head("/msg/text/all") must be equalTo (HttpServletResponse.SC_OK)
      //
      // Basic tests for regex paths
      //
      client.head("/re/msg") must be equalTo (HttpServletResponse.SC_OK)
      client.head("/re/msg/html") must be equalTo (HttpServletResponse.SC_OK)
      client.head("/re/msg/text") must be equalTo (HttpServletResponse.SC_OK)
      client.head("/re/msg/text/auth") must be equalTo (HttpServletResponse.SC_OK)
      client.head("/re/msg/text/2") must be equalTo (HttpServletResponse.SC_OK)
      client.head("/re/msg/text/auth/2") must be (HttpServletResponse.SC_OK)
      client.head("/re/msg/html/2") must be equalTo (HttpServletResponse.SC_OK)
      client.head("/re/msg/text/all") must be equalTo (HttpServletResponse.SC_OK)
      client.head("/re/echo/123") must be equalTo (HttpServletResponse.SC_OK)


      //
      // Basic OPTIONS tests for fixed paths
      //
      XML.load(new StringReader(client.options("/msg")._2)) must be equalTo (helloWordXHTML)
      XML.load(new StringReader(client.options("/msg/html")._2)) must be equalTo (helloWordXHTML)
      client.options("/msg/text")._2 must be equalTo (helloWorldText)
      XML.load(new StringReader(client.options("/msg/text/auth")._2)) must be equalTo (helloWordXHTML)
      client.options("/msg/text/2")._2 must be equalTo (helloWorldText)
      client.options("/msg/text/auth/2")._2 must be equalTo (helloWorldText)
      XML.load(new StringReader(client.options("/msg/html/2")._2)) must be equalTo (helloWordXHTML)
      client.options("/msg/text/all")._2 must be equalTo (helloWorldText)
      //
      // Basic tests for regex paths
      //
      XML.load(new StringReader(client.options("/re/msg")._2)) must be equalTo (helloWordXHTML)
      XML.load(new StringReader(client.options("/re/msg/html")._2)) must be equalTo (helloWordXHTML)
      client.options("/re/msg/text")._2 must be equalTo (helloWorldText)
      XML.load(new StringReader(client.options("/re/msg/text/auth")._2)) must be equalTo (helloWordXHTML)
      client.options("/re/msg/text/2")._2 must be equalTo (helloWorldText)
      client.options("/re/msg/text/auth/2")._2 must be equalTo (helloWorldText)
      XML.load(new StringReader(client.options("/re/msg/html/2")._2)) must be equalTo (helloWordXHTML)
      client.options("/re/msg/text/all")._2 must be equalTo (helloWorldText)
      client.options("/re/echo/123")._2 must be equalTo ("123")

      //
      // Basic TRACE tests for fixed paths
      //
      XML.load(new StringReader(client.trace("/msg")._2)) must be equalTo (helloWordXHTML)
      XML.load(new StringReader(client.trace("/msg/html")._2)) must be equalTo (helloWordXHTML)
      client.trace("/msg/text")._2 must be equalTo (helloWorldText)
      XML.load(new StringReader(client.trace("/msg/text/auth")._2)) must be equalTo (helloWordXHTML)
      client.trace("/msg/text/2")._2 must be equalTo (helloWorldText)
      client.trace("/msg/text/auth/2")._2 must be equalTo (helloWorldText)
      XML.load(new StringReader(client.trace("/msg/html/2")._2)) must be equalTo (helloWordXHTML)
      client.trace("/msg/text/all")._2 must be equalTo (helloWorldText)
      //
      // Basic tests for regex paths
      //
      XML.load(new StringReader(client.trace("/re/msg")._2)) must be equalTo (helloWordXHTML)
      XML.load(new StringReader(client.trace("/re/msg/html")._2)) must be equalTo (helloWordXHTML)
      client.trace("/re/msg/text")._2 must be equalTo (helloWorldText)
      XML.load(new StringReader(client.trace("/re/msg/text/auth")._2)) must be equalTo (helloWordXHTML)
      client.trace("/re/msg/text/2")._2 must be equalTo (helloWorldText)
      client.trace("/re/msg/text/auth/2")._2 must be equalTo (helloWorldText)
      XML.load(new StringReader(client.trace("/re/msg/html/2")._2)) must be equalTo (helloWordXHTML)
      client.trace("/re/msg/text/all")._2 must be equalTo (helloWorldText)
      client.trace("/re/echo/123")._2 must be equalTo ("123")

      server.stop
    }
  }

}