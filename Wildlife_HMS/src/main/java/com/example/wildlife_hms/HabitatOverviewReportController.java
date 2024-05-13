package com.example.wildlife_hms;

import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXFilterPane;
import io.github.palexdev.materialfx.controls.MFXPaginatedTableView;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.StringFilter;
import io.github.palexdev.materialfx.utils.StringUtils;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import io.github.palexdev.materialfx.utils.others.observables.When;
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
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Comparator;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.function.Predicate;

public class HabitatOverviewReportController implements Initializable {


    @FXML
    private TableColumn<HabitatOverviewReportModel, String> colDescription;

    @FXML
    private TableColumn<HabitatOverviewReportModel, String> colHabitatName;

    @FXML
    private TableColumn<HabitatOverviewReportModel, Double> colHabitatSize;

    @FXML
    private TableColumn<HabitatOverviewReportModel, Integer> colTotalObservation;

    @FXML
    private TableColumn<HabitatOverviewReportModel, Integer> colTotalSpecies;

    @FXML
    private MFXFilterComboBox<HabitatModel> combHabitatName;

    @FXML
    private TableView<HabitatOverviewReportModel> tblHabitatOverview;



    DatabaseConnection connectNow=new DatabaseConnection();
    Connection connectDB = connectNow.getConnection();

    HabitatController habitatController=new HabitatController();


    public void initialize(URL url, ResourceBundle resourceBundle) {

        StringConverter<HabitatModel> converter = FunctionalStringConverter.to(habitatModel -> (habitatModel == null) ? "" : STR."\{habitatModel.getName()}");
        Function<String, Predicate<HabitatModel>> filterFunction = s -> habitatModel -> StringUtils.containsIgnoreCase(converter.toString(habitatModel), s);
        combHabitatName.setItems(habitatController.getHabitat());
        combHabitatName.setConverter(converter);
        combHabitatName.setFilterFunction(filterFunction);

        combHabitatName.setOnAction(event -> filteredData());


        showHabitatOverview();

    }




    public ObservableList<HabitatOverviewReportModel>getHabitatReport() {


        ObservableList<HabitatOverviewReportModel> habitatsReport= FXCollections.observableArrayList();
        String query="Select h.HabitatName,h.Size,h.Description,COUNT(o.SpeciesID) as TotalSpecies,COUNT(o.ObservationID) as TotalObservation from habitats h,observations as o WHERE h.HabitatID=o.HabitatID GROUP BY HabitatName HAVING count(SpeciesID) ";
        try {
            Statement statement=connectDB.createStatement();
            ResultSet output =statement.executeQuery(query);
            while (output.next()){
                HabitatOverviewReportModel hrp =new HabitatOverviewReportModel(
                        output.getString("HabitatName"),
                        output.getDouble("Size"),
                        output.getString("Description"),
                        output.getInt("TotalSpecies"),
                        output.getInt("TotalObservation")

                );
                habitatsReport.add(hrp);


            }

        }catch (Exception e){
            throw new RuntimeException(e);
        }

        return habitatsReport;
    }



    public void showHabitatOverview(){
        ObservableList<HabitatOverviewReportModel>list=getHabitatReport();
        tblHabitatOverview.setItems(list);
        colHabitatName.setCellValueFactory(new PropertyValueFactory<>("habitatName"));
        colHabitatSize.setCellValueFactory(new PropertyValueFactory<>("size"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colTotalSpecies.setCellValueFactory(new PropertyValueFactory<>("totalSpecies"));
        colTotalObservation.setCellValueFactory(new PropertyValueFactory<>("totalObservation"));


    }

    private void filteredData() {

        if(!(combHabitatName.getSelectionModel().getSelectedItem() ==null)) {
            String selectedHabitatName = combHabitatName.getSelectionModel().getSelectedItem().getName();

            ObservableList<HabitatOverviewReportModel> filteredList = getHabitatReport().filtered(item ->
                    item.getHabitatName().equals(selectedHabitatName)
            );

            tblHabitatOverview.setItems(filteredList);
        }
    }


    public void clearFilter() {
        // Clear the selection in the habitat filter combo box
        combHabitatName.getSelectionModel().clearSelection();

        // Show all habitat overview data
        showHabitatOverview();
    }







}





