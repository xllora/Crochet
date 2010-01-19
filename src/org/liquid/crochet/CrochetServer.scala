package org.liquid.crochet

import org.mortbay.jetty.Server
import org.mortbay.jetty.handler.ResourceHandler
import java.io.File
import org.mortbay.jetty.servlet.{DefaultServlet, ServletHolder, Context}

/**
 * This class implements the a basic server to run crochet servlets
 *
 * @author Xavier Llora
 * @date Jan 9, 2010 at 5:13:03 PM
 *
 */
class CrochetServer ( val port:Int, val crochet:CrochetServlet, val folder:Option[String], val path:Option[String] ) {

  protected val server = new Server(port)

  if ( folder.isDefined && path.isDefined ) {
    val staticContext = new Context(server,path.get,Context.NO_SESSIONS)
    val fileServlet = new DefaultServlet
    staticContext setResourceBase folder.get
    staticContext.addServlet(new ServletHolder(fileServlet), "/*")
  }

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

  def apply( port:Int, crochet:CrochetServlet ) = new CrochetServer(port,crochet,None,None)

  def apply( port:Int, crochet:CrochetServlet, folder:String, path:String ) = new CrochetServer(port,crochet,Some(folder),Some(path))

  def apply( port:Int, crochet:CrochetServlet, folder:Option[String], path:Option[String] ) = new CrochetServer(port,crochet,folder,path)

  def unapply(cs:CrochetServer) : Option[(Int,CrochetServlet,Option[String],Option[String])] = Some((cs.port,cs.crochet,cs.folder,cs.path))

}