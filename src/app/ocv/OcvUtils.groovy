package app.ocv

import org.opencv.core.Mat
import java.awt.image.BufferedImage
import java.awt.image.DataBufferByte

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
}