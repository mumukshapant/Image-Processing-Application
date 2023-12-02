package model;

/**
 * This class represents metadata for an image, including its width, height, and RGB data.
 */
public class ImageMetadata {
  private final int width;
  private final int height;
  private final int[][][] rgb;

  /**
   * Constructs an ImageMetadata object with the specified width, height, and RGB data.
   *
   * @param width  The width of the image.
   * @param height The height of the image.
   * @param rgb    The RGB data for the image.
   */
  public ImageMetadata(int width, int height, int[][][] rgb) {
    this.width = width;
    this.height = height;
    this.rgb = rgb.clone();
  }

  /**
   * Get the width of the image.
   *
   * @return The width of the image.
   */
  public int getWidth() {
    return width;
  }

  /**
   * Get the height of the image.
   *
   * @return The height of the image.
   */
  public int getHeight() {
    return height;
  }

  /**
   * Get the RGB data for the image.
   *
   * @return The RGB data.
   */
  public int[][][] getRgb() {
    return rgb.clone();
  }
}
