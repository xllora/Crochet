package org.liquid.crochet

import scala.collection.mutable.{Map=>MMap}
import java.lang.{Long,Float,Double}
import java.util.UUID
import javax.servlet.http.{HttpServletRequest, HttpServletResponse, HttpServlet}
import util.matching.Regex

/**
 * This class implements the main routing machinery for Crochet
 *
 * @author Xavier Llora
 * @date Jan 9, 2010 at 5:13:03 PM
 * 
 */

abstract class CrochetServlet extends HttpServlet with DynamicEnvironment with ResponseCodes {

  //
  // The main structure used as dispatcher
  //
  protected var dispatcherMap = MMap[String,MMap[String,(()=>String,()=>Boolean,()=>Any)]]()
  dispatcherMap ++ List(
    "GET"     -> MMap[String, (() => String, () => Boolean, () => Any)](),
    "POST"    -> MMap[String, (() => String, () => Boolean, () => Any)](),
    "PUT"     -> MMap[String, (() => String, () => Boolean, () => Any)](),
    "DELETE"  -> MMap[String, (() => String, () => Boolean, () => Any)](),
    "HEAD"    -> MMap[String, (() => String, () => Boolean, () => Any)](),
    "OPTION" -> MMap[String, (() => String, () => Boolean, () => Any)](),
    "TRACE"   -> MMap[String, (() => String, () => Boolean, () => Any)]()
    )

  //
  // Implicit casting
  //
  protected implicit def stringToInt    (s:String) = Integer parseInt    s
  protected implicit def stringToLong   (s:String) = Long    parseLong   s
  protected implicit def stringToFloat  (s:String) = Float   parseFloat  s
  protected implicit def stringToDouble (s:String) = Double  parseDouble s
  protected implicit def stringToUUID   (s:String) = UUID    fromString  s


  override def service(request: HttpServletRequest, response: HttpServletResponse) = {
    val  method = request.getMethod
    val pathURI = request.getRequestURI

    try {
      pathVal.withValue(pathURI) {
        requestVal.withValue(request) {
          responseVal.withValue(response) {
            messageVal.withValue(Map[String, String]()) {
              headerVal.withValue(Map[String, String]()) {
                paramVal.withValue(Map[String, String]()) {
                  paramMapVal.withValue(Map[String, Array[Any]]()) {
                    elementsVal.withValue(List[String]()) {
                      val action = dispatcherMap(method)(pathURI)
                      val mime = action._1
                      val guard = action._2
                      val function = action._3
                      response.setStatus(HttpServletResponse.SC_OK)
                      response.setContentType(mime())
                      response.getWriter.println(function().toString)
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
    catch {
      case ex: NoSuchElementException => response.getWriter.println("requesting " + request.getMethod + " " + request.getRequestURI + " but only have "
              + Map)
    }
  }

  //  override def doGet (request:HttpServletRequest, response:HttpServletResponse) = {}
//  override def doDelete (request:HttpServletRequest, response:HttpServletResponse) = {}
//  override def doHead  (request:HttpServletRequest, response:HttpServletResponse) = {}
//  override def doOptions  (request:HttpServletRequest, response:HttpServletResponse) = {}
//  override def doPost (request:HttpServletRequest, response:HttpServletResponse) = {}
//  override def doPut (request:HttpServletRequest, response:HttpServletResponse) = {}
//  override def doTrace (request:HttpServletRequest, response:HttpServletResponse) = {}


  def get ( path:String )(fun: => Any ) =
    dispatcherMap("GET")+(path->(()=>"text/html",()=>true,()=>fun))

  def get ( path:String, mimeType:String )(fun: => Any ) =
    dispatcherMap("GET")+(path->(()=>mimeType,()=>true,()=>fun))

  def get ( path:String, guard: =>Boolean )(fun: => Any ) =
    dispatcherMap("GET")+(path->(()=>"text/html",()=>guard,()=>fun))

  def get ( path:String, mimeType:String, guard: =>Boolean )(fun: => Any ) =
    dispatcherMap("GET")+(path->(()=>mimeType,()=>guard,()=>fun))

  def get ( path:Regex )(fun: => Any ) = {}
  def get ( path:Regex, mimeType:String )(fun: => Any ) = {}
  def get ( path:Regex, guard: =>Boolean )(fun: => Any ) = {}
  def get ( path:Regex, mimeType:String, guard: =>Boolean )(fun: => Any ) = {}



}