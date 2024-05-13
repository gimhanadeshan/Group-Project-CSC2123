package com.example.wildlife_hms;

import io.github.palexdev.materialfx.controls.MFXButton;
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
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

import static java.lang.Integer.expand;
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
    private TableColumn<ObservationsModel, Integer> colPrint;


    @FXML
    private TableView<ObservationsModel> tblObservations;

    @FXML
    private TextField txtSearch;

    @FXML
    private MFXButton btnNew;


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
        if(userPermissions.isView()){
            initPrintButton();
            colPrint.setVisible(true);
        }else {
            colPrint.setVisible(false);
        }


    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

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
        Image icon= new Image("icons8-form-100.png");
        stage.getIcons().add(icon);
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
                    Image icon= new Image("icons8-form-100.png");
                    stage.getIcons().add(icon);
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


    public void initPrintButton() {
        colPrint.setCellFactory(param -> new TableCell<>() {
            private final Button printButton = new Button();
            private final ImageView imageView = new ImageView(new Image("icons8-preview-20.png")); // Path to your update image

            {

                printButton.setStyle("-fx-background-color: transparent;");
                printButton.setOnMouseEntered(event -> printButton.setStyle("-fx-background-color: lightgrey;"));
                printButton.setOnMouseExited(event -> printButton.setStyle("-fx-background-color: transparent;"));
                printButton.setGraphic(imageView);
                printButton.setOnAction(event -> {
                    ObservationsModel observationsModel = getTableView().getItems().get(getIndex());


                        printObservation(observationsModel.getObservationID());



                });
            }

            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(printButton);
                }
            }
        });
    }

    public void printObservation(int obsID) {
        try {

            JasperDesign design = JRXmlLoader.load("src/main/resources/com/example/wildlife_hms/observation.jrxml");

            // Compile the JasperReport
            JasperReport jasperReport = JasperCompileManager.compileReport(design);

            // Fill the report with data
            Map<String, Object> parameters = new HashMap<>();
            parameters.put(JRParameter.REPORT_CONNECTION, connectDB); // Pass the connection
            parameters.put("Parameter1",obsID); // Pass the observation ID as parameter
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,parameters, connectDB);

            // View the report
            JasperViewer viewer = new JasperViewer(jasperPrint, false);
            viewer.setExtendedState(JasperViewer.MAXIMIZED_BOTH); // Maximize the viewer window
            viewer.setVisible(true);

        } catch (JRException e) {
            throw new RuntimeException(e);
        }
    }


}
