package com.example.wildlife_hms;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXProgressSpinner;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import org.mindrot.jbcrypt.BCrypt;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private MFXButton cancelBtn;

    @FXML
    private Label loginMsg;

    @FXML
    private MFXPasswordField pwdField;

    @FXML
    public MFXTextField unameField;

    @FXML
    private AnchorPane ancLogin;




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Image backgroundImage = new Image("start.png");
        BackgroundImage background = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT
        );

        Background background1 = new Background(background);
        ancLogin.setBackground(background1);

    }



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

        String username = unameField.getText();
        String password = pwdField.getText();

        String verifyLogin = "SELECT password, Active FROM useraccounts WHERE username=?";

        try (PreparedStatement statement = connectDB.prepareStatement(verifyLogin)) {
            statement.setString(1, username);
            ResultSet queryResult = statement.executeQuery();

            if (queryResult.next()) {
                String hashedPassword = queryResult.getString("password");
                boolean isActive = queryResult.getBoolean("Active");

                if (isActive && BCrypt.checkpw(password, hashedPassword)) {
                    // Successful login
                    String finalUsername = username;
                    UserPermissionsModel userPermissions = UserPermissionsModel.getUserPermissionsByUsername(finalUsername);

                    PauseTransition pause = new PauseTransition(Duration.seconds(1));
                    pause.setOnFinished(_ -> {
                        Platform.runLater(() -> {
                            try {
                                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("dashboard.fxml"));
                                Parent load = fxmlLoader.load();

                                DashboardController controller = fxmlLoader.getController();
                                controller.setUserName(finalUsername);
                                controller.setUserPermissions(userPermissions);
                                controller.btnDashboardOnAction();

                                CompanyDetailsModel company = getCompany();
                                String imagePath = company.getLogo().trim();

                                Image icon = new Image(imagePath);

                                Stage stage = new Stage();
                                stage.setScene(new Scene(load));
                                stage.getIcons().add(icon);
                                stage.setTitle(company.getName());
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
            } else {
                // Username not found
                Platform.runLater(() -> {
                    loginMsg.setGraphic(null);
                    loginMsg.setText("Invalid Login. Please try again.");
                    loginMsg.setAlignment(Pos.BASELINE_CENTER);
                });
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


    private CompanyDetailsModel getCompany() {

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
    }

}
