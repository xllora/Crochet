package crochet

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

protected trait CrochetResponseCodes extends CrochetDynamicEnvironment {

  protected def _default_error_template =
    <html>
      <head>
        <title>Crochet: {errorSummary}</title>
        <style type="text/css">
            <![CDATA[
	          hr {
                height: 1px;
                background: #BBBBBB;
            }
            body {
                color: #444;
                background: white;
                font-family: Helvetica,Verdana;
            }
            #message {
                background: #EEEEEE;
                width: 90%;
                padding: 25px;
                margin-left: auto;
                margin-right: auto;
                border: 1px solid #DDDDDD;
                font-size:14px;
            }
            #header h1 {
                font-family: Helvetica,Verdana ;
                font-size: 36px;
                color: #888888;
                margin-bottom:0px;
                padding-bottom:0px;
                font-variant: small-caps;
            }
            h1 { font-size: 30px; }
            h2 { font-size: 20px; }
            h3 { font-size: 16px; }
            #footer {
                margin-top:0px;
                padding-top:0px;
                font-size:10px;
            }
            #footer-left {
                margin-top:0px;
                padding-top:0px;
                float: left;
            }

            #footer-right {
                margin-top:0px;
                padding-top:0px;
                float: right;
            }
            ]]>
        </style>
      </head>
      <body>
        <div id="header">
          <h1>Crochet</h1>
        </div>
        <hr/>
        <br/>
        <div id="message">
          <p>{message}</p>
        </div>
        <br/>
        <hr/>
        <div id="footer">
          <p id="footer-left">Crochet v.{version}</p><p id="footer-right">{new Date}</p>
        </div>
      </body>
    </html>
  

  protected var _error_template = _default_error_template _

  protected val errorCodeMap = MMap[Int,()=>Any]()


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
          sessionVal.withValue(extractSession(request)) {
            val smsg:String = e match {
              case Some(e) if e.getMessage!=null => e.printStackTrace(pw) ; e.getMessage.trim
              case Some(e) if e.getMessage==null => e.printStackTrace(pw) ; title()
              case _ => val t = title() ; pw.println(t) ; t
            }
            errorSummaryVal.withValue(smsg) {
              errorVal.withValue(sw.toString) {
                messageVal.withValue(msg()) {
                  val output = _error_template()
                  response.getWriter.print(output.toString)
                }
              }
            }
          }
        }
      }
    }
  }

  //
  // Response code implemented following the listing provided at
  // http://en.wikipedia.org/wiki/List_of_HTTP_status_codes
  //

  //
  // 100 codes
  //
  def _100(msg: () => Any) = errorCodeMap + (HttpServletResponse.SC_CONTINUE->msg)
  def _101(msg: () => Any) = errorCodeMap + (HttpServletResponse.SC_SWITCHING_PROTOCOLS->msg)
  // Webdav
  def _102(msg: () => Any) = errorCodeMap + (102->msg) // Processing (WebDAV) (RFC 2518)

  //
  // 200 codes
  //
  def _200(msg: () => Any) = errorCodeMap + (HttpServletResponse.SC_OK->msg)
  def _201(msg: () => Any) = errorCodeMap + (HttpServletResponse.SC_CREATED->msg)
  def _202(msg: () => Any) = errorCodeMap + (HttpServletResponse.SC_ACCEPTED->msg)
  def _203(msg: () => Any) = errorCodeMap + (HttpServletResponse.SC_NON_AUTHORITATIVE_INFORMATION->msg)
  def _204(msg: () => Any) = errorCodeMap + (HttpServletResponse.SC_NO_CONTENT->msg)
  def _205(msg: () => Any) = errorCodeMap + (HttpServletResponse.SC_RESET_CONTENT->msg)
  def _206(msg: () => Any) = errorCodeMap + (HttpServletResponse.SC_PARTIAL_CONTENT->msg)
  // Webdav
  def _207(msg: () => Any) = errorCodeMap + (207->msg) // Multi-Status (WebDAV) (RFC 2518)

  //
  // 300 codes
  //
  def _300(msg: () => Any) = errorCodeMap + (HttpServletResponse.SC_MULTIPLE_CHOICES->msg)
  def _301(msg: () => Any) = errorCodeMap + (HttpServletResponse.SC_MOVED_PERMANENTLY->msg)
  def _302(msg: () => Any) = errorCodeMap + (HttpServletResponse.SC_FOUND->msg)
  def _303(msg: () => Any) = errorCodeMap + (HttpServletResponse.SC_SEE_OTHER->msg)
  def _304(msg: () => Any) = errorCodeMap + (HttpServletResponse.SC_NOT_MODIFIED->msg)
  def _305(msg: () => Any) = errorCodeMap + (HttpServletResponse.SC_USE_PROXY->msg)
  // 306 No longer used (see http://en.wikipedia.org/wiki/List_of_HTTP_status_codes)
  def _307(msg: () => Any) = errorCodeMap + (HttpServletResponse.SC_TEMPORARY_REDIRECT->msg)

  //
  // 400 codes
  //
  def _400(msg: () => Any) = errorCodeMap + (HttpServletResponse.SC_BAD_REQUEST->msg)
  def _401(msg: () => Any) = errorCodeMap + (HttpServletResponse.SC_UNAUTHORIZED->msg)
  def _402(msg: () => Any) = errorCodeMap + (HttpServletResponse.SC_PAYMENT_REQUIRED->msg)
  def _403(msg: () => Any) = errorCodeMap + (HttpServletResponse.SC_FORBIDDEN->msg)
  def _404(msg: () => Any) = errorCodeMap + (HttpServletResponse.SC_NOT_FOUND->msg)
  def _405(msg: () => Any) = errorCodeMap + (HttpServletResponse.SC_METHOD_NOT_ALLOWED->msg)
  def _406(msg: () => Any) = errorCodeMap + (HttpServletResponse.SC_NOT_ACCEPTABLE->msg)
  def _407(msg: () => Any) = errorCodeMap + (HttpServletResponse.SC_PROXY_AUTHENTICATION_REQUIRED->msg)
  def _408(msg: () => Any) = errorCodeMap + (HttpServletResponse.SC_REQUEST_TIMEOUT->msg)
  def _409(msg: () => Any) = errorCodeMap + (HttpServletResponse.SC_CONFLICT->msg)
  def _410(msg: () => Any) = errorCodeMap + (HttpServletResponse.SC_GONE->msg)
  def _411(msg: () => Any) = errorCodeMap + (HttpServletResponse.SC_LENGTH_REQUIRED->msg)
  def _412(msg: () => Any) = errorCodeMap + (HttpServletResponse.SC_PRECONDITION_FAILED->msg)
  def _413(msg: () => Any) = errorCodeMap + (HttpServletResponse.SC_REQUEST_ENTITY_TOO_LARGE->msg)
  def _414(msg: () => Any) = errorCodeMap + (HttpServletResponse.SC_REQUEST_URI_TOO_LONG->msg)
  def _415(msg: () => Any) = errorCodeMap + (HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE->msg)
  def _416(msg: () => Any) = errorCodeMap + (HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE->msg)
  def _417(msg: () => Any) = errorCodeMap + (HttpServletResponse.SC_EXPECTATION_FAILED->msg)
  def _418(msg: () => Any) = errorCodeMap + (418->msg)  // I'm a tea pot :D

  // Webdav
  def _422(msg: () => Any) = errorCodeMap + (422->msg)  // Unprocessable Entity (WebDAV) (RFC 4918)
  def _423(msg: () => Any) = errorCodeMap + (423->msg)  // Locked (WebDAV) (RFC 4918)
  def _424(msg: () => Any) = errorCodeMap + (424->msg)  // Failed Dependency (WebDAV) (RFC 4918)
  def _425(msg: () => Any) = errorCodeMap + (425->msg)  // Unordered Collection (RFC 3648)
  def _426(msg: () => Any) = errorCodeMap + (426->msg)  // Upgrade Required (RFC 2817)

  // Windows extensions
  def _449(msg: () => Any) = errorCodeMap + (449->msg)  // Retry with
  def _450(msg: () => Any) = errorCodeMap + (450->msg)  // Blocked by Windows parental controls

  //
  // 500 codes
  //
  def _500(msg: () => Any) = errorCodeMap + (HttpServletResponse.SC_INTERNAL_SERVER_ERROR->msg)
  def _501(msg: () => Any) = errorCodeMap + (HttpServletResponse.SC_NOT_IMPLEMENTED->msg)
  def _502(msg: () => Any) = errorCodeMap + (HttpServletResponse.SC_BAD_GATEWAY->msg)
  def _503(msg: () => Any) = errorCodeMap + (HttpServletResponse.SC_SERVICE_UNAVAILABLE->msg)
  def _504(msg: () => Any) = errorCodeMap + (HttpServletResponse.SC_GATEWAY_TIMEOUT->msg)
  def _505(msg: () => Any) = errorCodeMap + (HttpServletResponse.SC_HTTP_VERSION_NOT_SUPPORTED->msg)
  def _506(msg: () => Any) = errorCodeMap + (506->msg)  // Variant Also Negotiates
  def _507(msg: () => Any) = errorCodeMap + (507->msg)  // Insufficient Storage
  def _509(msg: () => Any) = errorCodeMap + (509->msg)  // Bandwidth Limit Exceeded (Apache bw/limited extension)
  def _510(msg: () => Any) = errorCodeMap + (510->msg)  // Not Extended

  //
  // Default response codes set by default
  //
  _401 { () => <h3>Unauthorized access to {path}</h3><b>Reason:</b><pre>Not authorized</pre> }
  _404 { () => <h3>Problem accessing {path}</h3><b>Reason:</b><pre>not found</pre> }
  _500 { () => <h3>Internal server error accessing {path}</h3><b>Reason:</b><pre>{error}</pre> }
}