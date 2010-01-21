package crochet

import org.specs.Specification
import crochet._
import crochet.net.utils._

/**
 * A simple Hello World using Crochet
 *
 * @author Xavier Llora
 * @date Jan 18, 2010 at 4:40:23 PM
 *
 */

class SimpleHelloWorldSpecs extends Specification {

  "Hello World!" should {

    "return \"Hello World!\" plaint.text response" in {

      val TEST_PORT = 8080

      //
      // The basic crochet hello world servlet
      //
      val helloWorldService = new CrochetServlet {

        get("/message","text/plain") { "Hello World!" }

      }

      //
      // Start a server, pull the content, check the return
      //
      val client = HttpClient("http", "localhost", TEST_PORT)
      val server = CrochetServer(TEST_PORT,helloWorldService)
      server.start
      val ret = client.get("/message")
      ret._1 must be equalTo(200)
      ret._2 must be equalTo("Hello World!")
      server.stop
    }

  }
}