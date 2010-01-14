package org.liquid.net.utils

import java.net.URL
import java.io.{InputStreamReader, InputStream, BufferedReader}
import org.apache.commons.httpclient.{HttpMethod, HttpStatus, HttpClient => ACHttpClient}
import org.apache.commons.httpclient.methods._

/**
 * Network utils to help make testing simpler
 *
 * @author Xavier Llora
 * @date Jan 10, 2010 at 6:51:41 PM
 *
 */

protected class HttpClient(baseURL: String) {

  def get(path:String, header:Map[String,String], params:Map[String,String]):(Int,String) = {
    val hc = new ACHttpClient
    val gp = params.foldLeft("")((s,t)=>s+"&"+t._1+"="+t._2)
    val fp = if (gp.length>0) baseURL+path.trim+"?"+gp.trim else baseURL+path.trim
    val mt = new GetMethod(fp)
    header.foreach ( (t) => mt.addRequestHeader(t._1,t._2) )
    val retCode = hc executeMethod mt
    (retCode, new String(mt.getResponseBody))
  }
  def get(path:String,params:Map[String,String]):(Int,String) = get(path,Map[String,String](),params)
  def get(path:String):(Int,String) = get(path,Map[String,String](),Map[String,String]())

  def post(path:String, header:Map[String,String],params:Map[String,String]):(Int,String) = {
    val hc = new ACHttpClient
    val mt = new PostMethod(baseURL + path)
    header.foreach ( (t) => mt.addRequestHeader(t._1,t._2) )
    params.foreach ( (t) => mt.addParameter(t._1,t._2) )
    val retCode = hc executeMethod mt
    (retCode, new String(mt.getResponseBody))
  }
  def post(path:String,params:Map[String,String]):(Int,String) = post(path,Map[String,String](),params)
  def post(path:String):(Int,String) = post(path,Map[String,String](),Map[String,String]())

  def put(path:String, header:Map[String,String],params:Map[String,String]):(Int,String) = {
    val hc = new ACHttpClient
    val mt = new PutMethod(baseURL + path)
    header.foreach ( (t) => mt.addRequestHeader(t._1,t._2) )
    val retCode = hc executeMethod mt
    (retCode, new String(mt.getResponseBody))
  }
  def put(path:String,params:Map[String,String]):(Int,String) = put(path,Map[String,String](),params)
  def put(path:String):(Int,String) = put(path,Map[String,String](),Map[String,String]())

  def delete(path:String, header:Map[String,String],params:Map[String,String]):(Int,String) = {
    val hc = new ACHttpClient
    val mt = new DeleteMethod(baseURL + path)
    header.foreach ( (t) => mt.addRequestHeader(t._1,t._2) )
    val retCode = hc executeMethod mt
    (retCode, new String(mt.getResponseBody))
  }
  def delete(path:String,params:Map[String,String]):(Int,String) = delete(path,Map[String,String](),params)
  def delete(path:String):(Int,String) = delete(path,Map[String,String](),Map[String,String]())

  def head(path:String, header:Map[String,String],params:Map[String,String]):Int = {
    val hc = new ACHttpClient
    val mt = new HeadMethod(baseURL + path)
    header.foreach ( (t) => mt.addRequestHeader(t._1,t._2) )
    val retCode = hc executeMethod mt
    retCode
  }
  def head(path:String,params:Map[String,String]):Int = head(path,Map[String,String](),params)
  def head(path:String):Int = head(path,Map[String,String](),Map[String,String]())

  def options(path:String, header:Map[String,String],params:Map[String,String]):(Int,String) = {
    val hc = new ACHttpClient
    val mt = new OptionsMethod(baseURL + path)
    header.foreach ( (t) => mt.addRequestHeader(t._1,t._2) )
    val retCode = hc executeMethod mt
    (retCode, new String(mt.getResponseBody))
  }
  def options(path:String,params:Map[String,String]):(Int,String) = options(path,Map[String,String](),params)
  def options(path:String):(Int,String) = options(path,Map[String,String](),Map[String,String]())


  def trace(path:String, header:Map[String,String],params:Map[String,String]):(Int,String) = {
    val hc = new ACHttpClient
    val mt = new TraceMethod(baseURL + path)
    header.foreach ( (t) => mt.addRequestHeader(t._1,t._2) )
    val retCode = hc executeMethod mt
    (retCode, new String(mt.getResponseBody))
  }
  def trace(path:String,params:Map[String,String]):(Int,String) = trace(path,Map[String,String](),params)
  def trace(path:String):(Int,String) = trace(path,Map[String,String](),Map[String,String]())

}

object HttpClient {

  def apply(protocol: String, host: String, port: Int) = new HttpClient(protocol + "://" + host + ":" + port)

}