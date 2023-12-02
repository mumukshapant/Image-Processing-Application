package controller;

import controller.commands.Histogram;
import controller.commands.RGBCombineImage;
import controller.commands.ImageProcessingCommand;
import controller.commands.SaveImage;
import controller.commands.BlurSharpenImage;
import controller.commands.BrightenImage;
import controller.commands.ColorCorrection;
import controller.commands.CompressImage;
import controller.commands.FlipImage;
import controller.commands.GreyscaleImage;
import controller.commands.ImageOperations;
import controller.commands.LevelsAdjustImage;
import controller.commands.LoadImage;
import controller.commands.SepiaImage;
import controller.commands.RGBSplitImage;


import utility.FileReadWriteUtility;
import model.ImageProcessingModel;

import java.io.OutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.BiFunction;

/**
 * Implementation of the {@link ImageProcessingController} interface.
 */
public class ImageProcessingControllerImpl implements ImageProcessingController {
  private final ImageProcessingModel model;
  private final OutputStream out;
  private final FileReadWriteUtility utility;


  /**
   * Constructs an ImageProcessingControllerImpl with the provided model,
   * output stream, and input stream.
   *
   * @param model The image processing model.
   * @param out   The output stream for displaying messages.
   * @param in    The input stream for user input.
   */
  public ImageProcessingControllerImpl(ImageProcessingModel model,
                                       OutputStream out, InputStream in,
                                       FileReadWriteUtility utility) {
    this.model = model;
    this.out = out;
    this.utility = utility;
  }


  /**
   * Starts the image processing controller, initiating the application's
   * image processing functionality. The user can enter various commands to interact
   * with the application, such as loading images, applying filters, and saving images.
   */

  @Override
  public void readUserCommands(OutputStream out, InputStream in) {
    PrintStream outputStream = new PrintStream(out);
    final String welcomeMessage = "Welcome to Image processing application!\n"
        + "Please type 'quit' to terminate the program.\n";
    outputStream.print(welcomeMessage);
    Scanner sc = new Scanner(in);
    String command = sc.nextLine();

    while (!command.equalsIgnoreCase("quit")) {

      String[] commandParts = command.split(" ");

      ImageOperations operation = ImageOperations.getOperation(commandParts[0]
          .toUpperCase().replace('-', '_'));
      if (operation != null && ImageOperations
          .isValidCommand(operation, commandParts.length)) {

        if (operation.equals(ImageOperations.RUN)) {
          inputFromScriptFile(commandParts[1], out);
        } else {
          executeModel(operation, commandParts, model);
        }
      } else {
        outputStream.print("Invalid command entered!\n");
      }
      command = sc.nextLine();
    }
  }

  /**
   * Executes an image processing command based on the provided arguments.
   *
   * @param arr   An array of strings containing the command and its arguments.
   * @param model The image processing model to apply the command to.
   * @return `true` if the command was executed successfully; `false` if the command is invalid.
   */
  protected boolean executeModel(
      ImageOperations operation, String[] arr, ImageProcessingModel model) {
    PrintStream outputStream = new PrintStream(out);

    Map<ImageOperations, BiFunction<String[], PrintStream, ImageProcessingCommand>> knownCommand
        = initializeCommandMap();

    BiFunction<String[], PrintStream, ImageProcessingCommand> cmd = knownCommand.get(operation);

    if (cmd != null) {
      ImageProcessingCommand commandProcessor = cmd.apply(arr, outputStream);
      boolean status = commandProcessor.execute(model, out);

      if (status) {
        outputStream.printf("Image %s operation successful.%n", arr[0]);
      } else {
        outputStream.printf("Image %s operation failed.%n", arr[0]);
      }

      return status;
    } else {
      outputStream.println("Invalid command entered!");
      return false;
    }
  }


  /**
   * Executes a series of image processing commands from a script file.
   *
   * @param filePath The path to the script file containing commands.
   */

  @Override
  public void inputFromScriptFile(String filePath, OutputStream out) {
    PrintStream outStream = new PrintStream(out);
    if (utility.doesDirectoryExist(filePath)) {
      outStream.println("Directory Not found");
      return;
    }
    if (!utility.doesFileExist(filePath)) {
      outStream.println("File " + filePath + " Not found");
      return;
    }

    StringBuilder fileContent = utility.getFileContent(filePath, outStream);

    Scanner sc = new Scanner(fileContent.toString());
    StringBuilder failedCommands = new StringBuilder();
    while (sc.hasNextLine()) {
      String line = sc.nextLine();
      if (line.length() == 0) {
        continue;
      }

      String[] arr = line.split(" ");
      ImageOperations operation = ImageOperations.valueOf(arr[0]
          .toUpperCase().replace('-', '_'));
      boolean commandExecuted = executeModel(operation, arr, model);
      if (!commandExecuted) {
        failedCommands.append("Invalid command provided in script '")
            .append(filePath).append("' command '")
            .append(line).append("'.\n");
      }
    }
    if (failedCommands.length() == 0) {
      outStream.println("Script " + filePath + " ran successfully.");
    } else {
      outStream.println(failedCommands);
    }
  }

  /**
   * Initializes a map of image processing commands.
   *
   * @return A map containing image processing operations and their corresponding command functions.
   */
  private Map<ImageOperations,
      BiFunction<String[], PrintStream, ImageProcessingCommand>> initializeCommandMap() {
    Map<ImageOperations, BiFunction<String[],
        PrintStream, ImageProcessingCommand>> knownCommand
        = new HashMap<>();

    knownCommand.put(ImageOperations.LOAD, LoadImage::new);
    knownCommand.put(ImageOperations.SAVE, SaveImage::new);
    knownCommand.put(ImageOperations.BRIGHTEN, BrightenImage::new);
    knownCommand.put(ImageOperations.COMPRESS, CompressImage::new);

    knownCommand.put(ImageOperations.VERTICAL_FLIP, FlipImage::new);
    knownCommand.put(ImageOperations.HORIZONTAL_FLIP, FlipImage::new);
    knownCommand.put(ImageOperations.RED_COMPONENT, GreyscaleImage::new);
    knownCommand.put(ImageOperations.GREEN_COMPONENT, GreyscaleImage::new);
    knownCommand.put(ImageOperations.BLUE_COMPONENT, GreyscaleImage::new);
    knownCommand.put(ImageOperations.VALUE_COMPONENT, GreyscaleImage::new);
    knownCommand.put(ImageOperations.INTENSITY_COMPONENT, GreyscaleImage::new);
    knownCommand.put(ImageOperations.LUMA_COMPONENT, GreyscaleImage::new);
    knownCommand.put(ImageOperations.RGB_SPLIT, RGBSplitImage::new);
    knownCommand.put(ImageOperations.BLUR, BlurSharpenImage::new);
    knownCommand.put(ImageOperations.SHARPEN, BlurSharpenImage::new);
    knownCommand.put(ImageOperations.SEPIA, SepiaImage::new);
    knownCommand.put(ImageOperations.HISTOGRAM, Histogram::new);

    knownCommand.put(ImageOperations.RGB_COMBINE, RGBCombineImage::new);
    knownCommand.put(ImageOperations.LEVELS_ADJUST, LevelsAdjustImage::new);
    knownCommand.put(ImageOperations.COLOR_CORRECT, ColorCorrection::new);

    return knownCommand;
  }

}