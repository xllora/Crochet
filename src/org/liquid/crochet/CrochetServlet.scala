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
  // Implicit casting
  //
  protected implicit def stringToInt    (s:String) = Integer parseInt    s
  protected implicit def stringToLong   (s:String) = Long    parseLong   s
  protected implicit def stringToFloat  (s:String) = Float   parseFloat  s
  protected implicit def stringToDouble (s:String) = Double  parseDouble s
  protected implicit def stringToUUID   (s:String) = UUID    fromString  s

  private def extractHeaderMap(request:HttpServletRequest) = {
    val nameEnum = request.getHeaderNames
    var map = Map[String,String]()
    while ( nameEnum.hasMoreElements ) {
      val key = nameEnum.nextElement.toString
      map += key->request.getHeader(key)
    }
    map
  }

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
                  if (dispatcherMap(method) contains pathURI)
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
                    else {
                      //
                      // May be on the regular expression pool
                      //
                      val   list = dispatcherRegexMap(method)
                      val target = list.find(
                            (t:Tuple4[Regex,()=>String,()=>Boolean,()=>Any])=> {
                              val r = t._1
                              pathURI match {
                                case r(_) => true
                                case _    => false
                              }
                            }
                      )
                      target match {
                         case Some(t) if  t._3() => // Matched and guard satisfied
                                                    elementsVal.withValue(extractMatches(pathURI,t._1)) {
                                                      response setStatus HttpServletResponse.SC_OK
                                                      response setContentType t._2()
                                                      response.getWriter.print(t._4().toString)
                                                    }
                         case Some(t) if !t._3() => // Guard not satisfied
                                                    elementsVal.withValue(List[String]()) {
                                                      guardViolation(path,request,response)
                                                    }
                         case _ => // Request could not be found either way
                                   elementsVal.withValue(List[String]()) {
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
      case ex: NoSuchElementException =>  response.getWriter.println("requesting " + request.getMethod + " " + request.getRequestURI + " but only have "+ Map)
      case s => response.getWriter.println("Something whent wrong "+s.toString)
    }
  }

  def extractMatches(pathURI:String,r:Regex):List[String] = {
    r.unapplySeq(pathURI) match {
      case Some(l) => l
      case None    => List[String]()
    }
  }

  def guardViolation(path: String, request: HttpServletRequest, response: HttpServletResponse) = {
    response setStatus HttpServletResponse.SC_EXPECTATION_FAILED
    response setContentType "text/html"
    val message = errorCodeMap(HttpServletResponse.SC_EXPECTATION_FAILED)()
    val  output = _error_template(Map("msg" -> message))
    response.getWriter.print(output)
  }


  def requestNotFound(path: String, request: HttpServletRequest, response: HttpServletResponse) = {
    response setStatus HttpServletResponse.SC_NOT_FOUND
    response setContentType "text/html"
    val message = errorCodeMap(HttpServletResponse.SC_NOT_FOUND)()
    val  output = _error_template(Map("msg" -> message))
    response.getWriter.print(output)
  }


  def get(path: String)(fun: => Any) =
    dispatcherMap("GET") + (path -> (() => "text/html", () => true, () => fun))

  def get(path: String, mimeType: String)(fun: => Any) =
    dispatcherMap("GET") + (path -> (() => mimeType, () => true, () => fun))

  def get(path: String, guard: => Boolean)(fun: => Any) =
    dispatcherMap("GET") + (path -> (() => "text/html", () => guard, () => fun))

  def get(path: String, mimeType: String, guard: => Boolean)(fun: => Any) =
    dispatcherMap("GET") + (path -> (() => mimeType, () => guard, () => fun))


  def get(re: Regex)(fun: => Any) =
    dispatcherRegexMap("GET") = dispatcherRegexMap("GET") ::: List((re, () => "text/html", () => true, () => fun))

  def get(re: Regex, mimeType: String)(fun: => Any) =
    dispatcherRegexMap("GET") = dispatcherRegexMap("GET") ::: List((re, () => mimeType, () => true, () => fun))

  def get(re: Regex, guard: => Boolean)(fun: => Any) =
    dispatcherRegexMap("GET") = dispatcherRegexMap("GET") ::: List((re, () => "text/html", () => guard, () => fun))

  def get(re: Regex, mimeType: String, guard: => Boolean)(fun: => Any) =
    dispatcherRegexMap("GET") = dispatcherRegexMap("GET") ::: List((re, () => mimeType, () => guard, () => fun))


}