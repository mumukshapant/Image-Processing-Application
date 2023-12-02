package controller.commands;

import model.ImageProcessingModel;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Command class for flipping an image horizontally or vertically.
 * Implements the {@link ImageProcessingCommand} interface.
 */
public class FlipImage implements ImageProcessingCommand {

  // Instance variables
  private final String flipType;
  private final String filename;
  private final String newFilename;

  /**
   * Constructs a FlipImage command with the provided inputs and print stream.
   *
   * @param inputs      An array of strings containing flip type,
   *                    input filename, and output filename.
   * @param printStream The print stream for displaying messages.
   */
  public FlipImage(String[] inputs, PrintStream printStream) {
    this.flipType = inputs[0];
    this.filename = inputs[1];
    this.newFilename = inputs[2];
  }

  /**
   * Executes the image flipping operation on the image using the provided model.
   *
   * @param model The image processing model to apply the command to.
   * @param out   The output stream for displaying messages.
   * @return `true` if the operation was successful; `false` otherwise.
   */
  @Override
  public boolean execute(ImageProcessingModel model, OutputStream out) {
    return model.doFlip(flipType, filename, newFilename);
  }
}
