/**
 * A simple script running a Hello World server
 *
 * @author Xavier Llora
 * @date Jan 18, 2010 at 5:49:34 PM
 * 
 */

import org.liquid.crochet._

Crochet ( new CrochetServlet {

  get("/message","text/plain") { "Hello World!" }

})