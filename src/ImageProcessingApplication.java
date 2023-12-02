import controller.ImageProcessingController;
import controller.ImageProcessingControllerImpl;
import controller.MVCController;
import model.ImageProcessingModel;
import model.ImageProcessingModelImpl;
import utility.FileReadWriteUtilityImpl;
import view.IView;
import view.ViewImpl;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * The main class for the Image Processing application.
 */
public class ImageProcessingApplication {

  /**
   * The main entry point for the Image Processing application.
   * Initializes the model and controller and starts the application.
   *
   * @param args Command-line arguments.
   * @throws IllegalArgumentException If an invalid argument is entered.
   */
  public static void main(String[] args) throws IllegalArgumentException {

    final OutputStream out = System.out;
    final InputStream in = System.in;

    ImageProcessingController controller = new ImageProcessingControllerImpl(
        new ImageProcessingModelImpl(out),
        out,
        in,
        new FileReadWriteUtilityImpl()
    );

    if (args.length >= 1) {

      if (args[0].equals("-file")) {
        controller.inputFromScriptFile(args[1], out);
      } else if (args[0].equals("-text")) {
        controller.readUserCommands(out, in);
      } else {
        throw new IllegalArgumentException("Invalid argument entered");
      }
    } else {
      IView view = new ViewImpl();
      ImageProcessingModel model = new ImageProcessingModelImpl(out);
      new MVCController(model, view, new FileReadWriteUtilityImpl(), out);
    }
  }
}
