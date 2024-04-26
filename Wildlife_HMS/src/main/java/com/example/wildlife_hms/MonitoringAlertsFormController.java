package com.example.wildlife_hms;

import io.github.palexdev.materialfx.controls.MFXDatePicker;
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





public class MonitoringAlertsFormController implements Initializable, LookupHabitatData {






    @FXML
    public Button btnSave;

    @FXML
    public Button btnUpdate;

    @FXML
    public Button btnClear;



    @FXML
    private MFXDatePicker dateAlertDate;

    @FXML
    private MFXFilterComboBox<HabitatModel> filterCombo;

    @FXML
    private MFXTextField txtAlertDescription;

    @FXML
    private MFXTextField txtAlertType;

    @FXML
    private MFXTextField txtAlertid;

    @FXML
    private TextField txtHabitatId;

    @FXML
    private MFXTextField txtHabitatName;

    @FXML
    private MFXFilterComboBox<AlertStatusModel> combAlertStatus;

    @FXML
    private MFXFilterComboBox<AlertTypeModel> combAlertType;





    int id=0;


    DatabaseConnection connectNow=new DatabaseConnection();
    Connection connectDB = connectNow.getConnection();

    HabitatController habitatController=new HabitatController();
    OtherMasterFilesController otherMasterFilesController=new OtherMasterFilesController();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        StringConverter<HabitatModel> converter = FunctionalStringConverter.to(habitatModel -> (habitatModel == null) ? "" : STR."\{habitatModel.getHaId()} | \{habitatModel.getName()}");
        Function<String, Predicate<HabitatModel>> filterFunction = s -> habitatModel -> StringUtils.containsIgnoreCase(converter.toString(habitatModel), s);
        filterCombo.setItems(habitatController.getHabitat());
        filterCombo.setConverter(converter);
        filterCombo.setFilterFunction(filterFunction);



        filterCombo.setOnAction(event -> lookupHabitatName());

        StringConverter<AlertTypeModel> converter1 = FunctionalStringConverter.to(alertTypeModel -> (alertTypeModel == null) ? "" : STR."\{alertTypeModel.getAlertType()}");
        Function<String, Predicate<AlertTypeModel>> filterFunction1 = s -> alertTypeModel -> StringUtils.containsIgnoreCase(converter1.toString(alertTypeModel), s);
        combAlertType.setItems(otherMasterFilesController.getAlertTypes());
        combAlertType.setConverter(converter1);
        combAlertType.setFilterFunction(filterFunction1);


        StringConverter<AlertStatusModel> converter2 = FunctionalStringConverter.to(alertStatusModel -> (alertStatusModel == null) ? "" : STR."\{alertStatusModel.getAlertStatus()}");
        Function<String, Predicate<AlertStatusModel>> filterFunction2 = s -> alertStatusModel -> StringUtils.containsIgnoreCase(converter2.toString(alertStatusModel), s);
        combAlertStatus.setItems(otherMasterFilesController.getAlertStatus());
        combAlertStatus.setConverter(converter2);
        combAlertStatus.setFilterFunction(filterFunction2);




    }





    @FXML
    void getData(AlertModel alertModel) {


        id=alertModel.getAlertId();
        combAlertType.setText(alertModel.getAlertType());
        txtAlertDescription.setText(alertModel.getAlertDisc());
        dateAlertDate.setValue(alertModel.getAlertDate().toLocalDate());
        txtHabitatId.setText(String.valueOf(alertModel.getHabitatID()));
        txtAlertid.setText(alertModel.getAlId());
        combAlertStatus.setText(alertModel.getAlertStatus());


        lookupHabitatNameForGetData();


    }





    public void clear(){
        txtAlertid.clear();
        txtHabitatId.clear();
        combAlertType.clearSelection();
        txtAlertDescription.clear();
        dateAlertDate.setValue(null);
        txtHabitatName.clear();
        filterCombo.clearSelection();
        combAlertStatus.clearSelection();



    }

    void afterUpdateOperation() {

        clear();
        Stage window = (Stage) txtHabitatId.getScene().getWindow();
        window.close();

    }





    private boolean validateFields() {
        return !combAlertType.getText().isEmpty() && !txtHabitatId.getText().isEmpty() && !txtAlertDescription.getText().isEmpty() && !dateAlertDate.getText().isEmpty();
    }




    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait();
    }




    @FXML
    void clearAlerts() {
        clear();
    }



    @FXML
    void saveAlerts() {

        if (validateFields()) {

            String query = "Insert into monitoringalerts(AlertType,AlertDescription,AlertDate,HabitatID,AlertStatus)values(?,?,?,?,?)";

            try {
                PreparedStatement statement = connectDB.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

                statement.setString(1, combAlertType.getText());
                statement.setString(2, txtAlertDescription.getText());
                statement.setDate(3, Date.valueOf(dateAlertDate.getValue()));
                statement.setInt(4, Integer.parseInt((txtHabitatId.getText())));
                statement.setString(5, combAlertStatus.getText());
                statement.executeUpdate();

                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int alertId = generatedKeys.getInt(1);


                    String updateQuery = "UPDATE monitoringalerts SET AL_ID = ? WHERE AlertID = ?";
                    PreparedStatement updateStatement = connectDB.prepareStatement(updateQuery);
                    String formattedVegetationId = String.format("AL%05d", alertId);
                    updateStatement.setString(1, formattedVegetationId);
                    updateStatement.setInt(2, alertId);
                    updateStatement.executeUpdate();
                }

                clear();
                showAlert(Alert.AlertType.INFORMATION, "Success", "Alerts saved successfully!");


            } catch (SQLException e) {
               // showAlert(Alert.AlertType.ERROR, "Database Error", "Error in saving Alerts");
                showAlert(Alert.AlertType.ERROR, "Database Error", STR."Error in saving Alerts:\n\{e.getMessage()}");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Some fields are invalid");
        }
    }

    @FXML
    void updateAlerts() {
        if (validateFields()) {

            String query = "UPDATE monitoringalerts SET AlertType=?,AlertDescription=?,AlertDate=?,HabitatID=?,AlertStatus=? where AlertID=?";
            try {
                PreparedStatement statement = connectDB.prepareStatement(query);

                statement.setString(1, combAlertType.getText());
                statement.setString(2, txtAlertDescription.getText());
                statement.setDate(3, Date.valueOf(dateAlertDate.getValue()));
                statement.setInt(4, Integer.parseInt((txtHabitatId.getText())));
                statement.setString(5, combAlertStatus.getText());
                statement.setInt(6, id);
                statement.executeUpdate();

                showAlert(Alert.AlertType.INFORMATION, "Success", "Alerts updated successfully!");
                afterUpdateOperation();


            } catch (NumberFormatException | SQLException e) {
                //showAlert(Alert.AlertType.ERROR, "Database Error", "Error in updating Alerts");
                showAlert(Alert.AlertType.ERROR, "Database Error", STR."Error in updating Alerts:\n\{e.getMessage()}");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Some fields are invalid");
        }
    }



    @Override
    public void lookupHabitatName() {
        try {

            if(!(filterCombo.getSelectedItem()==null)) {
                String habitatIdStr = filterCombo.getSelectionModel().getSelectedItem().getHaId().trim();

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
