package com.example.wildlife_hms;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.utils.StringUtils;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.File;
import java.net.URL;
import java.sql.*;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.function.Predicate;

public class PopulationFormController implements Initializable,LookupHabitatData,LookupSpeciesData {

    @FXML
    public MFXButton btnClear;

    @FXML
    public MFXButton btnSave;

    @FXML
    public MFXButton btnUpdate;

    @FXML
    private MFXFilterComboBox<HabitatModel> combHabitatID;

    @FXML
    private MFXFilterComboBox<SpeciesModel> combSpeciesID;

    @FXML
    private MFXDatePicker dateLastSurveyDate;

    @FXML
    private TextField txtHabitatId;

    @FXML
    private TextField txtSpeciesId;

    @FXML
    private MFXTextField txtHabitatName;

    @FXML
    private MFXTextField txtSize;

    @FXML
    private MFXTextField txtSpeciesName;

    @FXML
    private MFXTextField txtPopulationID;




    DatabaseConnection connectNow=new DatabaseConnection();
    Connection connectDB = connectNow.getConnection();

    HabitatController habitatController = new HabitatController();
    SpeciesController speciesController = new SpeciesController();

    int id=0;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        StringConverter<HabitatModel> converter = FunctionalStringConverter.to(habitatModel -> (habitatModel == null) ? "" : STR."\{habitatModel.getHaId()} | \{habitatModel.getName()}");
        Function<String, Predicate<HabitatModel>> filterFunction = s -> habitatModel -> StringUtils.containsIgnoreCase(converter.toString(habitatModel), s);
        combHabitatID.setItems(habitatController.getHabitat());
        combHabitatID.setConverter(converter);
        combHabitatID.setFilterFunction(filterFunction);
        combHabitatID.setOnAction(_ -> lookupHabitatName());

        StringConverter<SpeciesModel> converter1 = FunctionalStringConverter.to(habitatModel -> (habitatModel == null) ? "" : STR."\{habitatModel.getSpId()} | \{habitatModel.getCommonName()}");
        Function<String, Predicate<SpeciesModel>> filterFunction1 = s -> speciesModel -> StringUtils.containsIgnoreCase(converter1.toString(speciesModel), s);
        combSpeciesID.setItems(speciesController.getSpecies());
        combSpeciesID.setConverter(converter1);
        combSpeciesID.setFilterFunction(filterFunction1);
        combSpeciesID.setOnAction(_ -> lookupSpeciesName());

    }





    @FXML
    void getData(PopulationModel populationModel) {


        id = populationModel.getPopulationID();
        txtSize.setText(String.valueOf(populationModel.getPopulationSize()));
        dateLastSurveyDate.setValue(populationModel.getLastSurveyDate().toLocalDate());
        txtHabitatId.setText(String.valueOf(populationModel.getHabitatID()));
        txtSpeciesId.setText(String.valueOf(populationModel.getSpeciesID()));
        txtPopulationID.setText(populationModel.getPoId());

        lookupHabitatNameForGetData();
        lookupSpeciesForGetData();


    }

    @FXML
    void clearPopulation() {


        txtSize.clear();
        dateLastSurveyDate.setValue(null); // Clear date value
        combSpeciesID.clearSelection(); // Clear selected species
        combHabitatID.clearSelection(); // Clear selected habitat
        txtHabitatId.clear();
        txtSpeciesId.clear();
        txtHabitatName.clear();
        txtSpeciesName.clear();
        txtPopulationID.clear();


    }




    void afterUpdateOperation() {

        clearPopulation();
        Stage window = (Stage) txtHabitatId.getScene().getWindow();
        window.close();

    }


    private boolean validateFields() {
        return !txtHabitatId.getText().isEmpty() && !txtSize.getText().isEmpty() && !txtSpeciesId.getText().isEmpty() && !dateLastSurveyDate.getText().isEmpty();
    }


    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait();
    }


    @FXML
    void savePopulation() {

        if (validateFields()) {

            String query = "Insert into  population(HabitatID,SpeciesID,PopulationSize,LastSurveyDate)values(?,?,?,?)";

            try {
                PreparedStatement statement = connectDB.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

                statement.setInt(1, Integer.parseInt((txtHabitatId.getText())));
                statement.setInt(2, Integer.parseInt((txtSpeciesId.getText())));
                statement.setInt(3, Integer.parseInt(txtSize.getText()));
                statement.setDate(4, Date.valueOf(dateLastSurveyDate.getValue()));

                statement.executeUpdate();

                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int populationId = generatedKeys.getInt(1);


                    String updateQuery = "UPDATE population SET PO_ID = ? WHERE PopulationID = ?";
                    PreparedStatement updateStatement = connectDB.prepareStatement(updateQuery);
                    String formattedPopulationId = String.format("PL%05d", populationId);
                    updateStatement.setString(1, formattedPopulationId);
                    updateStatement.setInt(2, populationId);
                    updateStatement.executeUpdate();
                }

                clearPopulation();
                showAlert(Alert.AlertType.INFORMATION, "Success", "Population saved successfully!");


            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Error in saving Population");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Some fields are invalid");
        }
    }


    @FXML
    void updatePopulation() {
        if (validateFields()) {
            String query = "UPDATE population SET HabitatID=?, SpeciesID=?, PopulationSize=?,LastSurveyDate=? WHERE PopulationID=?";
            try {
                PreparedStatement statement = connectDB.prepareStatement(query);


                statement.setInt(1, Integer.parseInt(txtHabitatId.getText()));
                statement.setInt(2, Integer.parseInt(txtSpeciesId.getText()));
                statement.setInt(3, Integer.parseInt(txtSize.getText()));
                statement.setDate(4, Date.valueOf(dateLastSurveyDate.getValue()));
                statement.setInt(5, id);
                statement.executeUpdate();

                showAlert(Alert.AlertType.INFORMATION, "Success", "Population updated successfully!");
                afterUpdateOperation();

            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Invalid HabitatID or SpeciesID");
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", STR."Error in updating Population: \{e.getMessage()}");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Some fields are invalid");
        }
    }



    @Override
    public void lookupHabitatName() {
        try {

            if(!(combHabitatID.getSelectedItem()==null)) {
                String habitatIdStr = combHabitatID.getSelectedItem().getHaId().trim();

                if (!habitatIdStr.isEmpty()) {

                    String query = "SELECT HabitatName,HabitatID FROM habitats WHERE HA_ID = ?";
                    PreparedStatement statement = connectDB.prepareStatement(query);
                    statement.setString(1, habitatIdStr);


                    ResultSet resultSet = statement.executeQuery();

                    if (resultSet.next()) {
                        String habitatName = resultSet.getString("HabitatName");
                        int id = resultSet.getInt("HabitatID");
                        txtHabitatName.setText(habitatName);
                        txtHabitatId.setText(String.valueOf(id));
                    } else {
                        showAlert(Alert.AlertType.WARNING, "Not Found", STR."Habitat with ID \{habitatIdStr} not found");
                        txtHabitatName.clear();
                    }
                } else {
                    showAlert(Alert.AlertType.WARNING, "Empty Field", "Please enter a HabitatID");
                    txtHabitatName.clear();
                }
            }
        } catch (NumberFormatException | SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error in looking up Habitat Name");
            e.printStackTrace(); // You may want to log the exception for debugging purposes
        }
    }

    @Override
    public void lookupHabitatNameForGetData() {
        try {
            String habitatIdStr = txtHabitatId.getText().trim();

            if (!habitatIdStr.isEmpty()) {
                int habitatId = Integer.parseInt(habitatIdStr);

                String query = "SELECT HabitatName,HA_ID FROM habitats WHERE HabitatID = ?";
                PreparedStatement statement = connectDB.prepareStatement(query);
                statement.setInt(1, habitatId);

                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    String habitatName = resultSet.getString("HabitatName");
                    String haId = resultSet.getString("HA_ID");
                    txtHabitatName.setText(habitatName);
                    combHabitatID.setText(haId);
                } else {
                    showAlert(Alert.AlertType.WARNING, "Not Found", STR."Habitat with ID \{habitatId} not found");
                    txtHabitatName.clear();
                }
            } else {
                showAlert(Alert.AlertType.WARNING, "Empty Field", "Please enter a HabitatID");
                txtHabitatName.clear();
            }
        } catch (NumberFormatException | SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error in looking up Habitat Name");
            e.printStackTrace(); // You may want to log the exception for debugging purposes
        }
    }

    @Override
    public void lookupSpeciesName() {
        try {
            if(!(combSpeciesID.getSelectedItem()==null)) {
                String speciesIdStr = combSpeciesID.getSelectedItem().getSpId().trim();

                if (!speciesIdStr.isEmpty()) {

                    String query = "SELECT CommonName,SpeciesID  FROM species WHERE SP_ID  = ?";
                    PreparedStatement statement = connectDB.prepareStatement(query);
                    statement.setString(1, speciesIdStr);


                    ResultSet resultSet = statement.executeQuery();

                    if (resultSet.next()) {
                        String commonName = resultSet.getString("CommonName");
                        int id = resultSet.getInt("SpeciesID");
                        txtSpeciesName.setText(commonName);
                        txtSpeciesId.setText(String.valueOf(id));
                    } else {
                        showAlert(Alert.AlertType.WARNING, "Not Found", STR."Species with ID \{speciesIdStr} not found");
                        txtSpeciesName.clear();
                    }
                } else {
                    showAlert(Alert.AlertType.WARNING, "Empty Field", "Please enter a SpeciesID");
                    txtSpeciesName.clear();
                }

            }
        } catch (NumberFormatException | SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error in looking up Species CommonName");
            e.printStackTrace(); // You may want to log the exception for debugging purposes
        }
    }

    @Override
    public void lookupSpeciesForGetData() {
        try {
            String speciesIdStr = txtSpeciesId.getText().trim();

            if (!speciesIdStr.isEmpty()) {
                int speciesId = Integer.parseInt(speciesIdStr);

                String query = "SELECT CommonName,SP_ID  FROM species WHERE SpeciesID  = ?";
                PreparedStatement statement = connectDB.prepareStatement(query);
                statement.setInt(1, speciesId);

                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    String commonName = resultSet.getString("CommonName");
                    String spId = resultSet.getString("SP_ID");
                    txtSpeciesName.setText(commonName);
                    combSpeciesID.setText(spId);
                } else {
                    showAlert(Alert.AlertType.WARNING, "Not Found", STR."Species with ID \{speciesId} not found");
                    txtSpeciesName.clear();
                }
            } else {
                showAlert(Alert.AlertType.WARNING, "Empty Field", "Please enter a SpeciesID");
                txtSpeciesName.clear();
            }
        } catch (NumberFormatException | SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error in looking up Species CommonName");
            e.printStackTrace(); // You may want to log the exception for debugging purposes
        }
    }


}
