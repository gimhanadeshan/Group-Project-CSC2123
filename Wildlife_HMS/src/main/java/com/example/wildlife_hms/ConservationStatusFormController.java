package com.example.wildlife_hms;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.utils.StringUtils;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.util.StringConverter;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.function.Predicate;

public class ConservationStatusFormController implements Initializable {

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
    private MFXFilterComboBox<ConservationStatusModel> combSelectConservationStatus;

    @FXML
    private MFXTextField txtConservationStatus;

    @FXML
    private MFXTextField txtRemarks;
    DatabaseConnection connectNow=new DatabaseConnection();
    Connection connectDB = connectNow.getConnection();

    OtherMasterFilesController controller =new OtherMasterFilesController();
    int id=0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        initCombo();

    }

    void initCombo(){
        StringConverter<ConservationStatusModel> converter = FunctionalStringConverter.to(ConservationStatusModel -> (ConservationStatusModel == null) ? "" : STR."\{ConservationStatusModel.getConservationStatus()}");
        Function<String, Predicate<ConservationStatusModel>> filterFunction = s -> ConservationStatusModel -> StringUtils.containsIgnoreCase(converter.toString(ConservationStatusModel), s);
        combSelectConservationStatus.setItems(controller.getConservationStatus());
        combSelectConservationStatus.setConverter(converter);
        combSelectConservationStatus.setFilterFunction(filterFunction);

        combSelectConservationStatus.setOnAction(event -> getData());
    }

    private void getData() {
        ConservationStatusModel statusModel=combSelectConservationStatus.getSelectionModel().getSelectedItem();
        if(statusModel!=null){
            id=statusModel.getConservationStatusId();
            txtConservationStatus.setText(statusModel.getConservationStatus());
            txtRemarks.setText(statusModel.getRemark());
            btnUpdate.setDisable(false);
            btnDelete.setDisable(false);
            btnUndo.setDisable(false);
            txtConservationStatus.setDisable(false);
            txtRemarks.setDisable(false);
            btnNew.setDisable(true);

        }
    }
    public void newConservationAlertStatus(){
        txtConservationStatus.setDisable(false);
        txtRemarks.setDisable(false);
        combSelectConservationStatus.setDisable(true);
        btnDelete.setDisable(true);
        btnUndo.setDisable(false);
        btnSave.setDisable(false);
        btnUpdate.setDisable(true);
        btnNew.setDisable(true);
    }

    void disableForm(){
        txtConservationStatus.setDisable(true);
        txtRemarks.setDisable(true);
        btnDelete.setDisable(true);
        btnUndo.setDisable(true);
        btnSave.setDisable(true);
        btnUpdate.setDisable(true);
        btnNew.setDisable(false);
        combSelectConservationStatus.setDisable(false);
    }


    private boolean validateFields() {
        return !txtConservationStatus.getText().isEmpty() && !txtRemarks.getText().isEmpty();
    }



    private void showConservationAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void clearConservationAlertStatus(){
        txtConservationStatus.clear();
        txtRemarks.clear();
        combSelectConservationStatus.clearSelection();
        disableForm();
    }
    public void saveConservationAlertStatus() {

        if (validateFields()) {

            String query = "Insert into conservationstatus(ConservationStatus,Remark)values(?,?)";

            try {
                PreparedStatement statement = connectDB.prepareStatement(query);

                statement.setString(1, txtConservationStatus.getText());
                statement.setString(2, txtRemarks.getText());
                statement.executeUpdate();



                clearConservationAlertStatus();
                showConservationAlert(Alert.AlertType.INFORMATION, "Success", "ConservationStatus saved successfully!");
                initCombo();
                disableForm();

            } catch (SQLException e) {
                showConservationAlert(Alert.AlertType.ERROR, "Database Error", "Error in saving ConservationStatus");
            }
        } else {
            showConservationAlert(Alert.AlertType.ERROR, "Validation Error", "Some fields are invalid");
        }
    }
    public void updateConservationAlertStatus() {

        if (validateFields()) {

            String query = "update conservationstatus set ConservationStatus=?,Remark=? where ConservationStatusID=?";

            try {
                PreparedStatement statement = connectDB.prepareStatement(query);

                statement.setString(1, txtConservationStatus.getText());
                statement.setString(2, txtRemarks.getText());
                statement.setInt(3,id);
                statement.executeUpdate();


                clearConservationAlertStatus();
                showConservationAlert(Alert.AlertType.INFORMATION, "Success", "ConservationStatus updated successfully!");
                initCombo();
                disableForm();

            } catch (NumberFormatException | SQLException e) {
                showConservationAlert(Alert.AlertType.ERROR, "Database Error", "Error in updating ConservationStatus");
            }
        } else {
            showConservationAlert(Alert.AlertType.ERROR, "Validation Error", "Some fields are invalid");
        }
    }


    public void deleteConservationAlertStatus() {
        String query = "delete from conservationstatus where ConservationStatusID=?";
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
            clearConservationAlertStatus();
            initCombo();
            disableForm();
        } catch (SQLException e) {
            showConservationAlert(Alert.AlertType.ERROR, "Database Error", "Error in deleting ConservationStatus");
        }

    }


}

