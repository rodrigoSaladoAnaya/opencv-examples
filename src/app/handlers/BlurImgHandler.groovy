package app.handlers

import com.sun.net.httpserver.HttpHandler
import com.sun.net.httpserver.HttpExchange
import java.io.OutputStream
import java.net.URI
import app.ocv.*

class BlurImgHandler implements HttpHandler {

  def resourcesMapping(URI requestURI) {
    println "ImgHandler: [path: ${requestURI.path}, query: ${requestURI.query}]"
    def queryTimes = { q ->
      def tms = q.tokenize('=').last().toInteger()
      return q? tms: 0
    }
    return [
      path: "./resources/img/cathedral.jpg",
      imgFormat: 'jpg',
      times: queryTimes(requestURI.query)
    ]
  }

  void handle(HttpExchange he) {
    def resource = resourcesMapping(he.requestURI)
    def blur_img = new BlurImg()
    def imageInByte = blur_img.imgProcessor(resource)
    he.sendResponseHeaders(200, imageInByte.length)
    OutputStream os = he.getResponseBody()
    os.write(imageInByte,0,imageInByte.length)
    os.close()
  }
}
