package com.example.wildlife_hms;

import io.github.palexdev.materialfx.controls.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.scene.image.Image;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

public class UserRegisterFormController implements Initializable {

    public Button btnClose;

    public Button btnUpdate;

    public Button btnRegister;

    public Button btnClear;
    public Label regMsgLable;

    @FXML
    private Label PwdMsgLable;



    @FXML
    private MFXComboBox<String> combRoll;

    @FXML
    private MFXDatePicker dateRegDate;

    @FXML
    public MFXPasswordField pwdConfirmPassword;

    @FXML
    public MFXPasswordField pwdPassword;

    @FXML
    private MFXRadioButton rBtnMale;

    @FXML
    private MFXRadioButton rBtnFemale;


    @FXML
    private StackPane stackPaneDp;

    @FXML
    private MFXTextField txtEmail;

    @FXML
    private MFXTextField txtFname;

    @FXML
    private MFXTextField txtLname;

    @FXML
    private MFXTextField txtUsername;

    @FXML
    private MFXCheckbox checkBoxActive;

    int id=0;

    @FXML
    private ImageView imageViewDp;

    private static final String IMAGE_PATH_PREFIX = "file:///"; // Adjust this prefix based on your file system

    DatabaseConnection connectNow=new DatabaseConnection();
    Connection connectDB = connectNow.getConnection();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        dateRegDate.setValue(LocalDate.now());
        combRoll.getItems().addAll("User","Admin","Researcher");

        imageViewDp.setOnDragOver(event -> {
            if (event.getGestureSource() != imageViewDp && event.getDragboard().hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });

        imageViewDp.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasFiles()) {
                success = true;
                String imagePath = IMAGE_PATH_PREFIX + db.getFiles().get(0).getPath();
                imageViewDp.setImage(new Image(imagePath));
            }
            event.setDropCompleted(success);
            event.consume();
        });

    }


    @FXML
    void getData(UserModel userModel) {
        id = userModel.getUserid();
        txtFname.setText(userModel.getFirstName());
        txtLname.setText(userModel.getLastName());
        txtEmail.setText(userModel.getEmail());
        txtUsername.setText(userModel.getUserName());
        pwdPassword.setText(userModel.getPwd());
        combRoll.setText(userModel.getRoll());
        dateRegDate.setValue(userModel.getRegDate().toLocalDate());

        if (userModel.getGender().equals("Female")) {
            rBtnFemale.setSelected(true);
            rBtnMale.setSelected(false);
        } else {
            rBtnFemale.setSelected(false);
            rBtnMale.setSelected(true);
        }

        checkBoxActive.setSelected(userModel.getActive());

        // Load and set profile picture
        String imagePath = userModel.getDp();
        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                Image image = new Image(imagePath);
                imageViewDp.setImage(image);
            } catch (Exception e) {
                // Handle any exceptions related to loading the image
                e.printStackTrace();
            }
        }
    }


    public void clear(){
        txtUsername.clear();
        txtEmail.clear();
        txtLname.clear();
        txtFname.clear();
        dateRegDate.setValue(null);
        rBtnMale.setSelected(false);
        rBtnFemale.setSelected(false);
        pwdPassword.clear();
        pwdConfirmPassword.clear();
        checkBoxActive.setSelected(false);
        combRoll.clearSelection();
        PwdMsgLable.setText("");
        regMsgLable.setText("");
        //imageViewDp.setImage(new Image("img/icons8-user-100.png"));

    }

    void afterUpdateOperation() {

        clear();
        Stage window = (Stage) txtUsername.getScene().getWindow();
        window.close();

    }

    private boolean validateFields() {
        return !txtUsername.getText().isEmpty() && !txtFname.getText().isEmpty() && !pwdPassword.getText().isEmpty() && !dateRegDate.getText().isEmpty() && !combRoll.getText().isEmpty();
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait();
    }

    @FXML
    void clearUser(){
        clear();
    }


    public void btnCloseOnAction(){
        Stage stage=(Stage)btnClose.getScene().getWindow();
        stage.close();
    }


    public void saveUser() {
        if (validateFields()) {
            String query = "INSERT INTO useraccounts (username, password, RegisterDate, FirstName, LastName, Email, Roll, Gender, DP, Active) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try {
                PreparedStatement statement = connectDB.prepareStatement(query);
                statement.setString(1, txtUsername.getText());
                statement.setString(2, pwdPassword.getText());
                statement.setDate(3, Date.valueOf(dateRegDate.getValue()));
                statement.setString(4, txtFname.getText());
                statement.setString(5, txtLname.getText());
                statement.setString(6, txtEmail.getText());
                statement.setString(7, combRoll.getText());

                // Check which radio button is selected for gender
                if (rBtnMale.isSelected()) {
                    statement.setString(8, "Male");
                } else {
                    statement.setString(8, "Female");
                }

                // Save the image path to the database
                statement.setString(9, imageViewDp.getImage() != null ? imageViewDp.getImage().getUrl() : null);

                // Set the active status based on the checkbox state
                statement.setBoolean(10, checkBoxActive.isSelected());

                statement.executeUpdate();

                clear();
                regMsgLable.setAlignment(Pos.BASELINE_CENTER);
                regMsgLable.setText("User Registration Successfully");
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", STR."Error in creating user: \{e.getMessage()}");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Some fields are invalid");
        }
    }

    public void btnRegisterOnAction(){
        if (pwdPassword.getText().equals(pwdConfirmPassword.getText())){
            saveUser();
            PwdMsgLable.setText("");

        }else{
            PwdMsgLable.setAlignment(Pos.BASELINE_CENTER);
            PwdMsgLable.setText("Password does not match!");

        }

    }



    public void updateUser() {
        if (validateFields()) {
            String query = "UPDATE useraccounts SET username=?,password=?,RegisterDate=?,FirstName=?,LastName=?,Email=?,Roll=?, Gender=?, DP=?, Active=? where id =?";
            try {
                PreparedStatement statement = connectDB.prepareStatement(query);
                statement.setString(1, txtUsername.getText());
                statement.setString(2, pwdPassword.getText());
                statement.setDate(3, Date.valueOf(dateRegDate.getValue()));
                statement.setString(4, txtFname.getText());
                statement.setString(5, txtLname.getText());
                statement.setString(6, txtEmail.getText());
                statement.setString(7, combRoll.getText());

                // Check which radio button is selected for gender
                if (rBtnMale.isSelected()) {
                    statement.setString(8, "Male");
                } else {
                    statement.setString(8, "Female");
                }

                // Update the image path in the database
                statement.setString(9, imageViewDp.getImage() != null ? imageViewDp.getImage().getUrl() : null);

                // Set the active status based on the checkbox state
                statement.setBoolean(10, checkBoxActive.isSelected());

                // Assuming you have an id field for the user
                statement.setInt(11, id);

                statement.executeUpdate();

                showAlert(Alert.AlertType.INFORMATION, "Success", "User Profile updated successfully!");
                afterUpdateOperation();
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Error in updating User Profile");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Some fields are invalid");
        }
    }






    public void goBackToLoginForm() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root = fxmlLoader.load();

            // Get the current stage
            Stage stage = (Stage) txtUsername.getScene().getWindow();

            // Set the new scene in the same stage
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





}
