package controller.commands;

import model.ImageProcessingModel;

import java.io.OutputStream;

/**
 * Abstract base class for image processing operations.
 * Implements the {@link ImageProcessingCommand} interface.
 */
public abstract class AbstractImageOperations implements ImageProcessingCommand {
  private static final int MIN_PERCENTAGE = 0;
  private static final int MAX_PERCENTAGE = 100;

  /**
   * Executes the image processing operation on the image using the provided model.
   *
   * @param model The image processing model to apply the command to.
   * @param out   The output stream for displaying messages.
   * @return `true` if the operation was successful; `false` otherwise.
   */
  public abstract boolean execute(ImageProcessingModel model, OutputStream out);

  /**
   * Checks if the "split" keyword is present in the given array.
   *
   * @param array The array of strings to check.
   * @return `true` if the "split" keyword is present; `false` otherwise.
   */
  boolean containsSplit(String[] array) {
    for (String element : array) {
      if ("split".equals(element)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Performs an operation preview using the provided model.
   *
   * @param percentage  The percentage for the operation preview.
   * @param filename    The input filename.
   * @param newFilename The output filename.
   * @param model       The image processing model to apply the preview to.
   * @return `true` if the operation preview was successful; `false` otherwise.
   */
  boolean doOperationPreview(
      int percentage, String filename, String newFilename, ImageProcessingModel model) {
    return model.operationPreview(percentage, filename, newFilename);
  }

  boolean isPercentageInRange(Integer percentage) {
    return percentage == null || percentage < MIN_PERCENTAGE || percentage > MAX_PERCENTAGE;
  }
}
