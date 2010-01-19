package org.liquid.crochet

/**
 * A basic starting self sufficient server
 *
 * @author Xavier Llora
 * @date Jan 18, 2010 at 5:26:35 PM
 * 
 */
trait Crochet extends CrochetServlet {

  def on(port:Int) = new CrochetServer(port,this).go
  
}
