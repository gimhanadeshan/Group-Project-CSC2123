package com.example.wildlife_hms;

import io.github.palexdev.materialfx.controls.MFXCheckbox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserPermissionsFormController {


    @FXML
    private MFXCheckbox checkCreate;

    @FXML
    private MFXCheckbox checkDelete;

    @FXML
    private MFXCheckbox checkFieldData;

    @FXML
    private MFXCheckbox checkHabitat;

    @FXML
    private MFXCheckbox checkOtherFiles;

    @FXML
    private MFXCheckbox checkReporting;

    @FXML
    private MFXCheckbox checkResearch;

    @FXML
    private MFXCheckbox checkSettings;

    @FXML
    private MFXCheckbox checkUpdate;

    @FXML
    private MFXCheckbox checkUser;

    @FXML
    private MFXCheckbox checkView;

    @FXML
    private MFXTextField txtUserName;

    int id=0;


    DatabaseConnection connectNow=new DatabaseConnection();
    Connection connectDB = connectNow.getConnection();



    void getData(UserPermissionsModel userPermissionsModel) {

        id=userPermissionsModel.getId();
        txtUserName.setText(userPermissionsModel.getUserName());
        checkCreate.setSelected(userPermissionsModel.isCreate());
        checkDelete.setSelected(userPermissionsModel.isDelete());
        checkUpdate.setSelected(userPermissionsModel.isUpdate());
        checkView.setSelected(userPermissionsModel.isView());
        checkHabitat.setSelected(userPermissionsModel.isHabitatManagement());
        checkUser.setSelected(userPermissionsModel.isUserManagement());
        checkFieldData.setSelected(userPermissionsModel.isFieldDataCollection());
        checkResearch.setSelected(userPermissionsModel.isResearch());
        checkReporting.setSelected(userPermissionsModel.isReporting());
        checkOtherFiles.setSelected(userPermissionsModel.isOtherFiles());
        checkSettings.setSelected(userPermissionsModel.isSettings());

    }


    void afterUpdateOperation(){

        Stage window = (Stage) txtUserName.getScene().getWindow();
        window.close();

    }


    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait();
    }


    public void saveChanges() {

            String query = "UPDATE userpermissions SET HabitatManagement=?,UserManagement=?,FieldDataCollection=?,Research=?,Reporting=?,Settings=?,OtherFiles=?,CreateP=?,DeleteP=?,UpdateP=?,View=? WHERE id=?";
            try {
                PreparedStatement statement = connectDB.prepareStatement(query);
                statement.setBoolean(1, checkHabitat.isSelected());
                statement.setBoolean(2, checkUser.isSelected());
                statement.setBoolean(3, checkFieldData.isSelected());
                statement.setBoolean(4, checkResearch.isSelected());
                statement.setBoolean(5, checkReporting.isSelected());
                statement.setBoolean(6, checkSettings.isSelected());
                statement.setBoolean(7, checkOtherFiles.isSelected());
                statement.setBoolean(8, checkCreate.isSelected());
                statement.setBoolean(9, checkDelete.isSelected());
                statement.setBoolean(10, checkUpdate.isSelected());
                statement.setBoolean(11, checkView.isSelected());
                statement.setInt(12, id);

                statement.executeUpdate();
                showAlert(Alert.AlertType.INFORMATION, "Success", "Permissions Changed successfully!");
                afterUpdateOperation();

            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", STR."Error in changing permissions: \n\{e.getMessage()}");
            }

    }

    public void resetDefault(){


    }





}
