module com.example.whildlif_hms {
    requires javafx.controls;
    requires javafx.fxml;
    requires mysql.connector.j;
    requires java.sql;
    requires javafx.graphics;
    requires MaterialFX;


    opens com.example.wildlife_hms to javafx.fxml;
    exports com.example.wildlife_hms;



}