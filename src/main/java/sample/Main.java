package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../../resources/sample.fxml"));
        primaryStage.setTitle("Hello World");
        Scene scene = new Scene(root, 300, 275);
        primaryStage.setScene(scene);
        scene.setFill(Color.TRANSPARENT);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setAlwaysOnTop(true);

        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

//set Stage boundaries to the lower right corner of the visible bounds of the main screen
        primaryStage.setX(primaryScreenBounds.getMinX() + primaryScreenBounds.getWidth() - 450);
        primaryStage.setY(primaryScreenBounds.getMinY() + primaryScreenBounds.getHeight() - 1400);
        primaryStage.setWidth(400);
        primaryStage.setHeight(300);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
