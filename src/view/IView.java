package view;

import java.awt.image.BufferedImage;
import java.io.File;

/**
 * This interface contains the methods for interacting with the view (GUI),
 * for ImageProcessingApplication.
 */
public interface IView {

  /**
   * Adds features to the view (GUI) and setting them with event listeners and actions.
   *
   * @param features represents the Feature object.
   * @throws IllegalArgumentException when Invalid feature is added.
   */
  void addFeatures(Features features) throws IllegalArgumentException;

  /**
   * Chose a file to load and fetch it from the chosen location.
   *
   * @return the selected file.
   */
  File fetchFile();

  /**
   * it places the images on the screen.
   *
   * @param imageName name of the image which is to be shown.
   * @param image     Buffered Image which is to be shown.
   */
  void placeImageInScreen(String imageName, BufferedImage image);

  /**
   * shows the histogram of the image currently displayed.
   *
   * @param imageName name of the image of which histogram is to be shown.
   * @param image     Buffered Image of which histogram is to be shown.
   */
  void showHistogram(String imageName, BufferedImage image);

  /**
   * It shows an error to the user when attempting to perform any operation,
   * without loading an image.
   */
  void showNoImageError();

  /**
   * opens a file save dialog to allow user to save a file at the chosen destination.
   *
   * @return File object saved at the selected location.
   */
  File getFilesToSave();

  /**
   * The preview window displays the original image, a split view with the applied filter,
   * and the textbox to specify the percentage of the split view to be displayed.
   *
   * @param image        A Buffered Image.
   * @param originalFile refers to the original image without any operations.
   * @param filteredFile refers to the original image after applying filters.
   * @param features     The feature set containing image processing operations.
   * @return true if the user wants to apply the filter else false.
   */
  boolean openPreviewWindow(BufferedImage image,
                            String originalFile,
                            String filteredFile,
                            Features features);

  /**
   * shows the status after saving a file.
   *
   * @param message shown after file is successfully saved or unable to save.
   */
  void showSaveStatus(String message);

  /**
   * returns if the current image is saved or not.
   *
   * @return the saved status of the current image.
   */
  boolean saveStatus();

  /**
   * Prompts the user to continue loading new image.
   *
   * @return true if the user responds positively, false otherwise.
   */
  boolean promptUser();

  /**
   * Helps to display error messages on the GUI.
   *
   */
  void showErrorMessage(String message);
}
