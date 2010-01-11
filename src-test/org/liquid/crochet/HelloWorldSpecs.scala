package org.liquid.crochet

import scala.concurrent.ops.spawn
import org.specs.Specification

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
        get("/msg") {
          <html>
            <head>
              <title>Hello World!</title>
            </head>
            <body>
              <b>Hello World!</b>
            </body>
          </html>
        }

        get("/msg/text", {header("Accept").contains("text/plain")}) {
          "Hello World!"
        }

        get("/msg/html", "text/html") {
          <html>
            <head>
              <title>Hello World!</title>
            </head>
            <body>
              <b>Hello World!</b>
            </body>
          </html>
        }

        get("/msg/text/guard", "text/html", {header("Accept").contains("text/plain")}) {
          "Hello World!"
        }

        _404 { () => path+" not found" }

      }

      val client = NetUtils("http","localhost",TEST_PORT)
      val server = new CrochetServer(TEST_PORT,hwc)
      spawn { server.go }
      Thread.sleep(500)
      println(client get "/msg")
      server.stop
    }
  }

}