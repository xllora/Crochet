package org.liquid.crochet

import scala.concurrent.ops.spawn
import org.specs.Specification
import java.io.StringReader
import scala.xml.XML


/**
 * A basic simple specs for a Hello World! example
 *
 * @author Xavier Llora
 * @date Jan 9, 2010 at 9:12:58 PM
 *
 */

object HelloWorldSpecs extends Specification {

  protected val TEST_PORT = 8080

  "A simple Hello World! example " should {

    "Return Hello World!" in {

      val hwc = new CrochetServlet {

        val helloWordXHTML = <html>
          <head>
            <title>Hello World!</title>
          </head>
          <body>
            <b>Hello World!</b>
          </body>
        </html>

        val helloWorldText = "Hello World!"

        get("/msg") {
           helloWordXHTML
        }

        get("/msg/html", "text/html") {
          helloWordXHTML
        }

        get("/msg/text", {header("Host")=="localhost:"+TEST_PORT} ) {
          helloWorldText
        }

        get("/msg/text/guard", "text/plain", {header("Host")=="localhost:"+TEST_PORT}) {
          helloWorldText
        }

        get("/re/msg".r) {
           helloWordXHTML
        }

        get("/re/msg/html".r, "text/html") {
          helloWordXHTML
        }

        get("/re/msg/text".r, {header("Accept").contains("text/plain")}) {
          helloWorldText
        }

        get("/re/msg/text/guard".r, "text/html", {header("Accept").contains("text/plain")}) {
          helloWorldText
        }

        _404 { () => path+" not found" }
        _417 { () => "Guard to "+path+" failed" }

      }

      val client = NetUtils("http","localhost",TEST_PORT)
      val server = new CrochetServer(TEST_PORT,hwc)
      spawn { server.go }
      Thread.sleep(500)
      XML.load(new StringReader(client.get("/msg")._2)) must be equalTo(hwc.helloWordXHTML)
      XML.load(new StringReader(client.get("/msg/html")._2)) must be equalTo(hwc.helloWordXHTML)
      client.get("/msg/text")._2 must be equalTo(hwc.helloWorldText)
      client.get("/msg/text/guard")._2 must be equalTo(hwc.helloWorldText)
      // TODO Test the regular expression based ones
      server.stop
    }
  }

}