package app.ocv

import org.opencv.core.Mat
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc

class BlurImg {

  def imgProcessor(Map params) {
    def utils = new OcvUtils()
    return utils.convertToImg(params, { img, query ->
      return utils.toBufferedImage(blur(img, query.times))
    })
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
