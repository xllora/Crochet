package org.liquid.crochet

/**
 * A basic starting self sufficient test server
 *
 * @author Xavier Llora
 * @date Jan 18, 2010 at 5:26:35 PM
 * 
 */
class Crochet(cs:CrochetServlet) extends CrochetServer(8080,cs) {

  go

}

/**
 * The companion object for basic starting self sufficient test server
 *
 * @author Xavier Llora
 * @date Jan 18, 2010 at 5:26:35 PM
 *
 */
object Crochet {

  def apply(cs:CrochetServlet) = new Crochet(cs)

}

