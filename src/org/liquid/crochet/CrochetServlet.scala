package org.liquid.crochet

import java.lang.{Long,Float,Double}
import java.util.UUID
import javax.servlet.http.{HttpServletRequest, HttpServletResponse, HttpServlet}
import util.matching.Regex
import util.DynamicVariable

/**
 * This class implements the main routing machinery for Crochet
 *
 * @author Xavier Llora
 * @date Jan 9, 2010 at 5:13:03 PM
 * 
 */

abstract class CrochetServlet extends HttpServlet {

  protected val  requestVal = new DynamicVariable[HttpServletRequest](null)
  protected val responseVal = new DynamicVariable[HttpServletResponse](null)
  protected val elementsVal = new DynamicVariable[List[String]](null)

  def  request = requestVal.value
  def response = responseVal.value
  def elements = elementsVal.value

  protected implicit def stringToInt    (s:String) = Integer parseInt    s
  protected implicit def stringToLong   (s:String) = Long    parseLong   s
  protected implicit def stringToFloat  (s:String) = Float   parseFloat  s
  protected implicit def stringToDouble (s:String) = Double  parseDouble s
  protected implicit def stringToUUID   (s:String) = UUID    fromString  s

  override def doGet (request:HttpServletRequest, response:HttpServletResponse) = {
    //TODO add the invocation machinery
  }


  override def doDelete (request:HttpServletRequest, response:HttpServletResponse) = {}
  override def doHead  (request:HttpServletRequest, response:HttpServletResponse) = {}
  override def doOptions  (request:HttpServletRequest, response:HttpServletResponse) = {}
  override def doPost (request:HttpServletRequest, response:HttpServletResponse) = {}
  override def doPut (request:HttpServletRequest, response:HttpServletResponse) = {}
  override def doTrace (request:HttpServletRequest, response:HttpServletResponse) = {}




  def get ( path:Regex )( fun: =>Any ) = {
      // TODO Add the proper REGEX storage
  }

  def get ( path:String )(fun: =>Any ) = {

      // TODO Add the proper REGEX storage
  }

}