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
import java.util.Optional;
import java.util.ResourceBundle;

import static java.lang.Integer.parseInt;

public class VegetationTypesController implements Initializable, ButtonAction{



    @FXML
    private TableColumn<VegetationModel, Integer> colUpdate;

    @FXML
    private TableColumn<VegetationModel, Integer> colDelete;
    @FXML
    private TableColumn<VegetationModel, String> colDesc;

    @FXML
    private TableColumn<VegetationModel, String> colId;

    @FXML
    private TableColumn<VegetationModel, String> colName;

    @FXML
    private TableColumn<VegetationModel, String> colHabitatID;

    @FXML
    private TableColumn<HabitatModel,String> colHabitatName;


    @FXML
    private TableView<VegetationModel> tblVegetation;



    @FXML
    private TextField txtSearch;
    @FXML
    private MFXButton btnNew;



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






    DatabaseConnection connectNow=new DatabaseConnection();
    Connection connectDB = connectNow.getConnection();





    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        showVegetation();



    }

    public ObservableList<VegetationModel> getVegetation() {
        ObservableList<VegetationModel> vegetation = FXCollections.observableArrayList();
        String query = "SELECT v.*, h.HabitatName,h.HA_ID FROM vegetationtypes v INNER JOIN habitats h ON v.HabitatID = h.HabitatID";
        try {
            Statement statement = connectDB.createStatement();
            ResultSet output = statement.executeQuery(query);
            while (output.next()) {
                VegetationModel vg = new VegetationModel(
                        output.getInt("VegetationID"),
                        output.getString("VG_ID"),
                        output.getString("VegetationName"),
                        output.getString("Description"),
                        output.getInt("HabitatID"),
                        output.getString("HA_ID"),
                        output.getString("HabitatName")); // Fetch HabitatName from result set
                vg.setId(output.getInt("VegetationID"));
                vg.setVgId(output.getString("VG_ID"));
                vg.setName(output.getString("VegetationName"));
                vg.setDesc(output.getString("Description"));
                vg.setHaId(output.getString("HA_ID"));
                vg.setHabitatName(output.getString("HabitatName"));
                vegetation.add(vg);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return vegetation;
    }


    public void showVegetation(){
        ObservableList<VegetationModel>list=getVegetation();

        tblVegetation.setItems(list);
        colId.setCellValueFactory(new PropertyValueFactory<>("vgId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDesc.setCellValueFactory(new PropertyValueFactory<>("desc"));
        colHabitatID.setCellValueFactory(new PropertyValueFactory<>("haId"));
        colHabitatName.setCellValueFactory(new PropertyValueFactory<>("habitatName"));



    }


    public void openVegetationForm() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("vegetationTypesForm.fxml"));
        Parent root = fxmlLoader.load();
        VegetationTypesFormController vegetationTypesFormController=fxmlLoader.getController() ;
        vegetationTypesFormController.btnUpdate.setDisable(true);
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        Image icon= new Image("icons8-form-100.png");
        stage.getIcons().add(icon);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL); // Set modality to application modal
        stage.showAndWait(); // Show the stage and wait for it to be closed
    }




    public void search(){
        String srch = txtSearch.getText();

        try {
            String query = "SELECT v.*, h.HabitatName,h.HA_ID FROM vegetationtypes v INNER JOIN habitats h ON v.HabitatID = h.HabitatID WHERE VegetationName LIKE ? OR VegetationID = ?";
            PreparedStatement statement = connectDB.prepareStatement(query);
            statement.setString(1, STR."%\{srch}%");
            // Assuming srch is an integer representing HabitatID, change the condition to equality
            try {
                int vegetationId = parseInt(srch);
                statement.setInt(2, vegetationId);
            } catch (NumberFormatException ex) {
                // Handle the case where srch is not a valid integer for HabitatID
                statement.setInt(2, -1); // Set an ID that doesn't exist to fetch no results
            }

            ResultSet resultSet = statement.executeQuery();

            ObservableList<VegetationModel> searchResults = FXCollections.observableArrayList();
            while (resultSet.next()) {
                VegetationModel vegetation = new VegetationModel(
                        resultSet.getInt("VegetationID"),
                        resultSet.getString("VG_ID"),
                        resultSet.getString("VegetationName"),
                        resultSet.getString("Description"),
                        resultSet.getInt("HabitatID"),
                        resultSet.getString("HA_ID"),
                        resultSet.getString("HabitatName")



                );
                searchResults.add(vegetation);
            }


            tblVegetation.setItems(searchResults);

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
    void deleteVegetation(int id) {
        String query = "delete from vegetationtypes where VegetationID=?";
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

            showVegetation();

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error in deleting Vegetation");
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
                    VegetationModel vegetationModel = getTableView().getItems().get(getIndex());



                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("vegetationTypesForm.fxml"));
                    Parent root = null;
                    try {
                        root = fxmlLoader.load();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    VegetationTypesFormController vegetationTypesFormController=fxmlLoader.getController() ;
                    vegetationTypesFormController.getData(vegetationModel);
                    vegetationTypesFormController.btnSave.setDisable(true);
                    vegetationTypesFormController.btnClear.setDisable(true);
                    vegetationTypesFormController.btnUpdate.setDisable(false);
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
        colDelete.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button();
            private final ImageView imageView = new ImageView(new Image("icons8-delete-20.png")); // Path to your delete image

            {
                deleteButton.setStyle("-fx-background-color: transparent;");
                deleteButton.setOnMouseEntered(event -> deleteButton.setStyle("-fx-background-color: lightgrey;"));
                deleteButton.setOnMouseExited(event -> deleteButton.setStyle("-fx-background-color: transparent;"));
                deleteButton.setGraphic(imageView);
                deleteButton.setOnAction(event -> {
                    VegetationModel vegetationModel = getTableView().getItems().get(getIndex());
                    // Call delete method with species ID
                    deleteVegetation(vegetationModel.getId());
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
