package com.example.wildlife_hms;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXProgressSpinner;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoginController {
    @FXML
    private MFXButton cancelBtn;

    @FXML
    private Label loginMsg;

    @FXML
    private MFXPasswordField pwdField;

    @FXML
    private MFXTextField unameField;






    public void loginBtnOnAction(){

        if (!unameField.getText().isBlank() && !pwdField.getText().isBlank()){

            validateLogin();

        }else{
            loginMsg.setText("Pls Enter username and password");
            loginMsg.setAlignment(Pos.BASELINE_CENTER);
        }
    }
    public void cancelBtnOnAction()  {
        Stage stage =(Stage)cancelBtn.getScene().getWindow();
        stage.close();
    }



    public void validateLogin() {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        // Show loading spinner
        MFXProgressSpinner loadingSpinner = new MFXProgressSpinner();
        loadingSpinner.setRadius(7);

        loginMsg.setGraphic(loadingSpinner);
        loginMsg.setText("Logging in...");
        loginMsg.setAlignment(Pos.CENTER);

        String verifyLogin = STR."SELECT count(1) FROM useraccounts WHERE username='\{unameField.getText()}' AND password='\{pwdField.getText()}' AND Active=1 ";


        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(verifyLogin);

            while (queryResult.next()) {
                if (queryResult.getInt(1) == 1) {

                    PauseTransition pause = new PauseTransition(Duration.seconds(1));
                    pause.setOnFinished(_ -> {
                        // Successful login
                        Platform.runLater(() -> {
                            try {
                                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("dashboard.fxml"));
                                Parent load = fxmlLoader.load();

                                DashboardController controller = fxmlLoader.getController();
                                controller.setUserName(unameField.getText());
                                controller.btnDashboardOnAction();
                                Stage stage = new Stage();
                                stage.setScene(new Scene(load));
                                stage.show();

                                // Close current window
                                Stage window = (Stage) pwdField.getScene().getWindow();
                                window.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    });
                    pause.play();
                } else {


                    // Invalid login
                    Platform.runLater(() -> {
                        loginMsg.setGraphic(null);
                        loginMsg.setText("Invalid Login. Please try again.");
                        loginMsg.setAlignment(Pos.BASELINE_CENTER);
                    });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close database connection
            try {
                connectDB.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void openRegisterForm() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("userRegisterForm.fxml"));
            Parent root = fxmlLoader.load();

            // Get the controller of the registration form
            UserRegisterFormController controller = fxmlLoader.getController();
            // Optionally, you can pass any data to the controller if needed
            // controller.setData(data);

            // Get the current stage
            Stage stage = (Stage) cancelBtn.getScene().getWindow();

            // Set the new scene in the same stage
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
