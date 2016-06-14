package app.ocv

import org.opencv.core.Mat
import org.opencv.core.Point
import org.opencv.core.Scalar
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc
import javax.imageio.ImageIO

class PaintCircleImg {

  def imgProcessor(Map params) {
    String file_path = params.path
    Mat img = Imgcodecs.imread(file_path)
    if(img.dataAddr() != 0) {
      paint(img, params.query.x, params.query.y)
      def buff = new OcvUtils().toBufferedImage(img)
      ByteArrayOutputStream baos = new ByteArrayOutputStream()
      ImageIO.write(buff, params.imgFormat, baos)
      byte[] image_in_byte = baos.toByteArray()
      baos.close()
      return image_in_byte
    }
  }

  public paint(image, x, y) {
    Imgproc.circle(image, new Point(x, y), 20, new Scalar(0, 0, 255), 4);
  }
}