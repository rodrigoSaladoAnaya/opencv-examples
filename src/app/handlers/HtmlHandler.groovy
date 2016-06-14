package app.handlers

import com.sun.net.httpserver.HttpHandler
import com.sun.net.httpserver.HttpExchange
import java.io.OutputStream

class HtmlHandler implements HttpHandler {

  def resourcesMapping(URI requestURI) {
    println "HtmlHandler: ${requestURI.path}"
    def view = requestURI.path.tokenize("/").last()
    return new File("./resources/html/${view}.html").text
  }

  void handle(HttpExchange he) {
    def response = resourcesMapping(he.requestURI)
    he.sendResponseHeaders(200, response.length())
    OutputStream os = he.responseBody
    os.write(response.bytes)
    os.close()
  }
}
