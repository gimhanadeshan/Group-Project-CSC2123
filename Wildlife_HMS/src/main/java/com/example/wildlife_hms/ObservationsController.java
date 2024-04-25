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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;

import static java.lang.Integer.parseInt;

public class ObservationsController implements Initializable,ButtonAction  {

    @FXML
    private TableColumn<ObservationsModel, Integer> colDelete;

    @FXML
    private TableColumn<HabitatModel, String> colHabitatID;

    @FXML
    private TableColumn<HabitatModel, String> colHabitatName;

    @FXML
    private TableColumn<ObservationsModel, String> colNotes;

    @FXML
    private TableColumn<ObservationsModel, File> colAttachments;

    @FXML
    private TableColumn<ObservationsModel,Date> colObservationDate;

    @FXML
    private TableColumn<ObservationsModel, String> colObservationId;

    @FXML
    private TableColumn<ObservationsModel, String> colObservationName;

    @FXML
    private TableColumn<SpeciesModel, String> colSpeciesId;

    @FXML
    private TableColumn<SpeciesModel, String> colSpeciesName;

    @FXML
    private TableColumn<ObservationsModel, Integer> colUpdate;

    @FXML
    private TableView<ObservationsModel> tblObservations;

    @FXML
    private TextField txtSearch;




    DatabaseConnection connectNow=new DatabaseConnection();
    Connection connectDB = connectNow.getConnection();



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        initUpdateButton();
        initDeleteButton();
        showObservations();

    }

    public ObservableList<ObservationsModel> getObservations() {
        ObservableList<ObservationsModel> observations = FXCollections.observableArrayList();
        String query = "SELECT o.*, h.HabitatName, h.HA_ID, s.CommonName, s.SP_ID FROM observations o INNER JOIN habitats h ON o.HabitatID = h.HabitatID INNER JOIN species s ON o.SpeciesID = s.SpeciesID";
        try {
            Statement statement = connectDB.createStatement();
            ResultSet output = statement.executeQuery(query);
            while (output.next()) {
                ObservationsModel obs = new ObservationsModel(
                        output.getInt("ObservationID"),
                        output.getString("OB_ID"),
                        output.getString("ObserverName"),
                        output.getDate("ObservationDate"),
                        output.getString("Notes"),
                        output.getInt("HabitatID"),
                        output.getString("HA_ID"),
                        output.getString("HabitatName"),
                        output.getInt("SpeciesID"),
                        output.getString("SP_ID"),
                        output.getString("CommonName"),
                        output.getString("Attachments")
                );

                observations.add(obs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return observations;
    }





    public void showObservations(){
        ObservableList<ObservationsModel>list=getObservations();
        tblObservations.setItems(list);
        colObservationId.setCellValueFactory(new PropertyValueFactory<>("obsId"));
        colObservationName.setCellValueFactory(new PropertyValueFactory<>("observerName"));
        colNotes.setCellValueFactory(new PropertyValueFactory<>("notes"));
        colObservationDate.setCellValueFactory(new PropertyValueFactory<ObservationsModel,Date>("observationDate"));
        colHabitatID.setCellValueFactory(new PropertyValueFactory<>("haId"));
        colHabitatName.setCellValueFactory(new PropertyValueFactory<>("habitatName"));
        colSpeciesId.setCellValueFactory(new PropertyValueFactory<>("spId"));
        colSpeciesName.setCellValueFactory(new PropertyValueFactory<>("speciesName"));
        colAttachments.setCellValueFactory(new PropertyValueFactory<>("attachments") );



    }

    @FXML
    void openObservationsForm() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("observationForm.fxml"));
        Parent root = fxmlLoader.load();
        ObservationsFormController observationsFormController=fxmlLoader.getController() ;
        observationsFormController.btnUpdate.setDisable(true);
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.setTitle("Observations Entry");
        stage.initModality(Modality.APPLICATION_MODAL); // Set modality to application modal
        stage.showAndWait(); // Show the stage and wait for it to be closed

    }

    @FXML
    void search() {

        String srch = txtSearch.getText();

        try {
            String query = "SELECT o.*, h.HabitatName, h.HA_ID, s.CommonName, s.SP_ID FROM observations o INNER JOIN habitats h ON o.HabitatID = h.HabitatID INNER JOIN species s ON o.SpeciesID = s.SpeciesID  WHERE ObserverName LIKE ? OR ObservationID = ?";
            PreparedStatement statement = connectDB.prepareStatement(query);
            statement.setString(1, STR."%\{srch}%");
            // Assuming srch is an integer representing HabitatID, change the condition to equality
            try {
                int observationsId = parseInt(srch);
                statement.setInt(2, observationsId);
            } catch (NumberFormatException ex) {
                // Handle the case where srch is not a valid integer for HabitatID
                statement.setInt(2, -1); // Set an ID that doesn't exist to fetch no results
            }

            ResultSet resultSet = statement.executeQuery();



            ObservableList<ObservationsModel> searchResults = FXCollections.observableArrayList();
            while (resultSet.next()) {
                ObservationsModel observations = new ObservationsModel(
                        resultSet.getInt("ObservationID"),
                        resultSet.getString("OB_ID"),
                        resultSet.getString("ObserverName"),
                        resultSet.getDate("ObservationDate"),
                        resultSet.getString("Notes"),
                        resultSet.getInt("HabitatID"),
                        resultSet.getString("HA_ID"),
                        resultSet.getString("HabitatName"),
                        resultSet.getInt("SpeciesID"),
                        resultSet.getString("SP_ID"),
                        resultSet.getString("CommonName"),
                        resultSet.getString("Attachments")
                );
                searchResults.add(observations);
            }


            tblObservations.setItems(searchResults);

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
    void deleteObservation(int id) {
        String query = "delete from observations where ObservationID =?";
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

            showObservations();

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error in deleting Observations");
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
                    ObservationsModel observationsModel = getTableView().getItems().get(getIndex());



                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("observationForm.fxml"));
                    Parent root = null;
                    try {
                        root = fxmlLoader.load();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    ObservationsFormController controller=fxmlLoader.getController() ;
                    controller.getData(observationsModel);
                    controller.btnSave.setDisable(true);
                    controller.btnClear.setDisable(true);
                    controller.btnUpdate.setDisable(false);
                    //controller.selectedFilesPane.setDisable(true);
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
                    ObservationsModel observationsModel = getTableView().getItems().get(getIndex());
                    // Call delete method with species ID
                    deleteObservation(observationsModel.getObservationID());
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


}
