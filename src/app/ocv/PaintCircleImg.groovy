package app.ocv

import org.opencv.core.Point
import org.opencv.core.Scalar
import org.opencv.imgproc.Imgproc

class PaintCircleImg {

  def imgProcessor(Map params) {
    def utils = new OcvUtils()
    return utils.convertToImg(params, { img, query ->
      paint(img, query.x, query.y)
      return utils.toBufferedImage(img)
    })
  }

  public paint(image, x, y) {
    Imgproc.circle(image, new Point(x, y), 20, new Scalar(0, 0, 255), 4)
  }
}