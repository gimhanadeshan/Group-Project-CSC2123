package com.example.wildlife_hms;


import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXCheckbox;
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


public class UserDetailsController implements Initializable,ButtonAction {

    @FXML
    private TableColumn<UserModel, Integer> colUpdate;

    @FXML
    private TableColumn<UserModel, Integer> colDelete;

    @FXML
    private TableColumn<UserModel, Boolean> colActive;


    @FXML
    private TableColumn<UserModel, Date> colDate;


    @FXML
    private TableColumn<UserModel, String> colDp;

    @FXML
    private TableColumn<UserModel, String> colEmail;

    @FXML
    private TableColumn<UserModel, String> colFname;

    @FXML
    private TableColumn<UserModel, String> colUserName;

    @FXML
    private TableColumn<UserModel, String> colGender;

    @FXML
    private TableColumn<UserModel, String> colLname;

    @FXML
    private TableColumn<UserModel, String> colRoll;

    @FXML
    private TextField txtSearch;
    @FXML
    public TableView<UserModel> tblUsers;

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


        showUsers();
        initDpColumn();
        initActiveColumn();



    }

    public ObservableList<UserModel> getUsers() {
        ObservableList<UserModel> users = FXCollections.observableArrayList();
        String query = "SELECT * FROM useraccounts ";
        try {
            Statement statement = connectDB.createStatement();
            ResultSet output = statement.executeQuery(query);
            while (output.next()) {
                UserModel user = new UserModel(output.getInt("id"),
                        output.getString("username"),
                        output.getString("password"),
                        output.getDate("RegisterDate"),
                        output.getString("FirstName"),
                        output.getString("LastName"),
                        output.getString("Email"),
                        output.getString("Roll"),
                        output.getString("Gender"),
                        output.getString("DP"),
                        output.getBoolean("Active")


                );

                users.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return users;
    }




    public void showUsers(){
        ObservableList<UserModel>list=getUsers();
        tblUsers.setItems(list);
        colFname.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLname.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("regDate"));
        colRoll.setCellValueFactory(new PropertyValueFactory<>("roll"));
        colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        colDp.setCellValueFactory(new PropertyValueFactory<>("dp"));
        colActive.setCellValueFactory(new PropertyValueFactory<>("active") );
        colUserName.setCellValueFactory(new PropertyValueFactory<>("userName"));







    }



    public void openUserForm() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("userRegisterFormAdminView.fxml"));
        Parent root = fxmlLoader.load();
        UserRegisterFormController userRegisterFormController=fxmlLoader.getController() ;
        userRegisterFormController.btnUpdate.setDisable(true);
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL); // Set modality to application modal
        stage.showAndWait(); // Show the stage and wait for it to be closed
    }






    public void search() {
        String srch = txtSearch.getText();

        try {
            String query = "SELECT * FROM useraccounts WHERE username LIKE ? OR id = ?";
            PreparedStatement statement = connectDB.prepareStatement(query);
            statement.setString(1, STR."%\{srch}%");
            // Assuming srch is an integer representing HabitatID, change the condition to equality
            try {
                int userId = parseInt(srch);
                statement.setInt(2, userId);
            } catch (NumberFormatException ex) {
                // Handle the case where srch is not a valid integer for HabitatID
                statement.setInt(2, -1); // Set an ID that doesn't exist to fetch no results
            }

            ResultSet resultSet = statement.executeQuery();

            ObservableList<UserModel> searchResults = FXCollections.observableArrayList();
            while (resultSet.next()) {
                UserModel users = new UserModel(
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getDate("RegisterDate"),
                        resultSet.getString("FirstName"),
                        resultSet.getString("LastName"),
                        resultSet.getString("Email"),
                        resultSet.getString("Roll"),
                        resultSet.getString("Gender"),
                        resultSet.getString("DP")

                );
                searchResults.add(users);
            }


            tblUsers.setItems(searchResults);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }






    private void showAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Database Error");
        alert.setContentText("Error in deleting User");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait();
    }






    @FXML
    void deleteUsers(int id) {
        String query = "delete from useraccounts where id=?";
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete");
        alert.setContentText("Are you sure to delete this user?");
        Optional<ButtonType> result = alert.showAndWait();

        try {
            PreparedStatement statement = connectDB.prepareStatement(query);
            statement.setInt(1, id);

            if (result.isPresent() && result.get() == ButtonType.OK) {
                statement.executeUpdate();
            }

            showUsers();

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
                    UserModel userModel = getTableView().getItems().get(getIndex());



                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("userRegisterFormAdminView.fxml"));
                    Parent root;
                    try {
                        root = fxmlLoader.load();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    UserRegisterFormController userRegisterFormController=fxmlLoader.getController() ;
                    userRegisterFormController.getData(userModel);
                    userRegisterFormController.btnRegister.setDisable(true);
                    userRegisterFormController.btnClear.setDisable(true);
                    userRegisterFormController.btnUpdate.setDisable(false);
                    userRegisterFormController.pwdPassword.setVisible(false);
                    userRegisterFormController.pwdConfirmPassword.setVisible(false);
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
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
        colDelete.setCellFactory(_ -> new TableCell<>() {
            private final Button deleteButton = new Button();
            private final ImageView imageView = new ImageView(new Image("icons8-delete-20.png")); // Path to your delete image

            {
                deleteButton.setStyle("-fx-background-color: transparent;");
                deleteButton.setOnMouseEntered(_ -> deleteButton.setStyle("-fx-background-color: lightgrey;"));
                deleteButton.setOnMouseExited(_ -> deleteButton.setStyle("-fx-background-color: transparent;"));
                deleteButton.setGraphic(imageView);
                deleteButton.setOnAction(_ -> {
                    UserModel userModel = getTableView().getItems().get(getIndex());
                    // Call delete method with species ID
                    deleteUsers(userModel.getUserid());
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


    private void initDpColumn() {
        colDp.setCellFactory(_ -> new TableCell<>() {
            private final ImageView imageView = new ImageView();

            {
                int imageSize = 30;
                imageView.setFitWidth(imageSize);
                imageView.setFitHeight(imageSize);

            }

            @Override
            protected void updateItem(String imagePath, boolean empty) {
                super.updateItem(imagePath, empty);
                if (empty || imagePath == null) {
                    setGraphic(null);
                } else {
                    try {
                        // Load the image from the given path
                        Image image = new Image(imagePath);
                        imageView.setImage(image);
                        setGraphic(imageView);
                    } catch (Exception e) {
                        // Handle any exceptions related to loading the image
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    private void initActiveColumn() {
        colActive.setCellFactory(param -> new TableCell<>() {
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



}
