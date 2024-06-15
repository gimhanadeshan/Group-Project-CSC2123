package com.example.wildlife_hms;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static javafx.stage.StageStyle.TRANSPARENT;
import static javafx.stage.StageStyle.UNDECORATED;

public class MainApplication extends Application {

    @FXML
    public ImageView imageViewDp;
    @FXML
    public Label lblCompanyName;

    @Override
    public void start(Stage stage) throws IOException {

        //CompanyDetailsModel company=getCompany();

        //String imagePath = company.getLogo();
        String imagePath = "logo.jpeg";
        Image icon= new Image(imagePath);


        FXMLLoader startLoader = new FXMLLoader(MainApplication.class.getResource("start.fxml"));
        Scene startScene = new Scene(startLoader.load());



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
   /* private CompanyDetailsModel getCompany() {

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String query = "SELECT * FROM companydetails WHERE ID=1;";
        CompanyDetailsModel company = null;
        try {
            Statement statement = connectDB.createStatement();
            ResultSet output = statement.executeQuery(query);

            // Check if the ResultSet has any rows
            if (output.next()) {
                company = new CompanyDetailsModel(
                        output.getString("Name"),
                        output.getString("Logo")
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return company;
    }*/

    public static void main(String[] args) {
        launch();
    }

}
