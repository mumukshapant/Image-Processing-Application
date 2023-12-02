package controller.commands;

import model.ImageProcessingModel;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Command class for applying sepia effect to an image.
 */
public class SepiaImage extends AbstractImageOperations {

  // Instance variables
  private final String filename;
  private final String newFilename;
  private final Boolean split;
  private final PrintStream outstream;
  private Integer percentage;

  /**
   * Constructs a SepiaImage command with the provided inputs and print stream.
   *
   * @param inputs      An array of strings containing the input image name and output image name.
   * @param printStream The print stream for displaying messages.
   */
  public SepiaImage(String[] inputs, PrintStream printStream) {
    this.outstream = printStream;
    this.filename = inputs[1];
    this.newFilename = inputs[2];
    if (containsSplit(inputs)) {
      try {
        this.percentage = Integer.valueOf(inputs[4]);
      } catch (NumberFormatException e) {
        printStream.println(inputs[4] + " cannot be parsed into Integer.");
      }
      this.split = true;
    } else {
      this.percentage = null;
      this.split = false;
    }
  }

  /**
   * Executes the command to apply sepia effect to an image.
   *
   * @param model The image processing model containing the original image.
   * @param out   The output stream for displaying messages.
   * @return `true` if the sepia effect was successfully applied; `false` otherwise.
   */
  @Override
  public boolean execute(ImageProcessingModel model, OutputStream out) {
    if (split && percentage == null) {
      return false;
    } else if (split && isPercentageInRange(percentage)) {
      outstream.println("Invalid value " + percentage + " for percentage.");
      return false;
    }

    boolean status = model.getSepia(filename, newFilename);

    if (split) {
      status = super.doOperationPreview(percentage, filename, newFilename, model);
    }
    return status;
  }
}
