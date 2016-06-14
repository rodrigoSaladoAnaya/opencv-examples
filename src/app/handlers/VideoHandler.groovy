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

  void handle(HttpExchange connection) {
try{
    byte[] data
    Mat webcamMatImage = new Mat();
    String boundary = "VNetPhysics"
    Headers responseHeaders = connection.getResponseHeaders();
    responseHeaders.add("Content-Type", String.format("multipart/x-mixed-replace; boundary=--%s", boundary));
    responseHeaders.add("Cache-Control", "no-cache, private");
    responseHeaders.add("Pragma", "no-cache");
    responseHeaders.add("Max-Age", "0");
    responseHeaders.add("Expires", "0");
    connection.sendResponseHeaders(200, 0);
    OutputStream responseBody = connection.getResponseBody();
    VideoCapture capture = new VideoCapture(0);
    capture.set(Videoio.CAP_PROP_FRAME_WIDTH,320);
    capture.set(Videoio.CAP_PROP_FRAME_HEIGHT,240);
    def ocvu = new OcvUtils()

    if(capture.isOpened()){
      while (true){
        capture.read(webcamMatImage);
        if(!webcamMatImage.empty() ){
          BufferedImage bufferedImage = ocvu.toBufferedImage(webcamMatImage);
          ByteArrayOutputStream os = new ByteArrayOutputStream(8192 * 4);
          ImageIO.write(bufferedImage, "jpg", os);
          data = os.toByteArray();
          os.close();
          responseBody.write(("--" + boundary + "\r\n"
                  + "Content-type: image/jpg\r\n"
                  + "Content-Length: "
                  + data.length
                  + "\r\n\r\n").getBytes());

          responseBody.write(data);
          responseBody.flush();
        } else {
          println(" -- Frame not captured -- Break!");
          break;
        }
      }
    } else{
      println("Couldn't open capture.");
    }
} catch(e) { println e }
  }
}
