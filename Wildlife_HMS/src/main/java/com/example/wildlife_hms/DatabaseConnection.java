package com.example.wildlife_hms;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DatabaseConnection {

    public Connection databaseLink;

    public Connection getConnection() {
        String databaseName = "wildlifehms";
        String databaseUser = "root";
        String databasePassword = "";
        String url = STR."jdbc:mysql://localhost/\{databaseName}";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink = DriverManager.getConnection(url, databaseUser, databasePassword);


        } catch (SQLException | ClassNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to connect to the database. Please make sure the server is running.", ButtonType.OK);
            alert.showAndWait();

        }

        return databaseLink;
    }
}
