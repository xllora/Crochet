package org.liquid.crochet

import scala.collection.mutable.{Map=>MMap}
import java.util.Date
import javax.servlet.http.HttpServletResponse

/**
 * The list of possible configurable response codes and their payload
 *
 * @author Xavier Llora
 * @date Jan 10, 2010 at 11:09:14 AM
 *
 */

trait ResponseCodes extends DynamicEnvironment {

  protected def _default_error_template(map:Map[String,String]) =
    <html>
      <head>
      </head>
      <body>
        <br/>
        <br/>
        <br/>
        <h3>{map("msg")}</h3>
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

  def _404(msg: () => String) = errorCodeMap + (HttpServletResponse.SC_NOT_FOUND->msg)
  def _417(msg: () => String) = errorCodeMap + (HttpServletResponse.SC_EXPECTATION_FAILED->msg)

  //
  // Default response codes
  //
  _404 { () => path+" not found" }
  _417 { () => "Guard to "+path+" failed" }
}