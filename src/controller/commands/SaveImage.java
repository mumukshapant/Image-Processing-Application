package controller.commands;

import model.ImageProcessingModel;
import utility.FileReadWriteUtility;
import utility.FileReadWriteUtilityImpl;

import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Command class for saving an image to a specified output file.
 */
public class SaveImage implements ImageProcessingCommand {

  // Instance variables
  private final String outputFileName;
  private final String filename;
  private final FileReadWriteUtility utility;
  private final PrintStream outputStream;

  /**
   * Constructs a SaveImage command with the provided inputs and print stream.
   *
   * @param inputs      An array of strings containing the output file name and original image name.
   * @param printStream The print stream for displaying messages.
   */
  public SaveImage(String[] inputs, PrintStream printStream) {
    this.outputFileName = inputs[1];
    this.filename = inputs[2];
    this.utility = new FileReadWriteUtilityImpl();
    this.outputStream = printStream;
  }

  /**
   * Checks if a file extension is valid for image processing.
   *
   * @param extension The file extension to check.
   * @return `true` if the extension is valid; `false` otherwise.
   */
  private static boolean isValidExtension(String extension) {
    switch (extension.toLowerCase()) {
      case "ppm":
      case "jpeg":
      case "jpg":
      case "png":
        return true;
      default:
        return false;
    }
  }

  /**
   * Retrieves the file extension from a given filename.
   *
   * @param filename The name of the file.
   * @return The file extension (e.g., "jpg", "png").
   */
  private static String getFileExtension(String filename) {
    int dotIndex = filename.lastIndexOf('.');
    if (dotIndex == -1) {
      return "";
    }
    return filename.substring(dotIndex + 1);
  }

  /**
   * Executes the command to save an image to the specified output file.
   *
   * @param model The image processing model containing the original image.
   * @param out   The output stream for displaying messages.
   * @return `true` if the image was successfully saved; `false` otherwise.
   */
  @Override
  public boolean execute(ImageProcessingModel model, OutputStream out) {
    String fileExtension = getFileExtension(outputFileName);

    if (!isValidExtension(fileExtension)) {
      outputStream.println("Unsupported file format - " + fileExtension + "\n");
      return false;
    }

    if (utility.doesDirectoryExist(outputFileName)) {
      outputStream.print("Invalid Directory Path '" + outputFileName + "'.\n");
      return false;
    }

    BufferedImage image = model.getImage(filename);

    if (image == null) {
      return false;
    }

    boolean status;
    switch (fileExtension) {
      case "jpg":
      case "png":
      case "jpeg":
        status = utility.saveIOImage(outputFileName, image);
        return status;
      case "ppm":
        status = utility.savePPMImage(outputFileName, image);
        return status;
      default:
        return false;
    }
  }
}
