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

import static java.lang.Integer.parseInt;


public class EnvironmentalConditionsFormController implements Initializable, LookupHabitatData {

    @FXML
    public Button btnClear;



    @FXML
    public Button btnSave;

    @FXML
    public Button btnUpdate;




    @FXML
    private TextField txtHabitatId;

    @FXML
    private MFXTextField txtConditionId;

    @FXML
    private MFXTextField txtConditionType;

    @FXML
    private MFXTextField txtConditionValue;


    @FXML
    private MFXTextField txtHabitatName;





    @FXML
    private MFXFilterComboBox<HabitatModel> filterCombo;





    int id=0;


    DatabaseConnection connectNow=new DatabaseConnection();
    Connection connectDB = connectNow.getConnection();

    HabitatFormController habitatFormController=new HabitatFormController();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        StringConverter<HabitatModel> converter = FunctionalStringConverter.to(habitatModel -> (habitatModel == null) ? "" : STR."\{habitatModel.getHaId()} | \{habitatModel.getName()}");
        Function<String, Predicate<HabitatModel>> filterFunction = s -> habitatModel -> StringUtils.containsIgnoreCase(converter.toString(habitatModel), s);
        filterCombo.setItems(habitatFormController.getHabitat());
        filterCombo.setConverter(converter);
        filterCombo.setFilterFunction(filterFunction);



        filterCombo.setOnAction(event -> lookupHabitatName());


        

    }





    @FXML
    void getData(EnvironmentalConditionsModel environmentalConditionsModel) {

        id=environmentalConditionsModel.getConditionID();
        txtConditionId.setText(environmentalConditionsModel.getConId());
        txtConditionType.setText(environmentalConditionsModel.getConditionType());
        txtConditionValue.setText(String.valueOf(environmentalConditionsModel.getConditionValue()));
        txtHabitatId.setText(String.valueOf(environmentalConditionsModel.getHabitatID()));


        lookupHabitatNameForGetData();

    }



   public void clear(){
        txtConditionId.clear();
        txtHabitatId.clear();
        txtConditionValue.clear();
        txtConditionType.clear();
        txtHabitatName.clear();
        filterCombo.clearSelection();


    }


    void afterUpdateOperation() {

        clear();
        Stage window = (Stage) txtHabitatId.getScene().getWindow();
        window.close();

    }




    private boolean validateFields() {
        return !txtConditionType.getText().isEmpty() && !txtHabitatId.getText().isEmpty();
    }


    private boolean validateSize() {
        try {
            Double.parseDouble(txtConditionValue.getText());
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
    void clearCondition() {
        clear();
    }



    @FXML
    void saveCondition() {

        if (validateFields() && validateSize()) {

            String query = "Insert into environmentalconditions(ConditionType,ConditionValue,HabitatID)values(?,?,?)";

            try {
                PreparedStatement statement = connectDB.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

                statement.setString(1, txtConditionType.getText());
                statement.setDouble(2, Double.parseDouble(txtConditionValue.getText()));
                statement.setInt(3, Integer.parseInt((txtHabitatId.getText())));
                statement.executeUpdate();

                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int conditionId = generatedKeys.getInt(1);


                    String updateQuery = "UPDATE environmentalconditions SET EC_ID = ? WHERE ConditionID = ?";
                    PreparedStatement updateStatement = connectDB.prepareStatement(updateQuery);
                    String formattedVegetationId = String.format("EC%05d", conditionId);
                    updateStatement.setString(1, formattedVegetationId);
                    updateStatement.setInt(2, conditionId);
                    updateStatement.executeUpdate();
                }

                clear();
                showAlert(Alert.AlertType.INFORMATION, "Success", "Condition saved successfully!");


            } catch (NumberFormatException | SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Error in saving Condition");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Some fields are invalid");
        }
    }

    @FXML
    void updateCondition() {
        if(validateFields() && validateSize()) {

            String query = "UPDATE environmentalconditions SET ConditionType=?,ConditionValue=?,HabitatID=? where ConditionID=?";
            try {
                PreparedStatement statement = connectDB.prepareStatement(query);

                statement.setString(1, txtConditionType.getText());
                statement.setDouble(2, Double.parseDouble(txtConditionValue.getText()));
                statement.setInt(3, Integer.parseInt(txtHabitatId.getText()));
                statement.setInt(4, id);
                statement.executeUpdate();


                showAlert(Alert.AlertType.INFORMATION, "Success", "Condition updated successfully!");
                afterUpdateOperation();


            } catch (NumberFormatException | SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Error in updating Condition");
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
