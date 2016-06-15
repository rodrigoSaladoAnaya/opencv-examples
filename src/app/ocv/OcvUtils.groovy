package app.ocv

import org.opencv.core.Mat
import java.awt.image.BufferedImage
import java.awt.image.DataBufferByte
import org.opencv.imgcodecs.Imgcodecs
import javax.imageio.ImageIO
import org.opencv.videoio.VideoCapture
import org.opencv.videoio.Videoio


class OcvUtils {

  public BufferedImage toBufferedImage(Mat input_img) {
    int type = BufferedImage.TYPE_BYTE_GRAY
    if(input_img.channels() > 1) {
      type = BufferedImage.TYPE_3BYTE_BGR
    }
    int buffer_size = input_img.channels() * input_img.cols() * input_img.rows()
    byte [] buffer = new byte[buffer_size]
    input_img.get(0, 0, buffer)
    BufferedImage image = new BufferedImage(
      input_img.cols(), input_img.rows(), type
    )
    final byte[] pixels = (
      (DataBufferByte) image.getRaster().getDataBuffer()
    ).getData()
    System.arraycopy(buffer, 0, pixels, 0, buffer.length)
    return image
  }

  def convertToImg(Map params, img_cluzer) {
   String file_path = params.path
    Mat img = Imgcodecs.imread(file_path)
    if(img.dataAddr() != 0) {
      def buff = img_cluzer(img, params.query)
      ByteArrayOutputStream baos = new ByteArrayOutputStream()
      ImageIO.write(buff, params.imgFormat, baos)
      byte[] image_in_byte = baos.toByteArray()
      baos.close()
      return image_in_byte
    }
  }

  def convertToVid(OutputStream os, String boundary) {
    def webcamMatImage = new Mat()
    def capture = new VideoCapture(0)
    capture.set(Videoio.CAP_PROP_FRAME_WIDTH, 320)
    capture.set(Videoio.CAP_PROP_FRAME_HEIGHT, 240)
    def ocvu = new OcvUtils()
    byte[] data
    BufferedImage bufferedImage
    ByteArrayOutputStream baos
    if(capture.isOpened()) {
      while (true) {
        capture.read(webcamMatImage)
        if(!webcamMatImage.empty()) {
          bufferedImage = ocvu.toBufferedImage(webcamMatImage)
          baos = new ByteArrayOutputStream(8192 * 4)
          ImageIO.write(bufferedImage, "jpg", baos)
          data = baos.toByteArray()
          baos.close()
          os.write((
              "--${boundary}\r\n"
            + "Content-type: image/jpg\r\n"
            + "Content-Length: "
            + data.length
            + "\r\n\r\n"
          ).getBytes())
          os.write(data)
          os.flush()
        } else {
          println(" -- Frame not captured -- Break!")
          break;
        }
      }
    } else {
      println("Couldn't open capture.")
    }
    os.close()
  }
}