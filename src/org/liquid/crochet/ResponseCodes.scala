package org.liquid.crochet

import scala.collection.mutable.Map
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

  protected def _default_error_template =
    <html>
      <head>
      </head>
      <body>
        <br/>
        <br/>
        <br/>
        <br/>
        <br/>
        <br/>
        <hr/>
        <div id="footer">
          <p>Crochet v.{version}. {new Date}</p>
        </div>
      </body>
    </html>
  

  protected var _error_template = _default_error_template

  protected val errorCodeMap = Map[Int,()=>String]()

  def _404(msg: () => String) = errorCodeMap + (HttpServletResponse.SC_NOT_FOUND->msg)

}