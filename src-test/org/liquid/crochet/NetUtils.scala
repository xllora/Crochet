package org.liquid.crochet

import java.net.URL
import java.io.{InputStreamReader, InputStream, BufferedReader}
import org.apache.commons.httpclient.methods.GetMethod
import org.apache.commons.httpclient.{HttpStatus, HttpClient}

/**
 * Network utils to help make testing simpler
 *
 * @author Xavier Llora
 * @date Jan 10, 2010 at 6:51:41 PM
 * 
 */

object NetUtils {

  protected class NetworkLink ( baseURL:String ) {

    def get(path:String) = {
      val hc = new HttpClient
      val mt = new GetMethod(baseURL+path)
      hc executeMethod mt match {
        case HttpStatus.SC_OK => new String(mt.getResponseBody)
        case _ => ""
      }
    }

  }

  def apply( protocol:String, host:String, port:Int ) = new NetworkLink(protocol+"://"+host+":"+port)

}