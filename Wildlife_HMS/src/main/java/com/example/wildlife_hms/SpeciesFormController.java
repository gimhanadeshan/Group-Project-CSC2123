package com.example.wildlife_hms;

import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.utils.StringUtils;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.function.Predicate;


public class SpeciesFormController implements Initializable {


    DatabaseConnection connectNow=new DatabaseConnection();
    Connection connectDB = connectNow.getConnection();

    @FXML
    public Button btnClear;


    @FXML
    public Button btnSave;

    @FXML
    public Button btnUpdate;


    @FXML
    private MFXTextField txtCommonName;

    @FXML
    private MFXTextField txtConversationStatus;

    @FXML
    private MFXTextField txtDescription;

    @FXML
    private MFXTextField txtScientificName;


    @FXML
    private MFXTextField txtSpeciesId;

    @FXML
    private MFXFilterComboBox<ConservationStatusModel> combConversationStatus;

    OtherMasterFilesController controller=new OtherMasterFilesController();

    int id=0;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

       // combConversationStatus.getItems().addAll("Endangered","Least Concern","Not Evaluated");
        StringConverter<ConservationStatusModel> converter = FunctionalStringConverter.to(conservationStatusModel -> (conservationStatusModel == null) ? "" : STR."\{conservationStatusModel.getConservationStatus()}");
        Function<String,Predicate<ConservationStatusModel>> filterFunction = s -> conservationStatusModel -> StringUtils.containsIgnoreCase(converter.toString(conservationStatusModel), s);
        combConversationStatus.setItems(controller.getConservationStatus());
        combConversationStatus.setConverter(converter);
        combConversationStatus.setFilterFunction(filterFunction);

    }






    @FXML
    void getData(SpeciesModel speciesModel ) {
       // SpeciesModel speciesModel=tblSpecies.getSelectionModel().getSelectedItem();
        id=speciesModel.getId();
        txtSpeciesId.setText(speciesModel.getSpId());
        txtCommonName.setText(speciesModel.getCommonName());
        txtScientificName.setText(speciesModel.getScientificName());
        txtDescription.setText(speciesModel.getDesc());
        combConversationStatus.setText(speciesModel.getConservationStatus());


    }



    public void clear(){
        txtSpeciesId.clear();
        txtCommonName.clear();
        txtScientificName.clear();
        txtDescription.clear();
        combConversationStatus.setValue(null);


    }

    void afterUpdateOperation() {

        clear();
        Stage window = (Stage) txtSpeciesId.getScene().getWindow();
        window.close();


    }





    private boolean validateFields() {
        return !txtCommonName.getText().isEmpty() && !combConversationStatus.getText().isEmpty();
    }



    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait();
    }




    @FXML
    void clearSpecies() {
        clear();
    }



    @FXML
    void saveSpecies() {

        if (validateFields()) {

            String query = "Insert into species(CommonName,ScientificName,ConservationStatus,Description)values(?,?,?,?)";

            try {
                PreparedStatement statement = connectDB.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

                statement.setString(1, txtCommonName.getText());
                statement.setString(2, txtScientificName.getText());
                statement.setString(3, combConversationStatus.getText());
                statement.setString(4, txtDescription.getText());
                statement.executeUpdate();


                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int speciesId = generatedKeys.getInt(1);


                    String updateQuery = "UPDATE species SET SP_ID = ? WHERE SpeciesID = ?";
                    PreparedStatement updateStatement = connectDB.prepareStatement(updateQuery);
                    String formattedSpeciesId = String.format("SP%05d", speciesId);
                    updateStatement.setString(1, formattedSpeciesId);
                    updateStatement.setInt(2, speciesId);
                    updateStatement.executeUpdate();
                }

                clear();
                showAlert(Alert.AlertType.INFORMATION, "Success", "Species saved successfully!");


            } catch (NumberFormatException | SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Error in saving Species");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Some fields are invalid");
        }
    }

    @FXML
    void updateSpecies() {
        if(validateFields()) {
            String query = "update species set CommonName=?,ScientificName=?,ConservationStatus=?,Description=? where SpeciesID=?";
            try {
                PreparedStatement statement = connectDB.prepareStatement(query);

                statement.setString(1, txtCommonName.getText());
                statement.setString(2, txtScientificName.getText());
                statement.setString(3, combConversationStatus.getText());
                statement.setString(4, txtDescription.getText());
                statement.setInt(5, id);
                statement.executeUpdate();


                clear();
                showAlert(Alert.AlertType.INFORMATION, "Success", "Species updated successfully!");
                afterUpdateOperation();

            } catch (NumberFormatException | SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Error in updating habitat");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Some fields are invalid");
        }
    }



}
