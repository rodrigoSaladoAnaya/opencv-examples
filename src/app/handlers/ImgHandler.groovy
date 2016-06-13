package app.handlers

import com.sun.net.httpserver.HttpHandler
import com.sun.net.httpserver.HttpExchange
import java.io.OutputStream
import java.net.URI
import app.ocv.*


class ImgHandler implements HttpHandler {

  def resourcesMapping(URI requestURI) {
    println "ImgHandler: ${requestURI.path}"
    return './resources/img/cathedral.jpg'
  }

  void handle(HttpExchange he) {
try { //<<- BORRAR
    def resource = resourcesMapping(he.requestURI)
    def blur_img = new BlurImg()
    def imageInByte = blur_img.imgProcesor(resource)
    he.sendResponseHeaders(200, imageInByte.length)
    OutputStream os = he.getResponseBody()
    os.write(imageInByte,0,imageInByte.length)
    os.close()
} catch(e) { println e}
  }
}
