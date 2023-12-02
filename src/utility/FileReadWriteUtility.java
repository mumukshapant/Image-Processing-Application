package utility;

import java.awt.image.BufferedImage;
import java.io.PrintStream;

/**
 * This interface defines methods for reading and writing images in different formats.
 */
public interface FileReadWriteUtility {

  /**
   * Loads an image from a PPM file.
   *
   * @param filepath The file path to the PPM image file.
   * @return A BufferedImage representing the loaded image.
   */
  BufferedImage loadImageFromPPM(String filepath);

  /**
   * Loads an image using Java's ImageIO library.
   *
   * @param filepath The file path to the image file.
   * @return A BufferedImage representing the loaded image.
   */
  BufferedImage loadImageFromImageIO(String filepath);

  /**
   * Saves a BufferedImage as a PPM image.
   *
   * @param outputFileName The name of the output PPM image file.
   * @param image          The BufferedImage to be saved.
   * @return true if the image was successfully saved, false otherwise.
   */
  boolean savePPMImage(String outputFileName, BufferedImage image);

  /**
   * Saves a BufferedImage using Java's ImageIO library.
   *
   * @param outputFileName The name of the output image file.
   * @param image          The BufferedImage to be saved.
   * @return true if the image was successfully saved, false otherwise.
   */
  boolean saveIOImage(String outputFileName, BufferedImage image);

  /**
   * Reads the content of a file and removes empty lines and lines starting with '#' character.
   *
   * @param filePath The path to the file to read.
   * @param out      The PrintStream to display error messages.
   * @return A StringBuilder containing the cleaned content of the file.
   */
  StringBuilder getFileContent(String filePath, PrintStream out);

  /**
   * Checks if a directory exists at the specified file path.
   *
   * @param filePath The path of the directory to check.
   * @return {@code true} if the directory exists, {@code false} otherwise.
   */
  boolean doesDirectoryExist(String filePath);

  /**
   * Checks if a file exists at the specified file path.
   *
   * @param filePath The path of the file to check.
   * @return {@code true} if the file exists, {@code false} otherwise.
   */
  boolean doesFileExist(String filePath);

  //Get File Name
  String getFileName(String fileName);
}
