package model;

import java.awt.image.BufferedImage;

/**
 * This interface defines methods for image processing and management.
 */
public interface ImageProcessingModel {
  /**
   * Add an image to the image processing model.
   *
   * @param filename The name of the image file.
   * @param image    The Buffer Image object of the image.
   * @return True if the image is successfully added, false otherwise.
   */
  boolean addImage(String filename, BufferedImage image);

  /**
   * Get an image from the image processing model.
   *
   * @param filename The name of the image file.
   * @return A BufferedImage representing the requested image, or null if not found.
   */
  BufferedImage getImage(String filename);

  /**
   * Convert an image to grayscale.
   *
   * @param componentType       The type of grayscale conversion to perform.
   * @param sourceFilename      The name of the source image file.
   * @param destinationFilename The name of the destination image file.
   * @return True if the grayscale conversion is successful, false otherwise.
   */
  boolean getGreyScale(String componentType, String sourceFilename, String destinationFilename);

  /**
   * Perform a flip operation on an image.
   *
   * @param flipType            The type of flip operation to perform
   *                            (e.g., horizontal or vertical).
   * @param sourceFilename      The name of the source image file.
   * @param destinationFilename The name of the destination image file.
   * @return True if the flip operation is successful, false otherwise.
   */
  boolean doFlip(String flipType, String sourceFilename, String destinationFilename);

  /**
   * Brighten an image by a specified number of increments.
   *
   * @param increments          The number of increments to brighten the image.
   * @param sourceFilename      The name of the source image file.
   * @param destinationFilename The name of the destination image file.
   * @return True if the brightening operation is successful, false otherwise.
   */
  boolean doBrightening(int increments, String sourceFilename, String destinationFilename);

  /**
   * Split an image into its RGB components.
   *
   * @param sourceFilename           The name of the source image file.
   * @param redDestinationFilename   The name of the destination file for the red component.
   * @param greenDestinationFilename The name of the destination file for the green component.
   * @param blueDestinationFilename  The name of the destination file for the blue component.
   * @return True if the operation is successful, false otherwise.
   */
  boolean rgbSplit(String sourceFilename, String redDestinationFilename,
                   String greenDestinationFilename, String blueDestinationFilename);

  /**
   * Combine separate red, green, and blue images into one RGB image.
   *
   * @param destinationFilename The name of the destination image file.
   * @param redSourceFilename   The name of the source image file for the red component.
   * @param greenSourceFilename The name of the source image file for the green component.
   * @param blueSourceFilename  The name of the source image file for the blue component.
   * @return True if the operation is successful, false otherwise.
   */
  boolean rbgCombine(String destinationFilename, String redSourceFilename,
                     String greenSourceFilename, String blueSourceFilename);

  /**
   * Apply a blur or sharpen filter to an image.
   *
   * @param filterType          The type of filter to apply (e.g., blur or sharpen).
   * @param sourceFilename      The name of the source image file.
   * @param destinationFilename The name of the destination image file.
   * @return True if the filter is successfully applied, false otherwise.
   */
  boolean blurSharpenImage(String filterType, String sourceFilename, String destinationFilename);

  /**
   * Apply a sepia effect to an image.
   *
   * @param sourceFilename      The name of the source image file.
   * @param destinationFilename The name of the destination image file.
   * @return True if the sepia effect is successfully applied, false otherwise.
   */
  boolean getSepia(String sourceFilename, String destinationFilename);

  /**
   * Get metadata for an image.
   *
   * @param imageName The name of the image.
   * @return An ImageMetadata object containing information about the image.
   */
  ImageMetadata getImageData(String imageName);

  /**
   * Compresses an image by the specified percentage and saves the result to a destination file.
   *
   * @param percentage          The compression percentage to apply to the image.
   * @param sourceFilename      The name of the source image file.
   * @param destinationFilename The name of the destination image file.
   * @return True if the compression is successful, false otherwise.
   */
  boolean compress(int percentage, String sourceFilename, String destinationFilename);

  /**
   * Creates a histogram for the specified image and saves it to a destination file.
   *
   * @param sourceFilename      The name of the source image file.
   * @param destinationFilename The name of the destination file for the histogram.
   * @return True if the histogram creation is successful, false otherwise.
   */
  boolean createHistogram(String sourceFilename, String destinationFilename);

  /**
   * Applies color correction to the image and saves the result to a destination file.
   *
   * @param sourceFilename      The name of the source image file.
   * @param destinationFilename The name of the destination image file.
   * @return True if the color correction is successful, false otherwise.
   */
  boolean colorCorrection(String sourceFilename, String destinationFilename);

  /**
   * Adjusts the levels of an image and saves the result to a destination file.
   *
   * @param sourceFilename      The name of the source image file.
   * @param destinationFilename The name of the destination image file.
   * @param black               The black level adjustment value.
   * @param mid                 The mid-level adjustment value.
   * @param white               The white level adjustment value.
   * @return True if the level adjustment is successful, false otherwise.
   */
  boolean levelAdjustment(
      String sourceFilename, String destinationFilename, int black, int mid, int white);

  /**
   * Generates a preview of an image operation by applying the specified percentage
   * of the operation to the original image and saving the result to a destination file.
   *
   * @param percentage       The percentage of the operation to preview.
   * @param originalFilename The name of the original image file.
   * @param modifiedFilename The name of the modified image file.
   * @return True if the operation preview is successful, false otherwise.
   */
  boolean operationPreview(int percentage, String originalFilename, String modifiedFilename);

  /**
   * Generates a split preview of an image, displaying a side-by-side comparison of the original
   * and modified images with a specified percentage split.
   *
   * @param percentage       The percentage at which the split occurs. Should be between 0 and 100.
   * @param originalFilename The filename of the original image.
   * @param modifiedFilename The filename of the modified image.
   * @return A BufferedImage containing the split preview of the two images.
   */
  BufferedImage splitPreview(int percentage,
                             String originalFilename,
                             String modifiedFilename);

}
