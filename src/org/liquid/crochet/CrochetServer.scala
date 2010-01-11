package org.liquid.crochet

import org.mortbay.jetty.Server
import org.mortbay.jetty.servlet.{ServletHolder, Context}

/**
 * A basic server to 
 *
 * @author Xavier Llora
 * @date Jan 10, 2010 at 5:29:48 PM
 * 
 */

class CrochetServer ( port:Int, crochet:CrochetServlet ) {

  protected val server = new Server(port)
  protected val   root = new Context(server,"/",Context.SESSIONS)
  root.addServlet(new ServletHolder(crochet), "/*")

  def start = server.start
  def  stop = server.stop
  def  join = server.join

  def    go = { server.start; server.join }

}