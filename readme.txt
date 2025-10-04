Prerequisites:
-Java 21 and JavaFX 21 SDK already installed on your device

Run instructions:

-Open terminal/command prompt in the project directory

-Run the application using the following command line:
java --module-path "yourJavafxDirectory\lib" --add-modules javafx.controls,javafx.fxml -jar project-1-1.jar

instead of "yourJavafxDirectory\lib" you should the directory of your javaFX sdk
usually it is something like "C:\path\to\javafx-sdk-21\lib"

The program runs in this way:
-insert a student ID and click on the "Predict" button, and get the prediction for the missing courses
-in the lower lide of the program you will find a TextBox with the option to insert a course name
-push the button "Tree Debug" and get a regression tre for the selected course
