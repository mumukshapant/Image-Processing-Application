package controller.commands;

import model.ImageProcessingModel;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Command class for applying greyscale operations on an image.
 * Extends the {@link AbstractImageOperations} abstract class.
 */
public class GreyscaleImage extends AbstractImageOperations {

  // Instance variables
  private final String operationType;
  private final String filename;
  private final String newFilename;
  private final Boolean split;
  private final PrintStream outputStream;
  private Integer percentage;

  /**
   * Constructs a GreyscaleImage command with the provided inputs and print stream.
   *
   * @param inputs      An array of strings containing operation type,
   *                    input filename, and output filename.
   * @param printStream The print stream for displaying messages.
   */
  public GreyscaleImage(String[] inputs, PrintStream printStream) {
    this.outputStream = printStream;
    this.operationType = inputs[0];
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
   * Executes the greyscale operation on the image using the provided model.
   *
   * @param model The image processing model to apply the command to.
   * @param out   The output stream for displaying messages.
   * @return `true` if the operation was successful; `false` otherwise.
   */
  @Override
  public boolean execute(ImageProcessingModel model, OutputStream out) {
    if (split && percentage == null) {
      return false;
    } else if (split && isPercentageInRange(percentage)) {
      outputStream.println("Invalid value " + percentage + " for percentage.");
      return false;
    }

    boolean status = model.getGreyScale(operationType, filename, newFilename);

    if (split) {
      status = super.doOperationPreview(percentage, filename, newFilename, model);
    }
    return status;
  }
}
