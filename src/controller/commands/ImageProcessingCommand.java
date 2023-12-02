package controller.commands;

import model.ImageProcessingModel;

import java.io.OutputStream;

/**
 * Interface for image processing commands.
 * Implementing classes should define the behavior,
 * of executing the command on an image processing model.
 */
public interface ImageProcessingCommand {

  /**
   * Executes the image processing command on the provided model.
   *
   * @param model The image processing model to apply the command to.
   * @param out   The output stream for displaying messages.
   * @return `true` if the operation was successful; `false` otherwise.
   */
  boolean execute(ImageProcessingModel model, OutputStream out);
}
