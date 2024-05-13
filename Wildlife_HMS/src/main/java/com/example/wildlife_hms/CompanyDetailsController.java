package com.example.wildlife_hms;



import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class CompanyDetailsController implements Initializable {

    @FXML
    private MFXButton btnClear;

    @FXML
    private MFXButton btnUpdate;

    @FXML
    private ImageView imageViewDp;



    @FXML
    private MFXTextField txtAddress;

    @FXML
    private MFXTextField txtEmail;

    @FXML
    private MFXTextField txtTelNo;

    @FXML
    private MFXTextField txtname;

    private static final String IMAGE_PATH_PREFIX = "file:///";



    DatabaseConnection connectNow=new DatabaseConnection();
    Connection connectDB = connectNow.getConnection();





    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        imageViewDp.setOnDragOver(event -> {
            if (event.getGestureSource() != imageViewDp && event.getDragboard().hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });

        imageViewDp.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasFiles()) {
                success = true;
                String imagePath = IMAGE_PATH_PREFIX + db.getFiles().get(0).getPath();
                imageViewDp.setImage(new Image(imagePath));
            }
            event.setDropCompleted(success);
            event.consume();
        });
        showCompanyDetails();
    }



    public CompanyDetailsModel getCompany() {
        String query = "SELECT * FROM companydetails WHERE ID=1;";
        CompanyDetailsModel company = null;
        try {
            Statement statement = connectDB.createStatement();
            ResultSet output = statement.executeQuery(query);

            // Check if the ResultSet has any rows
            if (output.next()) {
                company = new CompanyDetailsModel(
                        output.getString("Name"),
                        output.getString("Address"),
                        output.getString("Email"),
                        output.getString("TelNo"),
                        output.getString("Logo")
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return company;
    }



    public void showCompanyDetails(){
        CompanyDetailsModel company=getCompany();
        if (company != null) {
            txtname.setText(company.getName());
            txtAddress.setText(company.getAddress());
            txtEmail.setText(company.getEmail());
            txtTelNo.setText(company.getTelNo());

            String imagePath = company.getLogo();
            if (imagePath != null && !imagePath.isEmpty()) {
                try {
                    Image image = new Image(imagePath);
                    imageViewDp.setImage(image);
                } catch (Exception e) {
                    // Handle any exceptions related to loading the image
                    e.printStackTrace();
                }
            }
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
    void updateCompany() {


        String query="UPDATE companydetails SET Name=?,Address= ?,Email=?,TelNo=?,Logo=? WHERE ID=1 ;";
        try {
            PreparedStatement preparedStatement = connectDB.prepareStatement(query);
            preparedStatement.setString(1,txtname.getText());
            preparedStatement.setString(2,txtAddress.getText());
            preparedStatement.setString(3,txtEmail.getText());
            preparedStatement.setString(4,txtTelNo.getText());
            preparedStatement.setString(5, imageViewDp.getImage() != null ? imageViewDp.getImage().getUrl() : null);


            preparedStatement.execute();
            btnUpdate.setDisable(true);
            txtname.setDisable(true);
            txtAddress.setDisable(true);
            txtEmail.setDisable(true);
            txtTelNo.setDisable(true);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Company Details updated successfully!");

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", STR."Error in updating Company:\n \{e.getMessage()}");
            throw new RuntimeException(e);
        }

        showCompanyDetails();


    }

    public void editCompany(){
        txtname.setDisable(false);
        txtAddress.setDisable(false);
        txtEmail.setDisable(false);
        txtTelNo.setDisable(false);
        btnUpdate.setDisable(false);
    }

}

