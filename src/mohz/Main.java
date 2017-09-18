package mohz;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.event.ActionListener;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("layout_main.fxml"));
        Scene mainScene = new Scene(root, 650, 350);
        primaryStage.setScene(mainScene);

        primaryStage.setTitle("WiCompanion");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("WiCompanion_logo.png")));
        primaryStage.setResizable(false);
        primaryStage.show();

    }
    public static void main(String[] args) {
        launch(args);
    }
}
