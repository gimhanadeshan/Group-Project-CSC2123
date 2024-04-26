package com.example.wildlife_hms;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

public class OtherMasterFilesController implements Initializable {

    @FXML
    private TableColumn<AlertStatusModel, String> colAlertStatus;

    @FXML
    private TableColumn<AlertTypeModel, String> colAlertTypes;

    @FXML
    private TableColumn<ConservationStatusModel, String> colConservationStatus;

    @FXML
    private TableColumn<AlertStatusModel, String> colRemarkAS;

    @FXML
    private TableColumn<ConservationStatusModel, String> colRemarkCS;

    @FXML
    private TableColumn<AlertTypeModel, String> colRemarksAT;

    @FXML
    private TableView<AlertStatusModel> tblAlertStatus;

    @FXML
    private TableView<AlertTypeModel> tblAlertTypes;

    @FXML
    private TableView<ConservationStatusModel> tblConservationStatus;

    @FXML
    private VBox as;

    @FXML
    private VBox at;

    @FXML
    private VBox cs;


    DatabaseConnection connectNow=new DatabaseConnection();
    Connection connectDB = connectNow.getConnection();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        showAlertStatus();
        showAlertTypes();
        showConservationStatus();

        as.setVisible(false);as.setManaged(false);
        at.setVisible(false);at.setManaged(false);
        cs.setVisible(false);cs.setManaged(false);



    }

    @FXML
    void toggleScrollAlertStatus() {
        if (as.isVisible()) {as.setVisible(false);as.setManaged(false);} else {as.setVisible(true);as.setManaged(true);}
    }

    @FXML
    void toggleScrollAlertTypes() {
        if (at.isVisible()) {at.setVisible(false);at.setManaged(false);} else {at.setVisible(true);at.setManaged(true);}
    }

    @FXML
    void toggleScrollConservationStatus() {
        if (cs.isVisible()) {cs.setVisible(false);cs.setManaged(false);} else {cs.setVisible(true);cs.setManaged(true);}
    }








    public ObservableList<AlertStatusModel> getAlertStatus() {
        ObservableList<AlertStatusModel> alertStatusModels = FXCollections.observableArrayList();
        String query = "select * from alertstatus";
        try {
            Statement statement = connectDB.createStatement();
            ResultSet output = statement.executeQuery(query);
            while (output.next()) {
                AlertStatusModel as = new AlertStatusModel(
                        output.getInt("AlertStatusID"),
                        output.getString("AlertStatus"),
                        output.getString("Remarks"));

                        alertStatusModels.addAll(as);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return alertStatusModels;
    }


    public void showAlertStatus(){
        ObservableList<AlertStatusModel>list=getAlertStatus();

        tblAlertStatus.setItems(list);
        colAlertStatus.setCellValueFactory(new PropertyValueFactory<>("alertStatus"));
        colRemarkAS.setCellValueFactory(new PropertyValueFactory<>("remark"));


    }

    public ObservableList<AlertTypeModel> getAlertTypes() {
        ObservableList<AlertTypeModel> alertTypeModels = FXCollections.observableArrayList();
        String query = "select * from alerttypes";
        try {
            Statement statement = connectDB.createStatement();
            ResultSet output = statement.executeQuery(query);
            while (output.next()) {
                AlertTypeModel at = new AlertTypeModel(
                        output.getInt("AlertTypeID"),
                        output.getString("AlertType"),
                        output.getString("Remarks"));

                alertTypeModels.addAll(at);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return alertTypeModels;
    }


    public void showAlertTypes(){
        ObservableList<AlertTypeModel>list=getAlertTypes();

        tblAlertTypes.setItems(list);
        colAlertTypes.setCellValueFactory(new PropertyValueFactory<>("alertType"));
        colRemarksAT.setCellValueFactory(new PropertyValueFactory<>("remark"));


    }



    public ObservableList<ConservationStatusModel> getConservationStatus() {
        ObservableList<ConservationStatusModel> conservationStatusModels = FXCollections.observableArrayList();
        String query = "select * from conservationstatus";
        try {
            Statement statement = connectDB.createStatement();
            ResultSet output = statement.executeQuery(query);
            while (output.next()) {
                ConservationStatusModel cs = new ConservationStatusModel(
                        output.getInt("ConservationStatusID"),
                        output.getString("ConservationStatus"),
                        output.getString("Remark"));

                conservationStatusModels.addAll(cs);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return conservationStatusModels;
    }


    public void showConservationStatus(){
        ObservableList<ConservationStatusModel>list=getConservationStatus();
        tblConservationStatus.setItems(list);
        colConservationStatus.setCellValueFactory(new PropertyValueFactory<>("conservationStatus"));
        colRemarkCS.setCellValueFactory(new PropertyValueFactory<>("remark"));


    }






}
