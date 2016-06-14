package app.handlers

import com.sun.net.httpserver.HttpHandler
import com.sun.net.httpserver.HttpExchange
import java.io.OutputStream
import java.net.URI
import app.ocv.*


class ImgHandler implements HttpHandler {

  def resourcesMapping(URI requestURI) {
    println "ImgHandler: path->${requestURI.path}, query->${requestURI.query}"
    def img = requestURI.path.tokenize("/").last()
    def querys = { q ->
      def out = [times: 0]
      if(q) {
        def p = q.tokenize('=')
        out[p[0]] = p[1].toInteger()
      }
      return out
    }
    return [
      path: "./resources/img/${img}",
      query: querys(requestURI.query)
    ]
  }

  void handle(HttpExchange he) {
    def resource = resourcesMapping(he.requestURI)
    def blur_img = new BlurImg()
    def imageInByte = blur_img.imgProcesor(resource)
    he.sendResponseHeaders(200, imageInByte.length)
    OutputStream os = he.getResponseBody()
    os.write(imageInByte,0,imageInByte.length)
    os.close()
  }
}
