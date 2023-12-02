package controller;

import controller.commands.ImageOperations;
import model.ImageProcessingModel;
import utility.FileReadWriteUtility;
import view.Features;
import view.IView;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Objects;

/**
 * This is a controller for the GUI performing all the image processing functions,
 * using the Model and the View.
 * This class is used to avoid any direct interaction of the Model and the View.
 */
public class MVCController extends ImageProcessingControllerImpl implements Features {

  private final ImageProcessingModel model;
  private final IView view;
  private final FileReadWriteUtility fileUtility;
  private final OutputStream out;

  /**
   * Constructs an MVCController with the specified model, view, and file utility.
   *
   * @param model       refers to the image processing model.
   * @param view        refers to the view component.
   * @param fileUtility refers to the file read-write utility.
   * @param out         output stream
   */
  public MVCController(ImageProcessingModel model, IView view,
                       FileReadWriteUtility fileUtility, OutputStream out) {
    super(model, out, System.in, fileUtility);
    this.model = model;
    this.view = view;
    this.fileUtility = fileUtility;
    this.out = out;
    view.addFeatures(this);
  }

  /**
   * Retrieves the file extension from a given filename.
   *
   * @param filename the name of the file.
   * @return the file extension as a string.
   */
  private static String getFileExtension(String filename) {
    int dotIndex = filename.lastIndexOf('.');
    if (dotIndex == -1) {
      return "";
    }
    return filename.substring(dotIndex + 1);
  }

  @Override
  public void loadImage() {
    File f = view.fetchFile();
    if (f == null) {
      return;
    }
    load(f);
  }

  /**
   * Loads an image into the application by processing the specified file.
   *
   * @param f the file to be loaded.
   */
  private void load(File f) {
    PrintStream outputStream = new PrintStream(out);
    outputStream.println(f.getName());

    if (!isImageSaved() && view.promptUser()) {
      return;
    }

    String loadCommand = "Load " + f.getAbsolutePath() + " " + f.getName();
    boolean status = super.executeModel(ImageOperations.LOAD,
        loadCommand.split(" "), model);

    if (status) {
      setImages(f.getName());
    }
  }

  @Override
  public void sepia(String sourceImage) {
    if (!isImageLoaded(sourceImage)) {
      return;
    }

    applyFilter("sepia", sourceImage, sourceImage + "-sepia");
  }

  @Override
  public void blur(String sourceImage) {
    if (!isImageLoaded(sourceImage)) {
      return;
    }

    applyFilter("blur", sourceImage, sourceImage + "-blur");
  }

  @Override
  public void sharpen(String sourceImage) {
    if (!isImageLoaded(sourceImage)) {
      return;
    }

    applyFilter("sharpen", sourceImage, sourceImage + "-sharpen");
  }

  @Override
  public void compress(int percent, String sourceImage) {
    PrintStream outputStream = new PrintStream(out);
    if (!isImageLoaded(sourceImage)) {
      return;
    }

    String newName = sourceImage + "-compress";

    outputStream.println("performing compress on " + sourceImage);
    String loadCommand = "compress " + percent + " " + sourceImage + " " + newName;

    boolean status = super.executeModel(ImageOperations.COMPRESS,
        loadCommand.split(" "), model);

    if (status) {
      setImages(newName);
    }
  }

  @Override
  public void brightness(int increment, String sourceImage) {
    PrintStream outputStream = new PrintStream(out);
    if (!isImageLoaded(sourceImage)) {
      return;
    }

    String newName = sourceImage + "-brighten";
    outputStream.println("performing brightness on " + sourceImage);
    String loadCommand = "brighten " + increment + " " + sourceImage + " " + newName;


    boolean status = super.executeModel(ImageOperations.BRIGHTEN,
        loadCommand.split(" "), model);

    if (status) {
      setImages(newName);
    }
  }

  @Override
  public void lumaComponent(String sourceImage) {
    if (!isImageLoaded(sourceImage)) {
      return;
    }

    applyFilter("luma-component", sourceImage, sourceImage + "-lumaComponent");
  }

  @Override
  public void levelsAdjust(int b, int m, int w, String sourceImage) {
    PrintStream outputStream = new PrintStream(out);

    if (!isImageLoaded(sourceImage)) {
      return;
    }
    if(b >= m || m >= w){
      view.showErrorMessage("Values should be in the order b < m < w");
      return;
    }

    String newName = sourceImage + "-levels_adjust";
    outputStream.println("performing levels adjust on " + sourceImage);
    String loadCommand =
        "levels_adjust " + b + " " + m + " " + w + " " + sourceImage + " " + newName;

    boolean status = super.executeModel(ImageOperations.LEVELS_ADJUST,
        loadCommand.split(" "), model);

    if (status) {
      boolean applyFilter = previewFilters(sourceImage, newName);

      if (applyFilter) {
        setImages(newName);
      }
    }
  }

  @Override
  public void saveImage(String sourceImage) {

    boolean saveStatus = false;

    if (!isImageLoaded(sourceImage)) {
      return;
    }

    File f = view.getFilesToSave();
    if (f == null) {
      return;
    }

    String path = f.getAbsolutePath();
    String fileExtension = getFileExtension(path);

    BufferedImage image = model.getImage(sourceImage);
    if (fileExtension.equalsIgnoreCase("ppm")) {
      saveStatus = fileUtility.savePPMImage(path, image);
    } else if (fileExtension.equalsIgnoreCase("jpeg") ||
        fileExtension.equalsIgnoreCase("png") ||
        fileExtension.equalsIgnoreCase("jpg")) {
      saveStatus = fileUtility.saveIOImage(path, image);
    }

    if (saveStatus) {
      view.showSaveStatus("File saved");
    } else {
      view.showSaveStatus("Unable to save file. Please try again.");
    }
  }

  @Override
  public void colorCorrect(String sourceImage) {
    if (!isImageLoaded(sourceImage)) {
      return;
    }
    applyFilter("color_correct", sourceImage, sourceImage + "-color_correct");
  }

  @Override
  public void intensityComponent(String sourceImage) {
    if (!isImageLoaded(sourceImage)) {
      return;
    }
    applyFilter("intensity-component", sourceImage, sourceImage + "-intensityComponent");

  }

  @Override
  public void valueComponent(String sourceImage) {
    if (!isImageLoaded(sourceImage)) {
      return;
    }
    applyFilter("value-component", sourceImage, sourceImage + "-valueComponent");

  }

  @Override
  public void redComponent(String sourceImage) {
    if (!isImageLoaded(sourceImage)) {
      return;
    }

    applyFilter("red-component", sourceImage, sourceImage + "-redComponent");

  }

  @Override
  public void greenComponent(String sourceImage) {
    if (!isImageLoaded(sourceImage)) {
      return;
    }
    applyFilter("green-component", sourceImage, sourceImage + "-greenComponent");
  }

  @Override
  public void blueComponent(String sourceImage) {
    if (!isImageLoaded(sourceImage)) {
      return;
    }
    applyFilter("blue-component", sourceImage, sourceImage + "-blueComponent");
  }

  /**
   * Retrieves a histogram image for a specified source image.
   *
   * @param sourceImage the name of the source image.
   * @return the histogram image.
   */
  private BufferedImage getHistogram(String sourceImage) {
    BufferedImage histogram;
    String histogramCommand = "Histogram " + sourceImage + " " + sourceImage + "-hist";
    boolean status = super.executeModel(ImageOperations.HISTOGRAM,
        histogramCommand.split(" "), model);
    if (status) {
      histogram = model.getImage(sourceImage + "-hist");
      return histogram;
    }
    return null;
  }

  /**
   * Checks if the given image has been loaded.
   *
   * @param sourceImage the name of the source image.
   * @return true if the image is loaded; false otherwise.
   */
  private boolean isImageLoaded(String sourceImage) {
    if (Objects.equals(sourceImage, null)) {
      view.showNoImageError();
      return false;
    }
    return true;
  }

  /**
   * Displays a preview of the applied filters to the user before finalizing the changes.
   *
   * @param originalFile the original image file.
   * @param filteredFile the filtered image file.
   * @return true if the user chooses to apply the filter; false otherwise.
   */
  private boolean previewFilters(String originalFile,
                                 String filteredFile) {

    BufferedImage splitFilteredImage = getSplitPreview(originalFile,
        filteredFile, 50);

    return view.openPreviewWindow(splitFilteredImage,
        originalFile, filteredFile, this);
  }

  @Override
  public BufferedImage getSplitPreview(String originalFile,
                                       String filteredFile,
                                       int splitPercentage) {
    return model.splitPreview(splitPercentage, originalFile, filteredFile);
  }

  /**
   * Applies a specified filter operation to the image and displays a preview to the user.
   *
   * @param operation   the filter operation to be applied.
   * @param sourceImage the source image name.
   * @param newName     the name of the new image after applying the filter.
   */

  private void applyFilter(String operation, String sourceImage, String newName) {

    String loadCommand = operation + " " + sourceImage + " " + newName;

    boolean status = super.executeModel(ImageOperations.valueOf(operation.
            toUpperCase().replace('-', '_')),
        loadCommand.split(" "), model);

    if (status) {
      boolean applyFilter = previewFilters(sourceImage, newName);

      if (applyFilter) {
        setImages(newName);
      }
    }
  }

  @Override
  public void flipImage(String sourceImage, String direction) {
    if (!isImageLoaded(sourceImage)) {
      return;
    }

    String newName = sourceImage + "-" + direction.toLowerCase() + "_flip";
    String loadCommand = direction + "-flip " + sourceImage + " " + newName;

    boolean status = super.executeModel(ImageOperations.valueOf(direction.toUpperCase() + "_FLIP"),
        loadCommand.split(" "), model);

    if (status) {
      setImages(newName);
    }
  }

  /**
   * Sets the images in the view after an operation has been successfully executed.
   *
   * @param filename the name of the image file.
   */
  private void setImages(String filename) {
    view.placeImageInScreen(filename, model.getImage(filename));
    view.showHistogram(filename + "-hist", getHistogram(filename));
  }

  /**
   * returns the status of the current image being saved or not.
   *
   * @return true if the image is saved else return false.
   */
  private boolean isImageSaved() {
    return view.saveStatus();
  }

}
