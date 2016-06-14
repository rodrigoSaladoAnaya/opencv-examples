package app.handlers

import com.sun.net.httpserver.HttpHandler
import com.sun.net.httpserver.HttpExchange
import java.io.OutputStream

class HtmlHandler implements HttpHandler {

  def resourcesMapping(URI requestURI) {
    println "HtmlHandler: ${requestURI.path}"
    def resource_path = "./resources${requestURI.path}"
    def resource_body
    try {
      resource_body = new File(resource_path)?.text
    } catch(java.io.FileNotFoundException e) {
      resource_body = null
    }
    return resource_body?:"Resources not found!: [${resource_path}]"
  }

  void handle(HttpExchange he) {
    def response = resourcesMapping(he.requestURI)
    he.sendResponseHeaders(200, response.length())
    OutputStream os = he.responseBody
    os.write(response.bytes)
    os.close()
  }
}
