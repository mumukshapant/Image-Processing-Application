import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the ImageProcessingApplication class.
 */
public class ImageProcessingApplicationTest {

  private final PrintStream originalOut = System.out;
  private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

  /**
   * Redirects the standard output to capture console output for testing.
   */
  @Before
  public void setUpStreams() {
    System.setOut(new PrintStream(outputStream));
  }

  /**
   * Restores the standard output after testing.
   */
  @After
  public void restoreStreams() {
    System.setOut(originalOut);
  }

  /**
   * Tests the main method with invalid path arguments.
   */
  @Test
  public void testMainWithInvalidPathArguments() {
    Path currentRelativePath = Paths.get("");
    String filePath = currentRelativePath.toAbsolutePath()
        + "/random/dataset/test_script.txt";

    ImageProcessingApplication.main(new String[]{"-file", filePath});
    String output = outputStream.toString();
    assertEquals("Directory Not found\n", output);
  }

  @Test
  public void testMainWithInvalidFileArguments() {

    ImageProcessingApplication.main(new String[]{"-file", "/test_script.txt"});
    String output = outputStream.toString();
    assertEquals("File /test_script.txt Not found\n", output);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMainWithInvalidArguments() {
    ImageProcessingApplication.main(new String[]{"-file do", "/test_script.txt"});
    String output = outputStream.toString();
    assertEquals("File /test_script.txt Not found\n", output);
  }

  @Test
  public void testMainWithValid() {
    Path currentRelativePath = Paths.get("");
    String filePath = currentRelativePath.toAbsolutePath()
        + "/test/dataset/test_script.txt";

    ImageProcessingApplication.main(new String[]{"-file", filePath});
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
    assertEquals(scriptOutput
            + "Script " + filePath + " ran successfully.\n",
        outputStream.toString());
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


}
