package view;

import java.awt.image.BufferedImage;

/**
 * This interface provides all the methods ( all filters , load and save operations),
 * which will be used for implementing the functionalities on the image in the GUI (IView).
 */
public interface Features {

  /**
   * It is used to load a file in PPM/PNG/JPG/JPEG Formats.
   * It utilises the model's load operation and displays the loaded image in the view.
   */
  void loadImage();

  /**
   * Apply Sepia filter on the loaded image. It utilises model's sepia operation and,
   * displays the filter-applied image in the view.
   *
   * @param sourceImage is the image which is loaded on which operation needs to be performed.
   */
  void sepia(String sourceImage);

  /**
   * Apply blur filter on the loaded image. It utilises model's blur operation and,
   * displays the filter-applied image in the view.
   *
   * @param sourceImage is the image which is loaded on which operation needs to be performed.
   */
  void blur(String sourceImage);

  /**
   * Apply to sharpen filter on the loaded image. It utilises model's sharpen operation and,
   * displays the filter-applied image in the view.
   *
   * @param sourceImage is the image which is loaded on which operation needs to be performed.
   */
  void sharpen(String sourceImage);

  /**
   * Apply color-correct filter on the loaded image.
   * It utilises model's color-correct operation and,
   * displays the filter-applied image in the view.
   *
   * @param sourceImage is the image which is loaded on which operation needs to be performed.
   */
  void colorCorrect(String sourceImage);

  /**
   * Apply compress filter on the loaded image. It utilises model's compress operation and,
   * displays the filter-applied image in the view.
   *
   * @param percent     is the percentage by with we want to split the filter.
   * @param sourceImage is the image which is loaded on which operation needs to be performed.
   */
  void compress(int percent, String sourceImage);


  /**
   * Apply brightness filter on the loaded image. It utilises model's brighten operation and,
   * displays the filter-applied image in the view.
   * It brightens an image by a specified number of increments.
   *
   * @param increment   The number of increments to brighten the image.
   * @param sourceImage is the image which is loaded on which operation needs to be performed.
   */
  void brightness(int increment, String sourceImage);


  /**
   * Apply luma filter on the loaded image. It utilises model's luma-component operation and,
   * displays the filter-applied image in the view.
   *
   * @param sourceImage is the image which is loaded on which operation needs to be performed.
   */
  void lumaComponent(String sourceImage);


  /**
   * Apply intensity filter on the loaded image.
   * It utilises model's intensity-component operation and,
   * displays the filter-applied image in the view.
   *
   * @param sourceImage is the image which is loaded on which operation needs to be performed.
   */
  void intensityComponent(String sourceImage);


  /**
   * Apply value-component filter on the loaded image.
   * It utilises model's value-component operation and,
   * displays the filter-applied image in the view.
   *
   * @param sourceImage is the image which is loaded on which operation needs to be performed.
   */
  void valueComponent(String sourceImage);


  /**
   * Apply red-component filter on the loaded image. It utilises model's greyscale red component,
   * operation and displays the filter-applied image in the view.
   *
   * @param sourceImage is the image which is loaded on which operation needs to be performed.
   */
  void redComponent(String sourceImage);


  /**
   * Apply green-component filter on the loaded image.
   * It utilises model's greyscale green-component operation and,
   * displays the filter-applied image in the view.
   *
   * @param sourceImage is the image which is loaded on which operation needs to be performed.
   */
  void greenComponent(String sourceImage);


  /**
   * Apply blue-component filter on the loaded image. It utilises model's  greyscale blue-component,
   * operation and displays the filter-applied image in the view.
   *
   * @param sourceImage is the image which is loaded on which operation needs to be performed.
   */
  void blueComponent(String sourceImage);

  /**
   * Apply levels adjust filter on the loaded image.
   * It utilises model's levels-adjust operation and displays the filter-applied image in the view.
   *
   * @param b           The black level adjustment value.
   * @param m           The mid-level adjustment value.
   * @param g           The white level adjustment value.
   * @param sourceImage is the image which is loaded on which operation needs to be performed.
   */
  void levelsAdjust(int b, int m, int g, String sourceImage);

  /**
   * It is used to save a file in PPM/PNG/JPG/JPEG Formats in the user-selected,
   * destination location.
   *
   * @param sourceImage is the image which is to be saved.
   */
  void saveImage(String sourceImage);

  /**
   * Generates a preview of an image operation by applying the specified percentage,
   * of the operation to the original image and saving the result to a destination file.
   *
   * @param originalFile    refers to the original image without any operations.
   * @param filteredFile    refers to the original image after applying filters.
   * @param splitPercentage refers to the specified percentage of split.
   * @return a Buffered Image.
   */
  BufferedImage getSplitPreview(String originalFile,
                                String filteredFile,
                                int splitPercentage);

  /**
   * Performs a flip operation on an image.
   * Flip direction can be vertical or horizontal.
   *
   * @param sourceImage is the image which is loaded on which operation needs to be performed.
   * @param direction   horizontal or vertical.
   */
  void flipImage(String sourceImage, String direction);
}
