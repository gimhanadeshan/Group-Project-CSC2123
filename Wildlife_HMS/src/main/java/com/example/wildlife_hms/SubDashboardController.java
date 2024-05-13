package com.example.wildlife_hms;

import com.mysql.cj.protocol.a.StringValueEncoder;
import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
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


    @FXML
    private LineChart<String,Number> lineChart;

    @FXML
    private PieChart pieChart;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        habitatCount();
        speciesCount();
        observationCount();
        alertsCount();
        populatePopulationGraph();
        habitatOverviewGraph();

        // Enable animations for LineChart
        lineChart.setCreateSymbols(true);
        FadeTransition ft = new FadeTransition(Duration.millis(1000), pieChart);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();



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

    void populatePopulationGraph() {
        try {
            String query = "SELECT SUM(p.PopulationSize), YEAR(p.LastSurveyDate) AS SurveyYear, h.HabitatName " +
                    "FROM population p INNER JOIN habitats h ON p.HabitatID = h.HabitatID " +
                    "GROUP BY h.HabitatName, SurveyYear";

            PreparedStatement statement = connectDB.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            // Map to store series for each habitat
            Map<String, XYChart.Series<String, Number>> habitatSeriesMap = new HashMap<>();

            while (resultSet.next()) {
                String habitatName = resultSet.getString("HabitatName");
                int populationSize = resultSet.getInt("SUM(p.PopulationSize)");
                String lastSurveyDate = resultSet.getString("SurveyYear");

                // Check if a series for this habitat already exists, if not, create a new one
                XYChart.Series<String, Number> series = habitatSeriesMap.get(habitatName);
                if (series == null) {
                    series = new XYChart.Series<>();
                    series.setName(habitatName); // Set the series name to the habitat name
                    habitatSeriesMap.put(habitatName, series);
                }

                // Assuming LastSurveyDate is a date or year value, you can use it as x-axis
                series.getData().add(new XYChart.Data<>(lastSurveyDate, populationSize));
            }

            // Add all series to the line chart
            lineChart.getData().addAll(habitatSeriesMap.values());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    void habitatOverviewGraph(){

        int hcount=0,scount=0,ocount=0,acount=0;
        try {
            String query1 = "SELECT COUNT(HabitatID) as habitatCount FROM habitats";
            String query2 = "SELECT COUNT(SpeciesID) as speciesCount FROM species";
            String query3 = "SELECT COUNT(ObservationID) as observationCount FROM observations";
            String query4 = "SELECT COUNT(AlertID ) as alertsCount FROM monitoringalerts";
            PreparedStatement statement1 = connectDB.prepareStatement(query1);
            PreparedStatement statement2 = connectDB.prepareStatement(query2);
            PreparedStatement statement3 = connectDB.prepareStatement(query3);
            PreparedStatement statement4 = connectDB.prepareStatement(query4);


            ResultSet resultSet1 = statement1.executeQuery();
            while (resultSet1.next()){
                 hcount =resultSet1.getInt("habitatCount");
            }
            ResultSet resultSet2 = statement2.executeQuery();
            while (resultSet2.next()){
                 scount =resultSet2.getInt("speciesCount");
            }
            ResultSet resultSet3 = statement3.executeQuery();
            while (resultSet3.next()){
                 ocount =resultSet3.getInt("observationCount");
            }
            ResultSet resultSet4 = statement4.executeQuery();
            while (resultSet4.next()){
                 acount =resultSet4.getInt("alertsCount");
            }

            ObservableList<PieChart.Data> pieChartData=
                    FXCollections.observableArrayList(
                            new PieChart.Data("Habitat",hcount),
                            new PieChart.Data("Species",scount),
                            new PieChart.Data("Observations",ocount),
                            new PieChart.Data("Alerts",acount)

                    );

            pieChartData.forEach(data ->
                    data.nameProperty().bind(
                            Bindings.concat(

                                    data.getName()," Amount ",data.pieValueProperty()
                            )
                    )
            );

            pieChart.getData().addAll(pieChartData);







        }catch (SQLException e){

            e.printStackTrace();
        }

    }



}
