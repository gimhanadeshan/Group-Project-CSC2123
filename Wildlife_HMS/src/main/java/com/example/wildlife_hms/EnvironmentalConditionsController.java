package com.example.wildlife_hms;

import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
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

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Arrays;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.lang.Integer.parseInt;


public class EnvironmentalConditionsController implements Initializable,ButtonAction {


    @FXML
    private TableColumn<EnvironmentalConditionsModel, Integer> colUpdate;

    @FXML
    private TableColumn<EnvironmentalConditionsModel, Integer> colDelete;

    @FXML
    private TableColumn<EnvironmentalConditionsModel, Integer> colConditionId;

    @FXML
    private TableColumn<EnvironmentalConditionsModel, String> colConditionType;

    @FXML
    private TableColumn<EnvironmentalConditionsModel, Double> colConditionValue;

    @FXML
    private TableColumn<EnvironmentalConditionsModel, String> colHabitatID;

    @FXML
    private TableColumn<EnvironmentalConditionsModel, String> colHabitatName;


    @FXML
    private TableView<EnvironmentalConditionsModel> tblEnvironmentalCondition;









    @FXML
    private TextField txtSearch;





    

    DatabaseConnection connectNow=new DatabaseConnection();
    Connection connectDB = connectNow.getConnection();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        initUpdateButton();
        initDeleteButton();
        showConditions();

        

    }

    public ObservableList<EnvironmentalConditionsModel> getConditions() {

        ObservableList<EnvironmentalConditionsModel> conditions= FXCollections.observableArrayList();
        String query = "SELECT e.*, h.HabitatName,h.HA_ID FROM environmentalconditions e INNER JOIN habitats h ON e.HabitatID = h.HabitatID";
        try {
            Statement statement=connectDB.createStatement();
            ResultSet output =statement.executeQuery(query);
            while (output.next()){
                EnvironmentalConditionsModel ec =new EnvironmentalConditionsModel(
                        output.getInt("ConditionID"),
                        output.getString("EC_ID"),
                        output.getString("ConditionType"),
                        output.getDouble("ConditionValue") ,
                        output.getInt("HabitatID"),
                        output.getString("HA_ID"),
                        output.getString("HabitatName")

                );

                conditions.add(ec);

            }

        }catch (Exception e){
            throw new RuntimeException(e);
        }

        return conditions;
    }

    public void showConditions(){
        ObservableList<EnvironmentalConditionsModel>list=getConditions();
        tblEnvironmentalCondition.setItems(list);
        colConditionId.setCellValueFactory(new PropertyValueFactory<>("conId"));
        colConditionType.setCellValueFactory(new PropertyValueFactory<>("conditionType"));
        colConditionValue.setCellValueFactory(new PropertyValueFactory<>("conditionValue"));
        colHabitatID.setCellValueFactory(new PropertyValueFactory<>("haId"));
        colHabitatName.setCellValueFactory(new PropertyValueFactory<>("habitatName"));

    }



    public void openConditionsForm() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("environmentalConditionsForm.fxml"));
        Parent root = fxmlLoader.load();
        EnvironmentalConditionsFormController environmentalConditionsFormController=fxmlLoader.getController() ;
        environmentalConditionsFormController.btnUpdate.setDisable(true);
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL); // Set modality to application modal
        stage.showAndWait(); // Show the stage and wait for it to be closed
    }





    public void search() {
        String srch = txtSearch.getText();

        try {
            String query = "SELECT e.*, h.HabitatName,h.HA_ID FROM environmentalconditions e INNER JOIN habitats h ON e.HabitatID = h.HabitatID WHERE ConditionType LIKE ? OR ConditionID = ?";
            PreparedStatement statement = connectDB.prepareStatement(query);
            statement.setString(1, STR."%\{srch}%");
            // Assuming srch is an integer representing HabitatID, change the condition to equality
            try {
                int conditionId = parseInt(srch);
                statement.setInt(2, conditionId);
            } catch (NumberFormatException ex) {
                // Handle the case where srch is not a valid integer for HabitatID
                statement.setInt(2, -1); // Set an ID that doesn't exist to fetch no results
            }

            ResultSet resultSet = statement.executeQuery();

            ObservableList<EnvironmentalConditionsModel> searchResults = FXCollections.observableArrayList();
            while (resultSet.next()) {
                EnvironmentalConditionsModel conditions = new EnvironmentalConditionsModel(
                        resultSet.getInt("ConditionID"),
                        resultSet.getString("EC_ID"),
                        resultSet.getString("ConditionType"),
                        resultSet.getDouble("ConditionValue"),
                        resultSet.getInt("HabitatID"),
                        resultSet.getString("HA_ID"),
                        resultSet.getString("HabitatName")

                );
                searchResults.add(conditions);
            }


            tblEnvironmentalCondition.setItems(searchResults);

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
    void deleteCondition(int id) {
        String query = "delete from environmentalconditions where ConditionID=?";
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

            showConditions();

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error in deleting Condition");
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
                    EnvironmentalConditionsModel conditionsModel = getTableView().getItems().get(getIndex());



                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("environmentalConditionsForm.fxml"));
                    Parent root = null;
                    try {
                        root = fxmlLoader.load();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    EnvironmentalConditionsFormController conditionsFormController=fxmlLoader.getController() ;
                    conditionsFormController.getData(conditionsModel);
                    conditionsFormController.btnSave.setDisable(true);
                    conditionsFormController.btnClear.setDisable(true);
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
        colDelete.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button();
            private final ImageView imageView = new ImageView(new Image("icons8-delete-20.png")); // Path to your delete image

            {
                deleteButton.setStyle("-fx-background-color: transparent;");
                deleteButton.setOnMouseEntered(event -> deleteButton.setStyle("-fx-background-color: lightgrey;"));
                deleteButton.setOnMouseExited(event -> deleteButton.setStyle("-fx-background-color: transparent;"));
                deleteButton.setGraphic(imageView);
                deleteButton.setOnAction(event -> {
                    EnvironmentalConditionsModel conditionsModel = getTableView().getItems().get(getIndex());
                    // Call delete method with species ID
                    deleteCondition(conditionsModel.getConditionID());
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
