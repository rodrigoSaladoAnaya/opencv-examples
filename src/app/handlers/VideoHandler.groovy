package app.handlers

import com.sun.net.httpserver.HttpHandler
import com.sun.net.httpserver.HttpExchange
import java.io.OutputStream
import java.net.URI
import app.ocv.*

import org.opencv.videoio.VideoCapture
import org.opencv.videoio.Videoio
import org.opencv.core.Mat
import javax.imageio.ImageIO
import com.sun.net.httpserver.Headers
import java.awt.image.BufferedImage


class VideoHandler implements HttpHandler {

  void handle(HttpExchange he) {
try{
    println "VideoHandler: ${he.requestURI.path}"
    Mat webcamMatImage = new Mat()
    def boundary = "OCVVideoTest"
    Headers headers = he.getResponseHeaders()
    headers.add("Content-Type", "multipart/x-mixed-replace; boundary=--${boundary}")
    headers.add("Cache-Control", "no-cache, private")
    headers.add("Pragma", "no-cache")
    headers.add("Max-Age", "0")
    headers.add("Expires", "0")
    he.sendResponseHeaders(200, 0)
    OutputStream os = he.responseBody
    def capture = new VideoCapture(0)
    capture.set(Videoio.CAP_PROP_FRAME_WIDTH,320)
    capture.set(Videoio.CAP_PROP_FRAME_HEIGHT,240)
    def ocvu = new OcvUtils()
    byte[] data
    BufferedImage bufferedImage
    ByteArrayOutputStream baos

    if(capture.isOpened()){
      while (true){
        capture.read(webcamMatImage)
        if(!webcamMatImage.empty() ){
          bufferedImage = ocvu.toBufferedImage(webcamMatImage)
          baos = new ByteArrayOutputStream(8192 * 4)
          ImageIO.write(bufferedImage, "jpg", baos)
          data = baos.toByteArray()
          baos.close()
          os.write(("--" + boundary + "\r\n"
                  + "Content-type: image/jpg\r\n"
                  + "Content-Length: "
                  + data.length
                  + "\r\n\r\n").getBytes())

          os.write(data)
          os.flush()
        } else {
          println(" -- Frame not captured -- Break!")
          break;
        }
      }
    } else{
      println("Couldn't open capture.")
    }
    os.close()
} catch(e) { println e }
  }
}
