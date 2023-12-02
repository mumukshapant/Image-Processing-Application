package controller.commands;

import model.ImageProcessingModel;
import utility.FileReadWriteUtility;
import utility.FileReadWriteUtilityImpl;

import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Command class for loading an image from a specified file path.
 */
public class LoadImage implements ImageProcessingCommand {

  // Instance variables
  private final String filepath;
  private final String filename;
  private final FileReadWriteUtility utility;
  private final PrintStream outputStream;

  /**
   * Constructs a LoadImage command with the provided inputs and print stream.
   *
   * @param inputs      An array of strings containing the file path and name for the image.
   * @param printStream The print stream for displaying messages.
   */
  public LoadImage(String[] inputs, PrintStream printStream) {
    this.filepath = inputs[1];
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
   * Executes the command to load an image from the specified file path.
   *
   * @param model The image processing model to add the loaded image to.
   * @param out   The output stream for displaying messages.
   * @return `true` if the image was successfully loaded and added to the model; `false` otherwise.
   */
  @Override
  public boolean execute(ImageProcessingModel model, OutputStream out) {
    String format = getFileExtension(filepath).toLowerCase();

    if (!isValidExtension(format)) {
      outputStream.print("File Format Not Supported. '" + format + "'.\n");
      return false;
    }

    if (utility.doesDirectoryExist(filepath)) {
      outputStream.print("Invalid Directory Path '" + filepath + "'.\n");
      return false;
    }

    if (!utility.doesFileExist(filepath)) {
      outputStream.print("File Not Found '" + filepath + "'.\n");
      return false;
    }

    BufferedImage image;

    switch (format) {
      case "ppm":
        image = utility.loadImageFromPPM(filepath);
        if (image == null) {
          return false;
        }
        return model.addImage(filename, image);
      case "jpg":
      case "png":
      case "jpeg":
        image = utility.loadImageFromImageIO(filepath);
        if (image == null) {
          return false;
        }
        return model.addImage(filename, image);
      default:
        outputStream.print("Invalid file extension ~ " + format + " \n");
        return false;
    }
  }
}
