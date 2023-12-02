package model;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import static model.helpers.ImageHelper.adjustPadding;
import static model.helpers.ImageHelper.correctPixelRange;
import static model.helpers.CompressionHelper.compressImage;
import static model.helpers.CompressionHelper.haarTransformation;
import static model.helpers.CompressionHelper.inverseHaarTransformation;
import static model.helpers.PlotHelper.getNormalizedHistogram;

/**
 * Implementation of the ImageProcessingModel interface for image processing and management.
 */
public class ImageProcessingModelImpl implements ImageProcessingModel {

  private final Map<String, ImageMetadata> imageNameDetailsMap;

  private final OutputStream out;

  /**
   * Constructs an ImageProcessingModelImpl object with the specified output stream.
   *
   * @param out The output stream for error messages and logging.
   */
  public ImageProcessingModelImpl(OutputStream out) {
    imageNameDetailsMap = new HashMap<>();
    this.out = out;
  }

  /**
   * Apply a filter to the image defined by the filter matrix.
   *
   * @param imageDetails The ImageMetadata object representing the input image.
   * @param filter       The filter matrix to be applied.
   * @return A new ImageMetadata object representing the filtered image.
   */
  private static ImageMetadata applyFilter(ImageMetadata imageDetails, double[][] filter) {
    int width = imageDetails.getWidth();
    int height = imageDetails.getHeight();
    int[][][] pixelMap = imageDetails.getRgb();
    int[][][] newPixelMap = new int[3][height][width];

    int filterSize = filter.length;
    int filterRadius = filterSize / 2;

    for (int i = filterRadius; i < height - filterRadius; i++) {
      for (int j = filterRadius; j < width - filterRadius; j++) {
        double red = 0.0;
        double green = 0.0;
        double blue = 0.0;

        for (int yOffset = -filterRadius; yOffset <= filterRadius; yOffset++) {
          for (int xOffset = -filterRadius; xOffset <= filterRadius; xOffset++) {
            int redVal = pixelMap[0][i + yOffset][j + xOffset];
            int greenVal = pixelMap[1][i + yOffset][j + xOffset];
            int blueVal = pixelMap[2][i + yOffset][j + xOffset];

            double filterValue = filter[filterRadius + yOffset][filterRadius + xOffset];
            red += redVal * filterValue;
            green += greenVal * filterValue;
            blue += blueVal * filterValue;

          }
        }

        int newRed = (int) Math.min(Math.max(red, 0), 255);
        int newGreen = (int) Math.min(Math.max(green, 0), 255);
        int newBlue = (int) Math.min(Math.max(blue, 0), 255);

        newPixelMap[0][i][j] = newRed;
        newPixelMap[1][i][j] = newGreen;
        newPixelMap[2][i][j] = newBlue;

      }
    }

    return new ImageMetadata(width, height, newPixelMap);
  }


  @Override
  public BufferedImage getImage(String filename) {
    if (isFileExisting(filename)) {
      return null;
    }
    ImageMetadata imageDetails = imageNameDetailsMap.get(filename);

    int width = imageDetails.getWidth();
    int height = imageDetails.getHeight();
    int[][][] rgb = imageDetails.getRgb();

    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {

        int r = rgb[0][i][j];
        int g = rgb[1][i][j];
        int b = rgb[2][i][j];

        int pixelValue = (255 << 24) | (r << 16) | (g << 8) | b;
        image.setRGB(j, i, pixelValue);
      }
    }

    return image;
  }

  @Override
  public boolean getGreyScale(String componentType, String sourceFilename,
                              String destinationFilename) {
    if (isFileExisting(sourceFilename)) {
      return false;
    }
    ImageMetadata imageDetails = imageNameDetailsMap.get(sourceFilename);
    ImageMetadata newImage = getGreyScaleComponents(componentType, imageDetails);
    imageNameDetailsMap.put(destinationFilename, newImage);
    return true;
  }

  @Override
  public boolean doFlip(String flipType, String sourceFilename, String destinationFilename) {
    if (isFileExisting(sourceFilename)) {
      return false;
    }
    ImageMetadata imageDetails = imageNameDetailsMap.get(sourceFilename);
    ImageMetadata newImage = flipImage(imageDetails, flipType);
    imageNameDetailsMap.put(destinationFilename, newImage);
    return true;
  }

  @Override
  public boolean doBrightening(
      int increments, String sourceFilename, String destinationFilename) {
    if (isFileExisting(sourceFilename)) {
      return false;
    }
    ImageMetadata imageDetails = imageNameDetailsMap.get(sourceFilename);

    ImageMetadata newImage = brightenDarken(imageDetails, increments);
    imageNameDetailsMap.put(destinationFilename, newImage);
    return true;
  }

  @Override
  public boolean rgbSplit(String sourceFilename, String redDestinationFilename,
                          String greenDestinationFilename, String blueDestinationFilename) {
    if (isFileExisting(sourceFilename)) {
      return false;
    }
    ImageMetadata imageDetails = imageNameDetailsMap.get(sourceFilename);

    ImageMetadata redImage = getGreyScaleComponents("red-component", imageDetails);
    imageNameDetailsMap.put(redDestinationFilename, redImage);

    ImageMetadata greenImage = getGreyScaleComponents("green-component", imageDetails);
    imageNameDetailsMap.put(greenDestinationFilename, greenImage);

    ImageMetadata blueImage = getGreyScaleComponents("blue-component", imageDetails);
    imageNameDetailsMap.put(blueDestinationFilename, blueImage);

    return true;
  }

  @Override
  public boolean rbgCombine(String destinationFilename, String redSourceFilename,
                            String greenSourceFilename, String blueSourceFilename) {
    if (isFileExisting(redSourceFilename)
        || isFileExisting(greenSourceFilename)
        || isFileExisting(blueSourceFilename)) {
      return false;
    }
    ImageMetadata redImageDetails = imageNameDetailsMap.get(redSourceFilename);

    ImageMetadata greenImageDetails = imageNameDetailsMap.get(greenSourceFilename);

    ImageMetadata blueImageDetails = imageNameDetailsMap.get(blueSourceFilename);


    ImageMetadata newImage = getMergedImage(redImageDetails, greenImageDetails, blueImageDetails);
    imageNameDetailsMap.put(destinationFilename, newImage);
    return true;
  }

  /**
   * Get the merged image from separate red, green, and blue component images.
   *
   * @param redImageDetails   The ImageMetadata object representing the red component image.
   * @param greenImageDetails The ImageMetadata object representing the green component image.
   * @param blueImageDetails  The ImageMetadata object representing the blue component image.
   * @return A new ImageMetadata object representing the merged RGB image.
   */
  private ImageMetadata getMergedImage(ImageMetadata redImageDetails,
                                       ImageMetadata greenImageDetails,
                                       ImageMetadata blueImageDetails) {

    int height = redImageDetails.getHeight();
    int width = redImageDetails.getWidth();

    int[][][] newPixelMap = new int[3][height][width];

    int[][][] redPixelMap = redImageDetails.getRgb();
    int[][][] greenPixelMap = greenImageDetails.getRgb();
    int[][][] bluePixelMap = blueImageDetails.getRgb();

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        newPixelMap[0][i][j] = redPixelMap[0][i][j];
        newPixelMap[1][i][j] = greenPixelMap[1][i][j];
        newPixelMap[2][i][j] = bluePixelMap[2][i][j];
      }
    }
    return new ImageMetadata(width, height, newPixelMap);
  }

  @Override
  public boolean blurSharpenImage(String filterType, String sourceFilename,
                                  String destinationFilename) {
    if (isFileExisting(sourceFilename)) {
      return false;
    }
    ImageMetadata imageDetails = imageNameDetailsMap.get(sourceFilename);

    final double[][] sharpenFilter = {
        {-1.0 / 8, -1.0 / 8, -1.0 / 8, -1.0 / 8, -1.0 / 8},
        {-1.0 / 8, 1.0 / 4, 1.0 / 4, 1.0 / 4, -1.0 / 8},
        {-1.0 / 8, 1.0 / 4, 1.0, 1.0 / 4, -1.0 / 8},
        {-1.0 / 8, 1.0 / 4, 1.0 / 4, 1.0 / 4, -1.0 / 8},
        {-1.0 / 8, -1.0 / 8, -1.0 / 8, -1.0 / 8, -1.0 / 8},
    };

    final double[][] blurFilter = {
        {1.0 / 16, 1.0 / 8, 1.0 / 16},
        {1.0 / 8, 1.0 / 4, 1.0 / 8},
        {1.0 / 16, 1.0 / 8, 1.0 / 16}
    };

    ImageMetadata newImage = applyFilter(imageDetails,
        filterType.equals("blur") ? blurFilter : sharpenFilter);
    imageNameDetailsMap.put(destinationFilename, newImage);
    return true;
  }

  @Override
  public boolean getSepia(String sourceFilename, String destinationFilename) {
    if (isFileExisting(sourceFilename)) {
      return false;
    }
    ImageMetadata imageDetails = imageNameDetailsMap.get(sourceFilename);
    ImageMetadata newImage = applySepia(imageDetails);
    imageNameDetailsMap.put(destinationFilename, newImage);
    return true;
  }

  @Override
  public ImageMetadata getImageData(String imageName) {
    ImageMetadata imageMetadata = imageNameDetailsMap.get(imageName);
    return new ImageMetadata(imageMetadata.getWidth(),
        imageMetadata.getHeight(), imageMetadata.getRgb());
  }

  @Override
  public boolean addImage(String filename, BufferedImage image) {
    imageNameDetailsMap.remove(filename);
    imageNameDetailsMap.put(filename, getImageMetadata(image));
    return true;
  }

  /**
   * Get grayscale components of an image based on the selected component type.
   *
   * @param greyScaleType The type of grayscale component to extract.
   * @param main          The ImageMetadata object representing the input image.
   * @return A new ImageMetadata object representing the selected grayscale component.
   */
  private ImageMetadata getGreyScaleComponents(String greyScaleType, ImageMetadata main) {
    int width = main.getWidth();
    int height = main.getHeight();
    int[][][] pixelMap = main.getRgb();
    int[][][] newPixelMap = new int[3][height][width];

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        int[] greyScale = new int[3];
        int value;

        switch (greyScaleType) {
          case "red-component":
            greyScale = new int[]{pixelMap[0][i][j], 0, 0};
            break;
          case "green-component":
            greyScale = new int[]{0, pixelMap[1][i][j], 0};
            break;
          case "blue-component":
            greyScale = new int[]{0, 0, pixelMap[2][i][j]};
            break;
          case "value-component":
            value = Math.max(Math.max(pixelMap[0][i][j], pixelMap[1][i][j]), pixelMap[2][i][j]);
            greyScale = new int[]{value, value, value};
            break;
          case "intensity-component":
            value = (pixelMap[0][i][j]
                + pixelMap[1][i][j] + pixelMap[2][i][j]) / 3;
            greyScale = new int[]{value, value, value};
            break;
          case "luma-component":
            value = (int) ((0.299 * pixelMap[0][i][j])
                + (0.587 * pixelMap[1][i][j]) + (0.114 * pixelMap[2][i][j]));
            greyScale = new int[]{value, value, value};
            break;
          default:
            // Handle any other cases or leave it empty if needed.
        }

        newPixelMap[0][i][j] = greyScale[0];
        newPixelMap[1][i][j] = greyScale[1];
        newPixelMap[2][i][j] = greyScale[2];
      }
    }

    return new ImageMetadata(width, height, newPixelMap);
  }

  /**
   * Flip an image horizontally or vertically.
   *
   * @param imageDetails The ImageMetadata object representing the input image.
   * @param flipType     The type of flip operation to perform.
   * @return A new ImageMetadata object representing the flipped image.
   */
  private ImageMetadata flipImage(ImageMetadata imageDetails, String flipType) {

    int width = imageDetails.getWidth();
    int height = imageDetails.getHeight();

    int[][][] pixelMap = imageDetails.getRgb();
    int[][][] newPixelMap = new int[3][height][width];
    int[][] redPixels = pixelMap[0];
    int[][] greenPixels = pixelMap[1];
    int[][] bluePixels = pixelMap[2];

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {

        int redVal = redPixels[i][j];
        int greenVal = greenPixels[i][j];
        int blueVal = bluePixels[i][j];

        int newI;
        int newJ;

        if ("horizontal-flip".equalsIgnoreCase(flipType)) {
          newI = i;
          newJ = width - 1 - j;
        } else if ("vertical-flip".equalsIgnoreCase(flipType)) {
          newI = height - 1 - i;
          newJ = j;
        } else {
          newI = height - 1 - i;
          newJ = width - 1 - j;
        }
        newPixelMap[0][newI][newJ] = redVal;
        newPixelMap[1][newI][newJ] = greenVal;
        newPixelMap[2][newI][newJ] = blueVal;
      }
    }

    return new ImageMetadata(width, height, newPixelMap);
  }

  /**
   * Brighten or darken an image by a specified value.
   *
   * @param imageDetails The ImageMetadata object representing the input image.
   * @param increments   The number of increments to brighten or darken the image.
   * @return A new ImageMetadata object representing the brightened or darkened image.
   */
  private ImageMetadata brightenDarken(ImageMetadata imageDetails, int increments) {
    int width = imageDetails.getWidth();
    int height = imageDetails.getHeight();
    int[][][] pixelMap = imageDetails.getRgb();
    int[][][] newPixelMap = new int[3][height][width];

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {

        int r = correctPixelRange(pixelMap[0][i][j] + increments);
        int g = correctPixelRange(pixelMap[1][i][j] + increments);
        int b = correctPixelRange(pixelMap[2][i][j] + increments);

        newPixelMap[0][i][j] = r;
        newPixelMap[1][i][j] = g;
        newPixelMap[2][i][j] = b;
      }

    }

    return new ImageMetadata(width, height, newPixelMap);
  }

  /**
   * Apply a sepia effect to an image.
   *
   * @param imageDetails The ImageMetadata object representing the input image.
   * @return A new ImageMetadata object representing the image with the sepia effect applied.
   */
  private ImageMetadata applySepia(ImageMetadata imageDetails) {
    int width = imageDetails.getWidth();
    int height = imageDetails.getHeight();

    int[][][] pixelMap = imageDetails.getRgb();
    int[][][] newPixelMap = new int[3][height][width];

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        int r = pixelMap[0][i][j];
        int g = pixelMap[1][i][j];
        int b = pixelMap[2][i][j];

        int newR = correctPixelRange((int) (0.393 * r + 0.769 * g + 0.189 * b));
        int newG = correctPixelRange((int) (0.349 * r + 0.686 * g + 0.168 * b));
        int newB = correctPixelRange((int) (0.272 * r + 0.534 * g + 0.131 * b));

        newPixelMap[0][i][j] = newR;
        newPixelMap[1][i][j] = newG;
        newPixelMap[2][i][j] = newB;

      }
    }

    return new ImageMetadata(width, height, newPixelMap);
  }

  /**
   * Check if a file with the given filename exists in the image processing model.
   *
   * @param filename The name of the file to check.
   * @return True if the file exists in the model, false otherwise.
   */
  private boolean isFileExisting(String filename) {
    PrintStream out = new PrintStream(this.out);
    if (!imageNameDetailsMap.containsKey(filename)) {
      out.print(filename + " not present in the application.\n");
      return true;
    }
    return false;
  }

  private ImageMetadata getImageMetadata(BufferedImage bufferedImage) {
    int width = bufferedImage.getWidth();
    int height = bufferedImage.getHeight();
    int[][][] pixelMap = new int[3][height][width];

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        int rgb = bufferedImage.getRGB(j, i);
        Color color = new Color(rgb);
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        pixelMap[0][i][j] = r;
        pixelMap[1][i][j] = g;
        pixelMap[2][i][j] = b;
      }
    }

    return new ImageMetadata(width, height, pixelMap);
  }

  /**
   * Compress an image using the Haar Wavelet Transform with a specified compression percentage.
   *
   * @param percentage          The compression percentage (between 0 and 100).
   * @param sourceFilename      The name of the source image.
   * @param destinationFilename The name of the destination compressed image.
   * @return True if the compression is successful, false otherwise.
   */
  @Override
  public boolean compress(int percentage, String sourceFilename, String destinationFilename) {
    if (isFileExisting(sourceFilename)) {
      return false;
    }
    ImageMetadata originalImage = imageNameDetailsMap.get(sourceFilename);

    ImageMetadata compressedImage = getCompressedImage(originalImage, percentage);

    imageNameDetailsMap.put(destinationFilename, compressedImage);

    return true;
  }


  /**
   * Applies a compression operation to the input image based on a given percentage.
   *
   * @param imageMetadata The {@link ImageMetadata} object representing the input image.
   * @param percentage    The percentage of compression to be applied.
   * @return A new {@link ImageMetadata} object representing the compressed image.
   */
  private ImageMetadata getCompressedImage(ImageMetadata imageMetadata, int percentage) {
    if (percentage < 1) {
      return imageMetadata;
    }
    int height = imageMetadata.getHeight();
    int width = imageMetadata.getWidth();
    int nextSq = 1;
    while (nextSq < Math.max(width, height)) {
      nextSq *= 2;
    }
    //padding

    ImageMetadata paddedImage = adjustPadding(imageMetadata, nextSq, nextSq);

    int size = paddedImage.getWidth();
    int[][][] pixelMap = paddedImage.getRgb();

    int[][] redPadded = pixelMap[0];
    int[][] greenPadded = pixelMap[1];
    int[][] bluePadded = pixelMap[2];

    //haar
    double[][] redHaar = haarTransformation(redPadded);
    double[][] greenHaar = haarTransformation(greenPadded);
    double[][] blueHaar = haarTransformation(bluePadded);

    //compress
    double[][] redCompressed = compressImage(redHaar, percentage);
    double[][] greenCompressed = compressImage(greenHaar, percentage);
    double[][] blueCompressed = compressImage(blueHaar, percentage);

    //invhaar
    double[][] redFinal = inverseHaarTransformation(redCompressed, size);
    double[][] greenFinal = inverseHaarTransformation(greenCompressed, size);
    double[][] blueFinal = inverseHaarTransformation(blueCompressed, size);

    //rgb combine
    int[][][] compressesRGB = new int[3][size][size];
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        compressesRGB[0][i][j] = (int) redFinal[i][j];
        compressesRGB[1][i][j] = (int) greenFinal[i][j];
        compressesRGB[2][i][j] = (int) blueFinal[i][j];
      }
    }

    ImageMetadata compressedImage = adjustPadding(new ImageMetadata(size, size, compressesRGB),
        imageMetadata.getHeight(), imageMetadata.getWidth());

    int[][][] compressedPixels = compressedImage.getRgb();
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        compressedPixels[0][i][j] = correctPixelRange(compressedPixels[0][i][j]);
        compressedPixels[1][i][j] = correctPixelRange(compressedPixels[1][i][j]);
        compressedPixels[2][i][j] = correctPixelRange(compressedPixels[2][i][j]);
      }
    }

    return new ImageMetadata(width, height, compressedPixels);
  }

  @Override
  public boolean createHistogram(String sourceFilename, String destinationFilename) {
    if (isFileExisting(sourceFilename)) {
      return false;
    }
    ImageMetadata imageDetails = imageNameDetailsMap.get(sourceFilename);
    ImageMetadata newImage = getNormalizedHistogram(imageDetails);
    imageNameDetailsMap.put(destinationFilename, newImage);
    return true;
  }

  @Override
  public boolean colorCorrection(String sourceFilename, String destinationFilename) {
    if (isFileExisting(sourceFilename)) {
      return false;
    }
    ImageMetadata imageDetails = imageNameDetailsMap.get(sourceFilename);
    ImageMetadata newImage = doColorCorrection(imageDetails);
    imageNameDetailsMap.put(destinationFilename, newImage);
    return true;
  }

  /**
   * Performs color correction on the input image based on the color frequency distribution.
   *
   * @param imageDetails The {@link ImageMetadata} object representing the input image.
   * @return A new {@link ImageMetadata} object representing the color-corrected image.
   */
  private ImageMetadata doColorCorrection(ImageMetadata imageDetails) {

    int height = imageDetails.getHeight();
    int width = imageDetails.getWidth();
    int[][][] rgb = imageDetails.getRgb();

    int[] redFrequency = new int[256];
    int[] greenFrequency = new int[256];
    int[] blueFrequency = new int[256];

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        redFrequency[rgb[0][i][j]]++;
        greenFrequency[rgb[1][i][j]]++;
        blueFrequency[rgb[2][i][j]]++;
      }
    }

    int redPeak = 0;
    int greenPeak = 0;
    int bluePeak = 0;
    int redIndex = 0;
    int greenIndex = 0;
    int blueIndex = 0;
    // to take only meaningful peaks
    for (int i = 10; i <= 245; i++) {
      if (redPeak < redFrequency[i]) {
        redPeak = redFrequency[i];
        redIndex = i;
      }
      if (greenPeak < greenFrequency[i]) {
        greenPeak = greenFrequency[i];
        greenIndex = i;
      }
      if (bluePeak < blueFrequency[i]) {
        bluePeak = blueFrequency[i];
        blueIndex = i;
      }
    }
    int averageIndex = (redIndex + greenIndex + blueIndex) / 3;

    int[][][] newRGB = new int[3][height][width];

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        newRGB[0][i][j] = adjustValue(rgb[0][i][j], redIndex, averageIndex);
        newRGB[1][i][j] = adjustValue(rgb[1][i][j], greenIndex, averageIndex);
        newRGB[2][i][j] = adjustValue(rgb[2][i][j], blueIndex, averageIndex);
      }
    }

    return new ImageMetadata(width, height, newRGB);
  }

  /**
   * Adjusts the original pixel value based on the peak value and average peak.
   * This method calculates the adjusted pixel value using the formula:
   * adjustedValue = originalValue + (averagePeak - peakValue)
   * The result is then passed through the,
   * correctPixelRange method to ensure it is within a valid range.
   *
   * @param originalValue The original pixel value to be adjusted.
   * @param peakValue     The peak value used in the adjustment calculation.
   * @param averagePeak   The average peak value used in the adjustment calculation.
   * @return The adjusted pixel value within a valid range.
   */
  private int adjustValue(int originalValue, int peakValue, int averagePeak) {
    int adjustedValue = originalValue + (averagePeak - peakValue);
    return correctPixelRange(adjustedValue);
  }

  @Override
  public boolean levelAdjustment(
      String sourceFilename, String destinationFilename, int black, int mid, int white) {
    if (isFileExisting(sourceFilename)) {
      return false;
    }
    ImageMetadata imageDetails = imageNameDetailsMap.get(sourceFilename);
    ImageMetadata newImage = levelAdjustmentProcess(imageDetails, black, mid, white);
    imageNameDetailsMap.put(destinationFilename, newImage);
    return true;

  }

  /**
   * Adjusts the levels of the input image based on specified black, mid, and white values.
   *
   * @param imageDetails The {@link ImageMetadata} object representing the input image.
   * @param black        The black level for adjustment.
   * @param mid          The mid-level for adjustment.
   * @param white        The white level for adjustment.
   * @return A new {@link ImageMetadata} object representing the level-adjusted image.
   */
  private ImageMetadata levelAdjustmentProcess(
      ImageMetadata imageDetails, int black, int mid, int white) {

    int height = imageDetails.getHeight();
    int width = imageDetails.getWidth();
    int[][][] pixelsMap = imageDetails.getRgb();
    double[] bestFit = getCurveFittingPoints(black, mid, white);

    double a = bestFit[0];
    double b = bestFit[1];
    double c = bestFit[2];

    int[][][] newLeveledPixelMap = new int[3][height][width];

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {

        int red = pixelsMap[0][i][j];
        newLeveledPixelMap[0][i][j]
            = correctPixelRange((int) ((a * red * red) + (b * red) + c));

        int green = pixelsMap[1][i][j];
        newLeveledPixelMap[1][i][j]
            = correctPixelRange((int) ((a * green * green) + (b * green) + c));

        int blue = pixelsMap[2][i][j];
        newLeveledPixelMap[2][i][j]
            = correctPixelRange((int) ((a * blue * blue) + (b * blue) + c));
      }
    }
    return new ImageMetadata(width, height, newLeveledPixelMap);

  }

  /**
   * Calculates the coefficients for curve fitting based on given black, mid, and white values.
   * This method computes the coefficients a, aA, aB, and aC for the quadratic equation:
   * a * x^2 + b * x + c = y, where x is the pixel value.
   *
   * @param black The pixel intensity corresponding to the darkest point.
   * @param mid   The pixel intensity corresponding to the mid-tones point.
   * @param white The pixel intensity corresponding to the brightest point.
   * @return An array with coefficient for quadratic equation.
   */
  private double[] getCurveFittingPoints(int black, int mid, int white) {
    double a = ((Math.pow(black, 2) * (mid - white)) - (black * (Math.pow(mid, 2)
        - Math.pow(white, 2))) + (white * Math.pow(mid, 2))
        - (mid * Math.pow(white, 2)));

    double aA = (-1 * black * (128 - 255)) + 128 * white - 255 * mid;

    double aB = ((Math.pow(black, 2) * (128 - 255))
        + (255 * Math.pow(mid, 2)) - (128 * Math.pow(white, 2)));

    double aC = ((Math.pow(black, 2) * (255 * mid - 128 * white))
        - (black * ((255 * Math.pow(mid, 2)) - (128 * Math.pow(white, 2)))));

    return new double[]{aA / a, aB / a, aC / a};
  }

  @Override
  public boolean operationPreview(
      int percentage,
      String originalfilename,
      String modifiedfilename) {
    if (isFileExisting(originalfilename) || isFileExisting(modifiedfilename)) {
      return false;
    }
    ImageMetadata originalImage = imageNameDetailsMap.get(originalfilename);
    ImageMetadata modifiedImage = imageNameDetailsMap.get(modifiedfilename);


    if (originalImage.getWidth() != modifiedImage.getWidth()
        || originalImage.getHeight() != modifiedImage.getHeight()) {
      return false;
    }

    int width = modifiedImage.getWidth();
    int startIndex = (int) (width * (percentage / 100.0));

    int[][][] originalPixels = originalImage.getRgb();
    int[][][] modifiedPixels = modifiedImage.getRgb();
    for (int i = 0; i < modifiedImage.getHeight(); i++) {
      for (int j = startIndex; j < width; j++) {
        modifiedPixels[0][i][j] = originalPixels[0][i][j];
        modifiedPixels[1][i][j] = originalPixels[1][i][j];
        modifiedPixels[2][i][j] = originalPixels[2][i][j];
      }
    }
    return true;
  }

  @Override
  public BufferedImage splitPreview(int percentage,
                                    String originalFilename,
                                    String modifiedFilename) {
    if (isFileExisting(originalFilename) || isFileExisting(modifiedFilename)) {
      return null;
    }
    ImageMetadata originalImage = imageNameDetailsMap.get(originalFilename);
    ImageMetadata modifiedImage = imageNameDetailsMap.get(modifiedFilename);


    int width = modifiedImage.getWidth();
    int startIndex = (int) (width * (percentage / 100.0));

    int[][][] originalPixels = originalImage.getRgb();
    int[][][] modifiedPixels = modifiedImage.getRgb();

    BufferedImage image = new BufferedImage(width,
        modifiedImage.getHeight(), BufferedImage.TYPE_INT_RGB);

    for (int i = 0; i < modifiedImage.getHeight(); i++) {
      for (int j = 0; j < width; j++) {
        int r;
        int g;
        int b;
        if (j < startIndex) {
          r = modifiedPixels[0][i][j];
          g = modifiedPixels[1][i][j];
          b = modifiedPixels[2][i][j];
        } else {
          r = originalPixels[0][i][j];
          g = originalPixels[1][i][j];
          b = originalPixels[2][i][j];
        }

        int pixelValue = (255 << 24) | (r << 16) | (g << 8) | b;
        image.setRGB(j, i, pixelValue);
      }
    }
    return image;
  }

}