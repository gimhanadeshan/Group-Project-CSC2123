package com.example.wildlife_hms;


import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXCheckbox;
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

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.lang.Integer.parseInt;


public class UserPermissionsController implements Initializable {

    @FXML
    private MFXButton btnClearFilter;

    @FXML
    private MFXButton btnRefresh;

    @FXML
    private TableColumn<UserPermissionsModel,Boolean> colCreate;

    @FXML
    private TableColumn<UserPermissionsModel, Boolean> colDelete;

    @FXML
    private TableColumn<UserPermissionsModel,Boolean> colUpdate;

    @FXML
    private TableColumn<UserPermissionsModel,Integer> colUpdatePermission;

    @FXML
    private TableColumn<UserPermissionsModel,String> colUserName;

    @FXML
    private TableColumn<UserPermissionsModel,Boolean> colView;

    @FXML
    private MFXFilterComboBox<UserPermissionsModel> combUserName;

    @FXML
    private TableView<UserPermissionsModel> tblUserPermissions;





    DatabaseConnection connectNow=new DatabaseConnection();
    Connection connectDB = connectNow.getConnection();



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        initUpdatePermissionButton();
        showUsersPermissions();
        initActiveColumn(colCreate);
        initActiveColumn(colDelete);
        initActiveColumn(colView);
        initActiveColumn(colUpdate);

        initCombobox();


    }


    @FXML
    void initCombobox() {
        StringConverter<UserPermissionsModel> converter = FunctionalStringConverter.to(userPermissionsModel -> (userPermissionsModel == null) ? "" : STR."\{userPermissionsModel.getUserName()}");
        Function<String, Predicate<UserPermissionsModel>> filterFunction = s -> userPermissionsModel -> StringUtils.containsIgnoreCase(converter.toString(userPermissionsModel), s);
        combUserName.setItems(getUsersPermission());
        combUserName.setConverter(converter);
        combUserName.setFilterFunction(filterFunction);

        combUserName.setOnAction(event -> filteredData());


    }

    public ObservableList<UserPermissionsModel> getUsersPermission() {
        ObservableList<UserPermissionsModel> usersPermissions = FXCollections.observableArrayList();
        String query = "SELECT * FROM userpermissions ";
        try {
            Statement statement = connectDB.createStatement();
            ResultSet output = statement.executeQuery(query);
            while (output.next()) {
                UserPermissionsModel user = new UserPermissionsModel(
                        output.getInt("ID"),
                        output.getString("UserName"),
                        output.getBoolean("HabitatManagement"),
                        output.getBoolean("UserManagement"),
                        output.getBoolean("FieldDataCollection"),
                        output.getBoolean("Research"),
                        output.getBoolean("Reporting"),
                        output.getBoolean("Settings"),
                        output.getBoolean("OtherFiles"),
                        output.getBoolean("CreateP"),
                        output.getBoolean("DeleteP"),
                        output.getBoolean("UpdateP"),
                        output.getBoolean("View")

                );

                usersPermissions.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return usersPermissions;
    }




    public void showUsersPermissions(){
        ObservableList<UserPermissionsModel>list=getUsersPermission();
        tblUserPermissions.setItems(list);
        colUserName.setCellValueFactory(new PropertyValueFactory<>("userName"));
        colCreate.setCellValueFactory(new PropertyValueFactory<>("create"));
        colDelete.setCellValueFactory(new PropertyValueFactory<>("delete"));
        colUpdate.setCellValueFactory(new PropertyValueFactory<>("update"));
        colView.setCellValueFactory(new PropertyValueFactory<>("view"));

    }



    private void showAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Database Error");
        alert.setContentText("Error in deleting User");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait();
    }











    public void initUpdatePermissionButton() {
        colUpdatePermission.setCellFactory(_ -> new TableCell<>() {
            private final Button updateButton = new Button();
            private final ImageView imageView = new ImageView(new Image("icons8-edit-20.png")); // Path to your update image

            {

                updateButton.setStyle("-fx-background-color: transparent;");
                updateButton.setOnMouseEntered(_ -> updateButton.setStyle("-fx-background-color: lightgrey;"));
                updateButton.setOnMouseExited(_ -> updateButton.setStyle("-fx-background-color: transparent;"));
                updateButton.setGraphic(imageView);
                updateButton.setOnAction(_ -> {
                    UserPermissionsModel userPermissionsModel = getTableView().getItems().get(getIndex());



                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("userPermissionsForm.fxml"));
                    Parent root;
                    try {
                        root = fxmlLoader.load();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    UserPermissionsFormController controller=fxmlLoader.getController() ;
                    controller.getData(userPermissionsModel);

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






    private void initActiveColumn(TableColumn<UserPermissionsModel,Boolean> col) {
        col.setCellFactory(param -> new TableCell<>() {
            private final MFXCheckbox checkBox = new MFXCheckbox();

            @Override
            protected void updateItem(Boolean active, boolean empty) {
                super.updateItem(active, empty);
                if (empty || active == null) {
                    setGraphic(null);
                } else {
                    checkBox.setSelected(active);
                    checkBox.setDisable(true);
                    setGraphic(checkBox);
                }
            }
        });
    }


    private void filteredData() {

        if(!(combUserName.getSelectionModel().getSelectedItem() ==null)) {
            String selectedUserName = combUserName.getSelectionModel().getSelectedItem().getUserName();

            ObservableList<UserPermissionsModel> filteredList = getUsersPermission().filtered(item ->
                    item.getUserName().equals(selectedUserName)
            );

            tblUserPermissions.setItems(filteredList);
        }
    }



    public void clearFilter() {

        combUserName.getSelectionModel().clearSelection();
        showUsersPermissions();
    }

    


}
