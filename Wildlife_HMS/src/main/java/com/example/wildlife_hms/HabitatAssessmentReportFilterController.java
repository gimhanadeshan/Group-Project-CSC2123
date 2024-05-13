package com.example.wildlife_hms;

import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.utils.StringUtils;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.util.StringConverter;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.function.Predicate;

public class HabitatAssessmentReportFilterController implements Initializable {

    DatabaseConnection connectNow=new DatabaseConnection();
    Connection connectDB = connectNow.getConnection();



    @FXML
    private TextField txtHabitatId;

    @FXML
    private MFXFilterComboBox<HabitatModel> filterCombo;

    @FXML
    public Button btnReportGenerate;

    HabitatController habitatController=new HabitatController();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        StringConverter<HabitatModel> converter = FunctionalStringConverter.to(habitatModel -> (habitatModel == null) ? "" : STR."\{habitatModel.getHaId()} | \{habitatModel.getName()}");
        Function<String, Predicate<HabitatModel>> filterFunction = s -> habitatModel -> StringUtils.containsIgnoreCase(converter.toString(habitatModel), s);
        filterCombo.setItems(habitatController.getHabitat());
        filterCombo.setConverter(converter);
        filterCombo.setFilterFunction(filterFunction);



        filterCombo.setOnAction(event -> lookupHabitatID());

    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait();
    }



    void lookupHabitatID() {
        try {

            if(!(filterCombo.getSelectedItem()==null)) {
                String habitatIdStr = filterCombo.getSelectionModel().getSelectedItem().getHaId().trim();

                if (!habitatIdStr.isEmpty()) {

                    String query = "SELECT HabitatName,HabitatID FROM habitats WHERE HA_ID = ?";
                    PreparedStatement statement = connectDB.prepareStatement(query);
                    statement.setString(1, habitatIdStr);


                    ResultSet resultSet = statement.executeQuery();

                    if (resultSet.next()) {
                        int id = resultSet.getInt("HabitatID");
                        txtHabitatId.setText(String.valueOf(id));
                    } else {
                        showAlert(Alert.AlertType.WARNING, "Not Found", STR."Habitat with ID \{habitatIdStr} not found");

                    }
                } else {
                    showAlert(Alert.AlertType.WARNING, "Empty Field", "Please enter a HabitatID");

                }
            }
        } catch (NumberFormatException | SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error in looking up Habitat Name");
            e.printStackTrace(); // You may want to log the exception for debugging purposes
        }
    }


    public void generateReport(int id) {
        try {
            JasperDesign design = JRXmlLoader.load("src/main/resources/com/example/wildlife_hms/habitatAssementReport.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(design);
            Map<String, Object> parameters = new HashMap<>();
            parameters.put(JRParameter.REPORT_CONNECTION, connectDB);
            parameters.put("Parameter1",id);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connectDB);
            JasperViewer viewer = new JasperViewer(jasperPrint, false);
            viewer.setExtendedState(JasperViewer.MAXIMIZED_BOTH);
            viewer.setVisible(true);
        } catch (JRException e) {
            throw new RuntimeException(e);
        }
    }

    public void btnReportGenerate() {
        int habitatID = Integer.parseInt(txtHabitatId.getText());
        generateReport(habitatID);
    }

}