/**
 * A simple script running a Hello World server
 *
 * @author Xavier Llora
 * @date Jan 18, 2010 at 5:49:34 PM
 * 
 */

import crochet._

new Crochet {

  get("/message","text/plain") { "Hello World!" }

} on 8080