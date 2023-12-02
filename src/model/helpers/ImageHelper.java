package model.helpers;

import model.ImageMetadata;

/**
 * Helper class for image processing operations.
 */
public class ImageHelper {

  /**
   * Transposes a 2D array of pixel values.
   *
   * @param pixelMap The 2D array of pixel values to transpose.
   * @param rows     The number of rows in the original array.
   * @param cols     The number of columns in the original array.
   * @return The transposed 2D array of pixel values.
   */
  static double[][] transpose(double[][] pixelMap, int rows, int cols) {
    double[][] transposedPixelMap = new double[cols][rows];
    for (int i = 0; i < cols; i++) {
      for (int j = 0; j < rows; j++) {
        transposedPixelMap[i][j] = pixelMap[j][i];
      }
    }
    return transposedPixelMap;
  }

  /**
   * Ensures that a pixel value is within the valid range [0, 255].
   *
   * @param pixelValue The original pixel value.
   * @return The corrected pixel value within the range [0, 255].
   */
  public static int correctPixelRange(int pixelValue) {
    return Math.min(255, Math.max(0, pixelValue));
  }

  /**
   * Adjusts the padding of an image to the specified height and width.
   *
   * @param imageMetadata The metadata of the original image.
   * @param height        The desired height after padding.
   * @param width         The desired width after padding.
   * @return The image metadata with adjusted padding.
   */
  public static ImageMetadata adjustPadding(ImageMetadata imageMetadata, int height, int width) {
    int[][][] newPixelPadded = new int[3][height][width];
    int[][][] oldPixels = imageMetadata.getRgb();
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        if (i >= imageMetadata.getHeight() || j >= imageMetadata.getWidth()) {
          newPixelPadded[0][i][j] = 0;
          newPixelPadded[1][i][j] = 0;
          newPixelPadded[2][i][j] = 0;
        } else {
          newPixelPadded[0][i][j] = oldPixels[0][i][j];
          newPixelPadded[1][i][j] = oldPixels[1][i][j];
          newPixelPadded[2][i][j] = oldPixels[2][i][j];
        }
      }
    }
    return new ImageMetadata(width, height, newPixelPadded);
  }
}
