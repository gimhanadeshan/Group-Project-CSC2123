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

public class AlertTypesFormController implements Initializable {

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
    private MFXFilterComboBox<AlertTypeModel> combSelectAlertType;

    @FXML
    private MFXTextField txtAlertType;

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
        StringConverter<AlertTypeModel> converter = FunctionalStringConverter.to( typeModel-> (typeModel == null) ? "" : STR."\{typeModel.getAlertType()}");
        Function<String, Predicate<AlertTypeModel>> filterFunction = s -> typeModel -> StringUtils.containsIgnoreCase(converter.toString(typeModel), s);
        combSelectAlertType.setItems(controller.getAlertTypes());
        combSelectAlertType.setConverter(converter);
        combSelectAlertType.setFilterFunction(filterFunction);
        combSelectAlertType.setOnAction(event -> getData());
    }

    void getData() {
        AlertTypeModel typeModel=combSelectAlertType.getSelectionModel().getSelectedItem();
        if(typeModel!=null){
            id=typeModel.getAlertTypeId();
            txtAlertType.setText(typeModel.getAlertType());
            txtRemarks.setText(typeModel.getRemark());
            btnUpdate.setDisable(false);
            btnDelete.setDisable(false);
            btnUndo.setDisable(false);
            txtAlertType.setDisable(false);
            txtRemarks.setDisable(false);
            btnNew.setDisable(true);

        }
    }

    public void newAlertType(){
        txtAlertType.setDisable(false);
        txtRemarks.setDisable(false);
        combSelectAlertType.setDisable(true);
        btnDelete.setDisable(true);
        btnUndo.setDisable(false);
        btnSave.setDisable(false);
        btnUpdate.setDisable(true);
        btnNew.setDisable(true);
    }

    void disableForm(){
        txtAlertType.setDisable(true);
        txtRemarks.setDisable(true);
        btnDelete.setDisable(true);
        btnUndo.setDisable(true);
        btnSave.setDisable(true);
        btnUpdate.setDisable(true);
        btnNew.setDisable(false);
        combSelectAlertType.setDisable(false);
    }

    private boolean validateFields() {
        return !txtAlertType.getText().isEmpty() && !txtRemarks.getText().isEmpty();
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
    public void clearAlertType(){
        txtAlertType.clear();
        txtRemarks.clear();
        combSelectAlertType.clearSelection();
        disableForm();
    }
    public void saveAlertType() {

        if (validateFields()) {

            String query = "Insert into alerttypes(AlertType,Remarks)values(?,?)";

            try {
                PreparedStatement statement = connectDB.prepareStatement(query);

                statement.setString(1, txtAlertType.getText());
                statement.setString(2, txtRemarks.getText());
                statement.executeUpdate();



                clearAlertType();
                showAlert(Alert.AlertType.INFORMATION, "Success", "AlertType saved successfully!");
                initCombo();
                disableForm();

            } catch (NumberFormatException | SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Error in saving AlertType");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Some fields are invalid");
        }
    }
    public void updateAlertType() {

        if (validateFields()) {

            String query = "update alerttypes set AlertType=?,Remarks=? where AlertTypeID=?";

            try {
                PreparedStatement statement = connectDB.prepareStatement(query);

                statement.setString(1, txtAlertType.getText());
                statement.setString(2, txtRemarks.getText());
                statement.setInt(3,id);
                statement.executeUpdate();


                clearAlertType();
                showAlert(Alert.AlertType.INFORMATION, "Success", "AlertType updated successfully!");
                initCombo();
                disableForm();

            } catch (NumberFormatException | SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Error in updating AlertType");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Some fields are invalid");
        }
    }
    public void deleteAlertType() {
        String query = "delete from alerttypes where AlertTypeID=?";
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
            clearAlertType();
            initCombo();
            disableForm();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error in deleting AlertType");
        }

    }



}
