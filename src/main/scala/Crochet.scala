package crochet

/**
 * A basic starting self sufficient server
 *
 * @author Xavier Llora
 * @date Jan 18, 2010 at 5:26:35 PM
 *
 */
trait Crochet extends CrochetServlet {
  var cs: Option[CrochetServer] = None
  var resFolder: Option[String] = None
  var servedPath: Option[String] = None

  def on(port: Int): Crochet = {
    val tcs = CrochetServer(port, this, resFolder, servedPath)
    cs = Some(tcs)
    tcs.go
    this
  }

  def serving(folder: String): Crochet = {
    resFolder = Some(folder)
    this
  }

  def as(path: String): Crochet = {
    servedPath = Some(path)
    this
  }

}
