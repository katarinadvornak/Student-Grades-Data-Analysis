package src;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.FileInputStream;

public class ChoiceApplication extends Application {
    private Stage stage;
    private FileDisplayer displayer;
    @Override
    public void start(Stage primaryStage) {
        displayer=new FileDisplayer();
        VBox root = new VBox(20); // VBox layout with spacing between children
        root.setAlignment(Pos.CENTER); // Center children in the VBox

        // Create a Text label and Button for the first application
        Text app1Label = new Text("STATISTICS");
        app1Label.setFont(new Font(24)); // Set the font size for the label
        Button app1Button = new Button();
        app1Button.setPrefSize(300, 300); // Set the preferred size for the button
        // You can set an image or color background for the button here
        setButtonImage(app1Button,"src/pictures/stats.png");

        // Create a Text label and Button for the second application
        Text app2Label = new Text("PREDICTION");
        app2Label.setFont(new Font(24)); // Set the font size for the label
        Button app2Button = new Button();
        app2Button.setPrefSize(300, 300); // Set the preferred size for the button
        // You can set an image or color background for the button here
        setButtonImage(app2Button,"src/pictures/chart-tree.png");


        // Set the actions for the buttons
        app1Button.setOnAction(event -> openCourseAnalyst());
        app2Button.setOnAction(event -> openPredictor());
        app1Button.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> app1Button.setEffect(new DropShadow()));
        app1Button.addEventHandler(MouseEvent.MOUSE_EXITED, e -> app1Button.setEffect(null));

        app2Button.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> app2Button.setEffect(new DropShadow()));
        app2Button.addEventHandler(MouseEvent.MOUSE_EXITED, e -> app2Button.setEffect(null));

        // Add the labels and buttons to the VBox
        root.getChildren().addAll(app1Label, app1Button, app2Label, app2Button);

        // Set the Scene and Stage
        Scene scene = new Scene(root, 600, 800);
        stage =primaryStage;
        primaryStage.setTitle("Program choice");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    private void setButtonImage(Button button,String imagePath)  {
        try{
            Image backgroundImage = new Image(new FileInputStream(imagePath));
            BackgroundImage backgroundImg = new BackgroundImage(backgroundImage,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(BackgroundSize.AUTO,
                            BackgroundSize.AUTO,
                            false,
                            false,
                            true,
                            false));
            button.setBackground(new Background(backgroundImg));
        }catch (Exception ex){

        }
    }

    private void openCourseAnalyst() {
        // Code to open Application 1
        stage.hide();
        CoursesAnalyst analyst=new CoursesAnalyst(displayer);
        analyst.setParentStage(stage);
        Stage analystStage=new Stage();
        analyst.start(analystStage);
    }

    private void openPredictor() {
        // Code to open Application 2
        stage.hide();
        GradePredictorApp predictorApp=new GradePredictorApp(displayer);
        predictorApp.setParentStage(stage);
        Stage analystStage=new Stage();
        predictorApp.start(analystStage);
    }
    public FileDisplayer getDisplayer(){
        return displayer;
    }
    public Stage getStage(){
        return stage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
