import model.ImageMetadata;
import model.ImageProcessingModelImpl;

import org.junit.Test;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


/**
 * Test class for testing the ImageProcessingModelImpl class.
 */
public class ImageProcessingModelImplTest {
  ImageProcessingModelImpl obj;
  OutputStream out;
  int[][][] rbgMap;
  BufferedImage image;


  /**
   * Sets up the initial state for the test cases.
   */
  @org.junit.Before
  public void setUp() {

    out = new ByteArrayOutputStream();
    obj = new ImageProcessingModelImpl(out);

    rbgMap = new int[][][]{
        {{255, 115, 0}, {255, 115, 0}, {255, 115, 0}},
        {{255, 115, 0}, {255, 115, 0}, {255, 115, 0}},
        {{20, 10, 0}, {20, 10, 0}, {20, 10, 0}}
    };


    image = createBufferImage(3, 3, rbgMap);
    obj.addImage("dummy-file", image);
  }

  /**
   * Test case to check if the test image is loaded as expected.
   */
  @Test
  public void TestLoadImage() {

    assertTrue(obj.addImage("Test_Image", image));
    ImageMetadata imageMetadata = obj.getImageData("Test_Image");
    assertEquals(3, imageMetadata.getHeight());
    assertEquals(3, imageMetadata.getWidth());
    int[][][] pixels = imageMetadata.getRgb();
    for (int i = 0; i < imageMetadata.getHeight(); i++) {
      for (int j = 0; j < imageMetadata.getWidth(); j++) {

        assertEquals(rbgMap[0][i][j],
            pixels[0][i][j]);
        assertEquals(rbgMap[1][i][j],
            pixels[1][i][j]);
        assertEquals(rbgMap[2][i][j],
            pixels[2][i][j]);
      }
    }

  }


  /**
   * Test case to check if the test image is saved as expected.
   */
  @Test
  public void testGetImage() {
    // Add an image with the test RGB data to the ImageProcessingModelImpl object
    obj.addImage("Test_Image", image);

    // Retrieve the image using the getImage method
    BufferedImage image = obj.getImage("Test_Image");

    // Verify that the retrieved image is not null
    assertNotNull(image);

    // Verify the dimensions of the retrieved image
    assertEquals(3, image.getWidth());
    assertEquals(3, image.getHeight());
  }


  @Test
  public void test_brighten_image() {

    obj.doBrightening(10, "dummy-file",
        "dummy-file-brighten");

    ImageMetadata src = obj.getImageData("dummy-file");
    ImageMetadata newfile = obj.getImageData("dummy-file-brighten");

    int[][][] srcPixels = src.getRgb();
    int[][][] newPixels = newfile.getRgb();

    for (int i = 0; i < src.getHeight(); i++) {
      for (int j = 0; j < src.getWidth(); j++) {

        int[] oldPixel = new int[]{srcPixels[0][i][j], srcPixels[1][i][j], srcPixels[2][i][j]};
        int[] newPixel = new int[]{newPixels[0][i][j], newPixels[1][i][j], newPixels[2][i][j]};

        assertEquals(Math.min(oldPixel[0] + 10, 255), newPixel[0], 0.01);
        assertEquals(Math.min(oldPixel[1] + 10, 255), newPixel[1], 0.01);
        assertEquals(Math.min(oldPixel[2] + 10, 255), newPixel[2], 0.01);
      }
    }


  }

  @Test
  public void test_darken_image() {

    obj.doBrightening(-10, "dummy-file",
        "dummy-file-dark");

    ImageMetadata src = obj.getImageData("dummy-file");
    ImageMetadata newfile = obj.getImageData("dummy-file-dark");

    int[][][] srcPixels = src.getRgb();
    int[][][] newPixels = newfile.getRgb();

    for (int i = 0; i < src.getHeight(); i++) {
      for (int j = 0; j < src.getWidth(); j++) {


        int[] oldPixel = new int[]{srcPixels[0][i][j], srcPixels[1][i][j], srcPixels[2][i][j]};
        int[] newPixel = new int[]{newPixels[0][i][j], newPixels[1][i][j], newPixels[2][i][j]};

        assertEquals(Math.max(oldPixel[0] - 10, 0), newPixel[0], 0.01);
        assertEquals(Math.max(oldPixel[1] - 10, 0), newPixel[1], 0.01);
        assertEquals(Math.max(oldPixel[2] - 10, 0), newPixel[2], 0.01);
      }
    }


  }


  // darken does not change size of image
  @Test
  public void darkenImageDimensionsRemainSame() {

    obj.doBrightening(-10, "dummy-file",
        "dummy-file-dark");

    ImageMetadata src = obj.getImageData("dummy-file");
    ImageMetadata newfile = obj.getImageData("dummy-file-dark");
    assertEquals(src.getWidth(), newfile.getWidth());
    assertEquals(src.getHeight(), newfile.getHeight());
  }


  // brighten does not change size of image
  @Test
  public void brightenImageDimensionsRemainSame() {

    obj.doBrightening(10, "dummy-file",
        "dummy-file-bright");

    ImageMetadata src = obj.getImageData("dummy-file");
    ImageMetadata newfile = obj.getImageData("dummy-file-bright");
    assertEquals(src.getWidth(), newfile.getWidth());
    assertEquals(src.getHeight(), newfile.getHeight());
  }

  //brightening multiple times
  @Test
  public void brightenMultiple() {

    obj.doBrightening(10, "dummy-file",
        "dummy-file-brighten");
    obj.doBrightening(10, "dummy-file-brighten",
        "dummy-file-brighten-2");
    obj.doBrightening(10, "dummy-file-brighten-2",
        "dummy-file-brighten-3");
    obj.doBrightening(10, "dummy-file-brighten-3",
        "dummy-file-brighten-4");

    ImageMetadata src = obj.getImageData("dummy-file");
    ImageMetadata newfile = obj.getImageData("dummy-file-brighten-4");

    int[][][] srcPixels = src.getRgb();
    int[][][] newPixels = newfile.getRgb();

    for (int i = 0; i < src.getHeight(); i++) {
      for (int j = 0; j < src.getWidth(); j++) {

        int[] oldPixel = new int[]{srcPixels[0][i][j], srcPixels[1][i][j], srcPixels[2][i][j]};
        int[] newPixel = new int[]{newPixels[0][i][j], newPixels[1][i][j], newPixels[2][i][j]};
        assertEquals(Math.min(oldPixel[0] + 40, 255), newPixel[0], 0.01);
        assertEquals(Math.min(oldPixel[1] + 40, 255), newPixel[1], 0.01);
        assertEquals(Math.min(oldPixel[2] + 40, 255), newPixel[2], 0.01);
      }
    }


  }


  //brightening cap at 255
  @Test
  public void brightenCap() {

    obj.doBrightening(200, "dummy-file",
        "dummy-file-brighten");
    obj.doBrightening(200, "dummy-file-brighten",
        "dummy-file-brighten-2");
    obj.doBrightening(200, "dummy-file-brighten-2",
        "dummy-file-brighten-3");
    obj.doBrightening(200, "dummy-file-brighten-3",
        "dummy-file-brighten-4");

    ImageMetadata src = obj.getImageData("dummy-file");
    ImageMetadata newfile = obj.getImageData("dummy-file-brighten-4");

    int[][][] srcPixels = src.getRgb();
    int[][][] newPixels = newfile.getRgb();

    for (int i = 0; i < src.getHeight(); i++) {
      for (int j = 0; j < src.getWidth(); j++) {

        int[] oldPixel = new int[]{srcPixels[0][i][j], srcPixels[1][i][j], srcPixels[2][i][j]};
        int[] newPixel = new int[]{newPixels[0][i][j], newPixels[1][i][j], newPixels[2][i][j]};

        assertEquals(Math.min(oldPixel[0] + 800, 255), newPixel[0], 0.01);
        assertEquals(Math.min(oldPixel[1] + 800, 255), newPixel[1], 0.01);
        assertEquals(Math.min(oldPixel[2] + 800, 255), newPixel[2], 0.01);
      }
    }


  }


  @Test
  public void darkenCap() {

    obj.doBrightening(-200, "dummy-file",
        "dummy-file-brighten");
    obj.doBrightening(-200, "dummy-file-brighten",
        "dummy-file-brighten-2");
    obj.doBrightening(-200, "dummy-file-brighten-2",
        "dummy-file-brighten-3");
    obj.doBrightening(-200, "dummy-file-brighten-3",
        "dummy-file-brighten-4");

    ImageMetadata src = obj.getImageData("dummy-file");
    ImageMetadata newfile = obj.getImageData("dummy-file-brighten-4");

    int[][][] srcPixels = src.getRgb();
    int[][][] newPixels = newfile.getRgb();

    for (int i = 0; i < src.getHeight(); i++) {
      for (int j = 0; j < src.getWidth(); j++) {

        int[] oldPixel = new int[]{srcPixels[0][i][j], srcPixels[1][i][j], srcPixels[2][i][j]};
        int[] newPixel = new int[]{newPixels[0][i][j], newPixels[1][i][j], newPixels[2][i][j]};

        assertEquals(Math.max(oldPixel[0] - 800, 0), newPixel[0], 0.01);
        assertEquals(Math.max(oldPixel[1] - 800, 0), newPixel[1], 0.01);
        assertEquals(Math.max(oldPixel[2] - 800, 0), newPixel[2], 0.01);
      }
    }


  }

  //multiple darken
  //brightening multiple times
  @Test
  public void darkenMultiple() {

    obj.doBrightening(-10, "dummy-file",
        "dummy-file-brighten");
    obj.doBrightening(-10, "dummy-file-brighten",
        "dummy-file-brighten-2");
    obj.doBrightening(-10, "dummy-file-brighten-2",
        "dummy-file-brighten-3");
    obj.doBrightening(-10, "dummy-file-brighten-3",
        "dummy-file-brighten-4");

    ImageMetadata src = obj.getImageData("dummy-file");
    ImageMetadata newfile = obj.getImageData("dummy-file-brighten-4");
    int[][][] srcPixels = src.getRgb();
    int[][][] newPixels = newfile.getRgb();

    for (int i = 0; i < src.getHeight(); i++) {
      for (int j = 0; j < src.getWidth(); j++) {

        int[] oldPixel = new int[]{srcPixels[0][i][j], srcPixels[1][i][j], srcPixels[2][i][j]};
        int[] newPixel = new int[]{newPixels[0][i][j], newPixels[1][i][j], newPixels[2][i][j]};

        assertEquals(Math.max(oldPixel[0] - 40, 0), newPixel[0], 0.01);
        assertEquals(Math.max(oldPixel[1] - 40, 0), newPixel[1], 0.01);
        assertEquals(Math.max(oldPixel[2] - 40, 0), newPixel[2], 0.01);
      }
    }


  }


  // Edge case- when one of the rgb is 0, it cannot be darkened. In this case,
  // darkened and brightened image are not same values.
  @Test
  public void brightenDarkEqualAmountEdgeCase() {

    obj.addImage("dummy-file", image);


    obj.doBrightening(-10, "dummy-file",
        "dummy-file-dark");


    obj.doBrightening(10, "dummy-file-dark",
        "dummy-file-bright");

    ImageMetadata src = obj.getImageData("dummy-file");
    ImageMetadata newfile = obj.getImageData("dummy-file-bright");
    int[][][] srcPixels = src.getRgb();
    int[][][] newPixels = newfile.getRgb();


    for (int i = 0; i < src.getHeight(); i++) {
      for (int j = 0; j < src.getWidth(); j++) {
        assertEquals(srcPixels[0][i][j], newPixels[0][i][j], 10);
        assertEquals(srcPixels[1][i][j], newPixels[1][i][j], 10);
        assertEquals(srcPixels[2][i][j], newPixels[2][i][j], 10);
      }
    }
  }


  //brighten 10 darken -10 image remains same
  @Test
  public void brightenDarkEqualAmount() {

    int[][][] pixelmap = {
        {{255, 115, 10}, {255, 115, 10}, {255, 115, 10}},
        {{255, 115, 10}, {255, 115, 10}, {255, 115, 10}},
        {{20, 10, 10}, {20, 10, 10}, {20, 10, 10}}
    };


    BufferedImage image = createBufferImage(3, 3, pixelmap);
    obj.addImage("dummy-file", image);


    obj.doBrightening(-10, "dummy-file",
        "dummy-file-dark");


    obj.doBrightening(10, "dummy-file-dark",
        "dummy-file-bright");

    ImageMetadata src = obj.getImageData("dummy-file");
    ImageMetadata newfile = obj.getImageData("dummy-file-bright");
    int[][][] srcPixels = src.getRgb();
    int[][][] newPixels = newfile.getRgb();

    for (int i = 0; i < src.getHeight(); i++) {
      for (int j = 0; j < src.getWidth(); j++) {

        int[] oldPixel = new int[]{srcPixels[0][i][j], srcPixels[1][i][j], srcPixels[2][i][j]};
        int[] newPixel = new int[]{newPixels[0][i][j], newPixels[1][i][j], newPixels[2][i][j]};

        assertEquals(oldPixel[0], newPixel[0]);
        assertEquals(oldPixel[1], newPixel[1]);
        assertEquals(oldPixel[2], newPixel[2]);
      }
    }
  }

  @Test
  public void flipImageH() {
    obj = new ImageProcessingModelImpl(out);


    // Create a new image for flipping
    int[][][] pixelmap = {
        {{20, 115, 0}, {43, 99, 0}, {123, 54, 23}},
        {{67, 115, 0}, {88, 10, 0}, {56, 115, 71}},
        {{45, 10, 0}, {255, 115, 78}, {20, 115, 34}}
    };


    int[][][] expected = {
        {{0, 115, 20}, {0, 99, 43}, {23, 54, 123}},
        {{0, 115, 67}, {0, 10, 88}, {71, 115, 56}},
        {{0, 10, 45}, {78, 115, 255}, {34, 115, 20}}
    };
    BufferedImage image = createBufferImage(3, 3, pixelmap);
    obj.addImage("dummy-file", image);

    obj.doFlip("horizontal-flip", "dummy-file",
        "dummy-file-flip-h");

    // Expected flipping


    ImageMetadata src = obj.getImageData("dummy-file");
    ImageMetadata newfile = obj.getImageData("dummy-file-flip-h");

    int[][][] newPixels = newfile.getRgb();

    for (int i = 0; i < pixelmap.length; i++) {
      for (int j = 0; j < pixelmap[0].length; j++) {

        assertEquals(expected[0][i][j], newPixels[0][i][j]);
        assertEquals(expected[1][i][j], newPixels[1][i][j]);
        assertEquals(expected[2][i][j], newPixels[2][i][j]);

      }
    }
  }


  @Test
  public void flipImageV() {
    obj = new ImageProcessingModelImpl(out);

    // Create a new image for flipping
    int[][][] pixelmap = {
        {{20, 115, 0}, {43, 99, 0}, {123, 54, 23}},
        {{67, 115, 0}, {88, 10, 0}, {56, 115, 71}},
        {{45, 10, 0}, {255, 115, 78}, {20, 115, 34}}
    };

    // Expected flipping
    int[][][] expected = {
        {{123, 54, 23}, {43, 99, 0}, {20, 115, 0}},
        {{56, 115, 71}, {88, 10, 0}, {67, 115, 0}},
        {{20, 115, 34}, {255, 115, 78}, {45, 10, 0}}
    };


    BufferedImage image = createBufferImage(3, 3, pixelmap);
    obj.addImage("dummy-file", image);

    obj.doFlip("vertical-flip", "dummy-file",
        "dummy-file-flip-v");


    ImageMetadata newfile = obj.getImageData("dummy-file-flip-v");
    int[][][] newPixel = newfile.getRgb();

    for (int i = 0; i < pixelmap.length; i++) {
      for (int j = 0; j < pixelmap[0].length; j++) {
        assertEquals(expected[0][i][j], newPixel[0][i][j]);

        assertEquals(expected[1][i][j], newPixel[1][i][j]);
        assertEquals(expected[2][i][j], newPixel[2][i][j]);

      }
    }
  }

  //flip 2 horizontal, 2 vertical leads to original image.
  @Test
  public void flipHflipHflipVflipV() {
    obj = new ImageProcessingModelImpl(out);

    // Create a new image for flipping
    int[][][] pixelmap = {
        {{20, 115, 0}, {43, 99, 0}, {123, 54, 23}},
        {{67, 115, 0}, {88, 10, 0}, {56, 115, 71}},
        {{45, 10, 0}, {255, 115, 78}, {20, 115, 34}}
    };


    BufferedImage image = createBufferImage(3, 3, pixelmap);
    obj.addImage("dummy-file", image);

    obj.doFlip("horizontal-flip", "dummy-file",
        "dummy-file-flip-h");
    obj.doFlip("horizontal-flip", "dummy-file-flip-h",
        "dummy-file-flip-hh");
    obj.doFlip("vertical-flip", "dummy-file-flip-hh",
        "dummy-file-flip-v");
    obj.doFlip("vertical-flip", "dummy-file-flip-v",
        "dummy-file-flip-vv");


    int[][][] expected = {
        {{20, 115, 0}, {43, 99, 0}, {123, 54, 23}},
        {{67, 115, 0}, {88, 10, 0}, {56, 115, 71}},
        {{45, 10, 0}, {255, 115, 78}, {20, 115, 34}}
    };

    ImageMetadata newfile = obj.getImageData("dummy-file-flip-vv");
    int[][][] newP = newfile.getRgb();


    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {

        assertEquals(expected[0][i][j], newP[0][i][j]);
        assertEquals(expected[1][i][j], newP[1][i][j]);
        assertEquals(expected[2][i][j], newP[2][i][j]);
      }
    }


  }

  //multiple v flip
  @Test
  public void flipVMultiple() {
    obj = new ImageProcessingModelImpl(out);

    // Create a new image for flipping
    int[][][] pixelmap = {
        {{20, 115, 0}, {43, 99, 0}, {123, 54, 23}},
        {{67, 115, 0}, {88, 10, 0}, {56, 115, 71}},
        {{45, 10, 0}, {255, 115, 78}, {20, 115, 34}}
    };


    BufferedImage image = createBufferImage(3, 3, pixelmap);
    obj.addImage("dummy-file", image);

    obj.doFlip("vertical-flip", "dummy-file",
        "dummy-file-flip-v");
    obj.doFlip("vertical-flip", "dummy-file-flip-v",
        "dummy-file-flip-vv");
    obj.doFlip("vertical-flip", "dummy-file-flip-vv",
        "dummy-file-flip-vvv");
    obj.doFlip("vertical-flip", "dummy-file-flip-vvv",
        "dummy-file-flip-vvvv");


    int[][][] expected = {
        {{20, 115, 0}, {43, 99, 0}, {123, 54, 23}},
        {{67, 115, 0}, {88, 10, 0}, {56, 115, 71}},
        {{45, 10, 0}, {255, 115, 78}, {20, 115, 34}}
    };

    ImageMetadata newfile = obj.getImageData("dummy-file-flip-vvvv");
    int[][][] newP = newfile.getRgb();


    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {

        assertEquals(expected[0][i][j], newP[0][i][j]);
        assertEquals(expected[1][i][j], newP[1][i][j]);
        assertEquals(expected[2][i][j], newP[2][i][j]);
      }
    }


  }

  //multiple h flip
  @Test
  public void flipHMultiple() {
    obj = new ImageProcessingModelImpl(out);

    // Create a new image for flipping
    int[][][] pixelmap = {
        {{20, 115, 0}, {43, 99, 0}, {123, 54, 23}},
        {{67, 115, 0}, {88, 10, 0}, {56, 115, 71}},
        {{45, 10, 0}, {255, 115, 78}, {20, 115, 34}}
    };

    BufferedImage image = createBufferImage(3, 3, pixelmap);
    obj.addImage("dummy-file", image);

    obj.doFlip("horizontal-flip", "dummy-file",
        "dummy-file-flip-h");
    obj.doFlip("horizontal-flip", "dummy-file-flip-h",
        "dummy-file-flip-hh");
    obj.doFlip("horizontal-flip", "dummy-file-flip-hh",
        "dummy-file-flip-hhh");
    obj.doFlip("horizontal-flip", "dummy-file-flip-hhh",
        "dummy-file-flip-hhhh");


    ImageMetadata newfile = obj.getImageData("dummy-file-flip-hhhh");
    int[][][] expected = {
        {{20, 115, 0}, {43, 99, 0}, {123, 54, 23}},
        {{67, 115, 0}, {88, 10, 0}, {56, 115, 71}},
        {{45, 10, 0}, {255, 115, 78}, {20, 115, 34}}
    };

    int[][][] newP = newfile.getRgb();

    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {

        assertEquals(expected[0][i][j], newP[0][i][j]);
        assertEquals(expected[1][i][j], newP[1][i][j]);
        assertEquals(expected[2][i][j], newP[2][i][j]);
      }
    }


  }

  //flipping H doesnot change size of image
  @Test
  public void flipImageHDimensions() {
    obj = new ImageProcessingModelImpl(out);

    // Create a new image for flipping
    int[][][] pixelmap = {
        {{20, 115, 0}, {43, 99, 0}, {123, 54, 23}},
        {{67, 115, 0}, {88, 10, 0}, {56, 115, 71}},
        {{45, 10, 0}, {255, 115, 78}, {20, 115, 34}}
    };


    BufferedImage image = createBufferImage(3, 3, rbgMap);
    obj.addImage("dummy-file", image);

    obj.doFlip("horizontal-flip", "dummy-file",
        "dummy-file-flip-h");

    // Verify the dimensions of the retrieved image
    assertEquals(3, obj.getImageData("dummy-file-flip-h").getWidth());
    assertEquals(3, obj.getImageData("dummy-file-flip-h").getWidth());
  }


  //flipping V doesnot change size of image
  @Test
  public void flipImageVDimensions() {
    obj = new ImageProcessingModelImpl(out);

    // Create a new image for flipping
    int[][][] pixelmap = {
        {{20, 115, 0}, {43, 99, 0}, {123, 54, 23}},
        {{67, 115, 0}, {88, 10, 0}, {56, 115, 71}},
        {{45, 10, 0}, {255, 115, 78}, {20, 115, 34}}
    };


    BufferedImage image = createBufferImage(3, 3, rbgMap);
    obj.addImage("dummy-file", image);

    obj.doFlip("vertical-flip", "dummy-file",
        "dummy-file-flip-v");

    // Verify the dimensions of the retrieved image
    assertEquals(3, obj.getImageData("dummy-file-flip-v").getWidth());
    assertEquals(3, obj.getImageData("dummy-file-flip-v").getWidth());
  }


  // flipping multiple times doesnot change size of image
  @Test
  public void multipleFlipImageDimensions() {
    obj = new ImageProcessingModelImpl(out);

    // Create a new image for flipping
    int[][][] pixelmap = {
        {{20, 115, 0}, {43, 99, 0}, {123, 54, 23}},
        {{67, 115, 0}, {88, 10, 0}, {56, 115, 71}},
        {{45, 10, 0}, {255, 115, 78}, {20, 115, 34}}
    };


    BufferedImage image = createBufferImage(3, 3, rbgMap);
    obj.addImage("dummy-file", image);

    obj.doFlip("horizontal-flip",
        "dummy-file", "dummy-file-flip-h");
    obj.doFlip("horizontal-flip",
        "dummy-file-flip-h", "dummy-file-flip-hh");
    obj.doFlip("vertical-flip",
        "dummy-file-flip-hh", "dummy-file-flip-v");
    obj.doFlip("vertical-flip",
        "dummy-file-flip-v", "dummy-file-flip-vv");


    assertEquals(3, obj.getImageData("dummy-file-flip-vv").getWidth());
    assertEquals(3, obj.getImageData("dummy-file-flip-vv").getHeight());


  }


  //blur
  @Test
  public void blurImage() {


    obj.blurSharpenImage("blur", "dummy-file",
        "dummy-file-blur");

    ImageMetadata src = obj.getImageData("dummy-file");
    ImageMetadata newfile = obj.getImageData("dummy-file-blur");

    int[][][] newP = newfile.getRgb();


    assertEquals(121, newP[0][1][1]);
    assertEquals(121, newP[1][1][1]);
    assertEquals(10, newP[2][1][1]);

  }

  //multiple blur
  @Test
  public void blurMultiple() {

    obj.blurSharpenImage("blur", "dummy-file",
        "dummy-file-blur");

    obj.blurSharpenImage("blur", "dummy-file-blur",
        "dummy-file-blurred");

    ImageMetadata newfile = obj.getImageData("dummy-file-blurred");
    int[][][] blurPixelValue = newfile.getRgb();


    assertEquals(30, blurPixelValue[0][1][1], 0.01);
    assertEquals(30, blurPixelValue[1][1][1], 0.01);
    assertEquals(2, blurPixelValue[2][1][1], 0.01);


  }

  //blur doesn't change size of image
  @Test
  public void blurDimensionRemainSame() {

    obj.blurSharpenImage("blur", "dummy-file",
        "dummy-file-blur");

    obj.blurSharpenImage("blur", "dummy-file-blur",
        "dummy-file-blurred");

    ImageMetadata src = obj.getImageData("dummy-file");
    ImageMetadata newfile = obj.getImageData("dummy-file-blurred");


    assertEquals(src.getWidth(), (newfile.getWidth()));
    assertEquals(src.getHeight(), (newfile.getHeight()));


  }


  //sharpen
  @Test
  public void sharpenImage() {


    int[][][] pixelmap = {
        {{255, 115, 0, 255, 115}, {255, 115, 0, 255, 115},
            {0, 255, 115, 0, 255}, {255, 115, 0, 255, 115}, {0, 255, 115, 0, 255}},
        {{255, 115, 20, 10, 8}, {0, 20, 10, 0, 255},
            {115, 0, 255, 115, 0}, {255, 115, 0, 255, 115}, {0, 255, 115, 0, 255}},
        {{0, 255, 115, 0, 255}, {115, 0, 255, 115, 0},
            {255, 115, 255, 115, 0}, {255, 115, 0, 255, 115}, {0, 255, 115, 0, 255}}
    };

    BufferedImage image = createBufferImage(5, 5, pixelmap);
    obj.addImage("dummy-file", image);

    obj.blurSharpenImage("sharpen", "dummy-file",
        "dummy-file-sharpen");

    ImageMetadata newfile = obj.getImageData("dummy-file-sharpen");
    int[][][] sharpenPixel = newfile.getRgb();

    assertEquals(68, sharpenPixel[0][2][2], 0.01);
    assertEquals(162, sharpenPixel[1][2][2], 0.01);
    assertEquals(248, sharpenPixel[2][2][2], 0.01);
  }

  //multiple sharpen

  @Test
  public void sharpenMultiple() {

    int[][][] pixelmap = {
        {{255, 115, 0, 255, 115}, {255, 115, 0, 255, 115},
            {0, 255, 115, 0, 255}, {255, 115, 0, 255, 115}, {0, 255, 115, 0, 255}},
        {{255, 115, 20, 10, 8}, {0, 20, 10, 0, 255},
            {115, 0, 255, 115, 0}, {255, 115, 0, 255, 115}, {0, 255, 115, 0, 255}},
        {{0, 255, 115, 0, 255}, {115, 0, 255, 115, 0},
            {255, 115, 255, 115, 0}, {255, 115, 0, 255, 115}, {0, 255, 115, 0, 255}}
    };


    BufferedImage image = createBufferImage(5, 5, pixelmap);
    obj.addImage("dummy-file", image);

    obj.blurSharpenImage("sharpen", "dummy-file",
        "dummy-file-sharpen");
    obj.blurSharpenImage("sharpen", "dummy-file-sharpen",
        "dummy-file-sharpen-again");
    obj.blurSharpenImage("sharpen", "dummy-file-sharpen-again",
        "dummy-file-sharpen-multiple");

    ImageMetadata newfile = obj.getImageData("dummy-file-sharpen");

    int[][][] sharpenPixel = newfile.getRgb();

    assertEquals(68, sharpenPixel[0][2][2], 0.01);
    assertEquals(162, sharpenPixel[1][2][2], 0.01);
    assertEquals(248, sharpenPixel[2][2][2], 0.01);

  }

  //sharpen doesnot change size of image
  @Test
  public void sharpenDimensionRemainSame() {

    int[][][] pixelmap = {
        {{255, 115, 0, 255, 115}, {255, 115, 0, 255, 115},
            {0, 255, 115, 0, 255}, {255, 115, 0, 255, 115}, {0, 255, 115, 0, 255}},
        {{255, 115, 20, 10, 8}, {0, 20, 10, 0, 255},
            {115, 0, 255, 115, 0}, {255, 115, 0, 255, 115}, {0, 255, 115, 0, 255}},
        {{0, 255, 115, 0, 255}, {115, 0, 255, 115, 0},
            {255, 115, 255, 115, 0}, {255, 115, 0, 255, 115}, {0, 255, 115, 0, 255}}
    };

    BufferedImage image = createBufferImage(5, 5, pixelmap);
    obj.addImage("dummy-file", image);

    obj.blurSharpenImage("sharpen", "dummy-file",
        "dummy-file-sharp");

    obj.blurSharpenImage("sharpen", "dummy-file-sharp",
        "dummy-file-sharped");

    assertEquals(obj.getImageData("dummy-file").getWidth(),
        (obj.getImageData("dummy-file-sharped").getWidth()));
    assertEquals(obj.getImageData("dummy-file").getHeight(),
        (obj.getImageData("dummy-file-sharped").getHeight()));


  }


  //blur sharpen then blur sharpen lead to original image
  @Test
  public void sharpenBlur() {

    int[][][] pixelmap = {
        {{255, 115, 0, 255, 115}, {255, 115, 0, 255, 115},
            {0, 255, 115, 0, 255}, {255, 115, 0, 255, 115}, {0, 255, 115, 0, 255}},
        {{255, 115, 20, 10, 8}, {0, 20, 10, 0, 255},
            {115, 0, 255, 115, 0}, {255, 115, 0, 255, 115}, {0, 255, 115, 0, 255}},
        {{0, 255, 115, 0, 255}, {115, 0, 255, 115, 0},
            {255, 115, 255, 115, 0}, {255, 115, 0, 255, 115}, {0, 255, 115, 0, 255}}
    };


    BufferedImage image = createBufferImage(5, 5, pixelmap);
    obj.addImage("dummy-file", image);

    obj.blurSharpenImage("sharpen", "dummy-file",
        "dummy-file-sharp");

    obj.blurSharpenImage("blur", "dummy-file-sharp",
        "dummy-file-blur");

    int[][][] expectedMap = {
        {{0, 0, 0, 0, 0}, {0, 4, 8, 4, 0},
            {0, 8, 17, 8, 0}, {0, 4, 8, 4, 0}, {0, 0, 0, 0, 0}},
        {{0, 0, 0, 0, 0}, {0, 10, 20, 10, 0},
            {0, 20, 40, 20, 0}, {0, 10, 20, 10, 0}, {0, 0, 0, 0, 0}},
        {{0, 0, 0, 0, 0}, {0, 15, 31, 15, 0},
            {0, 31, 62, 31, 0}, {0, 15, 31, 15, 0}, {0, 0, 0, 0, 0}}
    };
    ImageMetadata newfile = obj.getImageData("dummy-file-blur");

    int[][][] newP = newfile.getRgb();

    //get the RGB Values
    for (int i = 0; i < 5; i++) {
      for (int j = 0; j < 5; j++) {

        assertEquals(expectedMap[0][i][j], newP[0][i][j]);
        assertEquals(expectedMap[1][i][j], newP[1][i][j]);
        assertEquals(expectedMap[2][i][j], newP[2][i][j]);

      }
    }


  }

  //sepia
  @Test
  public void sepiaImage() {

    obj.getSepia("dummy-file",
        "dummy-file-sepia");

    ImageMetadata src = obj.getImageData("dummy-file");
    ImageMetadata newfile = obj.getImageData("dummy-file-sepia");

    int[][][] srcP = src.getRgb();
    int[][][] newP = newfile.getRgb();

    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {

        assertEquals(Math.min(255, (int) (0.393 * srcP[0][i][j]
            + 0.769 * srcP[1][i][j] + 0.189 * srcP[2][i][j])), newP[0][i][j], 0.01);
        assertEquals(Math.min(255, (int) (0.349 * srcP[0][i][j]
            + 0.686 * srcP[1][i][j] + 0.168 * srcP[2][i][j])), newP[1][i][j], 0.01);
        assertEquals(Math.min(255, (int) (0.272 * srcP[0][i][j]
            + 0.534 * srcP[1][i][j] + 0.131 * srcP[2][i][j])), newP[2][i][j], 0.01);
      }
    }
  }

  //sepia doesnot change size
  @Test
  public void sepiaImageDimensionNotChanged() {
    obj.getSepia("dummy-file",
        "dummy-file-sepia");

    ImageMetadata src = obj.getImageData("dummy-file");
    ImageMetadata newfile = obj.getImageData("dummy-file-sepia");

    assertEquals(src.getWidth(), newfile.getWidth());
    assertEquals(src.getHeight(), newfile.getHeight());
  }

  //greyscale-red component
  @Test
  public void greyscaleImageRed() {
    obj.getGreyScale("red-component",
        "dummy-file", "dummy-greyscale-red");
    ImageMetadata src = obj.getImageData("dummy-file");
    ImageMetadata newfile = obj.getImageData("dummy-greyscale-red");

    int[][][] srcP = src.getRgb();
    int[][][] newP = newfile.getRgb();

    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        srcP[1][i][j] = 0;
        srcP[2][i][j] = 0;

        assertEquals(srcP[0][i][j], newP[0][i][j]);
        assertEquals(srcP[1][i][j], newP[1][i][j]);
        assertEquals(srcP[2][i][j], newP[2][i][j]);


      }
    }


  }

  //greyscale-green component
  @Test
  public void greyscaleImageGreen() {
    obj.getGreyScale("green-component", "dummy-file",
        "dummy-greyscale-red");
    ImageMetadata src = obj.getImageData("dummy-file");
    ImageMetadata newfile = obj.getImageData("dummy-greyscale-red");

    int[][][] srcP = src.getRgb();
    int[][][] newP = newfile.getRgb();

    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        srcP[0][i][j] = 0;
        srcP[2][i][j] = 0;

        assertEquals(srcP[0][i][j], newP[0][i][j]);
        assertEquals(srcP[1][i][j], newP[1][i][j]);
        assertEquals(srcP[2][i][j], newP[2][i][j]);


      }
    }


  }


  //greyscale-blue component
  @Test
  public void greyscaleImageBlue() {
    obj.getGreyScale("blue-component",
        "dummy-file", "dummy-greyscale-red");
    ImageMetadata src = obj.getImageData("dummy-file");
    ImageMetadata newfile = obj.getImageData("dummy-greyscale-red");

    int[][][] srcP = src.getRgb();
    int[][][] newP = newfile.getRgb();

    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        srcP[0][i][j] = 0;
        srcP[1][i][j] = 0;

        assertEquals(srcP[0][i][j], newP[0][i][j]);
        assertEquals(srcP[1][i][j], newP[1][i][j]);
        assertEquals(srcP[2][i][j], newP[2][i][j]);


      }
    }


  }

  //greysscale - luma
  @Test
  public void greyscaleImageLuma() {

    obj.getGreyScale("luma-component",
        "dummy-file", "dummy-greyscale");
    ImageMetadata src = obj.getImageData("dummy-file");
    ImageMetadata newfile = obj.getImageData("dummy-greyscale");

    int[][][] srcP = src.getRgb();
    int[][][] newP = newfile.getRgb();

    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {

        assertEquals((int) ((0.299 * srcP[0][i][j])
            + (0.587 * srcP[1][i][j]) + (0.114 * srcP[2][i][j])), newP[0][i][j]);
        assertEquals((int) ((0.299 * srcP[0][i][j])
            + (0.587 * srcP[1][i][j]) + (0.114 * srcP[2][i][j])), newP[1][i][j]);
        assertEquals((int) ((0.299 * srcP[0][i][j])
            + (0.587 * srcP[1][i][j]) + (0.114 * srcP[2][i][j])), newP[2][i][j]);


      }
    }
  }

  //greysscale - value
  @Test
  public void greyscaleImageValue() {

    obj.getGreyScale("value-component",
        "dummy-file", "dummy-greyscale");
    ImageMetadata src = obj.getImageData("dummy-file");
    ImageMetadata newfile = obj.getImageData("dummy-greyscale");

    int[][][] srcP = src.getRgb();
    int[][][] newP = newfile.getRgb();

    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {

        assertEquals((int) (Math.max(Math.max(srcP[0][i][j], srcP[1][i][j]),
            srcP[2][i][j])), newP[0][i][j]);
        assertEquals((int) (Math.max(Math.max(srcP[0][i][j], srcP[1][i][j]),
            srcP[2][i][j])), newP[1][i][j]);
        assertEquals((int) (Math.max(Math.max(srcP[0][i][j], srcP[1][i][j]),
            srcP[2][i][j])), newP[2][i][j]);


      }
    }
  }

  //greysscale - intensity
  @Test
  public void greyscaleImageIntensity() {

    obj.getGreyScale("intensity-component",
        "dummy-file", "dummy-greyscale");
    ImageMetadata src = obj.getImageData("dummy-file");
    ImageMetadata newfile = obj.getImageData("dummy-greyscale");

    int[][][] srcP = src.getRgb();
    int[][][] newP = newfile.getRgb();

    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {


        assertEquals((int) ((srcP[0][i][j] + srcP[1][i][j]
            + srcP[2][i][j]) / 3), newP[0][i][j]);
        assertEquals((int) ((srcP[0][i][j] + srcP[1][i][j]
            + srcP[2][i][j]) / 3), newP[1][i][j]);
        assertEquals((int) ((srcP[0][i][j] + srcP[1][i][j]
            + srcP[2][i][j]) / 3), newP[2][i][j]);


      }
    }
  }


  //greyscale operation does not change size.
  @Test
  public void greyscaleImageDimensionNotChanged() {

    obj.getGreyScale("intensity-component", "dummy-file",
        "dummy-greyscale");
    ImageMetadata src = obj.getImageData("dummy-file");
    ImageMetadata newfile = obj.getImageData("dummy-greyscale");

    assertEquals(src.getWidth(), newfile.getWidth());
    assertEquals(src.getHeight(), newfile.getHeight());

  }

  //all the images split have same size.
  @Test
  public void rgbSplitDimensions() {
    obj.rgbSplit("dummy-file", "dummy-red",
        "dummy-green",
        "dummy-blue");
    ImageMetadata src = obj.getImageData("dummy-file");

    ImageMetadata newfileRed = obj.getImageData("dummy-red");
    ImageMetadata newfileGreen = obj.getImageData("dummy-green");
    ImageMetadata newfileBlue = obj.getImageData("dummy-blue");

    assertEquals(src.getWidth(), newfileRed.getWidth());
    assertEquals(src.getWidth(), newfileBlue.getWidth());
    assertEquals(src.getWidth(), newfileGreen.getWidth());

    assertEquals(src.getHeight(), newfileRed.getHeight());
    assertEquals(src.getWidth(), newfileBlue.getHeight());
    assertEquals(src.getWidth(), newfileGreen.getHeight());


  }


  @Test
  public void rgbSplitTest() {
    obj.rgbSplit("dummy-file", "dummy-red",
        "dummy-green",
        "dummy-blue");
    ImageMetadata src = obj.getImageData("dummy-file");

    ImageMetadata newfileRed = obj.getImageData("dummy-red");
    ImageMetadata newfileGreen = obj.getImageData("dummy-green");
    ImageMetadata newfileBlue = obj.getImageData("dummy-blue");

    int[][][] srcP = src.getRgb();
    int[][][] newRed = newfileRed.getRgb();
    int[][][] newGreen = newfileGreen.getRgb();
    int[][][] newBlue = newfileBlue.getRgb();

    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {

        assertEquals(srcP[0][i][j], newRed[0][i][j]);
        assertEquals(0, newRed[1][i][j]);
        assertEquals(0, newRed[2][i][j]);

        assertEquals(0, newGreen[0][i][j]);
        assertEquals(srcP[1][i][j], newGreen[1][i][j]);
        assertEquals(0, newGreen[2][i][j]);

        assertEquals(0, newBlue[0][i][j]);
        assertEquals(0, newBlue[1][i][j]);
        assertEquals(srcP[2][i][j], newBlue[2][i][j]);

      }

    }

  }

  //rgb combine
  @Test
  public void rgbCombineTest() {
    obj.rgbSplit("dummy-file", "dummy-red",
        "dummy-green",
        "dummy-blue");
    ImageMetadata src = obj.getImageData("dummy-file");
    ImageMetadata newfileRed = obj.getImageData("dummy-red");
    ImageMetadata newfileGreen = obj.getImageData("dummy-green");
    ImageMetadata newfileBlue = obj.getImageData("dummy-blue");

    obj.rbgCombine("dummy-combine",
        "dummy-red", "dummy-green",
        "dummy-blue");

    int[][][] srcP = src.getRgb();
    int[][][] newRed = newfileRed.getRgb();
    int[][][] newGreen = newfileGreen.getRgb();
    int[][][] newBlue = newfileBlue.getRgb();

    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {

        assertEquals(srcP[0][i][j], newRed[0][i][j]);
        assertEquals(srcP[1][i][j], newGreen[1][i][j]);
        assertEquals(srcP[2][i][j], newBlue[2][i][j]);

      }

    }
  }

  @Test
  public void testFilenameNotFound_getImage() {
    String filename = "random-file";
    obj.getImage(filename);
    assertEquals(filename + " not present in the application.\n", out.toString());
  }

  @Test
  public void testFilenameNotFound_getGreyScale() {
    String filename = "red-file";
    obj.getGreyScale("red-component", filename, "dummy-red");
    assertEquals(filename + " not present in the application.\n", out.toString());
  }

  @Test
  public void testFilenameNotFound_doFlip() {
    String filename = "vertical-file";

    obj.doFlip("vertical-flip", filename, "dummy-flip");
    assertEquals(filename + " not present in the application.\n", out.toString());
  }

  @Test
  public void testFilenameNotFound_doBrightening() {
    String filename = "brighten-file";
    obj.doBrightening(10, filename, "dummy-img");
    assertEquals(filename + " not present in the application.\n", out.toString());
  }

  @Test
  public void testFilenameNotFound_rgbSplit() {
    String filename = "rgbSplit-file";
    obj.rgbSplit(filename, "red",
        "green", "blue");
    assertEquals(filename + " not present in the application.\n", out.toString());
  }

  @Test
  public void testFilenameNotFound_rbgCombine() {
    String filename = "rgb-file";
    obj.rbgCombine("d", filename, filename, filename);
    assertEquals(filename + " not present in the application.\n", out.toString());
  }

  @Test
  public void testFilenameNotFound_blurSharpenImage() {
    String filename = "blue-file";

    obj.blurSharpenImage("blur", filename, "dummy-flip");
    assertEquals(filename + " not present in the application.\n", out.toString());
  }

  @Test
  public void testFilenameNotFound_getSepia() {
    String filename = "sepia-file";
    obj.getSepia(filename, "dummy-sepia");
    assertEquals(filename + " not present in the application.\n", out.toString());
  }

  @Test
  public void test_overwriting_existing_image() {
    //first image with name Test_Image
    assertTrue(obj.addImage("Test_Image", image));

    ImageMetadata imageMetadata = obj.getImageData("Test_Image");
    assertEquals(3, imageMetadata.getHeight());
    assertEquals(3, imageMetadata.getWidth());

    int[][][] pixelVal = imageMetadata.getRgb();


    for (int i = 0; i < imageMetadata.getHeight(); i++) {
      for (int j = 0; j < imageMetadata.getWidth(); j++) {
        assertEquals(rbgMap[0][i][j], pixelVal[0][i][j]);
        assertEquals(rbgMap[1][i][j], pixelVal[1][i][j]);
        assertEquals(rbgMap[2][i][j], pixelVal[2][i][j]);
      }
    }


    // adding new Image with same name
    int[][][] pixelmap = {
        // Red, Green, Blue
        {{255, 0, 0}, {0, 255, 0}, {0, 0, 255}},
        // Yellow, Cyan, Magenta
        {{255, 255, 0}, {0, 255, 255}, {255, 0, 255}},
        // Purple, Light Orange, Light Green
        {{128, 64, 192}, {192, 128, 64}, {64, 192, 128}}
    };
    HashMap<String, int[]> newMap = new HashMap<>();
    for (int i = 0; i < pixelmap.length; i++) {
      for (int j = 0; j < pixelmap[0].length; j++) {
        newMap.put(i + " " + j, pixelmap[i][j]);
      }
    }
    assertTrue(obj.addImage("Test_Image", image));

    ImageMetadata newMetadata = obj.getImageData("Test_Image");
    assertEquals(3, newMetadata.getHeight());
    assertEquals(3, newMetadata.getWidth());


  }

  private BufferedImage createBufferImage(int width, int height, int[][][] rgb) {
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {

        int red = rgb[0][i][j];
        int green = rgb[1][i][j];
        int blue = rgb[2][i][j];

        int pixelValue = (255 << 24) | (red << 16) | (green << 8) | blue;
        image.setRGB(j, i, pixelValue);
      }
    }
    return image;
  }


  //check compression working fine
  @Test
  public void compression() {
    obj.compress(40, "dummy-file", "dummy-compress");
    ImageMetadata src = obj.getImageData("dummy-file");
    ImageMetadata newfile = obj.getImageData("dummy-compress");

    int[][][] srcP = src.getRgb();
    int[][][] newP = newfile.getRgb();

    int[][][] expected = {
        {{237, 132, 0, 0}, {237, 132, 0, 0}, {237, 132, 0, 0}, {237, 132, 0, 0}},
        {{237, 132, 0, 0}, {237, 132, 0, 0}, {237, 132, 0, 0}, {237, 132, 0, 0}},
        {{18, 11, 0, 0}, {18, 11, 0, 0}, {18, 11, 0, 0}, {18, 11, 0, 0}}
    };

    for (int i = 0; i < newfile.getWidth(); i++) {
      for (int j = 0; j < newfile.getHeight(); j++) {

        assertEquals(expected[0][i][j], newP[0][i][j]);
        assertEquals(expected[1][i][j], newP[1][i][j]);
        assertEquals(expected[2][i][j], newP[2][i][j]);
      }
    }
  }

  //test file not found- compress
  @Test
  public void test_file_not_found_compress() {
    String filename = "compressed-file";

    obj.compress(20, filename, "dummy-compressing");
    assertEquals(filename + " not present in the application.\n", out.toString());
  }

  //compress 0 should give original image
  @Test
  public void compressionZero() {
    obj.compress(0, "dummy-file", "dummy-compress");
    ImageMetadata src = obj.getImageData("dummy-file");
    ImageMetadata newfile = obj.getImageData("dummy-compress");

    int[][][] srcP = src.getRgb();
    int[][][] newP = newfile.getRgb();

    for (int i = 0; i < newfile.getWidth(); i++) {
      for (int j = 0; j < newfile.getHeight(); j++) {

        assertEquals(srcP[0][i][j], newP[0][i][j]);
        assertEquals(srcP[1][i][j], newP[1][i][j]);
        assertEquals(srcP[2][i][j], newP[2][i][j]);
      }
    }


  }

  //compress 100 should give 0 0 0 values
  @Test
  public void compressionHundred() {
    obj.compress(100, "dummy-file",
        "dummy-compress");
    ImageMetadata src = obj.getImageData("dummy-file");
    ImageMetadata newfile = obj.getImageData("dummy-compress");

    int[][][] srcP = src.getRgb();
    int[][][] newP = newfile.getRgb();

    int[][][] expected = {
        {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}},
        {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}},
        {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}}
    };

    for (int i = 0; i < newfile.getWidth(); i++) {
      for (int j = 0; j < newfile.getHeight(); j++) {

        assertEquals(expected[0][i][j], newP[0][i][j]);
        assertEquals(expected[1][i][j], newP[1][i][j]);
        assertEquals(expected[2][i][j], newP[2][i][j]);
      }
    }


  }

  //multiple compress
  @Test
  public void multipleCompress() {
    obj.compress(40, "dummy-file",
        "dummy-compress");
    obj.compress(0, "dummy-compress",
        "dummy-compressed");
    obj.compress(1, "dummy-compressed",
        "dummy-multiple-compress");


    ImageMetadata src = obj.getImageData("dummy-file");
    ImageMetadata newfile = obj.getImageData("dummy-multiple-compress");

    int[][][] srcP = src.getRgb();
    int[][][] newP = newfile.getRgb();

    int[][][] expected = {
        {{236, 132, 0,}, {236, 132, 0}, {236, 132, 0}, {236, 132, 0}},
        {{236, 132, 0}, {236, 132, 0}, {236, 132, 0}, {236, 132, 0}},
        {{17, 10, 0}, {17, 10, 0}, {17, 10, 0}, {17, 10, 0}}
    };

    for (int i = 0; i < newfile.getWidth(); i++) {
      for (int j = 0; j < newfile.getHeight(); j++) {

        assertEquals(expected[0][i][j], newP[0][i][j]);
        assertEquals(expected[1][i][j], newP[1][i][j]);
        assertEquals(expected[2][i][j], newP[2][i][j]);
      }
    }

  }


  //apply filters then compress 0
  @Test
  public void filtersAndCompress() {
    obj.getGreyScale("luma-component",
        "dummy-file", "dummy-greyscale");
    obj.compress(0, "dummy-greyscale",
        "dummy-compressed");

    ImageMetadata src = obj.getImageData("dummy-file");
    ImageMetadata newfile = obj.getImageData("dummy-compressed");

    int[][][] srcP = src.getRgb();
    int[][][] newP = newfile.getRgb();

    for (int i = 0; i < newfile.getWidth(); i++) {
      for (int j = 0; j < newfile.getHeight(); j++) {

        assertEquals((int) ((0.299 * srcP[0][i][j])
            + (0.587 * srcP[1][i][j])
            + (0.114 * srcP[2][i][j])), newP[0][i][j], 0);
        assertEquals((int) ((0.299 * srcP[0][i][j])
            + (0.587 * srcP[1][i][j])
            + (0.114 * srcP[2][i][j])), newP[1][i][j], 0);
        assertEquals((int) ((0.299 * srcP[0][i][j])
            + (0.587 * srcP[1][i][j])
            + (0.114 * srcP[2][i][j])), newP[2][i][j], 0);


      }
    }


  }

  //apply filter then compress 40
  @Test
  public void applyFilterThenCompress() {
    obj.getGreyScale("luma-component",
        "dummy-file", "dummy-greyscale");
    obj.compress(40, "dummy-greyscale",
        "dummy-compressed");

    ImageMetadata src = obj.getImageData("dummy-file");
    ImageMetadata newfile = obj.getImageData("dummy-compressed");

    int[][][] srcP = src.getRgb();
    int[][][] newP = newfile.getRgb();

    int[][][] expected = {
        {{170, 77, 0}, {170, 77, 0}, {253, 159, 0}, {88, 0, 0}},
        {{170, 77, 0}, {170, 77, 0}, {253, 159, 0}, {88, 0, 0}},
        {{170, 77, 0}, {170, 77, 0}, {253, 159, 0}, {88, 0, 0}},
    };

    for (int i = 0; i < newfile.getWidth(); i++) {
      for (int j = 0; j < newfile.getHeight(); j++) {

        assertEquals(expected[0][i][j], newP[0][i][j]);
        assertEquals(expected[1][i][j], newP[1][i][j]);
        assertEquals(expected[2][i][j], newP[2][i][j]);
      }
    }


  }

  //compress does not change the dimension of the original image
  @Test
  public void compressDoesNotChangeDimensions() {
    obj.compress(40, "dummy-file", "dummy-compressed");

    ImageMetadata src = obj.getImageData("dummy-file");
    ImageMetadata newfile = obj.getImageData("dummy-compressed");

    assertEquals(src.getWidth(), newfile.getWidth());
    assertEquals(src.getHeight(), newfile.getHeight());

  }


  //Color correction doesnot change the size
  @Test
  public void colorCorrectionDimension() {
    obj.colorCorrection("dummy-file",
        "dummy-color-corrected");

    ImageMetadata src = obj.getImageData("dummy-file");
    ImageMetadata newfile = obj.getImageData("dummy-color-corrected");

    assertEquals(src.getHeight(), newfile.getHeight());
    assertEquals(src.getWidth(), newfile.getWidth());

  }

  //color correction apply
  @Test
  public void colorCorrectionValues() {
    int[][][] img = {
        {{10, 89}, {89, 32}},
        {{89, 22}, {56, 32}},
        {{61, 90}, {45, 78}}
    };

    image = createBufferImage(2, 2, img);
    obj.addImage("dummy-file", image);

    obj.colorCorrection("dummy-file", "dummy-color-corrected");

    ImageMetadata src = obj.getImageData("dummy-file");
    ImageMetadata newfile = obj.getImageData("dummy-color-corrected");

    int[][][] newP = newfile.getRgb();

    int[][][] expected = {
        {{0, 52}, {52, 0}},
        {{119, 52}, {86, 62}},
        {{68, 97}, {52, 85}}
    };

    for (int i = 0; i < newfile.getWidth(); i++) {
      for (int j = 0; j < newfile.getHeight(); j++) {
        {
          assertEquals(expected[0][i][j], newP[0][i][j]);
          assertEquals(expected[1][i][j], newP[1][i][j]);
          assertEquals(expected[2][i][j], newP[2][i][j]);

        }
      }
    }
  }

  //color correction apply
  @Test
  public void colorCorrectionValuesExtremeValues() {
    int[][][] img = {
        {{5, 5, 0}, {100, 50, 100}, {5, 0, 0}},
        {{250, 250, 250}, {100, 50, 100}, {250, 250, 250}},
        {{0, 0, 0}, {50, 100, 50}, {250, 255, 255}}
    };

    image = createBufferImage(3, 3, img);
    obj.addImage("dummy-file", image);

    obj.colorCorrection("dummy-file", "dummy-color-corrected");

    ImageMetadata newfile = obj.getImageData("dummy-color-corrected");

    int[][][] newP = newfile.getRgb();

    int[][][] expected = {
        {{0, 0, 0}, {83, 33, 83}, {0, 0, 0}},
        {{233, 233, 233}, {83, 33, 83}, {233, 233, 233}},
        {{33, 33, 33}, {83, 133, 83}, {255, 255, 255}}
    };

    for (int i = 0; i < newfile.getWidth(); i++) {
      for (int j = 0; j < newfile.getHeight(); j++) {
        {
          assertEquals(expected[0][i][j], newP[0][i][j]);
          assertEquals(expected[1][i][j], newP[1][i][j]);
          assertEquals(expected[2][i][j], newP[2][i][j]);

        }
      }
    }
  }

  //file not found - color correction
  @Test
  public void test_file_not_found_colorCorrect() {
    String filename = "color-corrected-file";

    obj.colorCorrection(filename, "final-color-corrected");
    assertEquals(filename
        + " not present in the application.\n", out.toString());
  }


  //preview blur
  @Test
  public void previewBlur() {

    rbgMap = new int[][][]{
        {{255, 115, 6}, {255, 115, 6}, {255, 115, 6}},
        {{255, 115, 6}, {255, 115, 6}, {255, 115, 6}},
        {{20, 10, 6}, {20, 10, 6}, {20, 10, 6}}
    };


    image = createBufferImage(3, 3, rbgMap);
    obj.addImage("dummy-file", image);

    obj.blurSharpenImage("blur", "dummy-file",
        "dummy-file-blur");

    obj.operationPreview(50, "dummy-file",
        "dummy-file-blur");

    ImageMetadata src = obj.getImageData("dummy-file");
    ImageMetadata blurred = obj.getImageData("dummy-file-blur");
    ImageMetadata preview = obj.getImageData("dummy-file-blur");

    int[][][] srcP = src.getRgb();
    int[][][] blurredP = blurred.getRgb();

    int[][][] previewP = preview.getRgb();


    for (int i = 0; i < preview.getHeight(); i++) {
      for (int j = 0; j < preview.getWidth(); j++) {
        if (j < 2) {
          assertEquals(blurredP[0][i][j], previewP[0][i][j]);
          assertEquals(blurredP[1][i][j], previewP[1][i][j]);
          assertEquals(blurredP[2][i][j], previewP[2][i][j]);
        } else {
          assertEquals(srcP[0][i][j], previewP[0][i][j]);
          assertEquals(srcP[1][i][j], previewP[1][i][j]);
          assertEquals(srcP[2][i][j], previewP[2][i][j]);
        }
      }

    }
  }


  //preview sepia
  @Test
  public void previewSepia() {

    rbgMap = new int[][][]{
        {{255, 115, 6}, {255, 115, 6}, {255, 115, 6}},
        {{255, 115, 6}, {255, 115, 6}, {255, 115, 6}},
        {{20, 10, 6}, {20, 10, 6}, {20, 10, 6}}
    };


    image = createBufferImage(3, 3, rbgMap);
    obj.addImage("dummy-file", image);

    obj.getSepia("dummy-file", "dummy-sepia");

    obj.operationPreview(50, "dummy-file",
        "dummy-sepia");

    ImageMetadata src = obj.getImageData("dummy-file");
    ImageMetadata sepia = obj.getImageData("dummy-sepia");
    ImageMetadata preview = obj.getImageData("dummy-sepia");

    int[][][] srcP = src.getRgb();
    int[][][] newP = sepia.getRgb();

    int[][][] previewP = preview.getRgb();


    for (int i = 0; i < preview.getHeight(); i++) {
      for (int j = 0; j < preview.getWidth(); j++) {
        if (j < 2) {
          assertEquals(newP[0][i][j], previewP[0][i][j]);
          assertEquals(newP[1][i][j], previewP[1][i][j]);
          assertEquals(newP[2][i][j], previewP[2][i][j]);
        } else {
          assertEquals(srcP[0][i][j], previewP[0][i][j]);
          assertEquals(srcP[1][i][j], previewP[1][i][j]);
          assertEquals(srcP[2][i][j], previewP[2][i][j]);
        }
      }

    }
  }


  //preview sharpen
  @Test
  public void previewSharpen() {

    rbgMap = new int[][][]{
        {{255, 115, 6}, {255, 115, 6}, {255, 115, 6}},
        {{255, 115, 6}, {255, 115, 6}, {255, 115, 6}},
        {{20, 10, 6}, {20, 10, 6}, {20, 10, 6}}
    };


    image = createBufferImage(3, 3, rbgMap);
    obj.addImage("dummy-file", image);

    obj.blurSharpenImage("sharpen", "dummy-file",
        "dummy-sharp");

    obj.operationPreview(50, "dummy-file",
        "dummy-sharp");

    ImageMetadata src = obj.getImageData("dummy-file");
    ImageMetadata sharp = obj.getImageData("dummy-sharp");
    ImageMetadata preview = obj.getImageData("dummy-sharp");

    int[][][] srcP = src.getRgb();
    int[][][] newP = sharp.getRgb();

    int[][][] previewP = preview.getRgb();


    for (int i = 0; i < preview.getHeight(); i++) {
      for (int j = 0; j < preview.getWidth(); j++) {
        if (j < 2) {
          assertEquals(newP[0][i][j], previewP[0][i][j]);
          assertEquals(newP[1][i][j], previewP[1][i][j]);
          assertEquals(newP[2][i][j], previewP[2][i][j]);
        } else {
          assertEquals(srcP[0][i][j], previewP[0][i][j]);
          assertEquals(srcP[1][i][j], previewP[1][i][j]);
          assertEquals(srcP[2][i][j], previewP[2][i][j]);
        }
      }

    }
  }


  //preview greyscale
  @Test
  public void previewGreyscale() {

    rbgMap = new int[][][]{
        {{255, 115, 6}, {255, 115, 6}, {255, 115, 6}},
        {{255, 115, 6}, {255, 115, 6}, {255, 115, 6}},
        {{20, 10, 6}, {20, 10, 6}, {20, 10, 6}}
    };


    image = createBufferImage(3, 3, rbgMap);
    obj.addImage("dummy-file", image);

    obj.getGreyScale("luma", "dummy-file",
        "dummy-luma");

    obj.operationPreview(50, "dummy-file", "dummy-luma");

    ImageMetadata src = obj.getImageData("dummy-file");
    ImageMetadata luma = obj.getImageData("dummy-luma");
    ImageMetadata preview = obj.getImageData("dummy-luma");

    int[][][] srcP = src.getRgb();
    int[][][] newP = luma.getRgb();

    int[][][] previewP = preview.getRgb();


    for (int i = 0; i < preview.getHeight(); i++) {
      for (int j = 0; j < preview.getWidth(); j++) {
        if (j < 2) {
          assertEquals(newP[0][i][j], previewP[0][i][j]);
          assertEquals(newP[1][i][j], previewP[1][i][j]);
          assertEquals(newP[2][i][j], previewP[2][i][j]);
        } else {
          assertEquals(srcP[0][i][j], previewP[0][i][j]);
          assertEquals(srcP[1][i][j], previewP[1][i][j]);
          assertEquals(srcP[2][i][j], previewP[2][i][j]);
        }
      }

    }
  }


  //preview color correction
  @Test
  public void previewColorCorrection() {

    rbgMap = new int[][][]{
        {{255, 115, 6}, {255, 115, 6}, {255, 115, 6}},
        {{255, 115, 6}, {255, 115, 6}, {255, 115, 6}},
        {{20, 10, 6}, {20, 10, 6}, {20, 10, 6}}
    };


    image = createBufferImage(3, 3, rbgMap);
    obj.addImage("dummy-file", image);

    obj.colorCorrection("dummy-file", "dummy-color-correct");

    obj.operationPreview(50, "dummy-file",
        "dummy-color-correct");

    ImageMetadata src = obj.getImageData("dummy-file");
    ImageMetadata colorCorrect = obj.getImageData("dummy-color-correct");
    ImageMetadata preview = obj.getImageData("dummy-color-correct");

    int[][][] srcP = src.getRgb();
    int[][][] newP = colorCorrect.getRgb();

    int[][][] previewP = preview.getRgb();


    for (int i = 0; i < preview.getHeight(); i++) {
      for (int j = 0; j < preview.getWidth(); j++) {
        if (j < 2) {
          assertEquals(newP[0][i][j], previewP[0][i][j]);
          assertEquals(newP[1][i][j], previewP[1][i][j]);
          assertEquals(newP[2][i][j], previewP[2][i][j]);
        } else {
          assertEquals(srcP[0][i][j], previewP[0][i][j]);
          assertEquals(srcP[1][i][j], previewP[1][i][j]);
          assertEquals(srcP[2][i][j], previewP[2][i][j]);
        }
      }

    }
  }


  //file not found -preview
  @Test
  public void previewFileNotFound() {

    String filename = "preview-test";
    obj.operationPreview(50, filename, "dummy-color-correct");

    assertEquals(filename + " not present in the application.\n", out.toString());

  }


  //preview levelAdjustment
  @Test
  public void previewLevelAdjustment() {

    rbgMap = new int[][][]{
        {{255, 115, 6}, {255, 115, 6}, {255, 115, 6}},
        {{255, 115, 6}, {255, 115, 6}, {255, 115, 6}},
        {{20, 10, 6}, {20, 10, 6}, {20, 10, 6}}
    };


    image = createBufferImage(3, 3, rbgMap);
    obj.addImage("dummy-file", image);

    obj.levelAdjustment("dummy-file",
        "dummy-leveladjust", 0, 0, 0);

    obj.operationPreview(50, "dummy-file",
        "dummy-leveladjust");

    ImageMetadata src = obj.getImageData("dummy-file");
    ImageMetadata colorCorrect = obj.getImageData("dummy-leveladjust");
    ImageMetadata preview = obj.getImageData("dummy-leveladjust");

    int[][][] srcP = src.getRgb();
    int[][][] newP = colorCorrect.getRgb();

    int[][][] previewP = preview.getRgb();


    for (int i = 0; i < preview.getHeight(); i++) {
      for (int j = 0; j < preview.getWidth(); j++) {
        if (j < 2) {
          assertEquals(newP[0][i][j], previewP[0][i][j]);
          assertEquals(newP[1][i][j], previewP[1][i][j]);
          assertEquals(newP[2][i][j], previewP[2][i][j]);
        } else {
          assertEquals(srcP[0][i][j], previewP[0][i][j]);
          assertEquals(srcP[1][i][j], previewP[1][i][j]);
          assertEquals(srcP[2][i][j], previewP[2][i][j]);
        }
      }

    }
  }

  //preview 0 - means original image
  @Test
  public void previewZero() {

    rbgMap = new int[][][]{
        {{255, 115, 6}, {255, 115, 6}, {255, 115, 6}},
        {{255, 115, 6}, {255, 115, 6}, {255, 115, 6}},
        {{20, 10, 6}, {20, 10, 6}, {20, 10, 6}}
    };


    image = createBufferImage(3, 3, rbgMap);
    obj.addImage("dummy-file", image);

    obj.levelAdjustment("dummy-file",
        "dummy-leveladjust", 0, 0, 0);

    obj.operationPreview(0, "dummy-file",
        "dummy-leveladjust");

    ImageMetadata src = obj.getImageData("dummy-file");
    ImageMetadata colorCorrect = obj.getImageData("dummy-leveladjust");
    ImageMetadata preview = obj.getImageData("dummy-leveladjust");

    int[][][] srcP = src.getRgb();
    int[][][] newP = colorCorrect.getRgb();

    int[][][] previewP = preview.getRgb();


    for (int i = 0; i < preview.getHeight(); i++) {
      for (int j = 0; j < preview.getWidth(); j++) {

        assertEquals(srcP[0][i][j], previewP[0][i][j]);
        assertEquals(srcP[1][i][j], previewP[1][i][j]);
        assertEquals(srcP[2][i][j], previewP[2][i][j]);

      }

    }
  }

  //preview 100 means the modified image
  @Test
  public void previewFull() {

    rbgMap = new int[][][]{
        {{255, 115, 6}, {255, 115, 6}, {255, 115, 6}},
        {{255, 115, 6}, {255, 115, 6}, {255, 115, 6}},
        {{20, 10, 6}, {20, 10, 6}, {20, 10, 6}}
    };


    image = createBufferImage(3, 3, rbgMap);
    obj.addImage("dummy-file", image);

    obj.levelAdjustment("dummy-file",
        "dummy-leveladjust", 0, 0, 0);

    obj.operationPreview(100, "dummy-file",
        "dummy-leveladjust");

    ImageMetadata src = obj.getImageData("dummy-file");
    ImageMetadata colorCorrect = obj.getImageData("dummy-leveladjust");
    ImageMetadata preview = obj.getImageData("dummy-leveladjust");

    int[][][] newP = colorCorrect.getRgb();

    int[][][] previewP = preview.getRgb();


    for (int i = 0; i < preview.getHeight(); i++) {
      for (int j = 0; j < preview.getWidth(); j++) {

        assertEquals(newP[0][i][j], previewP[0][i][j]);
        assertEquals(newP[1][i][j], previewP[1][i][j]);
        assertEquals(newP[2][i][j], previewP[2][i][j]);

      }

    }
  }


  //levelsAdjustment
  @Test
  public void levelsAdjustment() {
    obj.levelAdjustment("dummy-file",
        "dummy-leveladjust", 89, 55, 250);
    ImageMetadata src = obj.getImageData("dummy-file");
    ImageMetadata newfile = obj.getImageData("dummy-leveladjust");

    int[][][] srcP = src.getRgb();
    int[][][] newP = newfile.getRgb();

    int[][][] expected = {
        {{255, 0, 255}, {255, 0, 255}, {255, 0, 255}},
        {{255, 0, 255}, {255, 0, 255}, {255, 0, 255}},
        {{255, 255, 255}, {255, 255, 255}, {255, 255, 255}}
    };

    for (int i = 0; i < newfile.getWidth(); i++) {
      for (int j = 0; j < newfile.getHeight(); j++) {

        assertEquals(expected[0][i][j], newP[0][i][j]);
        assertEquals(expected[1][i][j], newP[1][i][j]);
        assertEquals(expected[2][i][j], newP[2][i][j]);
      }
    }


  }

  //levelsAdjustment does not change dimensions
  @Test
  public void levelsAdjustmentDimensionRemainSame() {
    obj.levelAdjustment(
        "dummy-file", "dummy-leveladjust",
        89, 55, 250);
    ImageMetadata src = obj.getImageData("dummy-file");
    ImageMetadata newfile = obj.getImageData("dummy-leveladjust");


    assertEquals(src.getWidth(), newfile.getWidth());
    assertEquals(src.getHeight(), newfile.getHeight());
  }

  //levelsAdjustment to make all zeros
  @Test
  public void levelsAdjustmentZero() {
    obj.levelAdjustment(
        "dummy-file", "dummy-leveladjust",
        2, 2, 2);
    ImageMetadata src = obj.getImageData("dummy-file");
    ImageMetadata newfile = obj.getImageData("dummy-leveladjust");

    int[][][] srcP = src.getRgb();
    int[][][] newP = newfile.getRgb();

    int[][][] expected = {
        {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}},
        {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}},
        {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}}
    };

    for (int i = 0; i < newfile.getWidth(); i++) {
      for (int j = 0; j < newfile.getHeight(); j++) {

        assertEquals(expected[0][i][j], newP[0][i][j]);
        assertEquals(expected[1][i][j], newP[1][i][j]);
        assertEquals(expected[2][i][j], newP[2][i][j]);
      }
    }

  }


  @Test
  public void histogram_FileNotFound_originalFile() {

    String filename = "histogram-test";
    obj.createHistogram(filename, "dummy-histogram");

    assertEquals(filename
        + " not present in the application.\n", out.toString());

  }

  @Test
  public void histogram_test() {

    int[][][] hh = {
        {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}},
        {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}},
        {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}}
    };
    BufferedImage image1 = createBufferImage(3, 3, hh);
    obj.addImage("hh", image1);
    obj.createHistogram("hh", "dummy-histogram");
    ImageMetadata histogram = obj.getImageData("dummy-histogram");
    assertNotEquals(histogram, null);

    assertEquals(256, histogram.getHeight());
    assertEquals(256, histogram.getWidth());


  }

  @Test
  public void histogram_Image_test() {

    int[][][] hh = {
        {{150, 150, 150}, {150, 150, 150}, {150, 150, 150}},
        {{200, 200, 200}, {200, 200, 200}, {200, 200, 200}},
        {{170, 170, 170}, {170, 170, 170}, {170, 170, 170}}
    };

    BufferedImage image1 = createBufferImage(3, 3, hh);
    obj.addImage("hh", image1);
    obj.createHistogram("hh", "dummy-histogram");
    ImageMetadata histogram = obj.getImageData("dummy-histogram");
    assertNotEquals(histogram, null);
    assertEquals(256, histogram.getHeight());
    assertEquals(256, histogram.getWidth());
    int[][][] histRGB = histogram.getRgb();

    for (int i = 1; i < 255; i++) {
      for (int j = 1; j < 255; j++) {
        if (j == 150 || j == 151) {
          assertEquals(histRGB[0][i][j], 255);
          assertEquals(histRGB[1][i][j], 0);
          assertEquals(histRGB[2][i][j], 0);
        } else if (j == 200 || j == 201) {
          assertEquals(histRGB[0][i][j], 0);
          assertEquals(histRGB[1][i][j], 255);
          assertEquals(histRGB[2][i][j], 0);
        } else if (j == 170 || j == 171) {
          assertEquals(histRGB[0][i][j], 0);
          assertEquals(histRGB[1][i][j], 0);
          assertEquals(histRGB[2][i][j], 255);
        } else {
          if (i % 15 == 0 || j % 15 == 0) {

            assertEquals(histRGB[0][i][j], 200);
            assertEquals(histRGB[1][i][j], 200);
            assertEquals(histRGB[2][i][j], 200);
          } else {
            assertEquals(histRGB[0][i][j], 255);
            assertEquals(histRGB[1][i][j], 255);
            assertEquals(histRGB[2][i][j], 255);
          }
        }
      }
    }


  }


}