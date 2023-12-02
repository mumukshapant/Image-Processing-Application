import controller.MVCController;
import model.ImageMetadata;
import model.ImageProcessingModel;


import org.junit.Before;
import org.junit.Test;

import utility.FileReadWriteUtility;
import utility.FileReadWriteUtilityImpl;
import view.Features;
import view.IView;

import javax.imageio.ImageIO;


import java.awt.image.BufferedImage;
import java.io.PrintStream;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * This class tests the main working ImageProcessing Controller.
 */
public class MVCControllerTest {

  OutputStream out;
  PrintStream outputStream;
  InputStream in;
  StringBuilder mockLog;
  ImageProcessingModel model;
  FileReadWriteUtility utility;

  IView view;

  MVCController mvcController;

  @Before
  public void setup() {
    out = new ByteArrayOutputStream();
    outputStream = new PrintStream(out);
    in = System.in;
    mockLog = new StringBuilder();
    model = new MockModel(mockLog);
    view = new MockVeiw(mockLog);
    utility = new FileReadWriteUtilityImpl();
    mvcController = new MVCController(model, view, utility, out);
  }

  /**
   * Test the 'load' command.
   */
  @Test
  public void testLoadImage() {
    File file = new File("test/dataset/test_images/tempImage.jpg");
    ((MockVeiw) view).setFileToReturn(file);
    mvcController.loadImage();
    assertEquals("tempImage.jpg", ((MockModel) model).getLastReceivedImageName());

    String logs = ((MockModel) model).getLogs();

    assertTrue(logs.equals("Received inputs: Received inputs: tempImage.jpg , tempImage.jpg-hist"));
  }

  @Test
  public void testSepia() {
    File file = new File("test/dataset/test_images/tempImage.jpg");
    ((MockVeiw) view).setFileToReturn(file);
    mvcController.loadImage();

    mvcController.sepia("tempImage.jpg");

    String logs = ((MockModel) model).getLogs();

    assertTrue(logs.equals("Received inputs: Received inputs: tempImage.jpg ," +
        " tempImage.jpg-histReceived inputs: tempImage.jpg , tempImage.jpg-sepiaReceived " +
        "inputs: tempImage.jpgReceived inputs: tempImage.jpg-sepia , tempImage.jpg-sepia-hist"));

  }


  @Test
  public void testBlur() {
    File file = new File("test/dataset/test_images/tempImage.jpg");
    ((MockVeiw) view).setFileToReturn(file);
    mvcController.loadImage();

    mvcController.blur("tempImage.jpg");

    String logs = ((MockModel) model).getLogs();

    assertEquals(logs, "Received inputs: Received inputs: tempImage.jpg , " +
        "tempImage.jpg-histReceived inputs: blur , tempImage.jpg , tempImage.jpg-blurReceived" +
        " inputs: tempImage.jpgReceived inputs: tempImage.jpg-blur , " +
        "tempImage.jpg-blur-hist");

  }

  @Test
  public void testSharpen() {
    File file = new File("test/dataset/test_images/tempImage.jpg");
    ((MockVeiw) view).setFileToReturn(file);
    mvcController.loadImage();

    mvcController.sharpen("tempImage.jpg");

    String logs = ((MockModel) model).getLogs();

    assertEquals(logs, "Received inputs: Received inputs: tempImage.jpg ," +
        " tempImage.jpg-histReceived inputs: sharpen , tempImage.jpg ," +
        " tempImage.jpg-sharpenReceived inputs: tempImage.jpgReceived inputs:" +
        " tempImage.jpg-sharpen , tempImage.jpg-sharpen-hist");

  }


  @Test
  public void testcolorCorrect() {
    File file = new File("test/dataset/test_images/tempImage.jpg");
    ((MockVeiw) view).setFileToReturn(file);
    mvcController.loadImage();

    mvcController.colorCorrect("tempImage.jpg");

    String logs = ((MockModel) model).getLogs();

    assertEquals(logs, "Received inputs: Received inputs: tempImage.jpg ," +
        " tempImage.jpg-histReceived inputs: tempImage.jpg , " +
        "tempImage.jpg-color_correctReceived inputs: tempImage.jpgReceived " +
        "inputs: tempImage.jpg-color_correct , tempImage.jpg-color_correct-hist");

  }

  @Test
  public void testcompress() {
    File file = new File("test/dataset/test_images/tempImage.jpg");
    ((MockVeiw) view).setFileToReturn(file);
    mvcController.loadImage();

    mvcController.compress(50, "tempImage.jpg");

    String logs = ((MockModel) model).getLogs();

    assertEquals(logs, "Received inputs: Received inputs: tempImage.jpg ," +
        " tempImage.jpg-histReceived inputs: tempImage.jpg , tempImage.jpg-compress ," +
        " 50Received inputs: tempImage.jpg-compress , tempImage.jpg-compress-hist");

  }

  @Test
  public void testbrightness() {
    File file = new File("test/dataset/test_images/tempImage.jpg");
    ((MockVeiw) view).setFileToReturn(file);
    mvcController.loadImage();

    mvcController.brightness(50, "tempImage.jpg");

    String logs = ((MockModel) model).getLogs();

    assertEquals(logs, "Received inputs: Received inputs: tempImage.jpg ," +
        " tempImage.jpg-histReceived inputs: 50 , tempImage.jpg , " +
        "tempImage.jpg-brightenReceived inputs: tempImage.jpg-brighten ," +
        " tempImage.jpg-brighten-hist");

  }

  @Test
  public void test_luma_component() {
    File file = new File("test/dataset/test_images/tempImage.jpg");
    ((MockVeiw) view).setFileToReturn(file);
    mvcController.loadImage();

    mvcController.lumaComponent("tempImage.jpg");

    String logs = ((MockModel) model).getLogs();

    assertEquals(logs, "Received inputs: Received inputs: tempImage.jpg ," +
        " tempImage.jpg-histReceived inputs: luma-component , tempImage.jpg ," +
        " tempImage.jpg-lumaComponentReceived inputs: tempImage.jpgReceived inputs:" +
        " tempImage.jpg-lumaComponent , tempImage.jpg-lumaComponent-hist");

  }

  @Test
  public void test_intensity_component() {
    File file = new File("test/dataset/test_images/tempImage.jpg");
    ((MockVeiw) view).setFileToReturn(file);
    mvcController.loadImage();

    mvcController.intensityComponent("tempImage.jpg");

    String logs = ((MockModel) model).getLogs();

    assertEquals(logs, "Received inputs: Received inputs: tempImage.jpg ," +
        " tempImage.jpg-histReceived inputs: intensity-component , " +
        "tempImage.jpg , tempImage.jpg-intensityComponentReceived inputs: " +
        "tempImage.jpgReceived inputs: tempImage.jpg-intensityComponent , " +
        "tempImage.jpg-intensityComponent-hist");

  }

  @Test
  public void test_value_component() {
    File file = new File("test/dataset/test_images/tempImage.jpg");
    ((MockVeiw) view).setFileToReturn(file);
    mvcController.loadImage();

    mvcController.valueComponent("tempImage.jpg");

    String logs = ((MockModel) model).getLogs();

    assertEquals(logs, "Received inputs: Received inputs: tempImage.jpg ," +
        " tempImage.jpg-histReceived inputs: value-component , tempImage.jpg , " +
        "tempImage.jpg-valueComponentReceived inputs: tempImage.jpgReceived inputs: " +
        "tempImage.jpg-valueComponent , tempImage.jpg-valueComponent-hist");

  }


  @Test
  public void test_red_component() {
    File file = new File("test/dataset/test_images/tempImage.jpg");
    ((MockVeiw) view).setFileToReturn(file);
    mvcController.loadImage();

    mvcController.redComponent("tempImage.jpg");

    String logs = ((MockModel) model).getLogs();

    assertEquals(logs, "Received inputs: Received inputs: tempImage.jpg , " +
        "tempImage.jpg-histReceived inputs: red-component , tempImage.jpg , " +
        "tempImage.jpg-redComponentReceived inputs: tempImage.jpgReceived inputs: " +
        "tempImage.jpg-redComponent , tempImage.jpg-redComponent-hist");

  }

  @Test
  public void test_green_component() {
    File file = new File("test/dataset/test_images/tempImage.jpg");
    ((MockVeiw) view).setFileToReturn(file);
    mvcController.loadImage();

    mvcController.greenComponent("tempImage.jpg");

    String logs = ((MockModel) model).getLogs();

    assertEquals(logs, "Received inputs: Received inputs: tempImage.jpg , " +
        "tempImage.jpg-histReceived inputs: green-component , tempImage.jpg , " +
        "tempImage.jpg-greenComponentReceived inputs: tempImage.jpgReceived inputs:" +
        " tempImage.jpg-greenComponent , tempImage.jpg-greenComponent-hist");

  }

  @Test
  public void test_blue_component() {
    File file = new File("test/dataset/test_images/tempImage.jpg");
    ((MockVeiw) view).setFileToReturn(file);
    mvcController.loadImage();

    mvcController.blueComponent("tempImage.jpg");

    String logs = ((MockModel) model).getLogs();

    assertEquals(logs, "Received inputs: Received inputs: tempImage.jpg " +
        ", tempImage.jpg-histReceived inputs: blue-component , tempImage.jpg , " +
        "tempImage.jpg-blueComponentReceived inputs: tempImage.jpgReceived inputs: " +
        "tempImage.jpg-blueComponent , tempImage.jpg-blueComponent-hist");

  }

  @Test
  public void test_levels_adjust() {
    File file = new File("test/dataset/test_images/tempImage.jpg");
    ((MockVeiw) view).setFileToReturn(file);
    mvcController.loadImage();

    mvcController.levelsAdjust(1, 2, 3, "tempImage.jpg");

    String logs = ((MockModel) model).getLogs();

    assertEquals(logs, "Received inputs: Received inputs: tempImage.jpg ," +
        " tempImage.jpg-histReceived inputs: tempImage.jpg , " +
        "tempImage.jpg-levels_adjust , 1 , 2 , 3Received inputs:" +
        " tempImage.jpgReceived inputs: tempImage.jpg-levels_adjust , " +
        "tempImage.jpg-levels_adjust-hist");

  }

  @Test
  public void test_image_flip() {
    File file = new File("test/dataset/test_images/tempImage.jpg");
    ((MockVeiw) view).setFileToReturn(file);
    mvcController.loadImage();

    mvcController.flipImage("tempImage.jpg", "horizontal");

    String logs = ((MockModel) model).getLogs();

    assertEquals(logs, "Received inputs: Received inputs: tempImage.jpg , " +
        "tempImage.jpg-histReceived inputs: horizontal-flip , tempImage.jpg ," +
        " tempImage.jpg-horizontal_flipReceived inputs: tempImage.jpg-horizontal_flip ," +
        " tempImage.jpg-horizontal_flip-hist");

  }

  @Test
  public void test_split() {
    File file = new File("test/dataset/test_images/tempImage.jpg");
    ((MockVeiw) view).setFileToReturn(file);
    mvcController.loadImage();

    BufferedImage bufferedImage =
        mvcController.getSplitPreview("tempImage.jpg", "tempImage.jpg", 40);

    assertNotNull(bufferedImage);
    String logs = ((MockModel) model).getLogs();

    assertEquals(logs, "Received inputs: Received inputs: tempImage.jpg , " +
        "tempImage.jpg-histReceived inputs: tempImage.jpg");

  }

  @Test
  public void test_save() {
    File file = new File("test/dataset/test_images/tempSavedImage.png");
    assertFalse(file.exists());
    mvcController.saveImage("tempSavedImage.png");
    assertTrue(file.exists());
    file.delete();
  }

  /**
   * Mock Model class used to test the ImageProcessingController.
   * We test if the inputs to the Model match the inputs passed by the controller.
   */
  public static class MockModel implements ImageProcessingModel {

    private final StringBuilder log;
    private String lastReceivedImageName;

    MockModel(StringBuilder log) {
      this.log = log;
    }

    private BufferedImage getSepiaImage(String filename) {
      return new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
    }

    @Override
    public BufferedImage getImage(String filename) {

      return new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
    }

    @Override
    public boolean addImage(String filename, BufferedImage image) {
      lastReceivedImageName = filename;
      return true;
    }

    String getLastReceivedImageName() {
      return lastReceivedImageName;
    }

    public String getLogs() {
      return log.toString();
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
      BufferedImage bufferedImage = null;
      log.append("Received inputs: ").append(originalFilename);
      try {
        File file = new File("resources/input/diya.png");
        bufferedImage = ImageIO.read(file);
      } catch (IOException e) {
        log.append(e);
      }
      return bufferedImage;
    }

  }

  /**
   * Mock View class used to test the MVCController.
   */
  public static class MockVeiw implements IView {
    private final StringBuilder log;

    MockVeiw(StringBuilder log) {
      this.log = log;
    }

    @Override
    public void addFeatures(Features features) throws IllegalArgumentException {
      log.append("Received inputs: ");
    }

    @Override
    public File fetchFile() {
      BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);

      try {
        File tempFile = new File("test/dataset/test_images/tempImage.jpg");

        ImageIO.write(image, "jpg", tempFile);

        return tempFile;
      } catch (IOException e) {
        e.printStackTrace();
      }

      return null;
    }


    @Override
    public void placeImageInScreen(String imageName, BufferedImage image) {
      return;
    }

    @Override
    public void showHistogram(String imageName, BufferedImage image) {
      return;
    }

    @Override
    public void showNoImageError() {
      return;
    }

    @Override
    public File getFilesToSave() {
      BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);

      try {
        File tempFile = new File("test/dataset/test_images/tempSavedImage.png");

        ImageIO.write(image, "png", tempFile);

        return tempFile;
      } catch (IOException e) {
        e.printStackTrace();
      }

      return null;
    }

    @Override
    public boolean openPreviewWindow(BufferedImage image, String originalFile,
                      String filteredFile, Features features) {
      return true;
    }

    @Override
    public void showSaveStatus(String message) {
      return;
    }

    /**
     * shows the saved status of the current image.
     */
    @Override
    public boolean saveStatus() {
      return false;
    }

    @Override
    public boolean promptUser() {
      return false;
    }

    @Override
    public void showErrorMessage(String message) {
      return;
    }
    void setFileToReturn(File file) {
      return;
    }
  }

}
