package com.example.wildlife_hms;

import io.github.palexdev.materialfx.controls.MFXComboBox;
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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.function.Predicate;

public class SpeciesDiversityReportController implements Initializable {


    @FXML
    private TableColumn<SpeciesDiversityReportModel, String> colCommonName;

    @FXML
    private TableColumn<SpeciesDiversityReportModel, String> colScientificName;

    @FXML
    private TableColumn<SpeciesDiversityReportModel, String> colConservationStatus;

    @FXML
    private TableColumn<SpeciesDiversityReportModel, Integer> colHabitatCount;


    @FXML
    private MFXFilterComboBox<SpeciesModel> combCommonName;

    @FXML
    private MFXComboBox<String> combConservationStatus;

    @FXML
    private TableView<SpeciesDiversityReportModel> tblSpeciesDiversity;



    DatabaseConnection connectNow=new DatabaseConnection();
    Connection connectDB = connectNow.getConnection();

    SpeciesController speciesController=new SpeciesController();


    public void initialize(URL url, ResourceBundle resourceBundle) {

        StringConverter<SpeciesModel> converter = FunctionalStringConverter.to(speciesModel -> (speciesModel == null) ? "" : STR."\{speciesModel.getCommonName()}");
        Function<String, Predicate<SpeciesModel>> filterFunction = s -> speciesModel -> StringUtils.containsIgnoreCase(converter.toString(speciesModel), s);
        combCommonName.setItems(speciesController.getSpecies());
        combCommonName.setConverter(converter);
        combCommonName.setFilterFunction(filterFunction);

        combConservationStatus.getItems().addAll("Endangered","Least Concern","Not Evaluated");




        showSpeciesDiversity();

    }




    public ObservableList<SpeciesDiversityReportModel>getSpeciesDiversity() {


        ObservableList<SpeciesDiversityReportModel> speciesDiversity= FXCollections.observableArrayList();
        String query="Select CommonName,ScientificName,ConservationStatus,COUNT(DISTINCT o.HabitatID) as HabitatCount from species s LEFT JOIN observations o ON s.SpeciesID =o.SpeciesID GROUP BY CommonName";
        try {
            Statement statement=connectDB.createStatement();
            ResultSet output =statement.executeQuery(query);
            while (output.next()){
                SpeciesDiversityReportModel hdrp =new SpeciesDiversityReportModel(
                        output.getString("CommonName"),
                        output.getString("ScientificName"),
                        output.getString("ConservationStatus"),
                        output.getInt("HabitatCount")


                );
                speciesDiversity.add(hdrp);


            }

        }catch (Exception e){
            throw new RuntimeException(e);
        }

        return speciesDiversity;
    }



    public void showSpeciesDiversity(){
        ObservableList<SpeciesDiversityReportModel>list=getSpeciesDiversity();
        tblSpeciesDiversity.setItems(list);
        colCommonName.setCellValueFactory(new PropertyValueFactory<>("commonName"));
        colScientificName.setCellValueFactory(new PropertyValueFactory<>("scientificName"));
        colConservationStatus.setCellValueFactory(new PropertyValueFactory<>("conservationStatus"));
        colHabitatCount.setCellValueFactory(new PropertyValueFactory<>("habitatCount"));


    }

    public void filteredSpeciesDiversityData() {
        String selectedCommonName;
        String selectedConservationStatus;

        // Get selected common name
        if (combCommonName.getSelectionModel().getSelectedItem() != null) {
            selectedCommonName = combCommonName.getSelectionModel().getSelectedItem().getCommonName();
        } else {
            selectedCommonName = null;
        }

        // Get selected conservation status
        if (combConservationStatus.getSelectionModel().getSelectedItem() != null) {
            selectedConservationStatus = combConservationStatus.getValue();
        } else {
            selectedConservationStatus = null;
        }

        // Apply filters
        ObservableList<SpeciesDiversityReportModel> filteredList = getSpeciesDiversity().filtered(item -> {
            boolean commonNameMatch = selectedCommonName == null || item.getCommonName().equals(selectedCommonName);
            boolean conservationStatusMatch = selectedConservationStatus == null || item.getConservationStatus().equals(selectedConservationStatus);

            // Logic for AND condition
            return commonNameMatch && conservationStatusMatch;

            // Logic for OR condition
            //return commonNameMatch || conservationStatusMatch;
        });

        tblSpeciesDiversity.setItems(filteredList);
    }



    public void clearSpeciesDiversityFilter() {
        // Clear the selection in the habitat filter combo box
        combCommonName.getSelectionModel().clearSelection();
        combConservationStatus.getSelectionModel().clearSelection();

        // Show all habitat overview data
        showSpeciesDiversity();
    }







}





