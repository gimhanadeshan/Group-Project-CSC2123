package com.example.wildlife_hms;

import io.github.palexdev.materialfx.controls.MFXProgressSpinner;
import javafx.animation.TranslateTransition;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.geometry.Pos;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;


import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static javafx.stage.StageStyle.UNDECORATED;


public class DashboardController implements Initializable {


    public Label user;

    @FXML
    public ImageView profile;
    public StackPane ancMain;

    @FXML
    private HBox reporting;

    @FXML
    private HBox researsh;
    public HBox btnDashboard;
    @FXML
    private HBox userManagement;

    @FXML
    private HBox settings;

    @FXML
    private HBox fieldDataCollection;

    @FXML
    public HBox habitatManagement;

    @FXML
    private HBox other;

    @FXML
    private HBox btnLogout;

    @FXML
    private MFXProgressSpinner psLoading;

    @FXML
    private BorderPane bpDashboard;


    @FXML
    private ScrollPane scrollPane; // Make sure to link this with your FXML ScrollPane

    @FXML
    private VBox h;

    @FXML
    private VBox o;

    @FXML
    private VBox s;

    @FXML
    private VBox a;

    @FXML
    private VBox se;

    @FXML
    private VBox um;

    @FXML
    private VBox of;

    public Label userID;


    @FXML
    private HBox habitatForm;
    @FXML
    private HBox alertForm;

    @FXML
    private HBox conditionForm;

    @FXML
    private HBox observationForm;

    @FXML
    private HBox observationForm1;

    @FXML
    private HBox populationForm;

    @FXML
    private HBox speciesForm;




    UserPermissionsModel userPermissions;



    public void setUserPermissions(UserPermissionsModel userPermissions) {


        if(userPermissions.isHabitatManagement()){
            habitatManagement.setVisible(true);habitatManagement.setManaged(true);
        }else {
            habitatManagement.setVisible(false);habitatManagement.setManaged(false);
        }

        if(userPermissions.isUserManagement()){
            userManagement.setVisible(true);userManagement.setManaged(true);
        }else {
            userManagement.setVisible(false);userManagement.setManaged(false);
        }

        if(userPermissions.isFieldDataCollection()){
            fieldDataCollection.setVisible(true);fieldDataCollection.setManaged(true);
        }else {
            fieldDataCollection.setVisible(false);fieldDataCollection.setManaged(false);
        }

        if(userPermissions.isResearch()){
            researsh.setVisible(true);researsh.setManaged(true);
        }else {
            researsh.setVisible(false);researsh.setManaged(false);
        }

        if(userPermissions.isReporting()){
            reporting.setVisible(true);reporting.setManaged(true);
        }else {
            reporting.setVisible(false);reporting.setManaged(false);
        }

        if(userPermissions.isOtherFiles()){
            other.setVisible(true);other.setManaged(true);
        }else {
            other.setVisible(false);other.setManaged(false);
        }

        if(userPermissions.isSettings()){
            settings.setVisible(true);settings.setManaged(true);
        }else {
            settings.setVisible(false);settings.setManaged(false);
        }

        if (userPermissions.isCreate()){
            habitatForm.setVisible(true);habitatForm.setManaged(true);
            alertForm.setVisible(true);alertForm.setManaged(true);
            conditionForm.setVisible(true);conditionForm.setManaged(true);
            observationForm.setVisible(true);observationForm.setManaged(true);
            observationForm1.setVisible(true);observationForm1.setManaged(true);
            populationForm.setVisible(true);populationForm.setManaged(true);
            speciesForm.setVisible(true);speciesForm.setManaged(true);

        }else{
            habitatForm.setVisible(false);habitatForm.setManaged(false);
            alertForm.setVisible(false);alertForm.setManaged(false);
            conditionForm.setVisible(false);conditionForm.setManaged(false);
            observationForm.setVisible(false);observationForm.setManaged(false);
            observationForm1.setVisible(false);observationForm1.setManaged(false);
            populationForm.setVisible(false);populationForm.setManaged(false);
            speciesForm.setVisible(false);speciesForm.setManaged(false);
        }


    }





    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        h.setVisible(false);h.setManaged(false);
        s.setVisible(false);s.setManaged(false);
        o.setVisible(false);o.setManaged(false);
        a.setVisible(false);a.setManaged(false);
        um.setVisible(false);um.setManaged(false);
        se.setVisible(false);se.setManaged(false);
        of.setVisible(false);of.setManaged(false);



    }



    public void setUserName(String username) {

        userID.setText(username);
        user.setText(STR."Welcome \{username}");
        user.setAlignment(Pos.BASELINE_CENTER);



        // Retrieve and set profile picture
        String query = "SELECT DP FROM useraccounts WHERE username = ?";
        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.getConnection();
            PreparedStatement statement = connectDB.prepareStatement(query);
            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                String image = rs.getString("DP");
                if (image != null) {
                    Image profileImage = new Image(image);
                    profile.setImage(profileImage);
                }
            }
            connectDB.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database error
        }


    }







    @FXML
    void toggleScrollHabitat() {
        if (h.isVisible()) {h.setVisible(false);h.setManaged(false);} else {h.setVisible(true);h.setManaged(true);}
    }

    @FXML
    void toggleScrollSpecies() {
        if (s.isVisible()) {s.setVisible(false);s.setManaged(false);} else {s.setVisible(true);s.setManaged(true);}
    }

    @FXML
    void toggleScrollObservation () {
        if (o.isVisible()) {o.setVisible(false);o.setManaged(false);} else {o.setVisible(true);o.setManaged(true);}
    }

    @FXML
    void toggleScrollAlerts() {
        if (a.isVisible()) {a.setVisible(false);a.setManaged(false);} else {a.setVisible(true);a.setManaged(true);}
    }

    @FXML
    void toggleScrollUserManagement() {
        if (um.isVisible()) {um.setVisible(false);um.setManaged(false);} else {um.setVisible(true);um.setManaged(true);}
    }

    @FXML
    void toggleScrollSetting() {
        if (se.isVisible()) {se.setVisible(false);se.setManaged(false);} else {se.setVisible(true);se.setManaged(true);}
    }

    @FXML
    void toggleScrollOtherFiles() {
        if (of.isVisible()) {of.setVisible(false);of.setManaged(false);} else {of.setVisible(true);of.setManaged(true);}
    }





    @FXML
    void toggleScrollPane() {
        TranslateTransition transition = new TranslateTransition(Duration.millis(400), scrollPane);

        if (scrollPane.isVisible()) {
            transition.setByX(-scrollPane.getWidth());
            transition.setOnFinished(actionEvent -> {
                scrollPane.setVisible(false);
                scrollPane.setManaged(false);
                scrollPane.setTranslateX(0);
            });
        } else {
            scrollPane.setVisible(true);
            scrollPane.setManaged(true);
            scrollPane.setTranslateX(-scrollPane.getWidth());
            transition.setByX(scrollPane.getWidth());
        }

        transition.play();
    }






    public void loadForm(String fxmlPath, Object controller) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent load = fxmlLoader.load();
        ancMain.getChildren().clear();
        ancMain.getChildren().add(load);

        
    }


    public void btnHabitatOnAction() throws IOException {

        userPermissions = UserPermissionsModel.getUserPermissionsByUsername(userID.getText());

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("habitat.fxml"));
        Parent load = fxmlLoader.load();
        HabitatController controller=fxmlLoader.getController();
        controller.setUserPermissions(userPermissions);

        ancMain.getChildren().clear();
        ancMain.getChildren().add(load);

    }

    public void btnHabitatFormOnAction() throws IOException {

        HabitatFormController controller = new HabitatFormController();
        loadForm("habitatForm.fxml", controller);

    }

      public void btnSpeciesOnAction() throws IOException {

          userPermissions = UserPermissionsModel.getUserPermissionsByUsername(userID.getText());

          FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("species.fxml"));
          Parent load = fxmlLoader.load();
          SpeciesController controller=fxmlLoader.getController();
          controller.setUserPermissions(userPermissions);

          ancMain.getChildren().clear();
          ancMain.getChildren().add(load);


    }

    public void btnSpeciesFormOnAction() throws IOException {

        SpeciesFormController controller=new SpeciesFormController();
        loadForm("speciesForm.fxml", controller);


    }

    public void btnEnvironmentalConditionsOnAction() throws IOException {

        userPermissions = UserPermissionsModel.getUserPermissionsByUsername(userID.getText());

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("environmentalConditions.fxml"));
        Parent load = fxmlLoader.load();
        EnvironmentalConditionsController controller=fxmlLoader.getController();
        controller.setUserPermissions(userPermissions);

        ancMain.getChildren().clear();
        ancMain.getChildren().add(load);



    }

    public void btnEnvironmentalConditionsFormOnAction() throws IOException {

        EnvironmentalConditionsFormController controller=new EnvironmentalConditionsFormController();
        loadForm("environmentalConditionsForm.fxml", controller);


    }

    public void btnPopulationOnAction() throws IOException {


        userPermissions = UserPermissionsModel.getUserPermissionsByUsername(userID.getText());

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("population.fxml"));
        Parent load = fxmlLoader.load();
        PopulationController controller=fxmlLoader.getController();
        controller.setUserPermissions(userPermissions);

        ancMain.getChildren().clear();
        ancMain.getChildren().add(load);

    }

    public void btnPopulationFormOnAction() throws IOException {


        VegetationTypesFormController controller=new VegetationTypesFormController();
        loadForm("populationForm.fxml", controller);

    }

    public void btnDashboardOnAction() throws IOException {


        SubDashboardController controller=new SubDashboardController();
        loadForm("subDashboard.fxml", controller);


    }

    public void btnMonitoringAlertsOnAction() throws IOException {

        userPermissions = UserPermissionsModel.getUserPermissionsByUsername(userID.getText());

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("monitoringAlerts.fxml"));
        Parent load = fxmlLoader.load();
        MonitoringAlertsController controller=fxmlLoader.getController();
        controller.setUserPermissions(userPermissions);

        ancMain.getChildren().clear();
        ancMain.getChildren().add(load);

    }

    public void btnMonitoringAlertsFormOnAction() throws IOException {

        MonitoringAlertsFormController controller=new MonitoringAlertsFormController();
        loadForm("monitoringAlertsForm.fxml", controller);

    }

    public void btnUserRegisterOnAction() throws IOException {


        userPermissions = UserPermissionsModel.getUserPermissionsByUsername(userID.getText());

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("userDetails.fxml"));
        Parent load = fxmlLoader.load();
        UserDetailsController controller=fxmlLoader.getController();
        controller.setUserPermissions(userPermissions);

        ancMain.getChildren().clear();
        ancMain.getChildren().add(load);


    }

    public void btnUserRegisterFormOnAction() throws IOException {


        UserRegisterFormController controller=new UserRegisterFormController();
        loadForm("userRegisterFormAdminView.fxml", controller);


    }

    public void userPermissions() throws IOException {

        UserPermissionsController controller=new UserPermissionsController();
        loadForm("userPermissions.fxml", controller);


    }



    public void btnOnActionObservation() throws IOException {

        userPermissions = UserPermissionsModel.getUserPermissionsByUsername(userID.getText());

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("observation.fxml"));
        Parent load = fxmlLoader.load();
        ObservationsController controller=fxmlLoader.getController();
        controller.setUserPermissions(userPermissions);

        ancMain.getChildren().clear();
        ancMain.getChildren().add(load);
    }

    public void btnOnActionObservationForm() throws IOException {
        ObservationsFormController controller=new ObservationsFormController();
        loadForm("observationForm.fxml", controller);
    }


    public void btnOnActionHabitatReport() throws IOException {
        HabitatOverviewReportController controller=new HabitatOverviewReportController();
        loadForm("habitatOverviewReport.fxml", controller);
    }

    public void btnOnActionSpeciesDiversityReport() throws IOException {
        SpeciesDiversityReportController controller=new SpeciesDiversityReportController();
        loadForm("speciesDiversityReport.fxml", controller);
    }



    public void btnOnActionObservationSummaryReport() throws IOException {
        ObservationSummaryReportController controller=new ObservationSummaryReportController();
        loadForm("observationSummaryReport.fxml", controller);
    }

    public void alertStatusForm() throws IOException {
        AlertStatusFormController controller=new AlertStatusFormController();
        loadForm("alertStatusForm.fxml", controller);
    }

    public void alertTypesForm() throws IOException {
        AlertTypesFormController controller=new AlertTypesFormController();
        loadForm("alertTypesForm.fxml", controller);
    }

    public void conservationStatusForm() throws IOException {
        ConservationStatusFormController controller=new ConservationStatusFormController();
        loadForm("conservationStatusForm.fxml", controller);
    }

    public void otherMasterFiles() throws IOException {
        OtherMasterFilesController controller=new OtherMasterFilesController();
        loadForm("otherMasterFiles.fxml", controller);
    }

    public void userProfile() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("userProfile.fxml"));
        Parent load = fxmlLoader.load();
        UserProfileController controller=fxmlLoader.getController();
        controller.setUserProfile(userID.getText());

        ancMain.getChildren().clear();
        ancMain.getChildren().add(load);
    }

    public void companyInfo() throws IOException {
        CompanyDetailsController controller=new CompanyDetailsController();
        loadForm("companyDetails.fxml", controller);
    }

    public void alertReport() throws IOException {
        AlertReportController controller=new AlertReportController();
        loadForm("alertReport.fxml", controller);
    }

    public void habitatAssessmentReport() throws IOException {
        HabitatAssessmentReportFilterController controller=new HabitatAssessmentReportFilterController();
        loadForm("habitatAssessmentReportFilter.fxml", controller);
    }




    public void btnLogoutOnAction(){


        FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("login.fxml"));
        Parent load = null;
        try {
            load = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Stage stage=new Stage();
        stage.setScene(new Scene(load));
        stage.initStyle(UNDECORATED);
        stage.show();



        Stage window = (Stage) btnLogout.getScene().getWindow();
        window.close();

    }

}


