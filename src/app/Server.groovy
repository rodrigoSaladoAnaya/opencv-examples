package app

import java.net.InetSocketAddress
import com.sun.net.httpserver.HttpServer
import app.handlers.*

class Server {

  def Server(int port) {
    def inet_socket_address = new InetSocketAddress(port)
    def backlog = 0
    def server = HttpServer.create(inet_socket_address, backlog)
    server.createContext('/', new HtmlHandler())
    server.createContext('/img', new ImgHandler())
    server.createContext('/vs', new VideoHandler())
    server.setExecutor(null)
    server.start()
    println "The server is running... at ${inet_socket_address.properties}"
  }
}
