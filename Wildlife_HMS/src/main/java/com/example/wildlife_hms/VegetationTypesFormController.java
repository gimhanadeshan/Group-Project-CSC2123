package com.example.wildlife_hms;

import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.utils.StringUtils;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.Initializable;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;


import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.lang.Integer.parseInt;

public class VegetationTypesFormController  implements Initializable, LookupHabitatData {

    @FXML
    public Button btnClear;

    @FXML
    public Button btnSave;

    @FXML
    public Button btnUpdate;

    @FXML
    private MFXTextField txtDesc;

    @FXML
    private TextField txtHabitatId;

    @FXML
    private MFXTextField txtHabitatName;

    @FXML
    private MFXTextField txtName;

    @FXML
    private MFXTextField txtid;

    @FXML
    private MFXFilterComboBox<HabitatModel> filterCombo;

    int id=0;


    DatabaseConnection connectNow=new DatabaseConnection();
    Connection connectDB = connectNow.getConnection();

    HabitatFormController habitatFormController=new HabitatFormController();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        initCombobox();

    }

    @FXML
    void initCombobox() {
        StringConverter<HabitatModel> converter = FunctionalStringConverter.to(habitatModel -> (habitatModel == null) ? "" : STR."\{habitatModel.getHaId()} | \{habitatModel.getName()}");
        Function<String, Predicate<HabitatModel>> filterFunction = s -> habitatModel -> StringUtils.containsIgnoreCase(converter.toString(habitatModel), s);
        filterCombo.setItems(habitatFormController.getHabitat());
        filterCombo.setConverter(converter);
        filterCombo.setFilterFunction(filterFunction);

        filterCombo.setOnAction(event -> lookupHabitatName());

        // Set default selection based on text

    }







    @FXML
    void getData(VegetationModel vegetationModel) {

        id=vegetationModel.getId();
        txtid.setText(vegetationModel.getVgId());
        txtName.setText(vegetationModel.getName());
        txtDesc.setText(vegetationModel.getDesc());
        txtHabitatId.setText(String.valueOf(vegetationModel.getHabitatId()));


        lookupHabitatNameForGetData();

    }


    private void clear() {
        txtid.clear();
        txtName.clear();
        txtDesc.clear();
        filterCombo.clearSelection();
        txtHabitatId.clear();
        txtHabitatName.clear();

    }


    void afterUpdateOperation() {

        clear();
        Stage window = (Stage) txtHabitatId.getScene().getWindow();
        window.close();

    }


    private boolean validateFields() {
        return !txtName.getText().isEmpty() && !txtHabitatId.getText().isEmpty();
    }



    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait();
    }




    @FXML
    void clearVegetation() {
        clear();
    }



    @FXML
    void saveVegetation() {

        if (validateFields()) {

            String query = "Insert into vegetationtypes(VegetationName,Description,HabitatID)values(?,?,?)";

            try {
                PreparedStatement statement = connectDB.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

                statement.setString(1, txtName.getText());
                statement.setString(2, txtDesc.getText());
                statement.setInt(3, Integer.parseInt((txtHabitatId.getText())));
                statement.executeUpdate();

                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int vegetationId = generatedKeys.getInt(1);


                    String updateQuery = "UPDATE vegetationtypes SET VG_ID = ? WHERE VegetationID = ?";
                    PreparedStatement updateStatement = connectDB.prepareStatement(updateQuery);
                    String formattedVegetationId = String.format("VG%05d", vegetationId);
                    updateStatement.setString(1, formattedVegetationId);
                    updateStatement.setInt(2, vegetationId);
                    updateStatement.executeUpdate();
                }

                clear();
                showAlert(Alert.AlertType.INFORMATION, "Success", "Vegetation saved successfully!");


            } catch (NumberFormatException | SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Error in saving Vegetation");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Some fields are invalid");
        }
    }

    @FXML
    void updateVegetation(ActionEvent event) {
        if(validateFields()) {
            String query = "update vegetationtypes set VegetationName=?,Description=?,HabitatID=? where VegetationID=?";
            try {
                PreparedStatement statement = connectDB.prepareStatement(query);

                statement.setString(1, txtName.getText());
                statement.setString(2, txtDesc.getText());
                statement.setInt(3, Integer.parseInt(txtHabitatId.getText()));
                statement.setInt(4, id);
                statement.executeUpdate();

                clear();
                showAlert(Alert.AlertType.INFORMATION, "Success", "Vegetation updated successfully!");
                afterUpdateOperation();

            } catch (NumberFormatException | SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Error in updating Vegetation");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Some fields are invalid");
        }
    }


    @Override
    public void lookupHabitatName() {
        try {

            if (!(filterCombo.getSelectedItem()==null)) {


                String habitatIdStr = filterCombo.getSelectedItem().getHaId().trim();


                if (!habitatIdStr.isEmpty()) {
                    String habitatId = habitatIdStr;

                    String query = "SELECT HabitatName,HabitatID FROM habitats WHERE HA_ID = ?";
                    PreparedStatement statement = connectDB.prepareStatement(query);
                    statement.setString(1, habitatId);


                    ResultSet resultSet = statement.executeQuery();

                    if (resultSet.next()) {
                        String habitatName = resultSet.getString("HabitatName");
                        int id = resultSet.getInt("HabitatID");
                        txtHabitatName.setText(habitatName);
                        txtHabitatId.setText(String.valueOf(id));
                    } else {
                        showAlert(Alert.AlertType.WARNING, "Not Found", STR."Habitat with ID \{habitatId} not found");
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
                    String haId=resultSet.getString("HA_ID");
                    txtHabitatName.setText(habitatName);
                    filterCombo.setText(haId);
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




}

