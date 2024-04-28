package com.example.wildlife_hms;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.utils.StringUtils;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.lang.Integer.parseInt;

public class PopulationController implements Initializable,ButtonAction  {

    @FXML
    private TableColumn<PopulationModel, Integer> colDelete;

    @FXML
    private TableColumn<HabitatModel, String> colHabitatID;

    @FXML
    private TableColumn<HabitatModel, String> colHabitatName;


    @FXML
    private TableColumn<PopulationModel,Date> colDate;

    @FXML
    private TableColumn<PopulationModel, String> colPopulationId;

    @FXML
    private TableColumn<PopulationModel, Integer> colPopulationSize;

    @FXML
    private TableColumn<SpeciesModel, String> colSpeciesId;

    @FXML
    private TableColumn<SpeciesModel, String> colSpeciesName;

    @FXML
    private TableColumn<PopulationModel, Integer> colUpdate;

    @FXML
    private TableView<PopulationModel> tblPopulations;

    @FXML
    private TextField txtSearch;

    @FXML
    private MFXButton btnNew;

    @FXML
    private MFXFilterComboBox<HabitatModel> combHabitat;

    @FXML
    private MFXFilterComboBox<SpeciesModel> combSpecies;


    HabitatController habitatController = new HabitatController();
    SpeciesController speciesController = new SpeciesController();


    DatabaseConnection connectNow=new DatabaseConnection();
    Connection connectDB = connectNow.getConnection();


    public void setUserPermissions(UserPermissionsModel userPermissions){

        if(userPermissions.isUpdate()){
            initUpdateButton();
            colUpdate.setVisible(true);
        }else {
            colUpdate.setVisible(false);
        }

        if (userPermissions.isDelete()){
            initDeleteButton();
            colDelete.setVisible(true);
        }else {
            colDelete.setVisible(false);
        }

        if(userPermissions.isCreate()){
            btnNew.setVisible(true);btnNew.setManaged(true);
        }else {
            btnNew.setVisible(false);btnNew.setManaged(false);
        }


    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        showPopulation();
        initCombobox();

    }

    void initCombobox() {
        StringConverter<HabitatModel> converter = FunctionalStringConverter.to(habitatModel -> (habitatModel == null) ? "" : STR."\{habitatModel.getName()}");
        Function<String, Predicate<HabitatModel>> filterFunction = s -> habitatModel -> StringUtils.containsIgnoreCase(converter.toString(habitatModel), s);
        combHabitat.setItems(habitatController.getHabitat());
        combHabitat.setConverter(converter);
        combHabitat.setFilterFunction(filterFunction);

        combHabitat.setOnAction(event -> filteredPopulationData());

        StringConverter<SpeciesModel> converter1 = FunctionalStringConverter.to(speciesModel -> (speciesModel == null) ? "" : STR."\{speciesModel.getCommonName()}");
        Function<String, Predicate<SpeciesModel>> filterFunction1 = s -> speciesModel -> StringUtils.containsIgnoreCase(converter1.toString(speciesModel), s);
        combSpecies.setItems(speciesController.getSpecies());
        combSpecies.setConverter(converter1);
        combSpecies.setFilterFunction(filterFunction1);

        combSpecies.setOnAction(event -> filteredPopulationData());


    }

    public ObservableList<PopulationModel> getPopulation() {
        ObservableList<PopulationModel> populationModels = FXCollections.observableArrayList();
        String query = "SELECT p.*, h.HabitatName, h.HA_ID, s.CommonName, s.SP_ID FROM population p INNER JOIN habitats h ON p.HabitatID = h.HabitatID INNER JOIN species s ON p.SpeciesID = s.SpeciesID";
        try {
            Statement statement = connectDB.createStatement();
            ResultSet output = statement.executeQuery(query);
            while (output.next()) {
                PopulationModel po = new PopulationModel(
                        output.getInt("PopulationID"),
                        output.getString("PO_ID"),
                        output.getInt("HabitatID"),
                        output.getString("HA_ID"),
                        output.getString("HabitatName"),
                        output.getInt("SpeciesID"),
                        output.getString("SP_ID"),
                        output.getString("CommonName"),
                        output.getInt("PopulationSize"),
                        output.getDate("LastSurveyDate")

                );

                populationModels.add(po);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return populationModels;
    }





    public void showPopulation(){
        ObservableList<PopulationModel>list=getPopulation();
        tblPopulations.setItems(list);
        colPopulationId.setCellValueFactory(new PropertyValueFactory<>("poId"));
        colPopulationSize.setCellValueFactory(new PropertyValueFactory<>("populationSize"));
        colDate.setCellValueFactory(new PropertyValueFactory<PopulationModel,Date>("lastSurveyDate"));
        colHabitatID.setCellValueFactory(new PropertyValueFactory<>("haId"));
        colHabitatName.setCellValueFactory(new PropertyValueFactory<>("habitatName"));
        colSpeciesId.setCellValueFactory(new PropertyValueFactory<>("spId"));
        colSpeciesName.setCellValueFactory(new PropertyValueFactory<>("speciesName"));



    }

    @FXML
    void openPopulationForm() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("populationForm.fxml"));
        Parent root = fxmlLoader.load();
        PopulationFormController populationFormController=fxmlLoader.getController() ;
        populationFormController.btnUpdate.setDisable(true);
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL); // Set modality to application modal
        stage.showAndWait(); // Show the stage and wait for it to be closed

    }

    @FXML
    void search() {

        String srch = txtSearch.getText();

        try {
            String query = "SELECT p.*, h.HabitatName, h.HA_ID, s.CommonName, s.SP_ID FROM population p INNER JOIN habitats h ON p.HabitatID = h.HabitatID INNER JOIN species s ON p.SpeciesID = s.SpeciesID  WHERE PO_ID LIKE ? OR PopulationID = ?";
            PreparedStatement statement = connectDB.prepareStatement(query);
            statement.setString(1, STR."%\{srch}%");

            try {
                int populationId = parseInt(srch);
                statement.setInt(2, populationId);
            } catch (NumberFormatException ex) {
                statement.setInt(2, -1); // Set an ID that doesn't exist to fetch no results
            }

            ResultSet resultSet = statement.executeQuery();



            ObservableList<PopulationModel> searchResults = FXCollections.observableArrayList();
            while (resultSet.next()) {
                PopulationModel po = new PopulationModel(
                        resultSet.getInt("PopulationID"),
                        resultSet.getString("PO_ID"),
                        resultSet.getInt("HabitatID"),
                        resultSet.getString("HA_ID"),
                        resultSet.getString("HabitatName"),
                        resultSet.getInt("SpeciesID"),
                        resultSet.getString("SP_ID"),
                        resultSet.getString("CommonName"),
                        resultSet.getInt("PopulationSize"),
                        resultSet.getDate("LastSurveyDate")

                );
                searchResults.add(po);
            }


            tblPopulations.setItems(searchResults);

        } catch (SQLException e) {
            e.printStackTrace();
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
    void deletePopulation(int id) {
        String query = "delete from population where PopulationID  =?";
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete");
        alert.setContentText("Are you sure to delete this record?");
        Optional<ButtonType> result = alert.showAndWait();

        try {
            PreparedStatement statement = connectDB.prepareStatement(query);
            statement.setInt(1, id);

            if (result.isPresent() && result.get() == ButtonType.OK) {
                statement.executeUpdate();
            }

            showPopulation();

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error in deleting Population");
        }

    }







    public void initUpdateButton() {
        colUpdate.setCellFactory(param -> new TableCell<>() {
            private final Button updateButton = new Button();
            private final ImageView imageView = new ImageView(new Image("icons8-edit-20.png")); // Path to your update image

            {

                updateButton.setStyle("-fx-background-color: transparent;");
                updateButton.setOnMouseEntered(event -> updateButton.setStyle("-fx-background-color: lightgrey;"));
                updateButton.setOnMouseExited(event -> updateButton.setStyle("-fx-background-color: transparent;"));
                updateButton.setGraphic(imageView);
                updateButton.setOnAction(event -> {
                    PopulationModel populationModel = getTableView().getItems().get(getIndex());



                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("populationForm.fxml"));
                    Parent root = null;
                    try {
                        root = fxmlLoader.load();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    PopulationFormController controller=fxmlLoader.getController() ;
                    controller.getData(populationModel);
                    controller.btnSave.setDisable(true);
                    controller.btnClear.setDisable(true);
                    controller.btnUpdate.setDisable(false);
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.setResizable(false);
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.showAndWait();


                });
            }

            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(updateButton);
                }
            }
        });
    }

    public void initDeleteButton() {
        colDelete.setCellFactory(param -> new TableCell<>(){
            private final Button deleteButton = new Button();
            private final ImageView imageView = new ImageView(new Image("icons8-delete-20.png")); // Path to your delete image

            {
                deleteButton.setStyle("-fx-background-color: transparent;");
                deleteButton.setOnMouseEntered(event -> deleteButton.setStyle("-fx-background-color: lightgrey;"));
                deleteButton.setOnMouseExited(event -> deleteButton.setStyle("-fx-background-color: transparent;"));
                deleteButton.setGraphic(imageView);
                deleteButton.setOnAction(event -> {
                    PopulationModel populationModel = getTableView().getItems().get(getIndex());
                    // Call delete method with species ID
                    deletePopulation(populationModel.getPopulationID());
                });
            }

            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });
    }


    public void filteredPopulationData() {
        String selectedHabitatName;
        String selectedCommonName;

        // Get selected common name
        if (combHabitat.getSelectionModel().getSelectedItem() != null) {
            selectedHabitatName = combHabitat.getSelectionModel().getSelectedItem().getName();
        } else {
            selectedHabitatName = null;
        }

        // Get selected conservation status
        if (combSpecies.getSelectionModel().getSelectedItem() != null) {
            selectedCommonName = combSpecies.getSelectionModel().getSelectedItem().getCommonName();
        } else {
            selectedCommonName = null;
        }

        // Apply filters
        ObservableList<PopulationModel> filteredList = getPopulation().filtered(item -> {
            boolean commonNameMatch = selectedCommonName == null || item.getSpeciesName().equals(selectedCommonName);
            boolean habitatNameMatch = selectedHabitatName == null || item.getHabitatName().equals(selectedHabitatName);

            // Logic for AND condition
            return commonNameMatch && habitatNameMatch;

        });

        tblPopulations.setItems(filteredList);
    }

    public void clearFilter(){

        combSpecies.clearSelection();
        combHabitat.clearSelection();
        showPopulation();

    }


}
