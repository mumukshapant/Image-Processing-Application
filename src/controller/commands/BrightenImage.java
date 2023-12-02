package controller.commands;

import model.ImageProcessingModel;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Command class for brightening an image.
 * Implements the {@link ImageProcessingCommand} interface.
 */
public class BrightenImage implements ImageProcessingCommand {

  private final String filename;
  private final String newFilename;
  private Integer increments;


  /**
   * Constructs a BrightenImage command with the provided inputs and print stream.
   *
   * @param inputs      An array of strings containing increments,
   *                    input filename, and output filename.
   * @param printStream The print stream for displaying messages.
   */
  public BrightenImage(String[] inputs, PrintStream printStream) {

    try {
      this.increments = Integer.parseInt(inputs[1]);
    } catch (NumberFormatException e) {
      printStream.println(inputs[1] + " cannot be parsed into Integer.");
    }
    this.filename = inputs[2];
    this.newFilename = inputs[3];
  }

  /**
   * Executes the brightening operation on the image using the provided model.
   *
   * @param model The image processing model to apply the command to.
   * @param out   The output stream for displaying messages.
   * @return `true` if the operation was successful; `false` otherwise.
   */
  @Override
  public boolean execute(ImageProcessingModel model, OutputStream out) {
    if (increments == null) {
      return false;
    }
    return model.doBrightening(increments, filename, newFilename);
  }
}
