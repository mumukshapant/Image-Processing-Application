package model.helpers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The CompressionHelper class provides methods for compressing and decompressing image data using
 * Haar wavelet transformation.
 */
public class CompressionHelper {
  /**
   * Performs the Haar wavelet transformation on the input pixel array.
   *
   * @param oldPixels The input pixel array to be transformed.
   * @return The transformed pixel array.
   */
  public static double[][] haarTransformation(int[][] oldPixels) {

    int size = oldPixels.length;
    double[][] newDoublePixel = new double[size][size];


    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        newDoublePixel[i][j] = oldPixels[i][j];
      }
    }

    int condition = size;
    while (condition > 1) {
      newDoublePixel = getTransformation(newDoublePixel, size, condition);
      newDoublePixel = ImageHelper.transpose(newDoublePixel, size, size);
      newDoublePixel = getTransformation(newDoublePixel, size, condition);
      newDoublePixel = ImageHelper.transpose(newDoublePixel, size, size);
      condition /= 2;
    }
    return newDoublePixel;
  }

  private static double[][] getTransformation(double[][] pixelMap, int maxIteration, int c) {
    for (int i = 0; i < maxIteration; i++) {
      List<Double> avg = new ArrayList<>();
      List<Double> diff = new ArrayList<>();

      for (int j = 0; j < c - 1; j += 2) {

        double rgbA = pixelMap[i][j];
        double rgbB = pixelMap[i][j + 1];

        avg.add((rgbA + rgbB) / Math.sqrt(2));
        diff.add((rgbA - rgbB) / Math.sqrt(2));


      }

      for (int j = 0; j < avg.size(); j++) {

        double avgVal = avg.get(j);
        double diffVal = diff.get(j);
        pixelMap[i][j] = avgVal;
        pixelMap[i][j + avg.size()] = diffVal;
      }
    }
    return pixelMap;
  }

  /**
   * Performs the inverse Haar wavelet transformation on the input pixel array.
   *
   * @param pixelMapOriginal The original transformed pixel array.
   * @param size             The size of the pixel array.
   * @return The inverse transformed pixel array.
   */
  public static double[][] inverseHaarTransformation(double[][] pixelMapOriginal, int size) {

    double[][] pixelMap = pixelMapOriginal;


    int condition = 2;

    while (condition <= size) {

      // Inverse transform rows
      pixelMap = ImageHelper.transpose(pixelMap, size, size);
      pixelMap = getInversion(pixelMap, size, condition);
      pixelMap = ImageHelper.transpose(pixelMap, size, size);
      pixelMap = getInversion(pixelMap, size, condition);

      condition *= 2;
    }


    return pixelMap;
  }

  private static double[][] getInversion(double[][] pixelMap, int maxIteration, int c) {
    for (int i = 0; i < maxIteration; i++) {

      List<Double> avg = new ArrayList<>();
      List<Double> diff = new ArrayList<>();

      for (int j = 0; j < c / 2; j++) {


        double avgVal = pixelMap[i][j];
        double diffVal = pixelMap[i][j + (c / 2)];

        double rgbAvg = (avgVal + diffVal) / Math.sqrt(2);

        avg.add(rgbAvg);
        double rgbDiff = (avgVal - diffVal) / Math.sqrt(2);

        diff.add(rgbDiff);

      }
      for (int j = 0; j < c; j += 2) {
        double avgVal = avg.get(j / 2);
        double diffVal = diff.get(j / 2);
        pixelMap[i][j] = avgVal;
        pixelMap[i][j + 1] = diffVal;

      }
    }
    return pixelMap;
  }

  /**
   * Compresses the input pixel array based on the given percentage.
   *
   * @param pixel      The input pixel array to be compressed.
   * @param percentage The compression percentage.
   * @return The compressed pixel array.
   */
  public static double[][] compressImage(double[][] pixel, int percentage) {
    double thresholdValue = Double.MAX_VALUE;

    if (percentage == 100) {
      return correctRange(pixel, thresholdValue);
    }

    List<Double> singleDimensional = new ArrayList<>();

    for (double[] doubles : pixel) {
      for (int j = 0; j < pixel[0].length; j++) {
        double num = Math.round(doubles[j] * 1000.0) / 1000.0;
        singleDimensional.add(Math.abs(num));
      }
    }

    Object[] distinctPixelValues = singleDimensional.stream().distinct().toArray();
    Arrays.sort(distinctPixelValues);
    int arrayLen = distinctPixelValues.length;

    int maxIndex = (int) Math.round((arrayLen * (percentage / 100.0)));
    thresholdValue = (double) distinctPixelValues[maxIndex];

    return correctRange(pixel, thresholdValue);
  }

  /**
   * Corrects the pixel values in the given array based on the threshold value.
   *
   * @param pixels    The input pixel array.
   * @param threshold The threshold value for compression.
   * @return The corrected pixel array.
   */
  private static double[][] correctRange(double[][] pixels, double threshold) {
    for (int i = 0; i < pixels.length; i++) {
      for (int j = 0; j < pixels[i].length; j++) {
        if (Math.abs(pixels[i][j]) < threshold) {
          pixels[i][j] = 0.0;
        }
      }
    }
    return pixels;
  }

}
