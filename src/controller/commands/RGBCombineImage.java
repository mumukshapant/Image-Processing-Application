package controller.commands;

import model.ImageProcessingModel;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Command class for combining separate red, green, and blue channel images into a single image.
 * Implements the {@link ImageProcessingCommand} interface.
 */
public class RGBCombineImage implements ImageProcessingCommand {

  // Instance variables
  private final String redSourceFilename;
  private final String greenSourceFilename;
  private final String blueSourceFilename;
  private final String newFilename;

  /**
   * Constructs a CombineImage command with the provided inputs and print stream.
   *
   * @param inputs      An array of strings containing output filename, red channel filename,
   *                    green channel filename, and blue channel filename.
   * @param printStream The print stream for displaying messages.
   */
  public RGBCombineImage(String[] inputs, PrintStream printStream) {
    this.newFilename = inputs[1];
    this.redSourceFilename = inputs[2];
    this.greenSourceFilename = inputs[3];
    this.blueSourceFilename = inputs[4];
  }

  /**
   * Executes the image combination operation using the provided model.
   *
   * @param model The image processing model to apply the command to.
   * @param out   The output stream for displaying messages.
   * @return `true` if the operation was successful; `false` otherwise.
   */
  @Override
  public boolean execute(ImageProcessingModel model, OutputStream out) {
    return model.rbgCombine(
        newFilename, redSourceFilename, greenSourceFilename, blueSourceFilename);
  }
}
