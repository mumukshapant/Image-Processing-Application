package controller.commands;

import model.ImageProcessingModel;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Command class for applying color correction on an image.
 * Implements the {@link ImageProcessingCommand} interface.
 */
public class ColorCorrection extends AbstractImageOperations {

  private final String filename;
  private final String newFilename;
  private final boolean split;
  private final PrintStream outputStream;
  private Integer percentage;


  /**
   * Constructs a ColorCorrection command with the provided inputs and print stream.
   *
   * @param inputs      An array of strings containing input filename and output filename.
   * @param printStream The print stream for displaying messages.
   */
  public ColorCorrection(String[] inputs, PrintStream printStream) {
    this.outputStream = printStream;
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
   * Executes the color correction operation on the image using the provided model.
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

    boolean status = model.colorCorrection(filename, newFilename);
    if (split) {
      status = super.doOperationPreview(percentage, filename, newFilename, model);
    }
    return status;
  }

}
