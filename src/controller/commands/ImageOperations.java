package controller.commands;

/**
 * Enum representing various image processing operations along with their valid lengths.
 */
public enum ImageOperations {

  // Enum constants with valid lengths for each operation
  RUN(2),
  LOAD(3),
  SAVE(3),
  BLUR(3, 5),
  SHARPEN(3, 5),
  SEPIA(3, 5),
  VALUE_COMPONENT(3, 5),
  LUMA_COMPONENT(3, 5),
  INTENSITY_COMPONENT(3, 5),
  RED_COMPONENT(3, 5),
  GREEN_COMPONENT(3, 5),
  BLUE_COMPONENT(3, 5),
  HORIZONTAL_FLIP(3),
  VERTICAL_FLIP(3),
  HISTOGRAM(3),
  COLOR_CORRECT(3, 5),
  BRIGHTEN(4),
  COMPRESS(4),
  RGB_SPLIT(5),
  RGB_COMBINE(5),
  LEVELS_ADJUST(6, 8);

  // Array of valid lengths for each operation
  private final int[] validLengths;

  /**
   * Constructs an ImageOperations enum constant with the provided valid lengths.
   *
   * @param validLengths The valid lengths for the operation.
   */
  ImageOperations(int... validLengths) {
    this.validLengths = validLengths;
  }

  /**
   * Checks if the given length is valid for the specified image processing operation.
   *
   * @param command The image processing operation.
   * @param length  The length to check for validity.
   * @return `true` if the length is valid; `false` otherwise.
   */
  public static boolean isValidCommand(ImageOperations command, int length) {
    for (int validLength : command.validLengths) {
      if (validLength == length) {
        return true;
      }
    }
    return false;
  }

  /**
   * Retrieves the corresponding ImageOperations enum constant based on the provided string.
   *
   * @param str The string representation of the image processing operation.
   * @return The corresponding ImageOperations enum constant, or `null` if not found.
   */
  public static ImageOperations getOperation(String str) {
    ImageOperations imageOperations;
    try {
      imageOperations = ImageOperations.valueOf(str);
    } catch (IllegalArgumentException e) {
      return null;
    }
    return imageOperations;
  }
}
