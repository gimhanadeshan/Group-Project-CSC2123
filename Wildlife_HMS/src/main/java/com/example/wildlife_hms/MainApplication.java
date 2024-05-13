package com.example.wildlife_hms;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

import static javafx.stage.StageStyle.TRANSPARENT;
import static javafx.stage.StageStyle.UNDECORATED;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader startLoader = new FXMLLoader(MainApplication.class.getResource("start.fxml"));
        Scene startScene = new Scene(startLoader.load());
        Image icon= new Image("icons8-habitat-100.png");

        stage.initStyle(TRANSPARENT);
        stage.initStyle(UNDECORATED);
        stage.setScene(startScene);
        stage.getIcons().add(icon);
       // stage.setOpacity(0.5);
        stage.show();

        // Create a PauseTransition to wait for 3 seconds
        PauseTransition delay = new PauseTransition(Duration.seconds(1));
        delay.setOnFinished(_ -> {
            try {
                FXMLLoader loginLoader = new FXMLLoader(MainApplication.class.getResource("login.fxml"));
                Scene loginScene = new Scene(loginLoader.load());
                stage.setScene(loginScene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        delay.play();
    }

    public static void main(String[] args) {
        launch();
    }
}
