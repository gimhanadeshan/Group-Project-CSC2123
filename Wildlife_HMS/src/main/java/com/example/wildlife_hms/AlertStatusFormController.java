package com.example.wildlife_hms;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.utils.StringUtils;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.util.StringConverter;

import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.function.Predicate;

public class AlertStatusFormController implements Initializable {

    @FXML
    private MFXButton btnDelete;

    @FXML
    private MFXButton btnNew;

    @FXML
    private MFXButton btnSave;

    @FXML
    private MFXButton btnUndo;

    @FXML
    private MFXButton btnUpdate;

    @FXML
    private MFXFilterComboBox<AlertStatusModel> combSelectAlertStatus;

    @FXML
    private MFXTextField txtAlertStatus;

    @FXML
    private MFXTextField txtRemarks;

    int id=0;



    DatabaseConnection connectNow=new DatabaseConnection();
    Connection connectDB = connectNow.getConnection();

    OtherMasterFilesController controller =new OtherMasterFilesController();




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        initCombo();


    }

    void initCombo(){
        StringConverter<AlertStatusModel> converter = FunctionalStringConverter.to(statusModel -> (statusModel == null) ? "" : STR."\{statusModel.getAlertStatus()}");
        Function<String, Predicate<AlertStatusModel>> filterFunction = s -> statusModel -> StringUtils.containsIgnoreCase(converter.toString(statusModel), s);
        combSelectAlertStatus.setItems(controller.getAlertStatus());
        combSelectAlertStatus.setConverter(converter);
        combSelectAlertStatus.setFilterFunction(filterFunction);

        combSelectAlertStatus.setOnAction(event -> getData());
    }

    @FXML
    void getData() {

        AlertStatusModel statusModel=combSelectAlertStatus.getSelectionModel().getSelectedItem();
        if(statusModel!=null){
            id=statusModel.getAlertStatusId();
            txtAlertStatus.setText(statusModel.getAlertStatus());
            txtRemarks.setText(statusModel.getRemark());
            btnUpdate.setDisable(false);
            btnDelete.setDisable(false);
            btnUndo.setDisable(false);
            txtAlertStatus.setDisable(false);
            txtRemarks.setDisable(false);
            btnNew.setDisable(true);

        }



    }

   public void newAlertStatus(){
        txtAlertStatus.setDisable(false);
        txtRemarks.setDisable(false);
        combSelectAlertStatus.setDisable(true);
        btnDelete.setDisable(true);
        btnUndo.setDisable(false);
        btnSave.setDisable(false);
        btnUpdate.setDisable(true);
        btnNew.setDisable(true);
    }

    void disableForm(){
        txtAlertStatus.setDisable(true);
        txtRemarks.setDisable(true);
        btnDelete.setDisable(true);
        btnUndo.setDisable(true);
        btnSave.setDisable(true);
        btnUpdate.setDisable(true);
        btnNew.setDisable(false);
        combSelectAlertStatus.setDisable(false);
    }


    private boolean validateFields() {
        return !txtAlertStatus.getText().isEmpty() && !txtRemarks.getText().isEmpty();
    }



    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

   public void clearAlertStatus(){
        txtAlertStatus.clear();
        txtRemarks.clear();
        combSelectAlertStatus.clearSelection();
        disableForm();
    }


    public void saveAlertStatus() {

        if (validateFields()) {

            String query = "Insert into alertstatus(AlertStatus,Remarks)values(?,?)";

            try {
                PreparedStatement statement = connectDB.prepareStatement(query);

                statement.setString(1, txtAlertStatus.getText());
                statement.setString(2, txtRemarks.getText());
                statement.executeUpdate();



                clearAlertStatus();
                showAlert(Alert.AlertType.INFORMATION, "Success", "AlertStatus saved successfully!");
                initCombo();
                disableForm();

            } catch (NumberFormatException | SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Error in saving AlertStatus");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Some fields are invalid");
        }
    }

    public void updateAlertStatus() {

        if (validateFields()) {

            String query = "update alertstatus set AlertStatus=?,Remarks=? where AlertStatusID=?";

            try {
                PreparedStatement statement = connectDB.prepareStatement(query);

                statement.setString(1, txtAlertStatus.getText());
                statement.setString(2, txtRemarks.getText());
                statement.setInt(3,id);
                statement.executeUpdate();


                clearAlertStatus();
                showAlert(Alert.AlertType.INFORMATION, "Success", "AlertStatus updated successfully!");
                initCombo();
                disableForm();

            } catch (NumberFormatException | SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Error in updating AlertStatus");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Some fields are invalid");
        }
    }


  public void deleteAlertStatus() {
        String query = "delete from alertstatus where AlertStatusID=?";
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete");
        alert.setContentText("Are you sure to delete this record?");
        Optional<ButtonType> result = alert.showAndWait();

        try {
            PreparedStatement statement = connectDB.prepareStatement(query);
            statement.setInt(1, id);

            if (result.isPresent() && result.get() == ButtonType.OK) {
                statement.executeUpdate();
            }
            clearAlertStatus();
            initCombo();
            disableForm();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error in deleting AlertStatus");
        }

    }




}
