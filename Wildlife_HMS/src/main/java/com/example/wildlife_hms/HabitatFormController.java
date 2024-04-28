package com.example.wildlife_hms;

import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import static java.lang.Integer.parseInt;

public class HabitatFormController implements Initializable{




    DatabaseConnection connectNow=new DatabaseConnection();
    Connection connectDB = connectNow.getConnection();

    @FXML
    public Button btnClear;

    @FXML
    public Button btnSave;

    @FXML
    public Button btnUpdate;


    @FXML
    private MFXTextField txtDesc;

    @FXML
    private MFXTextField txtLocation;

    @FXML
    private MFXTextField txtName;

    @FXML
    private MFXTextField txtSize;

    @FXML
    private MFXTextField txtid;

    int id=0;





    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


    @FXML
    void getData(HabitatModel habitatModel) {


       if (habitatModel != null) {
            id = habitatModel.getId();
            txtName.setText(habitatModel.getName());
            txtLocation.setText(habitatModel.getLocation());
            txtSize.setText(String.valueOf(habitatModel.getSize()));
            txtDesc.setText(habitatModel.getDesc());
            txtid.setText(habitatModel.getHaId());

        }
    }



   public void clear(){
       txtid.setText("");
       txtName.setText("");
       txtLocation.setText("");
       txtSize.setText("");
       txtDesc.setText("");


    }

    void afterUpdateOperation() {

        clear();
        Stage window = (Stage) txtName.getScene().getWindow();
        window.close();


    }





    private boolean validateFields() {
        return !txtName.getText().isEmpty() && !txtSize.getText().isEmpty();
    }

    private boolean validateSize() {
        try {
            Double.parseDouble(txtSize.getText());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait();
    }




    @FXML
    void clearHabitat() {
        clear();
    }


    @FXML
    void saveHabitat() {
        if (validateFields() && validateSize()) {
            String query = "INSERT INTO habitats(HabitatName, Location, Size, Description) VALUES (?, ?, ?, ?)";

            try {
                PreparedStatement statement = connectDB.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

                statement.setString(1, txtName.getText());
                statement.setString(2, txtLocation.getText());
                statement.setDouble(3, Double.parseDouble(txtSize.getText()));
                statement.setString(4, txtDesc.getText());
                statement.executeUpdate();

                // Get the generated HabitatID
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int habitatId = generatedKeys.getInt(1);

                    // Update the "HA_ID" column with the formatted value
                    String updateQuery = "UPDATE habitats SET HA_ID = ? WHERE HabitatID = ?";
                    PreparedStatement updateStatement = connectDB.prepareStatement(updateQuery);
                    String formattedHabitatId = String.format("HA%05d", habitatId);
                    updateStatement.setString(1, formattedHabitatId);
                    updateStatement.setInt(2, habitatId);
                    updateStatement.executeUpdate();
                }

                clear();
                showAlert(Alert.AlertType.INFORMATION, "Success", "Habitat saved successfully!");

            } catch (NumberFormatException | SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", STR."Error in saving habitat:\n \{e.getMessage()}");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Some fields are invalid");
        }
    }


    @FXML
    void updateHabitat() {
        if (validateFields() && validateSize()) {
            String query = "update habitats set HabitatName=?,Location=?,Size=?,Description=? where HabitatID=?";
            try {
                PreparedStatement statement = connectDB.prepareStatement(query);

                statement.setString(1, txtName.getText());
                statement.setString(2, txtLocation.getText());
                statement.setDouble(3, Double.parseDouble((txtSize.getText())));
                statement.setString(4, txtDesc.getText());
                statement.setInt(5, id);
                statement.executeUpdate();

                clear();
                showAlert(Alert.AlertType.INFORMATION, "Success", "Habitat updated successfully!");
                afterUpdateOperation();

            } catch (NumberFormatException | SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", STR."Error in updating habitat: \{e.getMessage()}");

            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Some fields are invalid");

        }
    }









}
