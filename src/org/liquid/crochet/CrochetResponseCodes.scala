package org.liquid.crochet

import scala.collection.mutable.{Map=>MMap}
import java.util.Date
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}
import java.io.{PrintWriter, StringWriter}

/**
 * The list of possible configurable response codes and their payload
 *
 * @author Xavier Llora
 * @date Jan 10, 2010 at 11:09:14 AM
 *
 */

trait CrochetResponseCodes extends CrochetDynamicEnvironment {

  protected def _default_error_template =
    <html>
      <head>
        <title>Crochet: {errorSummary}</title>
      </head>
      <body>
        <br/>
        <br/>
        <br/>
        <pre>{message}</pre>
        <br/>
        <br/>
        <br/>
        <hr/>
        <div id="footer">
          <p>Crochet v.{version}. {new Date}</p>
        </div>
      </body>
    </html>
  

  protected var _error_template = _default_error_template _

  protected val errorCodeMap = MMap[Int,()=>String]()


  //
  // Main error processing function
  //
  def reportError(code:Int, title:()=>String, path: String, request: HttpServletRequest, response: HttpServletResponse, e:Option[Throwable]) = {
    response setStatus code
    response setContentType "text/html"
    val msg = errorCodeMap(code)
    val sw = new StringWriter
    val pw = new PrintWriter(sw)
    pathVal.withValue(path) {
      requestVal.withValue(request) {
        responseVal.withValue(response) {
          val smsg:String = e match { 
            case Some(e) if e.getMessage!=null => e.printStackTrace(pw) ; e.getMessage.trim
            case Some(e) if e.getMessage==null => e.printStackTrace(pw) ; title()
            case _ => val t = title() ; pw.println(t) ; t
          }
          errorSummaryVal.withValue(smsg) {
            errorVal.withValue(sw.toString) {
              messageVal.withValue(msg()) {
                val output = _error_template()
                response.getWriter.print(output)
              }
            }
          }
        }
      }
    }
  }


  def _404(msg: () => String) = errorCodeMap + (HttpServletResponse.SC_NOT_FOUND->msg)
  def _417(msg: () => String) = errorCodeMap + (HttpServletResponse.SC_EXPECTATION_FAILED->msg)
  def _500(msg: () => String) = errorCodeMap + (HttpServletResponse.SC_INTERNAL_SERVER_ERROR->msg)

  //
  // Default response codes
  //
  _404 { () => path+" not found" }
  _417 { () => "Guard to "+path+" failed" }
  _500 { () => "Internal server error accessing "+path+" because of "+error }
}