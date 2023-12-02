package controller.commands;

import model.ImageProcessingModel;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Command class for creating a histogram for an image.
 * Implements the {@link ImageProcessingCommand} interface.
 */
public class Histogram implements ImageProcessingCommand {

  // Instance variables
  private final String filename;
  private final String newFilename;

  /**
   * Constructs a Histogram command with the provided inputs and print stream.
   *
   * @param inputs      An array of strings containing input filename and output filename.
   * @param printStream The print stream for displaying messages.
   */
  public Histogram(String[] inputs, PrintStream printStream) {
    this.filename = inputs[1];
    this.newFilename = inputs[2];
  }

  /**
   * Executes the histogram creation operation on the image using the provided model.
   *
   * @param model The image processing model to apply the command to.
   * @param out   The output stream for displaying messages.
   * @return `true` if the operation was successful; `false` otherwise.
   */
  @Override
  public boolean execute(ImageProcessingModel model, OutputStream out) {
    return model.createHistogram(filename, newFilename);
  }
}
