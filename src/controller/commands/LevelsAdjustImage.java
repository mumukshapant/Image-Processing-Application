package controller.commands;

import model.ImageProcessingModel;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Command class for adjusting levels of an image.
 * Extends the {@link AbstractImageOperations} abstract class.
 */
public class LevelsAdjustImage extends AbstractImageOperations {

  // Constants
  private static final int MIN_LEVEL = 0;
  private static final int MAX_LEVEL = 255;


  // Instance variables
  private final String filename;
  private final String newFilename;
  private final PrintStream outstream;
  private Integer black;
  private Integer mid;
  private Integer white;
  private Integer percentage;
  private Boolean split;

  /**
   * Constructs a LevelsAdjustImage command with the provided inputs and print stream.
   *
   * @param inputs      An array of strings containing black, mid,
   *                    white levels, input filename, and output filename.
   * @param printStream The print stream for displaying messages.
   */
  public LevelsAdjustImage(String[] inputs, PrintStream printStream) {
    this.outstream = printStream;
    this.filename = inputs[4];
    this.newFilename = inputs[5];
    parseInputValues(inputs);
  }

  private void parseInputValues(String[] inputs) {
    try {

      if (containsSplit(inputs)) {
        this.percentage = Integer.valueOf(inputs[7]);
        this.split = true;
      } else {
        this.percentage = null;
        this.split = false;
      }
      this.black = Integer.parseInt(inputs[1]);
      this.mid = Integer.parseInt(inputs[2]);
      this.white = Integer.parseInt(inputs[3]);
    } catch (NumberFormatException e) {
      outstream.println("Exception cannot be parsed into Integer.");
    }
  }


  /**
   * Executes the levels adjustment operation on the image using the provided model.
   *
   * @param model The image processing model to apply the command to.
   * @param out   The output stream for displaying messages.
   * @return `true` if the operation was successful; `false` otherwise.
   */
  @Override
  public boolean execute(ImageProcessingModel model, OutputStream out) {
    if (!validateInput()) {
      return false;
    }

    boolean status = model.levelAdjustment(filename, newFilename, black, mid, white);

    if (split) {
      status = doOperationPreview(model);
    }

    return status;
  }

  private boolean validateInput() {
    if ((split && percentage == null) || black == null || mid == null || white == null) {
      return false;
    } else if (isLevelInRange(black)
        || isLevelInRange(mid) || isLevelInRange(white)
        || black >= mid || mid >= white) {
      outstream.println("Invalid value for black/mid/white - value must be in ascending order"
          + " of B<M<W and within 0 to 255.");
      return false;
    } else if (split && isPercentageInRange(percentage)) {
      outstream.println("Invalid value " + percentage
          + " for percentage - must be within 0 to 100.");
      return false;
    }
    return true;
  }

  private boolean isLevelInRange(Integer level) {
    return level == null || level < MIN_LEVEL || level > MAX_LEVEL;
  }

  private boolean doOperationPreview(ImageProcessingModel model) {
    return super.doOperationPreview(percentage, filename, newFilename, model);
  }
}
