package org.liquid.crochet

import org.mortbay.jetty.Server
import org.mortbay.jetty.servlet.{ServletHolder, Context}

/**
 * This class implements the a basic server to run crochet servlets
 *
 * @author Xavier Llora
 * @date Jan 9, 2010 at 5:13:03 PM
 *
 */
class CrochetServer ( val port:Int, val crochet:CrochetServlet ) {

  protected val server = new Server(port)
  protected val   root = new Context(server,"/",Context.SESSIONS)
  root.addServlet(new ServletHolder(crochet), "/*")

  def start = server.start
  def  stop = server.stop
  def  join = server.join

  def    go = { server.start; server.join }

}

/**
 * The companion object for the basic server
 *
 * @author Xavier Llora
 * @date Jan 9, 2010 at 5:13:03 PM
 *
 */
object CrochetServer {

  def apply( port:Int, crochet:CrochetServlet ) = new CrochetServer(port,crochet)

  def unapply(cs:CrochetServer) : Option[(Int,CrochetServlet)] = Some((cs.port,cs.crochet))

}