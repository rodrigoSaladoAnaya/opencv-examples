package app.ocv

import org.opencv.core.Mat
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc
import javax.imageio.ImageIO

class BlurImg {

  def imgProcessor(Map params) {
    String file_path = params.path
    Mat img = Imgcodecs.imread(file_path)
    if(img.dataAddr() != 0) {
      def buff = new OcvUtils().toBufferedImage(blur(img, params.query.times))
      ByteArrayOutputStream baos = new ByteArrayOutputStream()
      ImageIO.write(buff, params.imgFormat, baos)
      byte[] image_in_byte = baos.toByteArray()
      baos.close()
      return image_in_byte
    }
  }

  public Mat blur(Mat input, int times) {
    Mat src_img = new Mat()
    Mat new_img = input.clone()
    for(int i = 0; i < times; i++) {
      src_img = new_img.clone()
      Imgproc.blur(src_img, new_img, new Size(3.0, 3.0))
    }
    return new_img
  }
}
