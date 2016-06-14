package app.handlers

import com.sun.net.httpserver.HttpHandler
import com.sun.net.httpserver.HttpExchange
import java.io.OutputStream
import java.net.URI
import app.ocv.*

class PaintCircleImgImgHandler implements HttpHandler {

  def resourcesMapping(URI requestURI) {
    println "PaintCircleImgImgHandler: [path: ${requestURI.path}, query: ${requestURI.query}]"
    def queryArgs = { query_req ->
      def out = [:]
      query_req.tokenize('&').each { q ->
        def p = q.tokenize('=')
        if(p.size() == 2) {
          out[p[0]] = p[1].toInteger()
        }
      }
      return out
    }
    return [
      path: "./resources/img/cathedral.jpg",
      imgFormat: 'jpg',
      query: queryArgs(requestURI.query)
    ]
  }

  void handle(HttpExchange he) {
try {

    def resource = resourcesMapping(he.requestURI)
    def blur_img = new PaintCircleImg()
    def imageInByte = blur_img.imgProcessor(resource)
    he.sendResponseHeaders(200, imageInByte.length)
    OutputStream os = he.getResponseBody()
    os.write(imageInByte,0,imageInByte.length)
    os.close()
} catch(e) {
  println e
}
  }
}
