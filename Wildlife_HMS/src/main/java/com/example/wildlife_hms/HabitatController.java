package com.example.wildlife_hms;



import io.github.palexdev.materialfx.controls.MFXPagination;
import io.github.palexdev.materialfx.controls.cell.MFXPage;
import io.github.palexdev.materialfx.utils.others.observables.When;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
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
import java.util.function.Function;

import static java.lang.Integer.parseInt;

public class HabitatController implements Initializable,ButtonAction {


    @FXML
    private TableColumn<HabitatModel, Integer> colUpdate;

    @FXML
    private TableColumn<HabitatModel, Integer> colDelete;


    DatabaseConnection connectNow=new DatabaseConnection();
    Connection connectDB = connectNow.getConnection();



    @FXML
    private TableView<HabitatModel> tblHabitat;


    @FXML
    private TextField txtSearch;






    @FXML
    private TableColumn<HabitatModel, String> colDesc;

    @FXML
    private TableColumn<HabitatModel, String> colId;

    @FXML
    private TableColumn<HabitatModel, String> colLocation;

    @FXML
    private TableColumn<HabitatModel, String> colName;

    @FXML
    private TableColumn<HabitatModel, Double> colSize;







    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        initUpdateButton();
        initDeleteButton();
        showHabitat();


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







    public void showHabitat(){
        ObservableList<HabitatModel>list=getHabitat();
        tblHabitat.setItems(list);
        colId.setCellValueFactory(new PropertyValueFactory<>("haId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        colSize.setCellValueFactory(new PropertyValueFactory<>("size"));
        colDesc.setCellValueFactory(new PropertyValueFactory<>("desc"));

    }



    public void openHabitatForm() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("habitatForm1.fxml"));
        Parent root = fxmlLoader.load();
        HabitatForm1Controller controller=fxmlLoader.getController() ;
        controller.btnUpdate.setDisable(true);
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL); // Set modality to application modal
        stage.showAndWait(); // Show the stage and wait for it to be closed
    }








    public void search() {

        txtSearch.setStyle("-fx-border-color:#252B48 ");
        String srch = txtSearch.getText();


        try {
            String query = "SELECT * FROM habitats WHERE HabitatName LIKE ? OR HabitatID = ?";
            PreparedStatement statement = connectDB.prepareStatement(query);
            statement.setString(1, "%" + srch + "%");
            // Assuming srch is an integer representing HabitatID, change the condition to equality
            try {
                int habitatId = parseInt(srch);
                statement.setInt(2, habitatId);
            } catch (NumberFormatException ex) {
                // Handle the case where srch is not a valid integer for HabitatID
                statement.setInt(2, -1); // Set an ID that doesn't exist to fetch no results
            }

            ResultSet resultSet = statement.executeQuery();

            ObservableList<HabitatModel> searchResults = FXCollections.observableArrayList();
            while (resultSet.next()) {
                HabitatModel habitat = new HabitatModel(
                        resultSet.getInt("HabitatID"),
                        resultSet.getString("HA_ID"),
                        resultSet.getString("HabitatName"),
                        resultSet.getString("Location"),
                        resultSet.getDouble("Size"),
                        resultSet.getString("Description")
                );
                searchResults.add(habitat);
            }


            tblHabitat.setItems(searchResults);

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
    void deleteHabitat(int id) {
        String query = "delete from habitats where HabitatID=?";
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

            showHabitat();

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error in deleting habitat");
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
                    HabitatModel habitatModel = (HabitatModel) getTableView().getItems().get(getIndex());

                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("habitatForm1.fxml"));
                    Parent root;
                    try {
                        root = fxmlLoader.load();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    HabitatForm1Controller controller=fxmlLoader.getController() ;
                    controller.getData(habitatModel);
                    controller.btnSave.setDisable(true);
                    controller.btnClear.setDisable(true);
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

    @Override
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
                    HabitatModel habitatModel = getTableView().getItems().get(getIndex());
                    // Call delete method with species ID
                    deleteHabitat(habitatModel.getId());
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
