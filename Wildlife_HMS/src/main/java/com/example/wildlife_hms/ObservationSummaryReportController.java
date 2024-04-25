package com.example.wildlife_hms;

import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.utils.StringUtils;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.function.Predicate;




public class ObservationSummaryReportController implements Initializable {


    @FXML
    private TableColumn<ObservationSummaryReportModel, String> colHabitatName;

    @FXML
    private TableColumn<ObservationSummaryReportModel, Date> colObservationDate;

    @FXML
    private TableColumn<ObservationSummaryReportModel, String> colObservedSpecies;

    @FXML
    private TableColumn<ObservationSummaryReportModel, String> colObserverName;

    @FXML
    private MFXFilterComboBox<HabitatModel> combHabitatName;

    @FXML
    private MFXFilterComboBox<String> combObserverName;

    @FXML
    private MFXDatePicker dateFrom;

    @FXML
    private MFXDatePicker dateTo;

    @FXML
    private TableView<ObservationSummaryReportModel> tblObservationSummary;


    DatabaseConnection connectNow=new DatabaseConnection();
    Connection connectDB = connectNow.getConnection();

    HabitatController habitatController=new HabitatController();
    ObservationsController observationsController=new ObservationsController();


    public void initialize(URL url, ResourceBundle resourceBundle) {

        StringConverter<HabitatModel> converter = FunctionalStringConverter.to(habitatModel -> (habitatModel == null) ? "" : STR."\{habitatModel.getName()}");
        Function<String, Predicate<HabitatModel>> filterFunction = s -> habitatModel -> StringUtils.containsIgnoreCase(converter.toString(habitatModel), s);
        combHabitatName.setItems(habitatController.getHabitat());
        combHabitatName.setConverter(converter);
        combHabitatName.setFilterFunction(filterFunction);

        /*StringConverter<ObservationsModel> converter1 = FunctionalStringConverter.to(observationsModel -> (observationsModel == null) ? "" : STR."\{observationsModel.getObserverName()}");
        Function<String, Predicate<ObservationsModel>> filterFunction1 = s -> observationsModel -> StringUtils.containsIgnoreCase(converter1.toString(observationsModel), s);
        combObserverName.setItems(observationsController.getObservations());
        combObserverName.setConverter(converter1);
        combObserverName.setFilterFunction(filterFunction1);*/

        observerNameList();

        showObservationSummary();

    }




    public ObservableList<ObservationSummaryReportModel>getObservationSummary() {


        ObservableList<ObservationSummaryReportModel> observationSummary= FXCollections.observableArrayList();
        String query="SELECT o.ObserverName,o.ObservationDate,h.HabitatName,s.CommonName AS ObservedSpecies FROM observations o LEFT JOIN habitats h ON o.HabitatID = h.HabitatID LEFT JOIN species s ON o.SpeciesID = s.SpeciesID;";
        try {
            Statement statement=connectDB.createStatement();
            ResultSet output =statement.executeQuery(query);
            while (output.next()){
                ObservationSummaryReportModel osrp =new ObservationSummaryReportModel(
                        output.getString("ObserverName"),
                        output.getDate("ObservationDate"),
                        output.getString("HabitatName"),
                        output.getString("ObservedSpecies")


                );
                observationSummary.add(osrp);


            }

        }catch (Exception e){
            throw new RuntimeException(e);
        }

        return observationSummary;
    }



    public void showObservationSummary(){
        ObservableList<ObservationSummaryReportModel>list=getObservationSummary();
        tblObservationSummary.setItems(list);
        colObserverName.setCellValueFactory(new PropertyValueFactory<>("observerName"));
        colObservationDate.setCellValueFactory(new PropertyValueFactory<>("observationDate"));
        colHabitatName.setCellValueFactory(new PropertyValueFactory<>("habitatName"));
        colObservedSpecies.setCellValueFactory(new PropertyValueFactory<>("observedSpecies"));


    }

    public void filteredObservationSummaryData() {
        // Get the selected filter criteria
        HabitatModel selectedHabitat = combHabitatName.getSelectionModel().getSelectedItem();
        String selectedObserver = combObserverName.getSelectedItem();
        Date fromDate = (dateFrom.getValue() != null) ? Date.valueOf(dateFrom.getValue()) : null;
        Date toDate = (dateTo.getValue() != null) ? Date.valueOf(dateTo.getValue()) : null;

        // Construct the SQL query based on the selected filters
        StringBuilder queryBuilder = new StringBuilder("SELECT o.ObserverName, o.ObservationDate, h.HabitatName, s.CommonName AS ObservedSpecies " +
                "FROM observations o " +
                "LEFT JOIN habitats h ON o.HabitatID = h.HabitatID " +
                "LEFT JOIN species s ON o.SpeciesID = s.SpeciesID " +
                "WHERE 1=1");

        if (selectedHabitat != null) {
            queryBuilder.append(" AND h.HabitatName = '").append(selectedHabitat.getName()).append("'");
        }
        if (selectedObserver != null) {
            queryBuilder.append(" AND o.ObserverName = '").append(selectedObserver).append("'");
        }
        if (fromDate != null && toDate != null) {
            queryBuilder.append(" AND o.ObservationDate BETWEEN '").append(fromDate).append("' AND '").append(toDate).append("'");
        }

        // Execute the query and update the table view
        ObservableList<ObservationSummaryReportModel> filteredList = FXCollections.observableArrayList();
        try {
            Statement statement = connectDB.createStatement();
            ResultSet output = statement.executeQuery(queryBuilder.toString());
            while (output.next()) {
                ObservationSummaryReportModel osrp = new ObservationSummaryReportModel(
                        output.getString("ObserverName"),
                        output.getDate("ObservationDate"),
                        output.getString("HabitatName"),
                        output.getString("ObservedSpecies")
                );
                filteredList.add(osrp);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Update the table view with filtered data
        tblObservationSummary.setItems(filteredList);
    }



    public void clearObservationSummaryFilter() {
        // Clear the selection in the habitat filter combo box
        combObserverName.getSelectionModel().clearSelection();
        combHabitatName.getSelectionModel().clearSelection();
        dateFrom.setValue(null);
        dateTo.setValue(null);

        // Show all habitat overview data
        showObservationSummary();
    }



   /* public void printReport() {
        // Create a PrinterJob
        PrinterJob printerJob = PrinterJob.createPrinterJob();

        // Check if the printer job is created successfully
        if (printerJob != null) {
            // Get the table view node
            Node tableNode = tblObservationSummary;

            // Scale the table view to fit the page
            double scaleX = 0.9;
            double scaleY = 0.9;
            Scale scale = new Scale(scaleX, scaleY);
            tableNode.getTransforms().add(scale);

            // Print the table view
            boolean printSuccess = printerJob.printPage(tableNode);

            if (printSuccess) {
                // End the printer job
                printerJob.endJob();
            } else {
                // Print failed, show an error message
                Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to print the report", ButtonType.OK);
                alert.showAndWait();
            }

            // Remove the scale transformation
            tableNode.getTransforms().remove(scale);
        } else {
            // Failed to create printer job, show an error message
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to create printer job", ButtonType.OK);
            alert.showAndWait();
        }
    }*/

    void observerNameList() {
        try {
            String query = "SELECT DISTINCT ObserverName FROM observations";
            PreparedStatement statement = connectDB.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            // Clear existing items in the combobox
            combObserverName.getItems().clear();

            // Populate the combobox with observer names
            while (resultSet.next()) {
                String observerName = resultSet.getString("ObserverName");
                combObserverName.getItems().add(observerName);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}





