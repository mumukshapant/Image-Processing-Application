package model.helpers;

import model.ImageMetadata;

import java.util.Arrays;

/**
 * Helper class for generating and manipulating image histograms.
 */
public class PlotHelper {
  /**
   * Generates a normalized histogram for the given image.
   *
   * @param image The input image metadata.
   * @return A new image metadata representing the normalized histogram.
   */
  public static ImageMetadata getNormalizedHistogram(ImageMetadata image) {
    int height = image.getHeight();
    int width = image.getWidth();

    int[][] redComponent = image.getRgb()[0];
    int[] redFrequencyArray = new int[256];
    int[][] greenComponent = image.getRgb()[1];
    int[] greenFrequencyArray = new int[256];
    int[][] blueComponent = image.getRgb()[2];
    int[] blueFrequencyArray = new int[256];

    // Calculate the red component frequencies
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        redFrequencyArray[redComponent[i][j]]++;
        greenFrequencyArray[greenComponent[i][j]]++;
        blueFrequencyArray[blueComponent[i][j]]++;
      }
    }


    // Find the maximum value
    int maxRedValue = Arrays.stream(redFrequencyArray).max().orElseThrow();
    int maxGreenValue = Arrays.stream(greenFrequencyArray).max().orElseThrow();
    int maxBlueValue = Arrays.stream(blueFrequencyArray).max().orElseThrow();
    int maxRGB = Math.max(maxBlueValue, Math.max(maxGreenValue, maxRedValue));

    // Apply Min-Max normalization
    int[] normalizedRedFrequencies = new int[256];
    int[] normalizedGreenFrequencies = new int[256];
    int[] normalizedBlueFrequencies = new int[256];

    for (int i = 0; i < 256; i++) {
      double redVal = Math.round(redFrequencyArray[i] * 255.0 / maxRGB);
      double greenVal = Math.round(greenFrequencyArray[i] * 255.0 / maxRGB);
      double blueVal = Math.round(blueFrequencyArray[i] * 255.0 / maxRGB);
      normalizedRedFrequencies[i] = (int) redVal;
      normalizedGreenFrequencies[i] = (int) greenVal;
      normalizedBlueFrequencies[i] = (int) blueVal;
    }


    // Create a new image with red components based on normalized frequencies
    int[][][] newImage = new int[3][256][256];
    for (int i = 0; i < 256; i++) {
      for (int j = 0; j < 256; j++) {
        if (i % 15 == 0 || j % 15 == 0) {
          newImage[0][i][j] = 200;
          newImage[1][i][j] = 200;
          newImage[2][i][j] = 200;
        } else {
          newImage[0][i][j] = 255;
          newImage[1][i][j] = 255;
          newImage[2][i][j] = 255;
        }

      }
    }


    int prevR = 0;
    int prevG = 0;
    int prevB = 0;
    for (int j = 0; j < 256; j++) {
      int currR = 255 - normalizedRedFrequencies[j];
      int currG = 255 - normalizedGreenFrequencies[j];
      int currB = 255 - normalizedBlueFrequencies[j];
      drawLine(prevR, currR, newImage, j, new int[]{255, 0, 0});
      prevR = currR;
      drawLine(prevG, currG, newImage, j, new int[]{0, 255, 0});
      prevG = currG;
      drawLine(prevB, currB, newImage, j, new int[]{0, 0, 255});
      prevB = currB;
    }

    return new ImageMetadata(256, 256, newImage);
  }

  /**
   * Draws a line in the image based on the start and end values along a specified index.
   *
   * @param start The starting value for the line.
   * @param end   The ending value for the line.
   * @param image The 3D array representing the image.
   * @param index The index along which the line is drawn.
   * @param rgb   The RGB values for the line.
   */
  private static void drawLine(int start, int end, int[][][] image, int index, int[] rgb) {
    if (start < end) {
      for (int i = start; i <= end; i++) {
        image[0][i][index] = rgb[0];
        image[1][i][index] = rgb[1];
        image[2][i][index] = rgb[2];
      }
    } else {
      for (int i = start; i >= end; i--) {
        image[0][i][index] = rgb[0];
        image[1][i][index] = rgb[1];
        image[2][i][index] = rgb[2];
      }
    }
  }

}
