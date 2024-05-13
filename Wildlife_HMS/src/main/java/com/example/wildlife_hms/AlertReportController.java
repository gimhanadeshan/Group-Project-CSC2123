package com.example.wildlife_hms;

import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.utils.StringUtils;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.function.Predicate;


public class AlertReportController implements Initializable {


    @FXML
    private TableColumn<AlertModel, String> colHabitatName;

    @FXML
    private TableColumn<AlertModel, Date> colAlertDate;

    @FXML
    private TableColumn<AlertModel, String> colAlertType;

    @FXML
    private TableColumn<AlertModel, String> colAlertDescription;

    @FXML
    private MFXFilterComboBox<HabitatModel> combHabitatName;

    @FXML
    private MFXFilterComboBox<AlertTypeModel> combAlertType;

    @FXML
    private MFXDatePicker dateFrom;

    @FXML
    private MFXDatePicker dateTo;

    @FXML
    private TableView<AlertModel> tblAlertsReport;

    @FXML
    private TableColumn<AlertModel,String> colAlertStatus;


    DatabaseConnection connectNow=new DatabaseConnection();
    Connection connectDB = connectNow.getConnection();

    HabitatController habitatController=new HabitatController();
    OtherMasterFilesController otherMasterFilesController=new OtherMasterFilesController();


    public void initialize(URL url, ResourceBundle resourceBundle) {

        StringConverter<HabitatModel> converter = FunctionalStringConverter.to(habitatModel -> (habitatModel == null) ? "" : STR."\{habitatModel.getName()}");
        Function<String, Predicate<HabitatModel>> filterFunction = s -> habitatModel -> StringUtils.containsIgnoreCase(converter.toString(habitatModel), s);
        combHabitatName.setItems(habitatController.getHabitat());
        combHabitatName.setConverter(converter);
        combHabitatName.setFilterFunction(filterFunction);

        StringConverter<AlertTypeModel> converter1 = FunctionalStringConverter.to(alertTypeModel -> (alertTypeModel == null) ? "" : STR."\{alertTypeModel.getAlertType()}");
        Function<String, Predicate<AlertTypeModel>> filterFunction1 = s -> alertTypeModel -> StringUtils.containsIgnoreCase(converter1.toString(alertTypeModel), s);
        combAlertType.setItems(otherMasterFilesController.getAlertTypes());
        combAlertType.setConverter(converter1);
        combAlertType.setFilterFunction(filterFunction1);

        showAlertReport();

    }




    public ObservableList<AlertModel>getAlerts() {


        ObservableList<AlertModel> alertModels= FXCollections.observableArrayList();
        String query="SELECT a.*,h.HabitatName FROM monitoringalerts a LEFT JOIN habitats h ON a.HabitatID = h.HabitatID ";
        try {
            Statement statement=connectDB.createStatement();
            ResultSet output =statement.executeQuery(query);
            while (output.next()){
                AlertModel al =new AlertModel(
                        output.getString("AlertType"),
                        output.getDate("AlertDate"),
                        output.getString("HabitatName"),
                        output.getString("AlertDescription"),
                        output.getString("AlertStatus")


                );
                alertModels.add(al);


            }

        }catch (Exception e){
            throw new RuntimeException(e);
        }

        return alertModels;
    }



    public void showAlertReport(){
        ObservableList<AlertModel>list=getAlerts();
        tblAlertsReport.setItems(list);
        colAlertType.setCellValueFactory(new PropertyValueFactory<>("alertType"));
        colAlertDate.setCellValueFactory(new PropertyValueFactory<>("alertDate"));
        colHabitatName.setCellValueFactory(new PropertyValueFactory<>("habitatName"));
        colAlertDescription.setCellValueFactory(new PropertyValueFactory<>("alertDisc"));
        colAlertStatus.setCellValueFactory(new PropertyValueFactory<>("alertStatus"));



    }

    public void filteredAlertReport() {
        // Get the selected filter criteria
        HabitatModel selectedHabitat = combHabitatName.getSelectionModel().getSelectedItem();
        AlertTypeModel selectedAlertType = combAlertType.getSelectionModel().getSelectedItem();
        Date fromDate = (dateFrom.getValue() != null) ? Date.valueOf(dateFrom.getValue()) : null;
        Date toDate = (dateTo.getValue() != null) ? Date.valueOf(dateTo.getValue()) : null;

        // Construct the SQL query based on the selected filters
        StringBuilder queryBuilder = new StringBuilder("SELECT a.AlertType,a.AlertDate,h.HabitatName,a.AlertDescription,a.AlertStatus FROM monitoringalerts a LEFT JOIN habitats h ON a.HabitatID = h.HabitatID WHERE 1=1");

        if (selectedHabitat != null) {
            queryBuilder.append(" AND h.HabitatName = '").append(selectedHabitat.getName()).append("'");
        }
        if (selectedAlertType != null) {
            queryBuilder.append(" AND a.AlertType = '").append(selectedAlertType.getAlertType()).append("'");
        }
        if (fromDate != null && toDate != null) {
            queryBuilder.append(" AND a.AlertDate BETWEEN '").append(fromDate).append("' AND '").append(toDate).append("'");
        }

        // Execute the query and update the table view
        ObservableList<AlertModel> filteredList = FXCollections.observableArrayList();
        try {
            Statement statement = connectDB.createStatement();
            ResultSet output = statement.executeQuery(queryBuilder.toString());
            while (output.next()){
                AlertModel al =new AlertModel(
                        output.getString("AlertType"),
                        output.getDate("AlertDate"),
                        output.getString("HabitatName"),
                        output.getString("AlertDescription"),
                        output.getString("AlertStatus")
                );

                filteredList.add(al);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Update the table view with filtered data
        tblAlertsReport.setItems(filteredList);
    }



    public void clearAlertFilter() {
        // Clear the selection in the habitat filter combo box
        combAlertType.getSelectionModel().clearSelection();
        combHabitatName.getSelectionModel().clearSelection();
        dateFrom.setValue(null);
        dateTo.setValue(null);

        showAlertReport();
    }



}





