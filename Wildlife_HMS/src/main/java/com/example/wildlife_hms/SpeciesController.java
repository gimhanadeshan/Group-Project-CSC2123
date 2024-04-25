package com.example.wildlife_hms;


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

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;

import static java.lang.Integer.parseInt;

public class SpeciesController implements Initializable,ButtonAction {

    @FXML
    private TableColumn<SpeciesModel, Integer> colUpdate;

    @FXML
    private TableColumn<SpeciesModel, Integer> colDelete;

    DatabaseConnection connectNow=new DatabaseConnection();
    Connection connectDB = connectNow.getConnection();


    @FXML
    private TableColumn<SpeciesModel, String> colCommonName;

    @FXML
    private TableColumn<SpeciesModel, String> colConservationStatus;

    @FXML
    private TableColumn<SpeciesModel, String> colDescription;

    @FXML
    private TableColumn<SpeciesModel, String> colScientificName;

    @FXML
    private TableColumn<SpeciesModel, String> colSpeciesId;



    @FXML
    private TableView<SpeciesModel> tblSpecies;



    @FXML
    private TextField txtSearch;









    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        initUpdateButton();
        initDeleteButton();
        showSpecies();
    }

    public ObservableList<SpeciesModel> getSpecies() {


        ObservableList<SpeciesModel> species= FXCollections.observableArrayList();
        String query="Select * from species";
        try {
            Statement statement=connectDB.createStatement();
            ResultSet output =statement.executeQuery(query);
            while (output.next()){
                SpeciesModel sp =new SpeciesModel(output.getInt("SpeciesID"),output.getString("SP_ID"), output.getString("CommonName"), output.getString("ScientificName"), output.getString("ConservationStatus"), output.getString("Description"));
                sp.setId(output.getInt("SpeciesID"));
                sp.setSpId(output.getString("SP_ID"));
                sp.setCommonName(output.getString("CommonName"));
                sp.setScientificName(output.getString("ScientificName"));
                sp.setConservationStatus(output.getString("ConservationStatus"));
                sp.setDesc(output.getString("Description"));
                species.add(sp);

            }

        }catch (Exception e){
            throw new RuntimeException(e);
        }

        return species;
    }

    public void showSpecies(){
        ObservableList<SpeciesModel>list=getSpecies();
        tblSpecies.setItems(list);
        colSpeciesId.setCellValueFactory(new PropertyValueFactory<>("spId"));
        colCommonName.setCellValueFactory(new PropertyValueFactory<>("commonName"));
        colScientificName.setCellValueFactory(new PropertyValueFactory<>("scientificName"));
        colConservationStatus.setCellValueFactory(new PropertyValueFactory<>("conservationStatus"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("desc"));

    }


    public void openSpeciesForm() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("speciesForm.fxml"));
        Parent root = fxmlLoader.load();
        SpeciesFormController controller=fxmlLoader.getController() ;
        controller.btnUpdate.setDisable(true);
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL); // Set modality to application modal
        stage.showAndWait(); // Show the stage and wait for it to be closed
    }




    public void search(){
        String srch = txtSearch.getText();

        try {
            String query = "SELECT * FROM species WHERE CommonName LIKE ? OR SpeciesID = ?";
            PreparedStatement statement = connectDB.prepareStatement(query);
            statement.setString(1, STR."%\{srch}%");
            // Assuming srch is an integer representing HabitatID, change the condition to equality
            try {
                int speciesId = parseInt(srch);
                statement.setInt(2, speciesId);
            } catch (NumberFormatException ex) {
                // Handle the case where srch is not a valid integer for HabitatID
                statement.setInt(2, -1); // Set an ID that doesn't exist to fetch no results
            }

            ResultSet resultSet = statement.executeQuery();

            ObservableList<SpeciesModel> searchResults = FXCollections.observableArrayList();
            while (resultSet.next()) {
                SpeciesModel species = new SpeciesModel(
                        resultSet.getInt("SpeciesID"),
                        resultSet.getString("SP_ID"),
                        resultSet.getString("CommonName"),
                        resultSet.getString("ScientificName"),
                        resultSet.getString("ConservationStatus"),
                        resultSet.getString("Description")
                );
                searchResults.add(species);
            }


            tblSpecies.setItems(searchResults);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }





    private void showAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Database Error");
        alert.setContentText("Error in deleting Species");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait();
    }






    @FXML
    void deleteSpecies(int id) {
        String query = "delete from species where SpeciesID= ?";
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

            showSpecies();

        } catch (SQLException e) {
            showAlert();
        }
    }





     public void initUpdateButton() {
        colUpdate.setCellFactory(_ -> new TableCell<>() {
            private final Button updateButton = new Button();
            private final ImageView imageView = new ImageView(new Image("icons8-edit-20.png")); // Path to your update image

            {

                updateButton.setStyle("-fx-background-color: transparent;");
                updateButton.setOnMouseEntered(_ -> updateButton.setStyle("-fx-background-color: lightgrey;"));
                updateButton.setOnMouseExited(_ -> updateButton.setStyle("-fx-background-color: transparent;"));
                updateButton.setGraphic(imageView);
                updateButton.setOnAction(_ -> {
                    SpeciesModel species = getTableView().getItems().get(getIndex());

                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("speciesForm.fxml"));
                    Parent root;
                    try {
                        root = fxmlLoader.load();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    SpeciesFormController controller=fxmlLoader.getController() ;
                    controller.getData(species);
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
        colDelete.setCellFactory(_ -> new TableCell<>() {
            private final Button deleteButton = new Button();
            private final ImageView imageView = new ImageView(new Image("icons8-delete-20.png")); // Path to your delete image

            {
                deleteButton.setStyle("-fx-background-color: transparent;");
                deleteButton.setOnMouseEntered(_ -> deleteButton.setStyle("-fx-background-color: lightgrey;"));
                deleteButton.setOnMouseExited(_ -> deleteButton.setStyle("-fx-background-color: transparent;"));
                deleteButton.setGraphic(imageView);
                deleteButton.setOnAction(_ -> {
                    SpeciesModel species = getTableView().getItems().get(getIndex());
                    // Call delete method with species ID
                    deleteSpecies(species.getId());
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


    public void refreshSpeciesTableData() {
        showSpecies();
    }
   
}
