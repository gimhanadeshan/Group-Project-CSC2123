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
import org.mindrot.jbcrypt.BCrypt;

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
        String oldPassword = pwdOld.getText();
        String newPassword = pwdNew.getText();
        String confirmPassword = pwdConfirm.getText();

        if (username.isEmpty() || oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            lblValidatationMsg.setText("Please fill in all fields.");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            lblValidatationMsg.setText("New password and confirm password do not match.");
            return;
        }

        String query = "SELECT password FROM useraccounts WHERE username = ?";
        try (PreparedStatement statement = connectDB.prepareStatement(query)) {
            statement.setString(1, username);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    String storedHashedPassword = rs.getString("password");

                    // Check if the old password matches the stored hashed password
                    if (BCrypt.checkpw(oldPassword, storedHashedPassword)) {
                        // Update the password with the new hashed password
                        String updateQuery = "UPDATE useraccounts SET password = ? WHERE username = ?";
                        try (PreparedStatement updateStatement = connectDB.prepareStatement(updateQuery)) {
                            String newHashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
                            updateStatement.setString(1, newHashedPassword);
                            updateStatement.setString(2, username);

                            int rowsAffected = updateStatement.executeUpdate();
                            if (rowsAffected > 0) {
                                lblValidatationMsg.setText("Password updated successfully.");
                            } else {
                                lblValidatationMsg.setText("Password update failed. Please try again.");
                            }
                        }
                    } else {
                        lblValidatationMsg.setText("Old password is incorrect.");
                    }
                } else {
                    lblValidatationMsg.setText("Username not found.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            lblValidatationMsg.setText("Database error occurred.");
        }

        // Reset form fields
        reset();
    }


    public void reset() {
        pwdOld.clear(); // Clear the old password field
        pwdNew.clear(); // Clear the new password field
        pwdConfirm.clear(); // Clear the confirm password field
        lblValidatationMsg.setText("");
    }



}

