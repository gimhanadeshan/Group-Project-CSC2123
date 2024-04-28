package com.example.wildlife_hms;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UserProfileController implements Initializable {


    @FXML
    private MFXButton btnReset;

    @FXML
    private MFXButton btnSubmit;

    @FXML
    private Label lblEmail;

    @FXML
    private Label lblFullName;

    @FXML
    private Label lblRole;

    @FXML
    private ImageView profile;

    @FXML
    private MFXPasswordField pwdConfirm;

    @FXML
    private MFXPasswordField pwdNew;

    @FXML
    private MFXPasswordField pwdOld;

    @FXML
    private Label lblUserName;

    @FXML
    private Label lblValidatationMsg;

    DatabaseConnection connectNow = new DatabaseConnection();
    Connection connectDB = connectNow.getConnection();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


    }


    public void setUserProfile(String username) {




        // Retrieve and set profile picture
        String query = "SELECT DP,username,FirstName,LastName,Email,Roll FROM useraccounts WHERE username = ?";
        try {

            PreparedStatement statement = connectDB.prepareStatement(query);
            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();
            while (rs.next()){

                String image = rs.getString("DP");
                String fName=rs.getString("FirstName");
                String lName=rs.getString("LastName");
                String email=rs.getString("Email");
                String role=rs.getString("Roll");
                String uname=rs.getString("username");

                if (image != null) {
                    Image profileImage = new Image(image);
                    profile.setImage(profileImage);
                }
                lblFullName.setText(STR."\{fName} \{lName}");
                lblEmail.setText(email);
                lblRole.setText(role);
                lblUserName.setText(uname);

            }


           // connectDB.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database error
        }



    }




    public void passwordChange() {
        String username = lblUserName.getText();
        String oldPassword = pwdOld.getText(); // Corrected
        String newPassword = pwdNew.getText(); // Corrected
        String confirmPassword = pwdConfirm.getText(); // Corrected

        if (!newPassword.isEmpty() && !oldPassword.isEmpty() && !confirmPassword.isEmpty()) { // Corrected condition

            // Check if the old password matches the one stored in the database
            String query = "SELECT password FROM useraccounts WHERE username =?";

            try {
                PreparedStatement statement = connectDB.prepareStatement(query);
                statement.setString(1, username);
                ResultSet rs = statement.executeQuery();
                if (rs.next()) {
                    String storedPassword = rs.getString("password");
                    if (storedPassword.equals(oldPassword)) {
                        if (newPassword.equals(confirmPassword)) {
                            // Update the password in the database
                            String updateQuery = "UPDATE useraccounts SET password = ? WHERE username = ?";
                            PreparedStatement updateStatement = connectDB.prepareStatement(updateQuery);
                            updateStatement.setString(1, newPassword);
                            updateStatement.setString(2, username);
                            updateStatement.executeUpdate();
                            reset();
                            lblValidatationMsg.setText("Password updated successfully.");

                        } else {
                            lblValidatationMsg.setText("New password and confirm password do not match.");
                        }
                    } else {
                        lblValidatationMsg.setText("Old password is incorrect.");
                        reset();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle database error
            }
        } else {
            lblValidatationMsg.setText("Please fill in all fields.");
        }
    }


    public void reset() {
        pwdOld.clear(); // Clear the old password field
        pwdNew.clear(); // Clear the new password field
        pwdConfirm.clear(); // Clear the confirm password field
        lblValidatationMsg.setText("");
    }



}

