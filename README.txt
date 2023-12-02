
title: CITATION
message: If you use this project, please cite it using this. 
authors: Dhruv Saini , Mumuksha Pant 
description:  The images used in this program are original, owned by Dhruv Saini and Mumuksha Pant and is authorized to
 be used in this project.

Work Ownership: Image Processing Application is a sole work of Dhruv Saini (saini.dh@northeastern.edu) & Mumuksha Pant
 (pant.mu@northeastern.edu) , this is an academic work.

This README is for Image Processing Application created as part of coursework in CS 5010 Programming Design Paradigms.
This is a GUI , text-based and script based, interactive application which supports loading, manipulating and saving images
using simple GUI or text-based commands (command line interface) .

The image is loaded from a source path and supports various Filtering and Transformation operations which are
performed on the pixels of the image to produce the desired result and be saved to a destination path.

Every image is a sequence of pixels. For a colored image, the pixel’s color is represented by breaking it into 3
components called the RGB model. In this model, a color is represented by three numbers (components): red, green,
 blue. Any color is a combination of these three base colors.

 ** Class Diagram for this is present in resources folder with name ClassDiagram.jpeg. **

To start this application, run the “ImageProcessingApplication” class after which the UI is displayed and user can perform various Image Operations.


GUI Based
---------
1. An image ( PNG/ JPG/ JPEG/ PPM ) can be loaded into the Application using 'Open a File' Button and selecting the file from appropriate location.
2. Loaded image will be visible in 'Image Preview' along with its Histogram in 'RGB Histogram' .
3. Different Image Operations can be performed on an Image by clicking on the Radio Button namely :
    - sepia ( supports Preview )
    - blur ( supports Preview )
    - sharpen ( supports Preview )
    - horizontal_flip
    - vertical_flip
    - color_correct ( supports Preview )
    - luma_component( supports Preview )
    - intensity_component ( supports Preview )
    - value_component ( supports Preview )
    - red_component ( supports Preview )
    - green_component ( supports Preview )
    - blue_component ( supports Preview )

4. To Compress an Image, enter a Compression percentage from 0 to 100 and click on 'Compress' button.
5. To Brighten or Darken an Image, enter a brightness factor ( -100 to 100 ) and click on 'Brighten' button.
6. To Levels Adjust an Image, enter the black tone (b) , mid tone(m) and white tone (w) values such that these values are from 0 to 255 and b<m<w and click on 'Levels Adjust' button.
7. To Preview an Image Operation, click on the filters supporting the Preview Functionality
   ( sepia, blur, sharpen , color_correct, luma_component, intensity_component, value_component,   red_component, green_component, blue_component ) ,
   Enter the 'Split Percentage' from 0 to 100 , click on 'Split' Button to see the Preview.
8. To view the Preview in the Image Preview section and view its Histogram, click on 'Apply Filter' button.
9. To cancel the Preview Operation, click on 'Cancel' Button.
10. To save an image after performing the operations, click on 'Save File' button to save the image in a particular location.
    Currently we're supporting only .ppm, .png, .jpeg, .jpg image formats.



Command Line Argument
---------------------

The user must enter valid commands for the application to successfully work. Otherwise, the program will throw an
 error.

The commands are as follows:
# -> new command added as part of assignment 5.

	1.	load   <image-path>    <image-name>
	2.	save    <image-path>    <image-name>
	3.	red-component    <image-name>    <dest-image-name>
	4.	horizontal-flip    <image-name>    <dest-image-name>
	5.	vertical-flip    <image-name>    <dest-image-name>
	6.	brighten    <increment>    <image-name>    <dest-image-name>
	7.	rgb-split    <image-name>    <dest-image-name-red>    <dest-image-name-green>    <dest-image-name-blue>
	8.	rgb-combine    <image-name>    <red-image green-image blue-image>
	9.	blur    <image-name>    <dest-image-name>
	10.	sharpen    <image-name>    <dest-image-name>
	11.	sepia    <image-name>    <dest-image-name>
	12.	run    <script-file-path>
	13.	quit

        14.     compress <percentage> <source-file-name> <dest-file-name>
        15.     color-correct <source-file-name> <dest-file-name>
        16.     levels-adjust <black> <medium> <white> <source-file-name> <dest-file-name>
        17.     histogram <source-file-name> <dest-file-name>
        18.     red-component    <image-name>    <dest-image-name> split <percentage>
        19.     blur    <image-name>    <dest-image-name> split <percentage>
        20.     sharpen    <image-name>    <dest-image-name> split <percentage>
        21.     levels-adjust <black> <medium> <white> <source-file-name> <dest-file-name> split <percentage>
        22.     color-correct <source-file-name> <dest-file-name> split <percentage>
        23.     sepia <source-file-name> <dest-file-name> split <percentage>

 Running the program :
1. Using Compiler
        If you are using compiler to test the program, run the “ImageProcessingApplication” class to start the application and see GUI.

2. Using Command Line :
		a. Open your terminal, Change your directory to the src folder of our project.
		b. Type "javac ImageProcessingApplication.java" to compile the program.
		c. Type "java ImageProcessingApplication.java -file ../resources/commandLine-script.txt" to run the queries from
		 the script.

3. Using Jar file :
* using text based CLI:
		a. At the resources folder you can find a jar file named - ImageProcessingApplication.jar, open your
 			terminal and change your current directory to this path.
		b. Type - "java -jar ImageProcessingApplication.jar -text" to run the command in terminal, you
		   should see a welcome message now.
		c. Now start exploring the application via this terminal:
			Ex commands:
				load input/diya.png dd
				rgb-split dd diva-red diva-green diva-blue
				save output/diya-red.jpeg diva-red
				quit
		 You can easily, see the input images in input folder and output images in output folder.
		d. "quit" can be used to terminate the program.

* using GUI:
    a. At the resources folder you can find a jar file named - ImageProcessingApplication.jar, open your
     			terminal and change your current directory to this path.
    b. Type - "java -jar ImageProcessingApplication.jar" to run the GUI, you
    		   should see a new GUI window now.

* using Script:
       a. At the resources folder you can find a jar file named - ImageProcessingApplication.jar, open your
           			terminal and change your current directory to this path.
       b. Type - "java -jar ImageProcessingApplication.jar -file ../resources/commandLine-script.txt" to run the script using the jar,
        you should see a series of operation logs in the terminal.

Design Approach:
----------------
----------------

We have tried to stick to SOLID principles as much as possible.
The Image Processing Application follows the model view controller design. The MVC architecture allows to isolate
the entire behavior of the program into categories: actual functionality, user display and user interaction and
delegation.

The Controller - ImageProcessingController
------------------------------------------

Controller helps us around managing all the user inputs and communicating with the model what operation to perform.

 The “readUserCommands()” method initializes the image processing controller, initiating the application's image
 processing functionality.

 The "inputFromScriptFile()" methods initializes the image processing controller, initiating the application's image
 processing functionality and helps us run the program from commandLine interface.

Controller utilizes a helper class ~ FileReadWriteUtility is used support all the input and output file operations,
that includes loading and parsing images in supported formats (“.png”,”.jpg”,”.jpeg”,”.ppm”) and read the contents
from the files/directories. The main idea around separating this helper class is to manage all the file I/O related
operations from a single class.

The Controller - MVCController
------------------------------------------

Controller helps us around managing all the view related actions and communicating with the model
 what operation to perform.
This controller implements all the methods listed in Features Interface are required by the view.
Not limited to this the controller helps to perform certain validations for the image processing application.

The Model - ImageProcessingModel
---------------------------------
The model contains Image Meta Data that acts as a base data structure to store image data (filename, height, width,
pixel map)and perform all the image manipulation tasks(red-component, filters, etc.).  “ImageProcessingModel”
implements the actual functionalities offered by the program and the logic for all the operations that are required
as per the assignment to performed on these images.

- Helpers :
     1. A CompressionHelper utility in our Model which provides methods for image compression using the
     Haar wavelet transformations and other methods used in compression. The Haar wavelet transformation is
     a mathematical technique commonly used in image processing.

     2. A PlotHelper utility has been implemented in our Model for generating image Histograms. It contains a method
     called getNormalizedHistogram that generates a normalized histogram for a given image. The colors of the lines
     represent the Red, Green, and Blue components of the Histogram. Why separate this, as in future we might need
     to add new types of plots and to maintain this histogram feature out of model.

     3. A ImageHelper utility, To reduce the code duplication and redundancy, we have created a helper class to support
     functionalities that are specific to the model data structure. Ex. Clamping pixels, finding transpose of the
     [][] rgb matrix, etc.

The View - IView
-----------------
The View represents the Graphical User Interface for our image processing application, this class
extends JFrame java class.
This allows a user to interactively load, perform filters, compression and save images.
Our program uses the Swing framework to show its UI.
The View provides a user options to 'Open a File' , perform Image Operations and then 'Save File'.
The view helps us to manage multiple validation and prompt user to input only valid data only.

ImageProcessingApplication
--------------------------
 The main class for the Image Processing application.
  This class serves as the entry point for the Image Processing application, initializing the
  necessary components and starting the application based on command-line arguments or
  default behavior.

  Our ImageProcessingApplication class encapsulates the core functionality
  of the Image Processing application. It orchestrates the setup of the model,
  view, and controller components, facilitating the seamless execution of the application.
  The main method handles command-line arguments, allowing for flexibility in
  script-based or interactive user-driven modes of operation.

  The primary responsibilities of this class include creating instances of the
  ImageProcessingController, ImageProcessingModel, and IView, establishing the
  communication channels between these components, and determining the mode of operation based on the provided
  command-line arguments



Design Changes:

1. We have implemented a View which features the Graphical User Interface for our Image Processing Application implemented using Swings UI.
2. We have implemented an interface 'Features' which contains all the methods ( all filters , load and save )  which will be used for implementing the functionalities on the image.
3. To avoid direct interaction of View and Model, a new controller called 'MVCController' which implements the 'Features' interface is created which is a GUI Controller performing all the image processing functions using the Model and the View.