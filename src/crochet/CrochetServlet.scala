package crochet

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
trait CrochetServlet extends HttpServlet with CrochetDispatcher with CrochetResponseCodes {
  
  //
  // Implicit casting
  //
  protected implicit def stringToInt    (s:String) = Integer parseInt    s
  protected implicit def stringToLong   (s:String) = Long    parseLong   s
  protected implicit def stringToFloat  (s:String) = Float   parseFloat  s
  protected implicit def stringToDouble (s:String) = Double  parseDouble s
  protected implicit def stringToUUID   (s:String) = UUID    fromString  s
  

  //
  // Internal server error thrown while processing the request
  //
  def internalServerError(path: String, request: HttpServletRequest, response: HttpServletResponse, e: Throwable) = {
    reportError(
        HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
        ()=> "Internal server error on "+path,
        path, request, response, Some(e)
    )
  }

  //
  // The requested was not authorized
  //
  def unauthorizedAccess(path: String, user:Option[String],request: HttpServletRequest, response: HttpServletResponse) = {
    reportError(
        HttpServletResponse.SC_UNAUTHORIZED,
        ()=> "Unauthorized access to "+path+" by user "+user.getOrElse("anonymous"),
        path, request, response, None
    )
  }

  //
  // The requested path could not be found
  //
  def requestNotFound(path: String, request: HttpServletRequest, response: HttpServletResponse) = {
    reportError(
        HttpServletResponse.SC_NOT_FOUND,
        ()=> "Not found: "+path,
        path, request, response, None
    )
  }


  //
  // GET methods
  //
  def get(path: String)(fun: => Any) =
    dispatcherMap("GET") + (path -> (() => "text/html", () => true, (a:String,b:Option[String])=>true, () => fun))

  def get(path: String, mimeType: String)(fun: => Any) =
    dispatcherMap("GET") + (path -> (() => mimeType, () => true, (a:String,b:Option[String])=>true, () => fun))

  def get(path: String, guard: => Boolean)(fun: => Any) =
    dispatcherMap("GET") + (path -> (() => "text/html", () => guard, (a:String,b:Option[String])=>true, () => fun))

  def get(path: String, auth: (String,Option[String])=>Boolean)(fun: => Any) =
    dispatcherMap("GET") + (path -> (() => "text/html", () => true, auth, () => fun))

  def get(path: String, mimeType: String, guard: => Boolean)(fun: => Any) =
    dispatcherMap("GET") + (path -> (() => mimeType, () => guard, (a:String,b:Option[String])=>true, () => fun))

  def get(path: String, mimeType: String, auth: (String,Option[String])=>Boolean)(fun: => Any) =
    dispatcherMap("GET") + (path -> (() => mimeType, () => true, auth, () => fun))

  def get(path: String, guard: => Boolean, auth: (String,Option[String])=>Boolean)(fun: => Any) =
    dispatcherMap("GET") + (path -> (() => "text/html", () => guard, auth, () => fun))

  def get(path: String, mimeType: String, guard: => Boolean, auth: (String,Option[String])=>Boolean)(fun: => Any) =
    dispatcherMap("GET") + (path -> (() => mimeType, () => guard, auth, () => fun))


  def get(re: Regex)(fun: => Any) =
    dispatcherRegexMap("GET") = dispatcherRegexMap("GET") ::: List((re, () => "text/html", () => true, (a:String,b:Option[String])=>true, () => fun))

  def get(re: Regex, mimeType: String)(fun: => Any) =
    dispatcherRegexMap("GET") = dispatcherRegexMap("GET") ::: List((re, () => mimeType, () => true, (a:String,b:Option[String])=>true, () => fun))

  def get(re: Regex, guard: => Boolean)(fun: => Any) =
    dispatcherRegexMap("GET") = dispatcherRegexMap("GET") ::: List((re, () => "text/html", () => guard, (a:String,b:Option[String])=>true, () => fun))

  def get(re: Regex, auth: (String,Option[String])=>Boolean)(fun: => Any) =
    dispatcherRegexMap("GET") = dispatcherRegexMap("GET") ::: List((re, () => "text/html", () => true, auth, () => fun))

  def get(re: Regex, mimeType: String, guard: => Boolean)(fun: => Any) =
    dispatcherRegexMap("GET") = dispatcherRegexMap("GET") ::: List((re, () => mimeType, () => guard, (a:String,b:Option[String])=>true, () => fun))

  def get(re: Regex, mimeType: String, auth: (String,Option[String])=>Boolean)(fun: => Any) =
    dispatcherRegexMap("GET") = dispatcherRegexMap("GET") ::: List((re, () => mimeType, () => true, auth, () => fun))

  def get(re: Regex, guard: => Boolean, auth: (String,Option[String])=>Boolean)(fun: => Any) =
    dispatcherRegexMap("GET") = dispatcherRegexMap("GET") ::: List((re, () => "text/html", () => guard, auth, () => fun))

  def get(re: Regex, mimeType: String, guard: => Boolean, auth: (String,Option[String])=>Boolean)(fun: => Any) =
    dispatcherRegexMap("GET") = dispatcherRegexMap("GET") ::: List((re, () => mimeType, () => guard, auth, () => fun))




  //
  // POST methods
  //
  def post(path: String)(fun: => Any) =
    dispatcherMap("POST") + (path -> (() => "text/html", () => true, (a:String,b:Option[String])=>true, () => fun))

  def post(path: String, mimeType: String)(fun: => Any) =
    dispatcherMap("POST") + (path -> (() => mimeType, () => true, (a:String,b:Option[String])=>true, () => fun))

  def post(path: String, guard: => Boolean)(fun: => Any) =
    dispatcherMap("POST") + (path -> (() => "text/html", () => guard, (a:String,b:Option[String])=>true, () => fun))

  def post(path: String, auth: (String,Option[String])=>Boolean)(fun: => Any) =
    dispatcherMap("POST") + (path -> (() => "text/html", () => true, auth, () => fun))

  def post(path: String, mimeType: String, guard: => Boolean)(fun: => Any) =
    dispatcherMap("POST") + (path -> (() => mimeType, () => guard, (a:String,b:Option[String])=>true, () => fun))

  def post(path: String, mimeType: String, auth: (String,Option[String])=>Boolean)(fun: => Any) =
     dispatcherMap("POST") + (path -> (() => mimeType, () => true, auth, () => fun))

  def post(path: String, guard: => Boolean, auth: (String,Option[String])=>Boolean)(fun: => Any) =
    dispatcherMap("POST") + (path -> (() => "text/html", () => guard, auth, () => fun))

  def post(path: String, mimeType: String, guard: => Boolean, auth: (String,Option[String])=>Boolean)(fun: => Any) =
    dispatcherMap("POST") + (path -> (() => mimeType, () => guard, auth, () => fun))


  def post(re: Regex)(fun: => Any) =
    dispatcherRegexMap("POST") = dispatcherRegexMap("POST") ::: List((re, () => "text/html", () => true, (a:String,b:Option[String])=>true, () => fun))

  def post(re: Regex, mimeType: String)(fun: => Any) =
    dispatcherRegexMap("POST") = dispatcherRegexMap("POST") ::: List((re, () => mimeType, () => true, (a:String,b:Option[String])=>true, () => fun))

  def post(re: Regex, guard: => Boolean)(fun: => Any) =
    dispatcherRegexMap("POST") = dispatcherRegexMap("POST") ::: List((re, () => "text/html", () => guard, (a:String,b:Option[String])=>true, () => fun))

  def post(re: Regex, auth: (String,Option[String])=>Boolean)(fun: => Any) =
    dispatcherRegexMap("POST") = dispatcherRegexMap("POST") ::: List((re, () => "text/html", () => true, auth, () => fun))

  def post(re: Regex, mimeType: String, guard: => Boolean)(fun: => Any) =
    dispatcherRegexMap("POST") = dispatcherRegexMap("POST") ::: List((re, () => mimeType, () => guard, (a:String,b:Option[String])=>true, () => fun))

  def post(re: Regex, mimeType: String, auth: (String,Option[String])=>Boolean)(fun: => Any) =
    dispatcherRegexMap("POST") = dispatcherRegexMap("POST") ::: List((re, () => mimeType, () => true, auth, () => fun))

  def post(re: Regex, guard: => Boolean, auth: (String,Option[String])=>Boolean)(fun: => Any) =
    dispatcherRegexMap("POST") = dispatcherRegexMap("POST") ::: List((re, () => "text/html", () => guard, auth, () => fun))

  def post(re: Regex, mimeType: String, guard: => Boolean, auth: (String,Option[String])=>Boolean)(fun: => Any) =
    dispatcherRegexMap("POST") = dispatcherRegexMap("POST") ::: List((re, () => mimeType, () => guard, auth, () => fun))



  //
  // PUT methods
  //
  def put(path: String)(fun: => Any) =
    dispatcherMap("PUT") + (path -> (() => "text/html", () => true, (a:String,b:Option[String])=>true, () => fun))

  def put(path: String, mimeType: String)(fun: => Any) =
    dispatcherMap("PUT") + (path -> (() => mimeType, () => true, (a:String,b:Option[String])=>true, () => fun))

  def put(path: String, guard: => Boolean)(fun: => Any) =
    dispatcherMap("PUT") + (path -> (() => "text/html", () => guard, (a:String,b:Option[String])=>true, () => fun))

  def put(path: String, auth: (String,Option[String])=>Boolean)(fun: => Any) =
    dispatcherMap("PUT") + (path -> (() => "text/html", () => true, auth, () => fun))

  def put(path: String, mimeType: String, guard: => Boolean)(fun: => Any) =
    dispatcherMap("PUT") + (path -> (() => mimeType, () => guard, (a:String,b:Option[String])=>true, () => fun))

  def put(path: String, mimeType: String, auth: (String,Option[String])=>Boolean)(fun: => Any) =
     dispatcherMap("PUT") + (path -> (() => mimeType, () => true, auth, () => fun))

  def put(path: String, guard: => Boolean, auth: (String,Option[String])=>Boolean)(fun: => Any) =
    dispatcherMap("PUT") + (path -> (() => "text/html", () => guard, auth, () => fun))

  def put(path: String, mimeType: String, guard: => Boolean, auth: (String,Option[String])=>Boolean)(fun: => Any) =
    dispatcherMap("PUT") + (path -> (() => mimeType, () => guard, auth, () => fun))


  def put(re: Regex)(fun: => Any) =
    dispatcherRegexMap("PUT") = dispatcherRegexMap("PUT") ::: List((re, () => "text/html", () => true, (a:String,b:Option[String])=>true, () => fun))

  def put(re: Regex, mimeType: String)(fun: => Any) =
    dispatcherRegexMap("PUT") = dispatcherRegexMap("PUT") ::: List((re, () => mimeType, () => true, (a:String,b:Option[String])=>true, () => fun))

  def put(re: Regex, guard: => Boolean)(fun: => Any) =
    dispatcherRegexMap("PUT") = dispatcherRegexMap("PUT") ::: List((re, () => "text/html", () => guard, (a:String,b:Option[String])=>true, () => fun))

  def put(re: Regex, auth: (String,Option[String])=>Boolean)(fun: => Any) =
    dispatcherRegexMap("PUT") = dispatcherRegexMap("PUT") ::: List((re, () => "text/html", () => true, auth, () => fun))

  def put(re: Regex, mimeType: String, guard: => Boolean)(fun: => Any) =
    dispatcherRegexMap("PUT") = dispatcherRegexMap("PUT") ::: List((re, () => mimeType, () => guard, (a:String,b:Option[String])=>true, () => fun))

  def put(re: Regex, mimeType: String, auth: (String,Option[String])=>Boolean)(fun: => Any) =
    dispatcherRegexMap("PUT") = dispatcherRegexMap("PUT") ::: List((re, () => mimeType, () => true, auth, () => fun))

  def put(re: Regex, guard: => Boolean, auth: (String,Option[String])=>Boolean)(fun: => Any) =
    dispatcherRegexMap("PUT") = dispatcherRegexMap("PUT") ::: List((re, () => "text/html", () => guard, auth, () => fun))

  def put(re: Regex, mimeType: String, guard: => Boolean, auth: (String,Option[String])=>Boolean)(fun: => Any) =
    dispatcherRegexMap("PUT") = dispatcherRegexMap("PUT") ::: List((re, () => mimeType, () => guard, auth, () => fun))



  //
  // DELETE methods
  //
  def delete(path: String)(fun: => Any) =
    dispatcherMap("DELETE") + (path -> (() => "text/html", () => true, (a:String,b:Option[String])=>true, () => fun))

  def delete(path: String, mimeType: String)(fun: => Any) =
    dispatcherMap("DELETE") + (path -> (() => mimeType, () => true, (a:String,b:Option[String])=>true, () => fun))

  def delete(path: String, guard: => Boolean)(fun: => Any) =
    dispatcherMap("DELETE") + (path -> (() => "text/html", () => guard, (a:String,b:Option[String])=>true, () => fun))

  def delete(path: String, auth: (String,Option[String])=>Boolean)(fun: => Any) =
    dispatcherMap("DELETE") + (path -> (() => "text/html", () => true, auth, () => fun))

  def delete(path: String, mimeType: String, guard: => Boolean)(fun: => Any) =
    dispatcherMap("DELETE") + (path -> (() => mimeType, () => guard, (a:String,b:Option[String])=>true, () => fun))

  def delete(path: String, mimeType: String, auth: (String,Option[String])=>Boolean)(fun: => Any) =
     dispatcherMap("DELETE") + (path -> (() => mimeType, () => true, auth, () => fun))

  def delete(path: String, guard: => Boolean, auth: (String,Option[String])=>Boolean)(fun: => Any) =
    dispatcherMap("DELETE") + (path -> (() => "text/html", () => guard, auth, () => fun))

  def delete(path: String, mimeType: String, guard: => Boolean, auth: (String,Option[String])=>Boolean)(fun: => Any) =
    dispatcherMap("DELETE") + (path -> (() => mimeType, () => guard, auth, () => fun))


  def delete(re: Regex)(fun: => Any) =
    dispatcherRegexMap("DELETE") = dispatcherRegexMap("DELETE") ::: List((re, () => "text/html", () => true, (a:String,b:Option[String])=>true, () => fun))

  def delete(re: Regex, mimeType: String)(fun: => Any) =
    dispatcherRegexMap("DELETE") = dispatcherRegexMap("DELETE") ::: List((re, () => mimeType, () => true, (a:String,b:Option[String])=>true, () => fun))

  def delete(re: Regex, guard: => Boolean)(fun: => Any) =
    dispatcherRegexMap("DELETE") = dispatcherRegexMap("DELETE") ::: List((re, () => "text/html", () => guard, (a:String,b:Option[String])=>true, () => fun))

  def delete(re: Regex, auth: (String,Option[String])=>Boolean)(fun: => Any) =
    dispatcherRegexMap("DELETE") = dispatcherRegexMap("DELETE") ::: List((re, () => "text/html", () => true, auth, () => fun))

  def delete(re: Regex, mimeType: String, guard: => Boolean)(fun: => Any) =
    dispatcherRegexMap("DELETE") = dispatcherRegexMap("DELETE") ::: List((re, () => mimeType, () => guard, (a:String,b:Option[String])=>true, () => fun))

  def delete(re: Regex, mimeType: String, auth: (String,Option[String])=>Boolean)(fun: => Any) =
    dispatcherRegexMap("DELETE") = dispatcherRegexMap("DELETE") ::: List((re, () => mimeType, () => true, auth, () => fun))

  def delete(re: Regex, guard: => Boolean, auth: (String,Option[String])=>Boolean)(fun: => Any) =
    dispatcherRegexMap("DELETE") = dispatcherRegexMap("DELETE") ::: List((re, () => "text/html", () => guard, auth, () => fun))

  def delete(re: Regex, mimeType: String, guard: => Boolean, auth: (String,Option[String])=>Boolean)(fun: => Any) =
    dispatcherRegexMap("DELETE") = dispatcherRegexMap("DELETE") ::: List((re, () => mimeType, () => guard, auth, () => fun))



  //
  // HEAD methods
  //
  def head(path: String) =
    dispatcherMap("HEAD") + (path -> (() => "text/html", () => true, (a:String,b:Option[String])=>true, () => ""))

  def head(path: String, mimeType: String) =
    dispatcherMap("HEAD") + (path -> (() => mimeType, () => true, (a:String,b:Option[String])=>true, () => ""))

  def head(path: String, guard: => Boolean) =
    dispatcherMap("HEAD") + (path -> (() => "text/html", () => guard, (a:String,b:Option[String])=>true, () => ""))

  def head(path: String, auth: (String,Option[String])=>Boolean) =
    dispatcherMap("HEAD") + (path -> (() => "text/html", () => true, auth, () => ""))

  def head(path: String, mimeType: String, guard: => Boolean) =
    dispatcherMap("HEAD") + (path -> (() => mimeType, () => guard, (a:String,b:Option[String])=>true, () => ""))

  def head(path: String, mimeType: String, auth: (String,Option[String])=>Boolean) =
     dispatcherMap("HEAD") + (path -> (() => mimeType, () => true, auth, () => ""))

  def head(path: String, guard: => Boolean, auth: (String,Option[String])=>Boolean) =
    dispatcherMap("HEAD") + (path -> (() => "text/html", () => guard, auth, () => ""))

  def head(path: String, mimeType: String, guard: => Boolean, auth: (String,Option[String])=>Boolean) =
    dispatcherMap("HEAD") + (path -> (() => mimeType, () => guard, auth, () => ""))


  def head(re: Regex) =
    dispatcherRegexMap("HEAD") = dispatcherRegexMap("HEAD") ::: List((re, () => "text/html", () => true, (a:String,b:Option[String])=>true, () => ""))

  def head(re: Regex, mimeType: String) =
    dispatcherRegexMap("HEAD") = dispatcherRegexMap("HEAD") ::: List((re, () => mimeType, () => true, (a:String,b:Option[String])=>true, () => ""))

  def head(re: Regex, guard: => Boolean) =
    dispatcherRegexMap("HEAD") = dispatcherRegexMap("HEAD") ::: List((re, () => "text/html", () => guard, (a:String,b:Option[String])=>true, () => ""))

  def head(re: Regex, auth: (String,Option[String])=>Boolean) =
    dispatcherRegexMap("HEAD") = dispatcherRegexMap("HEAD") ::: List((re, () => "text/html", () => true, auth, () => ""))

  def head(re: Regex, mimeType: String, guard: => Boolean) =
    dispatcherRegexMap("HEAD") = dispatcherRegexMap("HEAD") ::: List((re, () => mimeType, () => guard, (a:String,b:Option[String])=>true, () => ""))

  def head(re: Regex, mimeType: String, auth: (String,Option[String])=>Boolean) =
    dispatcherRegexMap("HEAD") = dispatcherRegexMap("HEAD") ::: List((re, () => mimeType, () => true, auth, () => ""))

  def head(re: Regex, guard: => Boolean, auth: (String,Option[String])=>Boolean) =
    dispatcherRegexMap("HEAD") = dispatcherRegexMap("HEAD") ::: List((re, () => "text/html", () => guard, auth, () => ""))

  def head(re: Regex, mimeType: String, guard: => Boolean, auth: (String,Option[String])=>Boolean) =
    dispatcherRegexMap("HEAD") = dispatcherRegexMap("HEAD") ::: List((re, () => mimeType, () => guard, auth, () => ""))



  //
  // OPTIONS methods
  //
  def options(path: String)(fun: => Any) =
    dispatcherMap("OPTIONS") + (path -> (() => "text/html", () => true, (a:String,b:Option[String])=>true, () => fun))

  def options(path: String, mimeType: String)(fun: => Any) =
    dispatcherMap("OPTIONS") + (path -> (() => mimeType, () => true, (a:String,b:Option[String])=>true, () => fun))

  def options(path: String, guard: => Boolean)(fun: => Any) =
    dispatcherMap("OPTIONS") + (path -> (() => "text/html", () => guard, (a:String,b:Option[String])=>true, () => fun))

  def options(path: String, auth: (String,Option[String])=>Boolean)(fun: => Any) =
    dispatcherMap("OPTIONS") + (path -> (() => "text/html", () => true, auth, () => fun))

  def options(path: String, mimeType: String, guard: => Boolean)(fun: => Any) =
    dispatcherMap("OPTIONS") + (path -> (() => mimeType, () => guard, (a:String,b:Option[String])=>true, () => fun))

  def options(path: String, mimeType: String, auth: (String,Option[String])=>Boolean)(fun: => Any) =
     dispatcherMap("OPTIONS") + (path -> (() => mimeType, () => true, auth, () => fun))

  def options(path: String, guard: => Boolean, auth: (String,Option[String])=>Boolean)(fun: => Any) =
    dispatcherMap("OPTIONS") + (path -> (() => "text/html", () => guard, auth, () => fun))

  def options(path: String, mimeType: String, guard: => Boolean, auth: (String,Option[String])=>Boolean)(fun: => Any) =
    dispatcherMap("OPTIONS") + (path -> (() => mimeType, () => guard, auth, () => fun))


  def options(re: Regex)(fun: => Any) =
    dispatcherRegexMap("OPTIONS") = dispatcherRegexMap("OPTIONS") ::: List((re, () => "text/html", () => true, (a:String,b:Option[String])=>true, () => fun))

  def options(re: Regex, mimeType: String)(fun: => Any) =
    dispatcherRegexMap("OPTIONS") = dispatcherRegexMap("OPTIONS") ::: List((re, () => mimeType, () => true, (a:String,b:Option[String])=>true, () => fun))

  def options(re: Regex, guard: => Boolean)(fun: => Any) =
    dispatcherRegexMap("OPTIONS") = dispatcherRegexMap("OPTIONS") ::: List((re, () => "text/html", () => guard, (a:String,b:Option[String])=>true, () => fun))

  def options(re: Regex, auth: (String,Option[String])=>Boolean)(fun: => Any) =
    dispatcherRegexMap("OPTIONS") = dispatcherRegexMap("OPTIONS") ::: List((re, () => "text/html", () => true, auth, () => fun))

  def options(re: Regex, mimeType: String, guard: => Boolean)(fun: => Any) =
    dispatcherRegexMap("OPTIONS") = dispatcherRegexMap("OPTIONS") ::: List((re, () => mimeType, () => guard, (a:String,b:Option[String])=>true, () => fun))

  def options(re: Regex, mimeType: String, auth: (String,Option[String])=>Boolean)(fun: => Any) =
    dispatcherRegexMap("OPTIONS") = dispatcherRegexMap("OPTIONS") ::: List((re, () => mimeType, () => true, auth, () => fun))

  def options(re: Regex, guard: => Boolean, auth: (String,Option[String])=>Boolean)(fun: => Any) =
    dispatcherRegexMap("OPTIONS") = dispatcherRegexMap("OPTIONS") ::: List((re, () => "text/html", () => guard, auth, () => fun))

  def options(re: Regex, mimeType: String, guard: => Boolean, auth: (String,Option[String])=>Boolean)(fun: => Any) =
    dispatcherRegexMap("OPTIONS") = dispatcherRegexMap("OPTIONS") ::: List((re, () => mimeType, () => guard, auth, () => fun))


  //
  // TRACE methods
  //
  def trace(path: String)(fun: => Any) =
    dispatcherMap("TRACE") + (path -> (() => "text/html", () => true, (a:String,b:Option[String])=>true, () => fun))

  def trace(path: String, mimeType: String)(fun: => Any) =
    dispatcherMap("TRACE") + (path -> (() => mimeType, () => true, (a:String,b:Option[String])=>true, () => fun))

  def trace(path: String, guard: => Boolean)(fun: => Any) =
    dispatcherMap("TRACE") + (path -> (() => "text/html", () => guard, (a:String,b:Option[String])=>true, () => fun))

  def trace(path: String, auth: (String,Option[String])=>Boolean)(fun: => Any) =
    dispatcherMap("TRACE") + (path -> (() => "text/html", () => true, auth, () => fun))

  def trace(path: String, mimeType: String, guard: => Boolean)(fun: => Any) =
    dispatcherMap("TRACE") + (path -> (() => mimeType, () => guard, (a:String,b:Option[String])=>true, () => fun))

  def trace(path: String, mimeType: String, auth: (String,Option[String])=>Boolean)(fun: => Any) =
     dispatcherMap("TRACE") + (path -> (() => mimeType, () => true, auth, () => fun))

  def trace(path: String, guard: => Boolean, auth: (String,Option[String])=>Boolean)(fun: => Any) =
    dispatcherMap("TRACE") + (path -> (() => "text/html", () => guard, auth, () => fun))

  def trace(path: String, mimeType: String, guard: => Boolean, auth: (String,Option[String])=>Boolean)(fun: => Any) =
    dispatcherMap("TRACE") + (path -> (() => mimeType, () => guard, auth, () => fun))


  def trace(re: Regex)(fun: => Any) =
    dispatcherRegexMap("TRACE") = dispatcherRegexMap("TRACE") ::: List((re, () => "text/html", () => true, (a:String,b:Option[String])=>true, () => fun))

  def trace(re: Regex, mimeType: String)(fun: => Any) =
    dispatcherRegexMap("TRACE") = dispatcherRegexMap("TRACE") ::: List((re, () => mimeType, () => true, (a:String,b:Option[String])=>true, () => fun))

  def trace(re: Regex, guard: => Boolean)(fun: => Any) =
    dispatcherRegexMap("TRACE") = dispatcherRegexMap("TRACE") ::: List((re, () => "text/html", () => guard, (a:String,b:Option[String])=>true, () => fun))

  def trace(re: Regex, auth: (String,Option[String])=>Boolean)(fun: => Any) =
    dispatcherRegexMap("TRACE") = dispatcherRegexMap("TRACE") ::: List((re, () => "text/html", () => true, auth, () => fun))

  def trace(re: Regex, mimeType: String, guard: => Boolean)(fun: => Any) =
    dispatcherRegexMap("TRACE") = dispatcherRegexMap("TRACE") ::: List((re, () => mimeType, () => guard, (a:String,b:Option[String])=>true, () => fun))

  def trace(re: Regex, mimeType: String, auth: (String,Option[String])=>Boolean)(fun: => Any) =
    dispatcherRegexMap("TRACE") = dispatcherRegexMap("TRACE") ::: List((re, () => mimeType, () => true, auth, () => fun))

  def trace(re: Regex, guard: => Boolean, auth: (String,Option[String])=>Boolean)(fun: => Any) =
    dispatcherRegexMap("TRACE") = dispatcherRegexMap("TRACE") ::: List((re, () => "text/html", () => guard, auth, () => fun))

  def trace(re: Regex, mimeType: String, guard: => Boolean, auth: (String,Option[String])=>Boolean)(fun: => Any) =
    dispatcherRegexMap("TRACE") = dispatcherRegexMap("TRACE") ::: List((re, () => mimeType, () => guard, auth, () => fun))


}

