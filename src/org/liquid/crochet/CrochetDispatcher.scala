package org.liquid.crochet

import scala.collection.mutable.{Map=>MMap}

import javax.servlet.http.{HttpServletResponse, HttpServletRequest, HttpServlet}
import util.matching.Regex

/**
 * The basic dispatcher for Chrochet servlet requests
 *
 * @author Xavier Llora
 * @date Jan 13, 2010 at 3:15:21 PM
 * 
 */

trait CrochetDispatcher extends HttpServlet with CrochetDynamicEnvironment {

  //
  // The main structures used as dispatchers
  //
  protected var dispatcherMap = MMap[String,MMap[String,(()=>String,()=>Boolean,()=>Any)]]()
  dispatcherMap ++ List(
    "GET"     -> MMap[String, (() => String, () => Boolean, () => Any)](),
    "POST"    -> MMap[String, (() => String, () => Boolean, () => Any)](),
    "PUT"     -> MMap[String, (() => String, () => Boolean, () => Any)](),
    "DELETE"  -> MMap[String, (() => String, () => Boolean, () => Any)](),
    "HEAD"    -> MMap[String, (() => String, () => Boolean, () => Any)](),
    "OPTION"  -> MMap[String, (() => String, () => Boolean, () => Any)](),
    "TRACE"   -> MMap[String, (() => String, () => Boolean, () => Any)]()
    )

  protected var dispatcherRegexMap = MMap[String,List[(Regex,()=>String,()=>Boolean,()=>Any)]]()
  dispatcherRegexMap ++ List(
    "GET"     -> List[(Regex, () => String, () => Boolean, () => Any)](),
    "POST"    -> List[(Regex, () => String, () => Boolean, () => Any)](),
    "PUT"     -> List[(Regex, () => String, () => Boolean, () => Any)](),
    "DELETE"  -> List[(Regex, () => String, () => Boolean, () => Any)](),
    "HEAD"    -> List[(Regex, () => String, () => Boolean, () => Any)](),
    "OPTION"  -> List[(Regex, () => String, () => Boolean, () => Any)](),
    "TRACE"   -> List[(Regex, () => String, () => Boolean, () => Any)]()
    )
 
  //
  // Dispatch a request to this Crochet servlet
  //
  override def service(request: HttpServletRequest, response: HttpServletResponse) = {
    val    method = request.getMethod
    val   pathURI = request.getRequestURI
    val headerMap = extractHeaderMap(request)
    val   (ms,ma) = extractParameters(request)

    try {
      pathVal.withValue(pathURI) {
        requestVal.withValue(request) {
          responseVal.withValue(response) {
            headerVal.withValue(headerMap) {
              paramVal.withValue(ms) {
                paramMapVal.withValue(ma) {
                  //
                  // Check if it a regular path.
                  //
                  if (dispatcherMap(method) contains pathURI) {
                    elementsVal.withValue(List[String]()) {
                      val (mime, guard, function) = dispatcherMap(method)(pathURI)
                      if (guard()) {
                        // Found and guard satisfied
                        response.setStatus(HttpServletResponse.SC_OK)
                        response.setContentType(mime())
                        response.getWriter.print(function().toString)
                      }
                      else {
                        // Found and guard not satisfied
                        guardViolation(path,request,response)
                      }
                    }
                  }
                  else {
                      //
                      // May be on the regular expression pool
                      //
                      val   list = dispatcherRegexMap(method)
                      val target = list.find(
                            (t:Tuple4[Regex,()=>String,()=>Boolean,()=>Any])=> {
                              val r = t._1
                              r.findFirstIn(pathURI) match {
                                case Some(_) => true
                                case None    => false
                              }
                            }
                      )
                      val elems = if (target==None) List[String]() else extractMatches(pathURI,target.get._1)
                      elementsVal.withValue(elems) {
                        target match {
                           case Some(t) if  t._3() => // Matched and guard satisfied
                                                      response setStatus HttpServletResponse.SC_OK
                                                      response setContentType t._2()
                                                      response.getWriter.print(t._4().toString)

                           case Some(t) if !t._3() => // Guard not satisfied
                                                      guardViolation(path,request,response)

                           case _ => // Request could not be found either way
                                     requestNotFound(path,request,response)

                        }
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
      case e => internalServerError(pathURI,request,response,e)
    }
  }


    //
    // Extract parameters form a request
    //
    private def extractParameters(request:HttpServletRequest) = {
      val     names = request.getParameterNames
      var mapSingle = Map[String,String]()
      var  mapArray = Map[String,Array[String]]()
      while (names.hasMoreElements) {
        val key = names.nextElement.toString
        mapSingle += key->request.getParameter(key)
        mapArray  += key->request.getParameterValues(key)
      }
      (mapSingle,mapArray)
    }

    //
    // Extract the matches of a URL
    //
    def extractMatches(pathURI:String,r:Regex):List[String] = {
      r.unapplySeq(pathURI) match {
        case Some(l) => l
        case None    => List[String]()
      }
    }

  //
  // Creates a map with the header options
  //
  private def extractHeaderMap(request:HttpServletRequest) = {
    val nameEnum = request.getHeaderNames
    var map = Map[String,String]()
    while ( nameEnum.hasMoreElements ) {
      val key = nameEnum.nextElement.toString
      map += key->request.getHeader(key)
    }
    map
  }

  def guardViolation(path: String, request: HttpServletRequest, response: HttpServletResponse)
  def internalServerError(path: String, request: HttpServletRequest, response: HttpServletResponse, e: Throwable)
  def requestNotFound(path: String, request: HttpServletRequest, response: HttpServletResponse)
}