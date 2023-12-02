package controller.commands;

import model.ImageProcessingModel;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Command class for applying blur or sharpen operations on an image.
 * Implements the {@link ImageProcessingCommand} interface.
 */
public class BlurSharpenImage extends AbstractImageOperations {

  private final String filterType;
  private final String filename;
  private final String newFilename;
  private final boolean split;
  private final PrintStream outputStream;
  private Integer percentage;

  /**
   * Constructs a BlurSharpenImage command with the provided inputs and print stream.
   *
   * @param inputs      An array of strings containing filter type,
   *                    input filename, and output filename.
   * @param printStream The print stream for displaying messages.
   */
  public BlurSharpenImage(String[] inputs, PrintStream printStream) {
    this.outputStream = printStream;
    this.filterType = inputs[0];
    this.filename = inputs[1];
    this.newFilename = inputs[2];

    if (super.containsSplit(inputs)) {
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
   * Executes the blur or sharpen operation on the image using the provided model.
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
    boolean status = model.blurSharpenImage(filterType, filename, newFilename);
    if (split) {
      status = super.doOperationPreview(percentage, filename, newFilename, model);
    }
    return status;
  }


}
