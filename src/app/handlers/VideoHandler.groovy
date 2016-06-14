package app.handlers

import com.sun.net.httpserver.HttpHandler
import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.Headers
import java.io.OutputStream
import java.net.URI
import app.ocv.*

class VideoHandler implements HttpHandler {

  void handle(HttpExchange he) {
    println "VideoHandler: ${he.requestURI.path}"
    def boundary = "OCVVideoTest"
    Headers headers = he.getResponseHeaders()
    headers.add("Content-Type", "multipart/x-mixed-replace; boundary=--${boundary}")
    headers.add("Cache-Control", "no-cache, private")
    headers.add("Pragma", "no-cache")
    headers.add("Max-Age", "0")
    headers.add("Expires", "0")
    he.sendResponseHeaders(200, 0)
    new OcvUtils().convertToVid(he.responseBody, boundary)
  }
}
