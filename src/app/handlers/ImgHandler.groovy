package app.handlers

import com.sun.net.httpserver.HttpHandler
import com.sun.net.httpserver.HttpExchange
import java.io.OutputStream
import java.net.URI
import app.ocv.*

class ImgHandler implements HttpHandler {

  def resourcesMapping(URI requestURI) {
    println "ImgHandler: [path: ${requestURI.path}, query: ${requestURI.query}]"
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

  def selectProcessor(String reqPath) {
    def processors = [
      '/img/circle': new PaintCircleImg(),
      '/img/blur'  : new BlurImg()
    ]
    return processors[reqPath]
  }

  void handle(HttpExchange he) {
    def resource = resourcesMapping(he.requestURI)
    def blur_img = selectProcessor(he.requestURI.path)
    def image_in_byte = blur_img.imgProcessor(resource)
    he.sendResponseHeaders(200, image_in_byte.length)
    OutputStream os = he.getResponseBody()
    os.write(image_in_byte,0,image_in_byte.length)
    os.close()
  }
}
