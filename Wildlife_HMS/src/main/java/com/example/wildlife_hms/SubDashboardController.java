package com.example.wildlife_hms;

import com.mysql.cj.protocol.a.StringValueEncoder;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class SubDashboardController implements Initializable {

    @FXML
    private Label lblAlertsCount;

    @FXML
    private Label lblHabitatCount;

    @FXML
    private Label lblObservationCount;

    @FXML
    private Label lblSpeciesCount;

    DatabaseConnection connectNow=new DatabaseConnection();
    Connection connectDB = connectNow.getConnection();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        habitatCount();
        speciesCount();
        observationCount();
        alertsCount();



    }

    void habitatCount(){

        try {
            String query = "SELECT COUNT(HabitatID) as habitatCount FROM habitats ";
            PreparedStatement statement = connectDB.prepareStatement(query);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                int hcount =resultSet.getInt("habitatCount");
                lblHabitatCount.setText(String.valueOf(hcount));
            }




        }catch (SQLException e){

            e.printStackTrace();
        }

    }

    void speciesCount(){

        try {
            String query = "SELECT COUNT(SpeciesID) as speciesCount FROM species ";
            PreparedStatement statement = connectDB.prepareStatement(query);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                int scount =resultSet.getInt("speciesCount");
                lblSpeciesCount.setText(String.valueOf(scount));
            }




        }catch (SQLException e){

            e.printStackTrace();
        }

    }

    void observationCount(){

        try {
            String query = "SELECT COUNT(ObservationID) as observationCount FROM observations ";
            PreparedStatement statement = connectDB.prepareStatement(query);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                int ocount =resultSet.getInt("observationCount");
                lblObservationCount.setText(String.valueOf(ocount));
            }




        }catch (SQLException e){

            e.printStackTrace();
        }

    }

    void alertsCount(){

        try {
            String query = "SELECT COUNT(AlertID ) as alertsCount FROM monitoringalerts ";
            PreparedStatement statement = connectDB.prepareStatement(query);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                int acount =resultSet.getInt("alertsCount");
                lblAlertsCount.setText(String.valueOf(acount));
            }




        }catch (SQLException e){

            e.printStackTrace();
        }

    }

}
