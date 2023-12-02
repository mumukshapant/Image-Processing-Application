package view;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JTextField;

import javax.swing.BoxLayout;
import javax.swing.JScrollPane;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.AbstractButton;
import javax.swing.JFileChooser;


import javax.swing.ImageIcon;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.JDialog;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.GridLayout;
import java.awt.FlowLayout;

import java.awt.Color;
import java.awt.Dimension;

import java.awt.BorderLayout;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Enumeration;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class represents the GUI for the Image Processing Application.
 * It extends JFrame and implements the IView interface.
 */
public class ViewImpl extends JFrame implements IView {

  private final JPanel imageScreen;

  private final JPanel histogramPanel;
  private final JFrame previewFrame;
  private JPanel radioPanel;
  private String currentImage;
  private ButtonGroup buttonGroup;
  private JButton openFileButton;
  private JButton saveFileButton;
  private JButton compressionButton;
  private JButton brightnessButton;
  private JButton levelsAdjustButton;
  private JTextField compressionText;
  private JTextField brightnessText;
  private JTextField levelsAdjustTextB;
  private JTextField levelsAdjustTextM;
  private JTextField levelsAdjustTextW;
  private JLabel errorB;
  private JLabel errorC;
  private JLabel errorLa;

  private boolean isSaved = false;

  /**
   * It constructs a ViewImpl object and initialises the GUI components.
   */
  public ViewImpl() {
    super();
    setTitle("Image Processing Application");
    setSize(600, 600);
    JPanel mainScreen = new JPanel();
    mainScreen.setLayout(new BoxLayout(mainScreen, BoxLayout.PAGE_AXIS));
    JScrollPane mainScrollPane = new JScrollPane(mainScreen);
    add(mainScrollPane);

    JPanel topPanel = new JPanel();
    addFileOpenButton(topPanel);
    addFileSaveButton(topPanel);
    mainScreen.add(topPanel);

    imageScreen = new JPanel();
    appendRadioButtons();
    mainScreen.add(radioPanel);
    imageScreen.setBorder(BorderFactory.createTitledBorder("Image Preview"));
    imageScreen.setLayout(new GridLayout(1, 0, 10, 10));


    addCompressionComponents(mainScreen, topPanel);
    addBrightnessComponents(mainScreen);
    addLevelsAdjustComponents(mainScreen);
    mainScreen.add(imageScreen);
    blankImage();

    histogramPanel = new JPanel();
    histogramPanel.setBorder(BorderFactory.createTitledBorder("R G B Histogram"));
    histogramPanel.setLayout(new GridLayout(1, 0, 10, 10));
    mainScreen.add(histogramPanel);

    previewFrame = new JFrame("Preview");
    previewFrame.setSize(300, 300);
    previewFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

    setVisible(true);
  }

  @Override
  public void addFeatures(Features features) throws IllegalArgumentException {
    addListeners(features);
    openFileButton.addActionListener(evt -> features.loadImage());

    compressionButton.addActionListener(evt -> {
          features.compress(Integer.parseInt(compressionText.getText()), this.currentImage);
          compressionText.setText("0");
        }
    );

    brightnessButton.addActionListener(evt -> {
      features.brightness(Integer.parseInt(brightnessText.getText()), this.currentImage);
      brightnessText.setText("0");
    });

    levelsAdjustButton.addActionListener(
        evt -> {
          features.levelsAdjust(Integer.parseInt(levelsAdjustTextB.getText()),
              Integer.parseInt(levelsAdjustTextM.getText()),
              Integer.parseInt(levelsAdjustTextW.getText()),
              this.currentImage
          );
          levelsAdjustTextB.setText("0");
          levelsAdjustTextM.setText("0");
          levelsAdjustTextW.setText("0");

        });

    saveFileButton.addActionListener(evt -> features.saveImage(this.currentImage));

    Enumeration<AbstractButton> buttons = buttonGroup.getElements();
    while (buttons.hasMoreElements()) {
      AbstractButton radioButton = buttons.nextElement();
      switch (radioButton.getActionCommand()) {
        case "sepia":
          radioButton.addActionListener(evt -> features.sepia(this.currentImage));
          break;
        case "blur":
          radioButton.addActionListener(evt -> features.blur(this.currentImage));
          break;
        case "sharpen":
          radioButton.addActionListener(evt -> features.sharpen(this.currentImage));
          break;
        case "horizontal_flip":
          radioButton.addActionListener(evt -> features.flipImage(this.currentImage, "horizontal"));
          break;
        case "vertical_flip":
          radioButton.addActionListener(evt -> features.flipImage(this.currentImage, "vertical"));
          break;
        case "color_correct":
          radioButton.addActionListener(evt -> features.colorCorrect(this.currentImage));
          break;
        case "lumaComponent":
          radioButton.addActionListener(evt -> features.lumaComponent(this.currentImage));
          break;
        case "intensityComponent":
          radioButton.addActionListener(evt -> features.intensityComponent(
              this.currentImage));
          break;
        case "valueComponent":
          radioButton.addActionListener(evt -> features.valueComponent(
              this.currentImage));
          break;
        case "redComponent":
          radioButton.addActionListener(evt -> features.redComponent(
              this.currentImage));
          break;
        case "greenComponent":
          radioButton.addActionListener(evt -> features.greenComponent(
              this.currentImage));
          break;
        case "blueComponent":
          radioButton.addActionListener(evt -> features.blueComponent(
              this.currentImage));
          break;
        default:
          throw new IllegalArgumentException("Invalid feature added!");
      }
    }
  }

  @Override
  public File fetchFile() {
    File f = null;
    final JFileChooser chooser = new JFileChooser(".");
    FileNameExtensionFilter filter = new FileNameExtensionFilter(
        "JPG, JPEG, PNG, PPM", "jpg",
        "jpeg", "png", "ppm");
    chooser.setFileFilter(filter);

    int revalue = chooser.showOpenDialog(ViewImpl.this);
    if (revalue == JFileChooser.APPROVE_OPTION) {
      f = chooser.getSelectedFile();
      String filePath = f.getPath().replace(" ", "\\s");
      f = new File(filePath);
    }
    return f;
  }


  @Override
  public void showHistogram(String imageName, BufferedImage image) {
    JLabel histLabel = new JLabel(new ImageIcon(image));
    JScrollPane imageScrollPane = new JScrollPane(histLabel);
    imageScrollPane.setPreferredSize(new Dimension(300, 300));

    histogramPanel.removeAll();
    histogramPanel.add(imageScrollPane);
    histogramPanel.revalidate();
    histogramPanel.repaint();
  }

  @Override
  public void showNoImageError() {
    clearAllInputFields();
    JOptionPane.showMessageDialog(null,
        "Load an Image using 'Open a File' Button to perform Operations",
        "Error", JOptionPane.INFORMATION_MESSAGE);
  }

  @Override
  public File getFilesToSave() {
    clearAllInputFields();
    File selectedFile = null;
    final JFileChooser chooser = new JFileChooser(".");
    int revalue = chooser.showSaveDialog(ViewImpl.this);
    if (revalue == JFileChooser.APPROVE_OPTION) {
      selectedFile = chooser.getSelectedFile();
    }

    return selectedFile;
  }

  @Override
  public boolean openPreviewWindow(BufferedImage image,
                                   String originalFile,
                                   String filteredFile,
                                   Features features) {
    JDialog previewDialog = new JDialog();
    previewDialog.setTitle("Preview");
    previewDialog.setSize(500, 500);

    JLabel previewLabel = new JLabel(new ImageIcon(image));
    JScrollPane previewScrollPane = new JScrollPane(previewLabel);

    JLabel splitLabel = new JLabel("Split Percentage:");
    JTextField splitTextField = new JTextField(5);
    splitTextField.setText("50");

    JButton applyButton = new JButton("Apply Filter");
    JButton cancelButton = new JButton("Cancel");

    AtomicBoolean filterApplied = new AtomicBoolean(false);

    JButton splitButton = new JButton("Split");

    splitButton.addActionListener(e -> {
      int splitPercentage = Integer.parseInt(splitTextField.getText());

      if (splitPercentage < 0 || splitPercentage > 100) {
        JOptionPane.showMessageDialog(previewFrame,
            "Split % must be between 0 to 100",
            "Error",
            JOptionPane.INFORMATION_MESSAGE);
      } else {
        BufferedImage splitImage =
            features.getSplitPreview(originalFile, filteredFile, splitPercentage);
        previewLabel.setIcon(new ImageIcon(splitImage));
      }
    });

    applyButton.addActionListener(e -> {
      JOptionPane.showMessageDialog(previewDialog, "Filter applied!");
      filterApplied.set(true);
      previewDialog.dispose();
    });

    cancelButton.addActionListener(e -> {
      previewDialog.dispose();
      clearAllInputFields();
    });

    JPanel buttonPanel = new JPanel();
    buttonPanel.add(applyButton);
    buttonPanel.add(cancelButton);
    buttonPanel.add(splitLabel);
    buttonPanel.add(splitTextField);
    buttonPanel.add(splitButton);

    previewDialog.setLayout(new BorderLayout());
    previewDialog.add(previewScrollPane, BorderLayout.CENTER);
    previewDialog.add(buttonPanel, BorderLayout.SOUTH);

    previewDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    previewDialog.setLocationRelativeTo(null);
    previewDialog.setModal(true);
    previewDialog.setVisible(true);

    return filterApplied.get();
  }

  /**
   * Adds components related to image compression to the main screen.
   *
   * @param mainScreen The main panel of the GUI.
   * @param topPanel   The top panel of the GUI.
   */
  private void addCompressionComponents(JPanel mainScreen, JPanel topPanel) {
    JPanel compressPanel = new JPanel();
    compressionButton = new JButton("Compress");
    compressPanel.setLayout(new FlowLayout());
    topPanel.add(compressPanel);
    compressPanel.add(compressionButton);
    compressPanel.setLayout(new FlowLayout());

    //compress textField
    JLabel cText = new JLabel("Enter Compression %");
    compressionText = new JTextField(10);
    compressionText.setText(String.valueOf(0));
    keyListenerValidationCompress(compressionText);

    compressPanel.add(compressionText);
    compressPanel.add(cText);
    mainScreen.add(compressPanel);

    errorC = new JLabel();
    errorC.setForeground(Color.red);
    mainScreen.add(errorC);
  }

  /**
   * Adds components related to adjusting image brightness to the main screen.
   *
   * @param mainScreen The main panel of the GUI.
   */
  private void addBrightnessComponents(JPanel mainScreen) {
    JPanel brightnessPanel = new JPanel();
    brightnessButton = new JButton("Brighten");
    brightnessPanel.add(brightnessButton);

    JLabel bText = new JLabel("Enter Brightness Factor");
    brightnessText = new JTextField(10);
    brightnessText.setText(String.valueOf(0));
    keyListenerValidationBrightness(brightnessText);

    brightnessPanel.add(brightnessText);
    brightnessPanel.add(bText);


    mainScreen.add(brightnessPanel);

    errorB = new JLabel();
    errorB.setForeground(Color.red);
    mainScreen.add(errorB);
  }

  /**
   * Adds components related to adjusting image levels to the main screen.
   *
   * @param mainScreen The main panel of the GUI.
   */
  private void addLevelsAdjustComponents(JPanel mainScreen) {
    JPanel laPanel = new JPanel();
    levelsAdjustButton = new JButton("Levels Adjust");

    laPanel.add(levelsAdjustButton);


    //levels adjust text fields
    JLabel laText = new JLabel("Enter BMW values for Level Adjust");
    levelsAdjustTextB = new JTextField(3);
    levelsAdjustTextM = new JTextField(3);
    levelsAdjustTextW = new JTextField(3);

    levelsAdjustTextB.setText(String.valueOf(0));
    levelsAdjustTextM.setText(String.valueOf(0));
    levelsAdjustTextW.setText(String.valueOf(0));

    keyListenerValidationLA(levelsAdjustTextB, levelsAdjustTextM, levelsAdjustTextW);


    laPanel.add(levelsAdjustTextB);
    laPanel.add(levelsAdjustTextM);
    laPanel.add(levelsAdjustTextW);
    laPanel.add(laText);

    mainScreen.add(laPanel);

    errorLa = new JLabel();
    errorLa.setForeground(Color.red);
    mainScreen.add(errorLa);
  }

  /**
   * Adds the "Open a File" button to the specified panel in the GUI.
   *
   * @param dialogBoxesPanel The panel to which the "Open a File" button is added.
   */
  private void addFileOpenButton(JPanel dialogBoxesPanel) {
    JPanel fileOpenPanel = new JPanel();
    fileOpenPanel.setLayout(new FlowLayout());
    dialogBoxesPanel.add(fileOpenPanel);
    openFileButton = new JButton("Open a file");
    openFileButton.setActionCommand("Open file");
    fileOpenPanel.add(openFileButton);
  }

  /**
   * Adds the "Save Image" button to the specified panel in the GUI.
   *
   * @param dialogBoxesPanel The panel to which the "Save Image" button is added.
   */
  private void addFileSaveButton(JPanel dialogBoxesPanel) {
    JPanel fileSavePanel = new JPanel();
    fileSavePanel.setLayout(new FlowLayout());
    dialogBoxesPanel.add(fileSavePanel);
    saveFileButton = new JButton("Save Image");
    saveFileButton.setActionCommand("Save Image");
    fileSavePanel.add(saveFileButton);
  }

  /**
   * Displays a placeholder image with a message to load an image.
   */
  private void blankImage() {
    JLabel imageLabel = new JLabel("Load an Image to use the Image Processing Application."
        , SwingConstants.CENTER);
    JScrollPane imageScrollPane = new JScrollPane(imageLabel);
    imageScrollPane.setPreferredSize(new Dimension(100, 300));
    imageScreen.add(imageScrollPane);
  }

  /**
   * Appends radio buttons for various image operations to the GUI.
   */
  private void appendRadioButtons() {
    radioPanel = new JPanel(new GridLayout(0, 4));
    radioPanel.setBorder(BorderFactory.createTitledBorder("Image Operations"));
    buttonGroup = new ButtonGroup();

    addRadioButton("sepia");
    addRadioButton("blur");
    addRadioButton("sharpen");
    addRadioButton("horizontal_flip");
    addRadioButton("vertical_flip");
    addRadioButton("color_correct");
    addRadioButton("lumaComponent");
    addRadioButton("intensityComponent");
    addRadioButton("valueComponent");
    addRadioButton("redComponent");
    addRadioButton("greenComponent");
    addRadioButton("blueComponent");
  }

  /**
   * Adds a radio button with the specified option name to the button group.
   *
   * @param optionName The name of the radio button.
   */
  private void addRadioButton(String optionName) {
    JRadioButton radioButton = new JRadioButton(optionName);
    radioButton.setActionCommand(optionName);
    buttonGroup.add(radioButton);
    radioPanel.add(radioButton);
  }

  /**
   * Displays an error message with the specified text and sets a timer to clear the error label.
   *
   * @param msg        The error message to display.
   * @param errorLabel The label to display the error message.
   */
  private void showError(String msg, JLabel errorLabel) {
    errorLabel.setText(msg);
    Timer timer = new Timer(3000,
        e -> errorLabel.setText(""));
    timer.start();

  }

  /**
   * Adds key listener validation for brightness input field.
   *
   * @param textField The text field for brightness input.
   */

  private void keyListenerValidationBrightness(JTextField textField) {
    textField.addKeyListener(new KeyAdapter() {
      @Override
      public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();

        if (!(Character.isDigit(c) ||
            (c == '-' && textField.getText().isEmpty() && !textField.getText().contains("-")))
            && c != KeyEvent.VK_BACK_SPACE
            && c != KeyEvent.VK_ENTER) {
          e.consume();

          showError("Please enter a valid number.", errorB);
          textField.setText("0");

        } else {
          try {
            String currentText = textField.getText();

            if (c != KeyEvent.VK_BACK_SPACE) {
              if (!(c == '-' && currentText.isEmpty())) {
                int value = Integer.parseInt(currentText + c);

                if (value < -100 || value > 100) {
                  e.consume();
                  textField.setText("0");
                  showError("Brightness Factor can be between -100 and 100.",
                      errorB);
                }
              }
            }
          } catch (NumberFormatException ex) {
            e.consume();

            JOptionPane.showMessageDialog(ViewImpl.this,
                "Please enter a valid number.",
                "Error", JOptionPane.WARNING_MESSAGE);
            textField.setText("0");
          }
        }
      }
    });
  }

  /**
   * Adds key listener validation for compression input field.
   *
   * @param textField The text field for compression input.
   */
  private void keyListenerValidationCompress(JTextField textField) {
    textField.addKeyListener(new KeyAdapter() {
      @Override
      public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();

        if (!Character.isDigit(c)
            && c != KeyEvent.VK_BACK_SPACE
            && c != KeyEvent.VK_ENTER
        ) {
          e.consume();
          showError("Please enter only numbers.", errorC);
           compressionText.setText("0");
        } else {
          try {

            String currentText = textField.getText();

            if (c != KeyEvent.VK_BACK_SPACE) {
              int value = Integer.parseInt(currentText + c);

              if (value < 0 || value > 100
                  && c != KeyEvent.VK_BACK_SPACE
                  && c != KeyEvent.VK_ENTER
              ) {
                e.consume();
                compressionText.setText("0");
                showError("Please enter a number between 0 and 100.", errorC);
              }
            }
          } catch (NumberFormatException ex) {
            e.consume();
            JOptionPane.showMessageDialog(ViewImpl.this,
                "Please enter a valid number.",
                "Error", JOptionPane.WARNING_MESSAGE);
          }
        }
      }
    });
  }

  /**
   * Adds key listener validation for level adjustment input fields.
   *
   * @param b The text field for the 'b' value.
   * @param m The text field for the 'm' value.
   * @param w The text field for the 'w' value.
   */
  private void keyListenerValidationLA(JTextField b, JTextField m, JTextField w) {

    KeyAdapter keyAdapter = new KeyAdapter() {
      @Override
      public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();

        if (!Character.isDigit(c)
            && c != KeyEvent.VK_BACK_SPACE
            && c != KeyEvent.VK_ENTER) {
          e.consume();
          showError("Please enter only numbers.", errorLa);

        } else {
          try {
            JTextField textField = (JTextField) e.getSource();
            String currentText = textField.getText();

            if (c != KeyEvent.VK_BACK_SPACE
                && c != KeyEvent.VK_ENTER) {
              int value = Integer.parseInt(currentText + c);

              if (value < 0 || value > 255) {
                e.consume();
                showError("Please enter a number between 0 and 255.", errorLa);
                textField.setText("");
              }
            }
          } catch (NumberFormatException ex) {
            // Handle the case where the entered text is not a valid integer
            e.consume();
            JOptionPane.showMessageDialog(ViewImpl.this,
                "Please enter a valid number.", "Error", JOptionPane.WARNING_MESSAGE);
          }
        }
      }
    };

    b.addKeyListener(keyAdapter);
    m.addKeyListener(keyAdapter);
    w.addKeyListener(keyAdapter);
  }

  /**
   * Places the specified image in the image screen of the GUI.
   *
   * @param imageName The name of the image.
   * @param image     The image to be displayed.
   */
  public void placeImageInScreen(String imageName, BufferedImage image) {
    this.currentImage = imageName;
    JLabel imageLabel = new JLabel(new ImageIcon(image));
    JScrollPane imageScrollPane = new JScrollPane(imageLabel);
    imageScrollPane.setPreferredSize(new Dimension(300, 300));
    imageScreen.removeAll();
    imageScreen.add(imageScrollPane);
    imageScreen.revalidate();
    imageScreen.repaint();
    isSaved = false;
  }

  /**
   * Adds key listeners for the "Open a File" button.
   *
   * @param features The features object to which the listeners are added.
   */
  private void addListeners(Features features) {
    openFileButton.addKeyListener(new KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
          features.loadImage();
        }
      }
    });
    openFileButton.addKeyListener(new KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
          features.loadImage();
        }
      }
    });
  }

  /**
   * Clears the selection of all radio buttons in the button group.
   */
  private void clearAllInputFields() {
    buttonGroup.clearSelection();
  }

  @Override
  public void showSaveStatus(String message) {
    if (message.equals("File saved")) {
      isSaved = true;
    }
    JOptionPane.showMessageDialog(null,
        message, "File Saved Operation.",
        JOptionPane.INFORMATION_MESSAGE);
  }

  /**
   * shows the saved status of the current image.
   */
  @Override
  public boolean saveStatus() {
    if (!Objects.equals(this.currentImage, null)) {
      return isSaved;
    }
    return true;
  }

  @Override
  public boolean promptUser() {

    int dialogResult = JOptionPane.showConfirmDialog(null,
        "Current Image is not saved, are you sure "
            + "you want to load another image?", "Confirmation",
        JOptionPane.YES_NO_OPTION);

    return dialogResult == JOptionPane.NO_OPTION;
  }

  @Override
  public void showErrorMessage(String message) {
    JOptionPane.showMessageDialog(ViewImpl.this,
        message,
        "Error",
        JOptionPane.WARNING_MESSAGE);
  }
}
