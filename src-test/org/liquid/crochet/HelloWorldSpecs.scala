package org.liquid.crochet

import org.specs.Specification

/**
 * A basic simple specs for a Hello World! example
 *
 * @author Xavier Llora
 * @date Jan 9, 2010 at 9:12:58 PM
 * 
 */

object HelloWorldSpecs extends Specification {

  "A simple Hello World! example " should {

    "Return Hello World!" in {

      class HelloWorld extends CrochetServlet {

        // TODO Figure out how to specify mime type of the response
        get("/msg") {
          response.getWriter.println("Hello World")
        }


        get("""/([0-9]+/?)+""".r) {
          elements.foldLeft(0)(_+_)
        }

      }
      
      true must beTrue
    }
  }

}