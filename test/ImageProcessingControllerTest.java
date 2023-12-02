import controller.ImageProcessingController;
import controller.ImageProcessingControllerImpl;
import model.ImageMetadata;
import model.ImageProcessingModel;

import org.junit.Before;
import org.junit.Test;

import utility.FileReadWriteUtility;
import utility.FileReadWriteUtilityImpl;

import javax.imageio.ImageIO;


import java.awt.image.BufferedImage;
import java.io.PrintStream;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * This class tests the main working ImageProcessing Controller.
 */
public class ImageProcessingControllerTest {

  OutputStream out;
  PrintStream outputStream;
  InputStream in;
  StringBuilder mockLog;
  ImageProcessingModel model;
  FileReadWriteUtility utility;
  ImageProcessingController imageProcessingController;
  String welcomeMessage = "Welcome to Image processing application!\n"
      + "Please type 'quit' to terminate the program.\n";

  @Before
  public void setup() {
    out = new ByteArrayOutputStream();
    outputStream = new PrintStream(out);
    in = System.in;
    mockLog = new StringBuilder();
    model = new MockModel(mockLog);
    utility = new FileReadWriteUtilityImpl();
  }

  /**
   * Test the 'load' command.
   */
  @Test
  public void testLoadCommand() {
    String command = "load test/dataset/test_images/JamaicaPlain-small.png jp\n"
        + "quit";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    imageProcessingController = new ImageProcessingControllerImpl(model, out, in, utility);
    imageProcessingController.readUserCommands(outputStream, in);

    assertEquals(welcomeMessage + "Image load operation successful.\n", out.toString());

    String mockResponse = "Received inputs: jp";
    assertEquals(mockResponse, mockLog.toString());
  }

  /**
   * Test the 'load' command with ppm.
   */
  @Test
  public void testLoadCommand_ppm() {
    String command = "load test/dataset/test_images/myimg-combine.ppm jp\n"
        + "quit";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    imageProcessingController = new ImageProcessingControllerImpl(model, out, in, utility);
    imageProcessingController.readUserCommands(outputStream, in);

    assertEquals(welcomeMessage + "Image load operation successful.\n", out.toString());

    String mockResponse = "Received inputs: jp";
    assertEquals(mockResponse, mockLog.toString());
  }


  /**
   * Test the 'save' command.
   */
  @Test
  public void testSaveCommand() {
    String command = "save test/dataset/diya.png diya\n"
        + "quit";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    imageProcessingController = new ImageProcessingControllerImpl(model, out, in, utility);
    imageProcessingController.readUserCommands(outputStream, in);

    assertEquals(welcomeMessage + "Image save operation successful.\n", out.toString());

    String mockResponse = "Received inputs: diya";
    assertEquals(mockResponse, mockLog.toString());

    // Check if the file exists in the file system
    File savedFile = new File("test/dataset/diya.png");
    assertTrue(savedFile.exists());

    // Delete the file if it exists
    if (savedFile.exists()) {
      savedFile.delete();
    }

    // Check if the file no longer exists after deletion
    assertFalse(savedFile.exists());
  }

  /**
   * Test the 'brighten' command.
   */
  @Test
  public void testBrightenCommand() {
    String command = "brighten 50 jp jp-brighten-50\n" +
        "quit";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    imageProcessingController = new ImageProcessingControllerImpl(model, out, in, utility);
    imageProcessingController.readUserCommands(outputStream, in);

    assertEquals(welcomeMessage + "Image brighten operation successful.\n", out.toString());

    String mockResponse = "Received inputs: 50 , jp , jp-brighten-50";
    assertEquals(mockResponse, mockLog.toString());
  }

  /**
   * Test the 'horizontal-flip' command.
   */
  @Test
  public void testHorizontalFlipCommand() {
    String command = "horizontal-flip jp jp-horizontal-flip\n" +
        "quit";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    imageProcessingController = new ImageProcessingControllerImpl(model, out, in, utility);
    imageProcessingController.readUserCommands(outputStream, in);

    assertEquals(welcomeMessage + "Image horizontal-flip operation successful.\n", out.toString());

    String mockResponse = "Received inputs: horizontal-flip , jp , jp-horizontal-flip";
    assertEquals(mockResponse, mockLog.toString());
  }

  /**
   * Test the 'luma-component' command for greyscale conversion.
   */
  @Test
  public void testLumaComponentCommand() {
    String command = "luma-component jp jp-luma\n" +
        "quit";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    imageProcessingController = new ImageProcessingControllerImpl(model, out, in, utility);
    imageProcessingController.readUserCommands(outputStream, in);

    assertEquals(welcomeMessage + "Image luma-component operation successful.\n", out.toString());

    String mockResponse = "Received inputs: luma-component , jp , jp-luma";
    assertEquals(mockResponse, mockLog.toString());
  }

  /**
   * Test the 'rgb-split' command.
   */
  @Test
  public void testRgbSplitCommand() {
    String command = "rgb-split jp jp-red jp-green jp-blue\n" +
        "quit";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    imageProcessingController = new ImageProcessingControllerImpl(model, out, in, utility);
    imageProcessingController.readUserCommands(outputStream, in);

    assertEquals(welcomeMessage + "Image rgb-split operation successful.\n", out.toString());

    String mockResponse = "Received inputs: jp , jp-red , jp-green and jp-blue";
    assertEquals(mockResponse, mockLog.toString());
  }

  /**
   * Test the 'blur' command.
   */
  @Test
  public void testBlurCommand() {
    String command = "blur jp jp-blur\n" +
        "quit";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    imageProcessingController = new ImageProcessingControllerImpl(model, out, in, utility);
    imageProcessingController.readUserCommands(outputStream, in);

    assertEquals(welcomeMessage + "Image blur operation successful.\n", out.toString());

    String mockResponse = "Received inputs: blur , jp , jp-blur";
    assertEquals(mockResponse, mockLog.toString());
  }

  /**
   * Test the 'rgb-combine' command.
   */
  @Test
  public void testRgbCombineCommand() {
    String command = "rgb-combine jp-combine jp-red jp-green jp-blue\n" +
        "quit";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    imageProcessingController = new ImageProcessingControllerImpl(model, out, in, utility);
    imageProcessingController.readUserCommands(outputStream, in);

    assertEquals(welcomeMessage + "Image rgb-combine operation successful.\n", out.toString());

    String mockResponse = "Received inputs: jp-combine , jp-red , jp-green , jp-blue";
    assertEquals(mockResponse, mockLog.toString());
  }

  /**
   * Test the 'run' command for executing a script.
   */
  @Test
  public void testRunScriptCommand() {
    String scriptOutput = "Image load operation successful.\n" +
        "Image blur operation successful.\n" +
        "Image sharpen operation successful.\n" +
        "Image rgb-split operation successful.\n" +
        "Image rgb-combine operation successful.\n" +
        "Image sepia operation successful.\n" +
        "Image value-component operation successful.\n" +
        "Image luma-component operation successful.\n" +
        "Image intensity-component operation successful.\n" +
        "Image horizontal-flip operation successful.\n" +
        "Image vertical-flip operation successful.\n" +
        "Image brighten operation successful.\n" +
        "Image brighten operation successful.\n" +
        "Image save operation successful.\n" +
        "Image save operation successful.\n";

    String command = "run test/dataset/test_script.txt\n" +
        "quit";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    imageProcessingController = new ImageProcessingControllerImpl(model, out, in, utility);
    imageProcessingController.readUserCommands(outputStream, in);

    assertEquals(welcomeMessage + scriptOutput
            + "Script test/dataset/test_script.txt ran successfully.\n",
        out.toString());

    File savedFile = new File("test/dataset/test_images/jp.png");
    assertTrue(savedFile.exists());

    // Delete the file if it exists
    if (savedFile.exists()) {
      savedFile.delete();
    }

    // Check if the file no longer exists after deletion
    assertFalse(savedFile.exists());

    savedFile = new File("test/dataset/test_images/jp.ppm");
    assertTrue(savedFile.exists());

    // Delete the file if it exists
    if (savedFile.exists()) {
      savedFile.delete();
    }

    // Check if the file no longer exists after deletion
    assertFalse(savedFile.exists());


    // Check if the file no longer exists after deletion
    assertFalse(savedFile.exists());
  }

  /**
   * Test the 'sepia' command for applying a sepia filter.
   */
  @Test
  public void testSepiaCommand() {
    String command = "sepia jp jp-sepia\n" +
        "quit";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    imageProcessingController = new ImageProcessingControllerImpl(model, out, in, utility);
    imageProcessingController.readUserCommands(outputStream, in);

    assertEquals(welcomeMessage + "Image sepia operation successful.\n", out.toString());

    String mockResponse = "Received inputs: jp , jp-sepia";
    assertEquals(mockResponse, mockLog.toString());
  }

  @Test
  public void testInvalidCommand() {
    String command = "loads resources/input/JamaicaPlain-small.jpg jp\n"
        + "quit";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    imageProcessingController = new ImageProcessingControllerImpl(model, out, in, utility);
    imageProcessingController.readUserCommands(outputStream, in);

    assertEquals(welcomeMessage + "Invalid command entered!\n", out.toString());

    String mockResponse = "";
    assertEquals(mockResponse, mockLog.toString());
  }

  @Test
  public void testSavePNG() {
    String command = "save test/dataset/JamaicaPlain-small.png jp\n"
        + "quit";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    imageProcessingController = new ImageProcessingControllerImpl(model, out, in, utility);
    imageProcessingController.readUserCommands(outputStream, in);

    assertEquals(welcomeMessage + "Image save operation successful.\n", out.toString());

    String mockResponse = "Received inputs: jp";
    assertEquals(mockResponse, mockLog.toString());

    // Check if the file exists in the file system
    File savedFile = new File("test/dataset/JamaicaPlain-small.png");
    assertTrue(savedFile.exists());

    // Delete the file if it exists
    if (savedFile.exists()) {
      savedFile.delete();
    }

    // Check if the file no longer exists after deletion
    assertFalse(savedFile.exists());
  }

  @Test
  public void testSaveJPG() {
    String command = "save test/dataset/JamaicaPlain-small.png jp\n"
        + "quit";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    imageProcessingController = new ImageProcessingControllerImpl(model, out, in, utility);
    imageProcessingController.readUserCommands(outputStream, in);

    assertEquals(welcomeMessage + "Image save operation successful.\n", out.toString());

    String mockResponse = "Received inputs: jp";
    assertEquals(mockResponse, mockLog.toString());

    // Check if the file exists in the file system
    File savedFile = new File("test/dataset/JamaicaPlain-small.png");
    assertTrue(savedFile.exists());

    // Delete the file if it exists
    if (savedFile.exists()) {
      savedFile.delete();
    }

    // Check if the file no longer exists after deletion
    assertFalse(savedFile.exists());
  }

  @Test
  public void testDirectoryNotFoundSave() {
    String command = "save xyz/JamaicaPlain-small.jpg jp\n"
        + "quit";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    imageProcessingController = new ImageProcessingControllerImpl(model, out, in, utility);
    imageProcessingController.readUserCommands(outputStream, in);
    assertEquals(welcomeMessage
        + "Invalid Directory Path 'xyz/JamaicaPlain-small.jpg'.\n"
        + "Image save operation failed.\n", out.toString());
  }

  @Test
  public void testFileFormatIncorrectSave() {
    String command = "save test/JamaicaPlain-small.pngg jp\n"
        + "quit";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    imageProcessingController = new ImageProcessingControllerImpl(model, out, in, utility);
    imageProcessingController.readUserCommands(outputStream, in);
    assertEquals(welcomeMessage + "Unsupported file format - pngg\n\n"
        + "Image save operation failed.\n", out.toString());
  }

  @Test
  public void testFileNotFoundSave() {
    String command = "load test/xyz.jpg jp\n"
        + "quit";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    imageProcessingController = new ImageProcessingControllerImpl(model, out, in, utility);
    imageProcessingController.readUserCommands(outputStream, in);
    assertEquals(welcomeMessage + "File Not Found 'test/xyz.jpg'.\n"
        + "Image load operation failed.\n", out.toString());
  }

  @Test
  public void testDirectoryNotFoundLoad() {
    String command = "load helllos/JamaicaPlain-small.jpg jp\n"
        + "quit";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    imageProcessingController = new ImageProcessingControllerImpl(model, out, in, utility);
    imageProcessingController.readUserCommands(outputStream, in);
    assertEquals(welcomeMessage
        + "Invalid Directory Path 'helllos/JamaicaPlain-small.jpg'.\n"
        + "Image load operation failed.\n", out.toString());
  }

  @Test
  public void testFileFormatNotFoundLoad() {
    String command = "load test/JamaicaPlain-small.pngg jp\n"
        + "quit";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    imageProcessingController = new ImageProcessingControllerImpl(model, out, in, utility);
    imageProcessingController.readUserCommands(outputStream, in);
    assertEquals(welcomeMessage
        + "File Format Not Supported. 'pngg'.\n"
        + "Image load operation failed.\n", out.toString());
  }

  // Assignment 5 implementation

  @Test
  public void testcompressCommand() {

    String command = "compress 50 mh mhB\n"
        + "quit";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    imageProcessingController = new ImageProcessingControllerImpl(model, out, in, utility);
    imageProcessingController.readUserCommands(outputStream, in);
    assertEquals(welcomeMessage
        + "Image compress operation successful.\n", out.toString());
    String mockResponse = "Received inputs: mh , mhB , 50";
    assertEquals(mockResponse, mockLog.toString());
  }

  @Test
  public void testcompressCommand_negative_perc() {

    String command = "compress -1 mh mhB\n"
        + "quit";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    imageProcessingController = new ImageProcessingControllerImpl(model, out, in, utility);
    imageProcessingController.readUserCommands(outputStream, in);
    assertEquals(welcomeMessage
        + "Invalid percentage for compression. Please use 0 to 100.\n" +
        "Image compress operation failed.\n", out.toString());
  }

  @Test
  public void testcompressCommand_101_perc() {

    String command = "compress 101 mh mhB\n"
        + "quit";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    imageProcessingController = new ImageProcessingControllerImpl(model, out, in, utility);
    imageProcessingController.readUserCommands(outputStream, in);
    assertEquals(welcomeMessage
        + "Invalid percentage for compression. Please use 0 to 100.\n" +
        "Image compress operation failed.\n", out.toString());
  }

  @Test
  public void testcompressCommand_invalidCommand() {

    String command = "compress one mh mhB\n"
        + "quit";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    imageProcessingController = new ImageProcessingControllerImpl(model, out, in, utility);
    imageProcessingController.readUserCommands(outputStream, in);
    assertEquals(welcomeMessage
        + "one cannot be parsed into Integer.\n" +
        "Image compress operation failed.\n", out.toString());
  }

  @Test
  public void testcompressCommand_invalidArguments() {

    String command = "compress 1 mh mhB full\n"
        + "quit";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    imageProcessingController = new ImageProcessingControllerImpl(model, out, in, utility);
    imageProcessingController.readUserCommands(outputStream, in);
    assertEquals(welcomeMessage +
        "Invalid command entered!\n", out.toString());
  }

  @Test
  public void testColorCommand() {

    String command = "color-correct mh mhCC\n"
        + "quit";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    imageProcessingController = new ImageProcessingControllerImpl(model, out, in, utility);
    imageProcessingController.readUserCommands(outputStream, in);
    assertEquals(welcomeMessage
        + "Image color-correct operation successful.\n", out.toString());
    String mockResponse = "Received inputs: mh , mhCC";
    assertEquals(mockResponse, mockLog.toString());
  }

  @Test
  public void testColorCommand_invalidArguments() {

    String command = "color-correct mh \n"
        + "quit";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    imageProcessingController = new ImageProcessingControllerImpl(model, out, in, utility);
    imageProcessingController.readUserCommands(outputStream, in);
    assertEquals(welcomeMessage
        + "Invalid command entered!\n", out.toString());
  }

  @Test
  public void testLevelAdjust() {

    String command = "levels-adjust 0 128 255 mh mhLA\n"
        + "quit";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    imageProcessingController = new ImageProcessingControllerImpl(model, out, in, utility);
    imageProcessingController.readUserCommands(outputStream, in);
    assertEquals(welcomeMessage
        + "Image levels-adjust operation successful.\n", out.toString());
    String mockResponse = "Received inputs: mh , mhLA , 0 , 128 , 255";
    assertEquals(mockResponse, mockLog.toString());
  }

  @Test
  public void testLevelsAdjustwithsplit_invalid_split_percentage() {
    String command = "levels-adjust 0 127 255 jp jp-ll split 190\n" +
        "quit";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    imageProcessingController = new ImageProcessingControllerImpl(model, out, in, utility);
    imageProcessingController.readUserCommands(outputStream, in);
    String resp = welcomeMessage + "Invalid value 190 for percentage - must be within 0 to 100.\n";
    assertEquals(resp + "Image levels-adjust operation failed.\n", out.toString());

  }

  @Test
  public void testsplit_invalid_split_percentage() {
    String command = "blur jp jp-ll split -1\n" +
        "quit";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    imageProcessingController = new ImageProcessingControllerImpl(model, out, in, utility);
    imageProcessingController.readUserCommands(outputStream, in);
    String resp = welcomeMessage + "Invalid value -1 for percentage.\n";
    assertEquals(resp + "Image blur operation failed.\n", out.toString());

  }

  @Test
  public void testLevelsAdjust_invalid_BMW_value() {
    String command = "levels-adjust -1 127 255 jp jp-ll\n" +
        "quit";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    imageProcessingController = new ImageProcessingControllerImpl(model, out, in, utility);
    imageProcessingController.readUserCommands(outputStream, in);
    String resp = welcomeMessage + "Invalid value for black/mid/white - " +
        "value must be in ascending order" +
        " of B<M<W and within 0 to 255.\n";
    assertEquals(resp + "Image levels-adjust operation failed.\n", out.toString());

  }

  @Test
  public void testLevelsAdjust_BMW_value_not_asc() {
    String command = "levels-adjust 255 14 0 jp jp-ll\n" +
        "quit";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    imageProcessingController = new ImageProcessingControllerImpl(model, out, in, utility);
    imageProcessingController.readUserCommands(outputStream, in);
    String resp = welcomeMessage + "Invalid value for black/mid/white - " +
        "value must be in ascending order" +
        " of B<M<W and within 0 to 255.\n";
    assertEquals(resp + "Image levels-adjust operation failed.\n", out.toString());

  }

  @Test
  public void testLevelsAdjust_BMW_Invalid_Arguments() {
    String command = "levels-adjust 255 one 0 jp jp-ll\n" +
        "quit";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    imageProcessingController = new ImageProcessingControllerImpl(model, out, in, utility);
    imageProcessingController.readUserCommands(outputStream, in);
    String resp = welcomeMessage + "Exception cannot be parsed into Integer.\n";
    assertEquals(resp + "Image levels-adjust operation failed.\n", out.toString());

  }

  @Test
  public void testLevelsAdjust_BMW_Invalid_Command() {
    String command = "levels-adjusts 255 one 0 jp jp-ll\n" +
        "quit";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    imageProcessingController = new ImageProcessingControllerImpl(model, out, in, utility);
    imageProcessingController.readUserCommands(outputStream, in);
    String resp = welcomeMessage + "Invalid command entered!\n";
    assertEquals(resp, out.toString());

  }

  @Test
  public void testHistogram() {

    String command = "histogram mh mhLA\n"
        + "quit";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    imageProcessingController = new ImageProcessingControllerImpl(model, out, in, utility);
    imageProcessingController.readUserCommands(outputStream, in);
    assertEquals(welcomeMessage
        + "Image histogram operation successful.\n", out.toString());
    String mockResponse = "Received inputs: mh , mhLA";
    assertEquals(mockResponse, mockLog.toString());
  }

  @Test
  public void testHistogram_invalidCommand() {

    String command = "histogram mh mhLA full\n"
        + "quit";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    imageProcessingController = new ImageProcessingControllerImpl(model, out, in, utility);
    imageProcessingController.readUserCommands(outputStream, in);
    assertEquals(welcomeMessage
        + "Invalid command entered!\n", out.toString());
  }


  @Test
  public void testColorAdjustwithSplit() {

    String command = "color-correct mh mhCC split 50\n"
        + "quit";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    imageProcessingController = new ImageProcessingControllerImpl(model, out, in, utility);
    imageProcessingController.readUserCommands(outputStream, in);
    assertEquals(welcomeMessage
        + "Image color-correct operation successful.\n", out.toString());
    String mockResponse = "Received inputs: mh , mhCC Split call inputs: mh , mhCC , 50";
    assertEquals(mockResponse, mockLog.toString());
  }


  @Test
  public void testBlurwithsplit() {
    String command = "blur jp jp-blur split 12\n" +
        "quit";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    imageProcessingController = new ImageProcessingControllerImpl(model, out, in, utility);
    imageProcessingController.readUserCommands(outputStream, in);

    assertEquals(welcomeMessage + "Image blur operation successful.\n", out.toString());

    String mockResponse = "Received inputs: blur , jp , jp-blur Split call inputs: jp ," +
        " jp-blur , 12";
    assertEquals(mockResponse, mockLog.toString());
  }


  @Test
  public void testSharpenwithsplit() {
    String command = "sharpen jp jp-sharpen split 90\n" +
        "quit";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    imageProcessingController = new ImageProcessingControllerImpl(model, out, in, utility);
    imageProcessingController.readUserCommands(outputStream, in);

    assertEquals(welcomeMessage + "Image sharpen operation successful.\n", out.toString());

    String mockResponse = "Received inputs: sharpen , jp , jp-sharpen Split " +
        "call inputs: jp , jp-sharpen , 90";
    assertEquals(mockResponse, mockLog.toString());
  }


  @Test
  public void testSepiawithsplit() {
    String command = "sepia jp jp-sepia split 90\n" +
        "quit";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    imageProcessingController = new ImageProcessingControllerImpl(model, out, in, utility);
    imageProcessingController.readUserCommands(outputStream, in);

    assertEquals(welcomeMessage + "Image sepia operation successful.\n", out.toString());

    String mockResponse = "Received inputs: jp , jp-sepia" +
        " Split call inputs: jp , jp-sepia , 90";
    assertEquals(mockResponse, mockLog.toString());
  }


  @Test
  public void testRedComponentwithsplit() {
    String command = "red-component jp jp-red split 90\n" +
        "quit";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    imageProcessingController = new ImageProcessingControllerImpl(model, out, in, utility);
    imageProcessingController.readUserCommands(outputStream, in);

    assertEquals(welcomeMessage + "Image red-component operation successful.\n", out.toString());

    String mockResponse = "Received inputs: red-component ," +
        " jp , jp-red Split call inputs: jp , jp-red , 90";
    assertEquals(mockResponse, mockLog.toString());
  }

  @Test
  public void testGreenComponentwithsplit() {
    String command = "green-component jp jp-green split 90\n" +
        "quit";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    imageProcessingController = new ImageProcessingControllerImpl(model, out, in, utility);
    imageProcessingController.readUserCommands(outputStream, in);

    assertEquals(welcomeMessage + "Image green-component operation successful.\n", out.toString());

    String mockResponse = "Received inputs: green-component , jp ," +
        " jp-green Split call inputs: jp , jp-green , 90";
    assertEquals(mockResponse, mockLog.toString());
  }

  @Test
  public void testBlueComponentwithsplit() {
    String command = "blue-component jp jp-blue split 90\n" +
        "quit";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    imageProcessingController = new ImageProcessingControllerImpl(model, out, in, utility);
    imageProcessingController.readUserCommands(outputStream, in);

    assertEquals(welcomeMessage + "Image blue-component operation successful.\n", out.toString());

    String mockResponse = "Received inputs: blue-component , jp " +
        ", jp-blue Split call inputs: jp , jp-blue , 90";
    assertEquals(mockResponse, mockLog.toString());
  }

  @Test
  public void testValueComponentwithsplit() {
    String command = "value-component jp jp-value split 90\n" +
        "quit";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    imageProcessingController = new ImageProcessingControllerImpl(model, out, in, utility);
    imageProcessingController.readUserCommands(outputStream, in);

    assertEquals(welcomeMessage + "Image value-component" +
        " operation successful.\n", out.toString());

    String mockResponse = "Received inputs: value-component , jp ," +
        " jp-value Split call inputs: jp , jp-value , 90";
    assertEquals(mockResponse, mockLog.toString());
  }

  @Test
  public void testLumaComponentwithsplit() {
    String command = "luma-component jp jp-luma split 90\n" +
        "quit";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    imageProcessingController = new ImageProcessingControllerImpl(model, out, in, utility);
    imageProcessingController.readUserCommands(outputStream, in);

    assertEquals(welcomeMessage + "Image luma-component" +
        " operation successful.\n", out.toString());

    String mockResponse = "Received inputs: luma-component , jp , " +
        "jp-luma Split call inputs: jp , jp-luma , 90";
    assertEquals(mockResponse, mockLog.toString());
  }

  @Test
  public void testintensityComponentwithsplit() {
    String command = "intensity-component jp jp-intensity split 90\n" +
        "quit";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    imageProcessingController = new ImageProcessingControllerImpl(model, out, in, utility);
    imageProcessingController.readUserCommands(outputStream, in);

    assertEquals(welcomeMessage + "Image intensity-component" +
        " operation successful.\n", out.toString());

    String mockResponse = "Received inputs: intensity-component , jp ," +
        " jp-intensity Split call inputs: jp ," +
        " jp-intensity , 90";
    assertEquals(mockResponse, mockLog.toString());
  }

  @Test
  public void testLevelsAdjustwithsplit() {
    String command = "levels-adjust 0 127 255 jp jp-ll split 90\n" +
        "quit";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    imageProcessingController = new ImageProcessingControllerImpl(model, out, in, utility);
    imageProcessingController.readUserCommands(outputStream, in);

    assertEquals(welcomeMessage + "Image levels-adjust operation" +
        " successful.\n", out.toString());

    String mockResponse = "Received inputs: jp , jp-ll , 0 , 127 , 255" +
        " Split call inputs: jp , jp-ll , 90";
    assertEquals(mockResponse, mockLog.toString());
  }


  @Test
  public void testsplit_invalid_split_percentage_invalidArguments() {
    String command = "blur jp jp-ll split aa\n" +
        "quit";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    imageProcessingController = new ImageProcessingControllerImpl(model, out, in, utility);
    imageProcessingController.readUserCommands(outputStream, in);
    String resp = welcomeMessage + "aa cannot be parsed into Integer.\n";
    assertEquals(resp + "Image blur operation failed.\n", out.toString());

  }

  @Test
  public void test_levels_adjust_invalid_BMW() {
    String command = "levels-adjust aa jj bb jp jp-ll\n" +
        "quit";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    imageProcessingController = new ImageProcessingControllerImpl(model, out, in, utility);
    imageProcessingController.readUserCommands(outputStream, in);
    String resp = welcomeMessage + "Exception cannot be parsed into Integer.\n";
    assertEquals(resp + "Image levels-adjust operation failed.\n", out.toString());

  }


  @Test
  public void testsplit_invalid_split_percentage_invalidPercentage() {
    String command = "blur jp jp-ll split -1\n" +
        "quit";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    imageProcessingController = new ImageProcessingControllerImpl(model, out, in, utility);
    imageProcessingController.readUserCommands(outputStream, in);
    String resp = welcomeMessage + "Invalid value -1 for percentage.\n";
    assertEquals(resp + "Image blur operation failed.\n", out.toString());

  }

  @Test
  public void testsplit_invalid_split_percentage_invalidPercentage_101() {
    String command = "blur jp jp-ll split 101\n" +
        "quit";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    imageProcessingController = new ImageProcessingControllerImpl(model, out, in, utility);
    imageProcessingController.readUserCommands(outputStream, in);
    String resp = welcomeMessage + "Invalid value 101 for percentage.\n";
    assertEquals(resp + "Image blur operation failed.\n", out.toString());

  }

  /**
   * Mock Model class used to test the ImageProcessingController.
   * We test if the inputs to the Model match the inputs passed by the controller.
   */
  public static class MockModel implements ImageProcessingModel {

    private final StringBuilder log;

    MockModel(StringBuilder log) {
      this.log = log;
    }

    @Override
    public boolean addImage(String filename, BufferedImage image) {
      log.append("Received inputs: ").append(filename);
      return true;
    }

    @Override
    public BufferedImage getImage(String filename) {
      BufferedImage bufferedImage = null;
      log.append("Received inputs: ").append(filename);
      try {
        File file = new File("resources/input/diya.png");
        bufferedImage = ImageIO.read(file);
      } catch (IOException e) {
        log.append(e);
      }
      return bufferedImage;
    }

    @Override
    public boolean getGreyScale(String componentType, String sourceFilename,
                                String destinationFilename) {
      log.append("Received inputs: ").append(componentType)
          .append(" , ").append(sourceFilename)
          .append(" , ").append(destinationFilename);
      return true;
    }

    @Override
    public boolean doFlip(String flipType, String sourceFilename, String destinationFilename) {
      log.append("Received inputs: ").append(flipType)
          .append(" , ").append(sourceFilename)
          .append(" , ").append(destinationFilename);
      return true;
    }

    @Override
    public boolean doBrightening(int increments, String sourceFilename,
                                 String destinationFilename) {
      log.append("Received inputs: ").append(increments)
          .append(" , ").append(sourceFilename)
          .append(" , ").append(destinationFilename);
      return true;
    }

    @Override
    public boolean rgbSplit(String imageName,
                            String redDestinationImageName,
                            String greenDestinationImageName,
                            String blueDestinationImageName) {
      log.append("Received inputs: ").append(imageName)
          .append(" , ").append(redDestinationImageName)
          .append(" , ").append(greenDestinationImageName).append(" and ")
          .append(blueDestinationImageName);
      return true;
    }

    @Override
    public boolean rbgCombine(String destinationFilename, String redSourceFilename,
                              String greenSourceFilename,
                              String blueSourceFilename) {
      log.append("Received inputs: ").append(destinationFilename)
          .append(" , ").append(redSourceFilename)
          .append(" , ").append(greenSourceFilename)
          .append(" , ").append(blueSourceFilename);
      return true;
    }

    @Override
    public boolean blurSharpenImage(String filterType, String sourceFilename,
                                    String destinationFilename) {
      log.append("Received inputs: ").append(filterType)
          .append(" , ").append(sourceFilename)
          .append(" , ").append(destinationFilename);
      return true;
    }

    @Override
    public boolean getSepia(String sourceFilename, String destinationFilename) {
      log.append("Received inputs: ").append(sourceFilename)
          .append(" , ").append(destinationFilename);
      return true;
    }

    @Override
    public ImageMetadata getImageData(String imageName) {
      return null;
    }

    @Override
    public boolean compress(int percentage, String sourceFilename, String destinationFilename) {
      log.append("Received inputs: ").append(sourceFilename)
          .append(" , ").append(destinationFilename).append(" , ").append(percentage);
      return true;
    }

    @Override
    public boolean createHistogram(String sourceFilename, String destinationFilename) {
      log.append("Received inputs: ").append(sourceFilename)
          .append(" , ").append(destinationFilename);
      return true;
    }

    @Override
    public boolean colorCorrection(String sourceFilename, String destinationFilename) {
      log.append("Received inputs: ").append(sourceFilename)
          .append(" , ").append(destinationFilename);
      return true;
    }

    @Override
    public boolean levelAdjustment(String sourceFilename,
                                   String destinationFilename, int black, int mid,
                                   int white) {
      log.append("Received inputs: ").append(sourceFilename)
          .append(" , ").append(destinationFilename)
          .append(" , ").append(black)
          .append(" , ").append(mid)
          .append(" , ").append(white);
      return true;
    }

    @Override
    public boolean operationPreview(int percentage,
                                    String originalfilename, String modifiedfilename) {
      log.append(" Split call inputs: ").append(originalfilename)
          .append(" , ").append(modifiedfilename).append(" , ").append(percentage);
      return true;
    }

    @Override
    public BufferedImage splitPreview(int percentage,
                                      String originalFilename, String modifiedFilename) {
      return null;
    }

  }
}
