package controller.commands;

import model.ImageProcessingModel;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Command class for compressing an image.
 * Implements the {@link ImageProcessingCommand} interface.
 */
public class CompressImage implements ImageProcessingCommand {

  // Instance variables
  private final String filename;
  private final String newFilename;
  private final PrintStream outputStream;
  private Integer percentage;

  /**
   * Constructs a CompressImage command with the provided inputs and print stream.
   *
   * @param inputs      An array of strings containing compression percentage,
   *                    input filename, and output filename.
   * @param printStream The print stream for displaying messages.
   */
  public CompressImage(String[] inputs, PrintStream printStream) {
    this.outputStream = printStream;
    try {
      this.percentage = Integer.valueOf(inputs[1]);
    } catch (NumberFormatException e) {
      outputStream.println(inputs[1] + " cannot be parsed into Integer.");
    }
    this.filename = inputs[2];
    this.newFilename = inputs[3];
  }

  /**
   * Executes the image compression operation on the image using the provided model.
   *
   * @param model The image processing model to apply the command to.
   * @param out   The output stream for displaying messages.
   * @return `true` if the operation was successful; `false` otherwise.
   */
  @Override
  public boolean execute(ImageProcessingModel model, OutputStream out) {
    if (percentage == null) {
      return false;
    } else if (percentage < 0 || percentage > 100) {
      outputStream.println("Invalid percentage for compression. Please use 0 to 100.");
      return false;
    }
    return model.compress(percentage, filename, newFilename);
  }
}
