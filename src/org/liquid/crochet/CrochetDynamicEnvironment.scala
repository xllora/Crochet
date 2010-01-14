package org.liquid.crochet

import util.DynamicVariable
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}

/**
 * This trait provides the basic dynamic environment for a web API
 *
 * @author Xavier Llora
 * @date Jan 10, 2010 at 2:38:45 PM
 *
 */

trait CrochetDynamicEnvironment {

   val version = "0.1vcli"
  
   //
   // Dynamic variables available to the method
   //
   protected val          pathVal = new DynamicVariable[String](null)
   protected val       requestVal = new DynamicVariable[HttpServletRequest](null)
   protected val      responseVal = new DynamicVariable[HttpServletResponse](null)
   protected val        headerVal = new DynamicVariable[Map[String,String]](null)
   protected val         paramVal = new DynamicVariable[Map[String,String]](null)
   protected val      paramMapVal = new DynamicVariable[Map[String,Array[String]]](null)
   protected val      elementsVal = new DynamicVariable[List[String]](null)
   protected val       messageVal = new DynamicVariable[String](null)
   protected val         errorVal = new DynamicVariable[String](null)
   protected val  errorSummaryVal = new DynamicVariable[String](null)

   //
   // Exposed names of the dynamic variables
   //
   def         path = pathVal.value
   def      request = requestVal.value
   def     response = responseVal.value
   def       header = headerVal.value
   def       params = paramVal.value
   def    paramsMap = paramMapVal.value
   def     elements = elementsVal.value
   def      message = messageVal.value
   def        error = errorVal.value
   def errorSummary = errorSummaryVal.value
 

}