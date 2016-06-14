package app.ocv

import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc
import java.awt.image.BufferedImage
import java.awt.image.DataBufferByte
import javax.imageio.ImageIO


class BlurImg {

  def imgProcessor(Map params) {
    String file_path = params.path
    Mat img = Imgcodecs.imread(file_path)
    if(img.dataAddr() != 0) {
      def buff = toBufferedImage(blur(img, params.query.times))
      ByteArrayOutputStream baos = new ByteArrayOutputStream()
      ImageIO.write(buff, params.imgFormat, baos)
      byte[] image_in_byte = baos.toByteArray()
      baos.close()
      return image_in_byte
    }
  }

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
