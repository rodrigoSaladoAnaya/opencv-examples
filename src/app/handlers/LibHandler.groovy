package app.handlers

import com.sun.net.httpserver.HttpHandler
import com.sun.net.httpserver.HttpExchange
import java.io.OutputStream

class LibHandler implements HttpHandler {

  def resourcesMapping(URI requestURI) {
    println "LibHandler: ${requestURI.path}"
    def file_str = requestURI.path.tokenize("/").last()
    return new File("./resources/lib/${file_str}").text
  }

  void handle(HttpExchange he) {
    def response = resourcesMapping(he.requestURI)
    he.sendResponseHeaders(200, response.length())
    OutputStream os = he.responseBody
    os.write(response.bytes)
    os.close()
  }
}
