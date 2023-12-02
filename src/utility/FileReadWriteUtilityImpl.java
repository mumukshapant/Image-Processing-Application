package utility;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.PrintStream;
import java.io.FileInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Scanner;

import javax.imageio.ImageIO;

/**
 * An implementation of the {@link FileReadWriteUtility} interface for reading and
 * writing images in different formats.
 */
public class FileReadWriteUtilityImpl implements FileReadWriteUtility {
  PrintStream out = new PrintStream(System.out);

  private static String getFileExtension(String filename) {
    int dotIndex = filename.lastIndexOf('.');
    if (dotIndex == -1) {
      return "";
    }
    return filename.substring(dotIndex + 1);
  }

  /**
   * Loads an image from a PPM file.
   * It returns null if there was an issue loading the image.
   *
   * @param filepath The file path to the PPM image file.
   * @return A BufferedImage representing the loaded image.
   */
  @Override
  public BufferedImage loadImageFromPPM(String filepath) {
    PrintStream outputStream = new PrintStream(this.out);
    try (Scanner scanner = new Scanner(new FileInputStream(filepath))) {
      scanner.useDelimiter("\n|\\s+");

      String token = scanner.next();
      if (!token.equals("P3")) {
        outputStream.print("Invalid PPM file: plain RAW file should begin with P3\n");
        return null;
      }
      int width = scanner.nextInt();
      int height = scanner.nextInt();
      BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

      for (int i = 0; i < height; i++) {
        for (int j = 0; j < width; j++) {
          int r = scanner.nextInt();
          int g = scanner.nextInt();
          int b = scanner.nextInt();
          Color pixelColor = new Color(r, g, b);
          image.setRGB(j, i, pixelColor.getRGB());
        }
      }

      return image;

    } catch (FileNotFoundException e) {
      System.out.println("File " + filepath + " not found!");
      return null;
    }
  }

  /**
   * Loads an image using Java's ImageIO library.
   *
   * @param filepath The file path to the image file.
   * @return A BufferedImage representing the loaded image.
   */
  @Override
  public BufferedImage loadImageFromImageIO(String filepath) {
    PrintStream outputStream = new PrintStream(this.out);
    try {
      String normalizedPath = filepath.replace("\\s", " ");
      File file = new File(normalizedPath);
      return ImageIO.read(file);
    } catch (IOException e) {
      outputStream.println("File " + filepath + " not found!");
    }
    return null;
  }

  /**
   * Saves a BufferedImage as a PPM image.
   *
   * @param outputFileName The name of the output PPM image file.
   * @param image          The BufferedImage to be saved.
   * @return true if the image was successfully saved, false otherwise.
   */
  @Override
  public boolean savePPMImage(String outputFileName, BufferedImage image) {
    File outputFile = new File(outputFileName);
    int width = image.getWidth();
    int height = image.getHeight();

    try (BufferedWriter ppmWriter = new BufferedWriter(new FileWriter(outputFile))) {
      ppmWriter.write("P3\n");
      ppmWriter.write(width + " " + height + "\n");
      ppmWriter.write("255\n");

      for (int i = 0; i < height; i++) {
        for (int j = 0; j < width; j++) {
          Color pixelColor = new Color(image.getRGB(j, i));
          int r = pixelColor.getRed();
          int g = pixelColor.getGreen();
          int b = pixelColor.getBlue();

          ppmWriter.write(String.format("%d %d %d ", r, g, b));
        }
        ppmWriter.write("\n");
      }
      return true;
    } catch (IOException e) {
      System.out.println("Save PPM Image operation failed: " + e.getMessage());
      return false;
    }
  }

  /**
   * Saves a BufferedImage using Java's ImageIO library.
   *
   * @param outputFileName The name of the output image file.
   * @param image          The BufferedImage to be saved.
   * @return true if the image was successfully saved, false otherwise.
   */
  @Override
  public boolean saveIOImage(String outputFileName, BufferedImage image) {
    try {
      File outputFile = new File(outputFileName);
      String fileExtension = getFileExtension(outputFileName);

      if (fileExtension.isEmpty()) {
        System.out.println("Invalid file extension.");
        return false;
      }

      if (!ImageIO.write(image, fileExtension.toUpperCase(), outputFile)) {
        System.out.println("Failed to save " + outputFileName + " image.");
        return false;
      }

      return true;
    } catch (IOException e) {
      System.out.println("Save " + outputFileName + " Image operation failed: " + e.getMessage());
      return false;
    }
  }

  /**
   * Reads the content of a file and removes empty lines and lines starting with '#' character.
   *
   * @param filePath The path to the file to read.
   * @param out      The PrintStream to display error messages.
   * @return A StringBuilder containing the cleaned content of the file.
   */
  public StringBuilder getFileContent(String filePath, PrintStream out) {
    StringBuilder builder = new StringBuilder();

    try {
      File fileObj = new File(filePath);
      Scanner reader = new Scanner(fileObj);
      while (reader.hasNextLine()) {
        String s = reader.nextLine();

        if (s.length() == 0) {
          continue;
        }

        if (s.charAt(0) != '#') {
          builder.append(s).append(System.lineSeparator());
        }
      }
      reader.close();
    } catch (FileNotFoundException e) {
      out.print("File " + filePath + " not found!");
    }

    return builder;
  }

  /**
   * Checks if the file exists in the system.
   *
   * @param filePath path where file should exist.
   * @return boolean true if file exists, false otherwise.
   */
  public boolean doesFileExist(String filePath) {
    String normalizedPath = filePath.replace("\\s", " ");
    File file = new File(normalizedPath);
    return file.exists();
  }


  /**
   * Checks if the directory exists in the system.
   *
   * @param filePath path where file should exist.
   * @return boolean true if directory exists, false otherwise.
   */
  public boolean doesDirectoryExist(String filePath) {
    String normalizedPath = filePath.replace("\\s", " ");
    File file = new File(normalizedPath);
    File directory = file.getParentFile(); // Get the parent directory of the file

    return directory == null || !directory.exists() || !directory.isDirectory();
  }

  @Override
  public String getFileName(String fileName) {
    return fileName.replaceFirst("[.][^.]+$", "");
  }

}
