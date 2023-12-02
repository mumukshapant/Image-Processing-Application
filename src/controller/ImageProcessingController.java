package controller;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * The ImageProcessingController interface defines a contract for controlling
 * image processing operations in an application.
 */
public interface ImageProcessingController {

  /**
   * Starts the image processing controller, initiating the application's
   * image processing functionality. The user can enter various commands to interact
   * with the application, such as loading images, applying filters, and saving images.
   */
  void readUserCommands(OutputStream out, InputStream in);

  /**
   * Executes a series of image processing commands from a script file.
   *
   * @param filePath The path to the script file containing commands.
   */
  void inputFromScriptFile(String filePath, OutputStream out);
}
