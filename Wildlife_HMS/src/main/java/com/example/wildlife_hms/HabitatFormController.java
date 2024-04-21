package com.example.wildlife_hms;

import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.StringFilter;
import io.github.palexdev.materialfx.utils.others.observables.When;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;


import java.net.URL;
import java.sql.*;
import java.util.Comparator;
import java.util.Optional;
import java.util.ResourceBundle;

import static java.lang.Integer.parseInt;

public class HabitatFormController implements Initializable,ButtonAction {


    @FXML
    private TableColumn<HabitatModel, Integer> colUpdate;

    @FXML
    private TableColumn<HabitatModel, Integer> colDelete;


    DatabaseConnection connectNow=new DatabaseConnection();
    Connection connectDB = connectNow.getConnection();

    @FXML
    private Button btnClear;



    @FXML
    private Button btnSave;


    @FXML
    private TableView<HabitatModel> tblHabitat;

    @FXML
    private MFXTextField txtDesc;

    @FXML
    private MFXTextField txtLocation;

    @FXML
    private MFXTextField txtName;

    @FXML
    private MFXTextField txtSize;

    @FXML
    private MFXTextField txtid;

    int id=0;


    @FXML
    private TextField txtSearch;


    @FXML
    private Button btnEnableForm;




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

    @FXML
    private MFXPaginatedTableView<HabitatModel> paginated;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initUpdateButton();
        initDeleteButton();
        setupPaginated();
        showHabitat();
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

    public void showHabitat(){
        ObservableList<HabitatModel>list=getHabitat();
        tblHabitat.setItems(list);
        colId.setCellValueFactory(new PropertyValueFactory<>("haId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        colSize.setCellValueFactory(new PropertyValueFactory<>("size"));
        colDesc.setCellValueFactory(new PropertyValueFactory<>("desc"));

    }



    @FXML
    void getData() {
       HabitatModel habitatModel = tblHabitat.getSelectionModel().getSelectedItem();

       if (habitatModel != null) {
            id = habitatModel.getId();
            txtName.setText(habitatModel.getName());
            txtLocation.setText(habitatModel.getLocation());
            txtSize.setText(String.valueOf(habitatModel.getSize()));
            txtDesc.setText(habitatModel.getDesc());
            txtid.setText(habitatModel.getHaId());
            btnSave.setDisable(true);
            btnEnableForm.setDisable(true);
            txtName.setDisable(false);
            txtLocation.setDisable(false);
            txtSize.setDisable(false);
            txtDesc.setDisable(false);
            colUpdate.setVisible(true);
            colDelete.setVisible(false);
            btnClear.setDisable(false);
        }
    }

    @FXML
    void enableForm() {
        txtName.setDisable(false);
        txtLocation.setDisable(false);
        txtSize.setDisable(false);
        txtDesc.setDisable(false);
        btnSave.setDisable(false);
        colUpdate.setVisible(false);
        colDelete.setVisible(true);
        btnClear.setDisable(false);
        btnEnableForm.setDisable(true);

    }

   public void disableForm(){
        txtName.setDisable(true);
        txtLocation.setDisable(true);
        txtSize.setDisable(true);
        txtDesc.setDisable(true);
        btnSave.setDisable(true);
        colUpdate.setVisible(false);
        colDelete.setVisible(true);
        btnClear.setDisable(true);
       btnEnableForm.setDisable(false);

    }

   public void clear(){
       txtid.setText("");
       txtName.setText("");
       txtLocation.setText("");
       txtSize.setText("");
       txtDesc.setText("");
       txtSearch.setText("");
       disableForm();
       showHabitat();



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

    private boolean validateFields() {
        return !txtName.getText().isEmpty() && !txtSize.getText().isEmpty();
    }

    private boolean validateSize() {
        try {
            Double.parseDouble(txtSize.getText());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }




    @FXML
    void clearHabitat() {
        clear();
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
            clear();
            showHabitat();
            disableForm();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error in deleting habitat");
        }
    }

    @FXML
    void saveHabitat() {
        if (validateFields() && validateSize()) {
            String query = "INSERT INTO habitats(HabitatName, Location, Size, Description) VALUES (?, ?, ?, ?)";

            try {
                PreparedStatement statement = connectDB.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

                statement.setString(1, txtName.getText());
                statement.setString(2, txtLocation.getText());
                statement.setDouble(3, Double.parseDouble(txtSize.getText()));
                statement.setString(4, txtDesc.getText());
                statement.executeUpdate();

                // Get the generated HabitatID
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int habitatId = generatedKeys.getInt(1);

                    // Update the "HA_ID" column with the formatted value
                    String updateQuery = "UPDATE habitats SET HA_ID = ? WHERE HabitatID = ?";
                    PreparedStatement updateStatement = connectDB.prepareStatement(updateQuery);
                    String formattedHabitatId = String.format("HA%05d", habitatId);
                    updateStatement.setString(1, formattedHabitatId);
                    updateStatement.setInt(2, habitatId);
                    updateStatement.executeUpdate();
                }

                clear();
                showHabitat();
                showAlert(Alert.AlertType.INFORMATION, "Success", "Habitat saved successfully!");
                disableForm();
            } catch (NumberFormatException | SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Error in saving habitat");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Some fields are invalid");
        }
    }


    @FXML
    void updateHabitat() {
        if (validateFields() && validateSize()) {
            String query = "update habitats set HabitatName=?,Location=?,Size=?,Description=? where HabitatID=?";
            try {
                PreparedStatement statement = connectDB.prepareStatement(query);

                statement.setString(1, txtName.getText());
                statement.setString(2, txtLocation.getText());
                statement.setDouble(3, Double.parseDouble((txtSize.getText())));
                statement.setString(4, txtDesc.getText());
                statement.setInt(5, id);
                statement.executeUpdate();

                clear();
                showHabitat();

                showAlert(Alert.AlertType.INFORMATION, "Success", "Habitat updated successfully!");
                disableForm();
            } catch (NumberFormatException | SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Error in updating habitat");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Some fields are invalid");
        }
    }





    ObservableList<HabitatModel>list=getHabitat();

     void setupPaginated() {


        MFXTableColumn<HabitatModel> idColumn = new MFXTableColumn<>("ID", false, Comparator.comparing(HabitatModel::getHaId));
        MFXTableColumn<HabitatModel> nameColumn = new MFXTableColumn<>("Habitat Name", false, Comparator.comparing(HabitatModel::getName));
        MFXTableColumn<HabitatModel> locationColumn = new MFXTableColumn<>("Location", false, Comparator.comparing(HabitatModel::getLocation));
        MFXTableColumn<HabitatModel> sizeColumn = new MFXTableColumn<>("Size", false, Comparator.comparing(HabitatModel::getSize));
        MFXTableColumn<HabitatModel> descColumn = new MFXTableColumn<>("Description", false, Comparator.comparing(HabitatModel::getDesc));
         //MFXTableColumn<HabitatModel> colUpdate = new MFXTableColumn<>("Update", false,null);
        // MFXTableColumn<HabitatModel> colDelete = new MFXTableColumn<>("Delete", false,null);

        idColumn.setRowCellFactory(habitatModel -> new MFXTableRowCell<>(HabitatModel::getHaId));
        nameColumn.setRowCellFactory(habitatModel -> new MFXTableRowCell<>(HabitatModel::getName));
        descColumn.setRowCellFactory(habitatModel -> new MFXTableRowCell<>(HabitatModel::getDesc));
        locationColumn.setRowCellFactory(habitatModel -> new MFXTableRowCell<>(HabitatModel::getLocation));
        sizeColumn.setRowCellFactory(habitatModel -> new MFXTableRowCell<>(HabitatModel::getSize));




        paginated.getTableColumns().addAll(idColumn, nameColumn, locationColumn, sizeColumn, descColumn);
      //  paginated.getTableColumns().addAll(colUpdate,colDelete);
        paginated.getFilters().addAll(
                new StringFilter<>("ID", HabitatModel::getHaId),
                new StringFilter<>("Name", HabitatModel::getName),
                new StringFilter<>("Location", HabitatModel::getLocation),
                new StringFilter<>("Description", HabitatModel::getDesc)

        );
        paginated.setItems(list);

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

                    // Call update method with species ID
                    updateHabitat();



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
