package com.example.wildlife_hms;

import io.github.palexdev.materialfx.controls.MFXFilterPane;
import io.github.palexdev.materialfx.controls.MFXPaginatedTableView;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.StringFilter;
import io.github.palexdev.materialfx.utils.others.observables.When;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Comparator;
import java.util.ResourceBundle;

public class HabitatOverviewReportController implements Initializable {


    DatabaseConnection connectNow=new DatabaseConnection();
    Connection connectDB = connectNow.getConnection();

    @FXML
    private MFXPaginatedTableView<HabitatModel> paginated;

    @FXML
    private MFXFilterPane<HabitatModel> habitatFilter;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setupPaginated();
        paginated.autosizeColumnsOnInitialization();

        When.onChanged(paginated.currentPageProperty())
                .then((oldValue, newValue) -> paginated.autosizeColumns())
                .listen();


    }




    public ObservableList<HabitatModel>getHabitat() {


        ObservableList<HabitatModel> habitats= FXCollections.observableArrayList();
        String query="Select * from habitats";
        try {
            Statement statement=connectDB.createStatement();
            ResultSet output =statement.executeQuery(query);
            while (output.next()){
                HabitatModel ht =new HabitatModel(output.getInt("HabitatID"),output.getString("HA_ID"), output.getString("HabitatName"), output.getString("Location"), output.getDouble("Size"), output.getString("Description"));
                ht.setId((output.getInt("HabitatID")));
                ht.setHaId(output.getString("HA_ID"));
                ht.setName(output.getString("HabitatName"));
                ht.setLocation(output.getString("Location"));
                ht.setSize(output.getDouble("Size"));
                ht.setDesc(output.getString("Description"));
                habitats.add(ht);

            }

        }catch (Exception e){
            throw new RuntimeException(e);
        }

        return habitats;
    }

    ObservableList<HabitatModel> list=getHabitat();

    void setupPaginated() {


        MFXTableColumn<HabitatModel> idColumn = new MFXTableColumn<>("ID", false, Comparator.comparing(HabitatModel::getHaId));
        MFXTableColumn<HabitatModel> nameColumn = new MFXTableColumn<>("Habitat Name", false, Comparator.comparing(HabitatModel::getName));
        MFXTableColumn<HabitatModel> locationColumn = new MFXTableColumn<>("Location", false, Comparator.comparing(HabitatModel::getLocation));
        MFXTableColumn<HabitatModel> sizeColumn = new MFXTableColumn<>("Size", false, Comparator.comparing(HabitatModel::getSize));
        MFXTableColumn<HabitatModel> descColumn = new MFXTableColumn<>("Description", false, Comparator.comparing(HabitatModel::getDesc));

        idColumn.setRowCellFactory(habitatModel -> new MFXTableRowCell<>(HabitatModel::getHaId));
        nameColumn.setRowCellFactory(habitatModel -> new MFXTableRowCell<>(HabitatModel::getName));
        descColumn.setRowCellFactory(habitatModel -> new MFXTableRowCell<>(HabitatModel::getDesc));
        locationColumn.setRowCellFactory(habitatModel -> new MFXTableRowCell<>(HabitatModel::getLocation));
        sizeColumn.setRowCellFactory(habitatModel -> new MFXTableRowCell<>(HabitatModel::getSize));




        paginated.getTableColumns().addAll(idColumn, nameColumn, locationColumn, sizeColumn, descColumn);
        paginated.getFilters().addAll(
                new StringFilter<>("ID", HabitatModel::getHaId),
                new StringFilter<>("Name", HabitatModel::getName),
                new StringFilter<>("Location", HabitatModel::getLocation),
                new StringFilter<>("Description", HabitatModel::getDesc)

        );
        paginated.setItems(list);

    }




}
