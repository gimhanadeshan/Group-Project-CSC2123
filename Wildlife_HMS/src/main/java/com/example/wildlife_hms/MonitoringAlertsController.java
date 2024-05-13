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

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Arrays;
import java.util.Optional;
import java.util.ResourceBundle;

import static java.lang.Integer.parseInt;


public class MonitoringAlertsController implements Initializable,ButtonAction {

    @FXML
    private TableColumn<AlertModel, Integer> colUpdate;

    @FXML
    private TableColumn<AlertModel, Integer> colDelete;




    @FXML
    private TableColumn<AlertModel, Date> colAlertDate;

    @FXML
    private TableColumn<AlertModel, String> colAlertDesc;

    @FXML
    private TableColumn<AlertModel, String> colAlertId;

    @FXML
    private TableColumn<AlertModel, String> colAlertType;

    @FXML
    private TableColumn<HabitatModel, String> colHabitatID;

    @FXML
    private TableColumn<HabitatModel, String> colHabitatName;

    @FXML
    private TableColumn<AlertModel, String> colAlertStatus;

    @FXML
    public TableView<AlertModel> tblAlert;

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


    }




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        showAlerts();

    }

    public ObservableList<AlertModel> getAlerts() {
        ObservableList<AlertModel> alerts = FXCollections.observableArrayList();
        String query = "SELECT a.*, h.HabitatName,h.HA_ID FROM monitoringalerts a INNER JOIN habitats h ON a.HabitatID = h.HabitatID";
        try {
            Statement statement = connectDB.createStatement();
            ResultSet output = statement.executeQuery(query);
            while (output.next()) {
                AlertModel al = new AlertModel(output.getInt("AlertID"),
                        output.getString("AL_ID"),
                        output.getString("AlertType"),
                        output.getString("AlertDescription"),
                        output.getDate("AlertDate"),
                        output.getInt("HabitatID"),
                        output.getString("HA_ID"),
                        output.getString("HabitatName"),
                        output.getString("AlertStatus")


                );

                alerts.add(al);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return alerts;
    }




    public void showAlerts(){
        ObservableList<AlertModel>list=getAlerts();
        tblAlert.setItems(list);
        colAlertId.setCellValueFactory(new PropertyValueFactory<>("alId"));
        colAlertType.setCellValueFactory(new PropertyValueFactory<>("alertType"));
        colAlertDesc.setCellValueFactory(new PropertyValueFactory<>("alertDisc"));
        colAlertDate.setCellValueFactory(new PropertyValueFactory<AlertModel,Date>("alertDate"));
        colHabitatID.setCellValueFactory(new PropertyValueFactory<>("haId"));
        colHabitatName.setCellValueFactory(new PropertyValueFactory<>("habitatName"));
        colAlertStatus.setCellValueFactory(new PropertyValueFactory<>("alertStatus"));


    }



    public void openAlertsForm() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("monitoringAlertsForm.fxml"));
        Parent root = fxmlLoader.load();
        MonitoringAlertsFormController monitoringAlertsFormController=fxmlLoader.getController() ;
        monitoringAlertsFormController.btnUpdate.setDisable(true);
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        Image icon= new Image("icons8-form-100.png");
        stage.getIcons().add(icon);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL); // Set modality to application modal
        stage.showAndWait(); // Show the stage and wait for it to be closed
    }






    public void search() {
        String srch = txtSearch.getText();

        try {
            String query = "SELECT a.*, h.HabitatName,h.HA_ID FROM monitoringalerts a INNER JOIN habitats h ON a.HabitatID = h.HabitatID WHERE AlertDescription LIKE ? OR AlertID = ?";
            PreparedStatement statement = connectDB.prepareStatement(query);
            statement.setString(1, STR."%\{srch}%");
            // Assuming srch is an integer representing HabitatID, change the condition to equality
            try {
                int alertId = parseInt(srch);
                statement.setInt(2, alertId);
            } catch (NumberFormatException ex) {
                // Handle the case where srch is not a valid integer for HabitatID
                statement.setInt(2, -1); // Set an ID that doesn't exist to fetch no results
            }

            ResultSet resultSet = statement.executeQuery();

            ObservableList<AlertModel> searchResults = FXCollections.observableArrayList();
            while (resultSet.next()) {
                AlertModel alerts = new AlertModel(
                        resultSet.getInt("AlertID"),
                        resultSet.getString("AL_ID"),
                        resultSet.getString("AlertType"),
                        resultSet.getString("AlertDescription"),
                        resultSet.getDate("AlertDate"),
                        resultSet.getInt("HabitatID"),
                        resultSet.getString("HA_ID"),
                        resultSet.getString("HabitatName"),
                        resultSet.getString("AlertStatus")

                );
                searchResults.add(alerts);
            }


            tblAlert.setItems(searchResults);

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
    void deleteAlerts(int id) {
        String query = "delete from monitoringalerts where AlertID=?";
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

            showAlerts();

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error in deleting Alerts");
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
                    AlertModel alertModel = getTableView().getItems().get(getIndex());



                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("monitoringAlertsForm.fxml"));
                    Parent root = null;
                    try {
                        root = fxmlLoader.load();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    MonitoringAlertsFormController monitoringAlertsFormController=fxmlLoader.getController() ;
                    monitoringAlertsFormController.getData(alertModel);
                    monitoringAlertsFormController.btnSave.setDisable(true);
                    monitoringAlertsFormController.btnClear.setDisable(true);
                    monitoringAlertsFormController.btnUpdate.setDisable(false);
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    Image icon= new Image("icons8-form-100.png");
                    stage.getIcons().add(icon);
                    stage.setResizable(false);
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.showAndWait();

                    // Call update method with species ID
                   // updateAlerts(alertModel.getAlertId());



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
        colDelete.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button();
            private final ImageView imageView = new ImageView(new Image("icons8-delete-20.png")); // Path to your delete image

            {
                deleteButton.setStyle("-fx-background-color: transparent;");
                deleteButton.setOnMouseEntered(event -> deleteButton.setStyle("-fx-background-color: lightgrey;"));
                deleteButton.setOnMouseExited(event -> deleteButton.setStyle("-fx-background-color: transparent;"));
                deleteButton.setGraphic(imageView);
                deleteButton.setOnAction(event -> {
                    AlertModel alertModel = getTableView().getItems().get(getIndex());
                    // Call delete method with species ID
                    deleteAlerts(alertModel.getAlertId());
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
