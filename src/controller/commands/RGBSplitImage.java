package controller.commands;

import model.ImageProcessingModel;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Command class for splitting an image into its RGB components.
 */
public class RGBSplitImage implements ImageProcessingCommand {

  // Instance variables
  private final String sourceFilename;
  private final String redDestinationFilename;
  private final String greenDestinationFilename;
  private final String blueDestinationFilename;

  /**
   * Constructs a SplitImage command with the provided inputs and print stream.
   *
   * @param inputs      An array of strings containing,
   *                    the source image name and destination image names.
   * @param printStream The print stream for displaying messages.
   */
  public RGBSplitImage(String[] inputs, PrintStream printStream) {
    this.blueDestinationFilename = inputs[4];
    this.greenDestinationFilename = inputs[3];
    this.redDestinationFilename = inputs[2];
    this.sourceFilename = inputs[1];
  }

  /**
   * Executes the command to split an image into its RGB components.
   *
   * @param model The image processing model containing the original image.
   * @param out   The output stream for displaying messages.
   * @return `true` if the image was successfully split; `false` otherwise.
   */
  @Override
  public boolean execute(ImageProcessingModel model, OutputStream out) {
    return model.rgbSplit(sourceFilename, redDestinationFilename,
        greenDestinationFilename, blueDestinationFilename);
  }
}
