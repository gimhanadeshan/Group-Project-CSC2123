package com.example.wildlife_hms;
import io.github.palexdev.materialfx.utils.StringUtils;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import javafx.fxml.FXML;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;


import java.io.File;
import java.net.URL;
import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public class ObservationsFormController implements Initializable ,LookupHabitatData,LookupSpeciesData {


    @FXML
    public FlowPane selectedFilesPane;

    @FXML
    public MFXButton btnAttachments;




    @FXML
    public MFXButton btnClear;

    @FXML
    public MFXButton btnSave;

    @FXML
    public MFXButton btnUpdate;


    @FXML
    public MFXFilterComboBox<HabitatModel> combHabitatID;

    @FXML
    public MFXFilterComboBox<SpeciesModel> combSpeciesID;

    @FXML
    public MFXDatePicker dateObservationDate;

    @FXML
    public TextArea txtNotes;

    @FXML
    public MFXTextField txtObservationID;

    @FXML
    public MFXTextField txtObserverName;

    @FXML
    private MFXTextField txtHabitatName;

    @FXML
    private MFXTextField txtSpeciesName;


    @FXML
    private TextField txtHabitatId;


    @FXML
    private TextField txtSpeciesId;


    int id = 0;


    DatabaseConnection connectNow = new DatabaseConnection();
    Connection connectDB = connectNow.getConnection();

    HabitatController habitatController = new HabitatController();
    SpeciesController speciesController = new SpeciesController();

    List<File> files=new ArrayList<>();



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        StringConverter<HabitatModel> converter = FunctionalStringConverter.to(habitatModel -> (habitatModel == null) ? "" : STR."\{habitatModel.getHaId()} | \{habitatModel.getName()}");
        Function<String, Predicate<HabitatModel>> filterFunction = s -> habitatModel -> StringUtils.containsIgnoreCase(converter.toString(habitatModel), s);
        combHabitatID.setItems(habitatController.getHabitat());
        combHabitatID.setConverter(converter);
        combHabitatID.setFilterFunction(filterFunction);
        combHabitatID.setOnAction(_ -> lookupHabitatName());

        StringConverter<SpeciesModel> converter1 = FunctionalStringConverter.to(habitatModel -> (habitatModel == null) ? "" : STR."\{habitatModel.getSpId()} | \{habitatModel.getCommonName()}");
        Function<String, Predicate<SpeciesModel>> filterFunction1 = s -> speciesModel -> StringUtils.containsIgnoreCase(converter1.toString(speciesModel), s);
        combSpeciesID.setItems(speciesController.getSpecies());
        combSpeciesID.setConverter(converter1);
        combSpeciesID.setFilterFunction(filterFunction1);
        combSpeciesID.setOnAction(_ -> lookupSpeciesName());


        // Set up drag and drop functionality
        selectedFilesPane.setOnDragOver(event -> {
            if (event.getDragboard().hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });





        selectedFilesPane.setOnDragDropped(event -> {
            Dragboard dragboard = event.getDragboard();
            boolean success = false;
            if (dragboard.hasFiles()) {
                success = true;
                List<File> files = dragboard.getFiles();

                // Check if the number of files being dropped exceeds three
                if (selectedFilesPane.getChildren().size() + files.size() > 3) {
                    showAlert(Alert.AlertType.WARNING, "Exceeded Limit", "You can only upload a maximum of three documents.");
                } else {
                    for (File file : files) {
                        displayFileWithIcon(file);
                    }
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });


        // Load the background image
        Image backgroundImage = new Image("drag-drop.jpeg");

        // Create a BackgroundImage
        BackgroundImage background = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT
        );

        // Create a Background with the BackgroundImage
         Background background1 = new Background(background);


        // Set the background to the FlowPane or GridPane
        selectedFilesPane.setBackground(background1);







    }


   public void docDragEntered(){
        selectedFilesPane.setBorder(Border.stroke(Color.GOLD));
    }

    public void docDragExit(){
        selectedFilesPane.setBorder(Border.stroke(Color.GRAY));
    }







    @FXML
    void getData(ObservationsModel observationsModel) {


        id = observationsModel.getObservationID();
        txtObserverName.setText(observationsModel.getObserverName());
        txtNotes.setText(observationsModel.getNotes());
        dateObservationDate.setValue(observationsModel.getObservationDate().toLocalDate());
        txtHabitatId.setText(String.valueOf(observationsModel.getHabitatID()));
        txtSpeciesId.setText(String.valueOf(observationsModel.getSpeciesID()));
        txtObservationID.setText(observationsModel.getObsId());

        // Assuming observationsModel has a method to retrieve selected files as a String
        String attachmentsString = observationsModel.getAttachments();

// Convert the attachmentsString to a list of File objects
        List<File> selectedFiles = stringToFileList(attachmentsString,"\n");

// Display information about selected files in selectedFilesPane
        for (File file : selectedFiles) {
            displayFileWithIcon(file); // You need to implement this method to display file information
        }


        lookupHabitatNameForGetData();
        lookupSpeciesForGetData();


    }

    @FXML
    void clearObservations() {
        txtObservationID.clear();
        txtObserverName.clear();
        txtNotes.clear();
        selectedFilesPane.getChildren().clear(); // Clear all elements in selectedFilesPane
        dateObservationDate.setValue(null); // Clear date value
        combSpeciesID.clearSelection(); // Clear selected species
        combHabitatID.clearSelection(); // Clear selected habitat
        txtHabitatId.clear();
        txtSpeciesId.clear();
        txtHabitatName.clear();
        txtSpeciesName.clear();


    }

     


    void afterUpdateOperation() {

        clearObservations();
        Stage window = (Stage) txtHabitatId.getScene().getWindow();
        window.close();

    }


    private boolean validateFields() {
        return !txtObserverName.getText().isEmpty() && !txtHabitatId.getText().isEmpty() && !txtNotes.getText().isEmpty() && !txtSpeciesId.getText().isEmpty() && !dateObservationDate.getText().isEmpty();
    }


    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait();
    }


    @FXML
    void saveObservations() {

        if (validateFields()) {

            String query = "Insert into  observations(ObserverName,ObservationDate,Notes,HabitatID,SpeciesID,Attachments)values(?,?,?,?,?,?)";

            try {
                PreparedStatement statement = connectDB.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

                statement.setString(1, txtObserverName.getText());
                statement.setDate(2, Date.valueOf(dateObservationDate.getValue()));
                statement.setString(3, txtNotes.getText());
                statement.setInt(4, Integer.parseInt((txtHabitatId.getText())));
                statement.setInt(5, Integer.parseInt((txtSpeciesId.getText())));
                // Process attachments
                StringBuilder attachments = new StringBuilder();
                for (Node node : selectedFilesPane.getChildren()) {
                    if (node instanceof VBox fileBox) {
                        Label filePathLabel = (Label) fileBox.getChildren().get(1);
                        String filePath = filePathLabel.getText();
                        attachments.append(filePath).append("\n"); // Separate file paths using a delimiter
                    }
                }
                // Set the attachments string to the prepared statement
                statement.setString(6, attachments.toString().trim());
                statement.executeUpdate();

                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int observationId = generatedKeys.getInt(1);


                    String updateQuery = "UPDATE observations SET OB_ID = ? WHERE ObservationID = ?";
                    PreparedStatement updateStatement = connectDB.prepareStatement(updateQuery);
                    String formattedObservationId = String.format("OB%05d", observationId);
                    updateStatement.setString(1, formattedObservationId);
                    updateStatement.setInt(2, observationId);
                    updateStatement.executeUpdate();
                }

                clearObservations();
                showAlert(Alert.AlertType.INFORMATION, "Success", "Observation saved successfully!");


            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Error in saving Observation");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Some fields are invalid");
        }
    }

    @FXML
    void updateObservations() {
        if (validateFields()) {
            String query = "UPDATE observations SET ObserverName=?, ObservationDate=?, Notes=?, HabitatID=?, SpeciesID=?, Attachments=? WHERE ObservationID=?";
            try {
                PreparedStatement statement = connectDB.prepareStatement(query);

                statement.setString(1, txtObserverName.getText());
                statement.setDate(2, Date.valueOf(dateObservationDate.getValue()));
                statement.setString(3, txtNotes.getText());
                statement.setInt(4, Integer.parseInt(txtHabitatId.getText()));
                statement.setInt(5, Integer.parseInt(txtSpeciesId.getText()));

                // Process attachments
                StringBuilder attachments = new StringBuilder();
                for (Node node : selectedFilesPane.getChildren()) {
                    if (node instanceof VBox fileBox) {
                        Label filePathLabel = (Label) fileBox.getChildren().get(1);
                        String filePath = filePathLabel.getText();
                        attachments.append(filePath).append("\n"); // Separate file paths using a delimiter
                    }
                }
                // Set the attachments string to the prepared statement
                statement.setString(6, attachments.toString().trim()); // Trim to remove extra newline at the end

                statement.setInt(7, id);
                statement.executeUpdate();

                showAlert(Alert.AlertType.INFORMATION, "Success", "Observation updated successfully!");
                afterUpdateOperation();

            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Invalid HabitatID or SpeciesID");
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", STR."Error in updating Observation: \{e.getMessage()}");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Some fields are invalid");
        }
    }


    @Override
    public void lookupHabitatName() {
        try {

            if(!(combHabitatID.getSelectedItem()==null)) {
                String habitatIdStr = combHabitatID.getSelectedItem().getHaId().trim();

                if (!habitatIdStr.isEmpty()) {

                    String query = "SELECT HabitatName,HabitatID FROM habitats WHERE HA_ID = ?";
                    PreparedStatement statement = connectDB.prepareStatement(query);
                    statement.setString(1, habitatIdStr);


                    ResultSet resultSet = statement.executeQuery();

                    if (resultSet.next()) {
                        String habitatName = resultSet.getString("HabitatName");
                        int id = resultSet.getInt("HabitatID");
                        txtHabitatName.setText(habitatName);
                        txtHabitatId.setText(String.valueOf(id));
                    } else {
                        showAlert(Alert.AlertType.WARNING, "Not Found", STR."Habitat with ID \{habitatIdStr} not found");
                        txtHabitatName.clear();
                    }
                } else {
                    showAlert(Alert.AlertType.WARNING, "Empty Field", "Please enter a HabitatID");
                    txtHabitatName.clear();
                }
            }
        } catch (NumberFormatException | SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error in looking up Habitat Name");
            e.printStackTrace(); // You may want to log the exception for debugging purposes
        }
    }

    @Override
    public void lookupHabitatNameForGetData() {
        try {
            String habitatIdStr = txtHabitatId.getText().trim();

            if (!habitatIdStr.isEmpty()) {
                int habitatId = Integer.parseInt(habitatIdStr);

                String query = "SELECT HabitatName,HA_ID FROM habitats WHERE HabitatID = ?";
                PreparedStatement statement = connectDB.prepareStatement(query);
                statement.setInt(1, habitatId);

                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    String habitatName = resultSet.getString("HabitatName");
                    String haId = resultSet.getString("HA_ID");
                    txtHabitatName.setText(habitatName);
                    combHabitatID.setText(haId);
                } else {
                    showAlert(Alert.AlertType.WARNING, "Not Found", STR."Habitat with ID \{habitatId} not found");
                    txtHabitatName.clear();
                }
            } else {
                showAlert(Alert.AlertType.WARNING, "Empty Field", "Please enter a HabitatID");
                txtHabitatName.clear();
            }
        } catch (NumberFormatException | SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error in looking up Habitat Name");
            e.printStackTrace(); // You may want to log the exception for debugging purposes
        }
    }

    @Override
    public void lookupSpeciesName() {
        try {
            if(!(combSpeciesID.getSelectedItem()==null)) {
                String speciesIdStr = combSpeciesID.getSelectedItem().getSpId().trim();

                if (!speciesIdStr.isEmpty()) {

                    String query = "SELECT CommonName,SpeciesID  FROM species WHERE SP_ID  = ?";
                    PreparedStatement statement = connectDB.prepareStatement(query);
                    statement.setString(1, speciesIdStr);


                    ResultSet resultSet = statement.executeQuery();

                    if (resultSet.next()) {
                        String commonName = resultSet.getString("CommonName");
                        int id = resultSet.getInt("SpeciesID");
                        txtSpeciesName.setText(commonName);
                        txtSpeciesId.setText(String.valueOf(id));
                    } else {
                        showAlert(Alert.AlertType.WARNING, "Not Found", STR."Species with ID \{speciesIdStr} not found");
                        txtSpeciesName.clear();
                    }
                } else {
                    showAlert(Alert.AlertType.WARNING, "Empty Field", "Please enter a SpeciesID");
                    txtSpeciesName.clear();
                }

            }
        } catch (NumberFormatException | SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error in looking up Species CommonName");
            e.printStackTrace(); // You may want to log the exception for debugging purposes
        }
    }

    @Override
    public void lookupSpeciesForGetData() {
        try {
            String speciesIdStr = txtSpeciesId.getText().trim();

            if (!speciesIdStr.isEmpty()) {
                int speciesId = Integer.parseInt(speciesIdStr);

                String query = "SELECT CommonName,SP_ID  FROM species WHERE SpeciesID  = ?";
                PreparedStatement statement = connectDB.prepareStatement(query);
                statement.setInt(1, speciesId);

                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    String commonName = resultSet.getString("CommonName");
                    String spId = resultSet.getString("SP_ID");
                    txtSpeciesName.setText(commonName);
                    combSpeciesID.setText(spId);
                } else {
                    showAlert(Alert.AlertType.WARNING, "Not Found", STR."Species with ID \{speciesId} not found");
                    txtSpeciesName.clear();
                }
            } else {
                showAlert(Alert.AlertType.WARNING, "Empty Field", "Please enter a SpeciesID");
                txtSpeciesName.clear();
            }
        } catch (NumberFormatException | SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error in looking up Species CommonName");
            e.printStackTrace(); // You may want to log the exception for debugging purposes
        }
    }


    public void OpenMultipleFileFileChooser() {
        // Check if the maximum limit of files has been reached
        if (selectedFilesPane.getChildren().size() >= 3) {
            showAlert(Alert.AlertType.WARNING, "Exceeded Limit", "You can only select a maximum of three documents.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Files");

        // Limit file selection to a maximum of three files if more than three files are selected
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(new Stage()); // Show file chooser dialog

        if (selectedFiles != null) {
            // If more than three files are selected, take only the first three files
            if (selectedFiles.size() > 3) {
                selectedFiles = selectedFiles.subList(0, 3);
            }
            for (File selectedFile : selectedFiles) {
                //Create an ImageView to display the icon
                ImageView iconView = new ImageView();
                iconView.setFitWidth(30); // Set width of the icon
                iconView.setFitHeight(30); // Set height of the icon

                // Try to load the icon for the file
                try {
                    // Use the default system icon for the file
                    Image icon = new Image(selectedFile.toURI().toString());
                    iconView.setImage(icon);
                } catch (Exception e) {
                    // If icon loading fails, use a default icon (you can customize this)
                    Image defaultIcon = new Image("icons8-document-50.png");
                    iconView.setImage(defaultIcon);
                }

                // Create a label to display the file path
                Label filePathLabel = new Label(selectedFile.getAbsolutePath());

                ImageView removeIcon = new ImageView(new Image("icons8-close-50.png")); // Replace "remove-icon.png" with your remove icon file
                removeIcon.setFitWidth(16); // Adjust as needed
                removeIcon.setFitHeight(16); // Adjust as needed

                // Create a VBox to hold the icon and file path label
                VBox fileBox = new VBox(iconView, filePathLabel, removeIcon);
                fileBox.setSpacing(5); // Set spacing between icon and label

                removeIcon.setOnMouseClicked(event -> {
                    selectedFilesPane.getChildren().remove(fileBox);
                });

                // Add the VBox to the FlowPane

                selectedFilesPane.getChildren().add(fileBox);
            }
        } else {
            System.out.println("No file selected.");
        }
    }


    private void displayFileWithIcon(File file) {


        // Create an ImageView to display the icon
        ImageView iconView = new ImageView();
        iconView.setFitWidth(30); // Set width of the icon
        iconView.setFitHeight(30); // Set height of the icon

        // Try to load the icon for the file
        try {
            // Use the default system icon for the file
            Image icon = new Image(file.toURI().toString());
            iconView.setImage(icon);

        } catch (Exception e) {
            // If icon loading fails, use a default icon (you can customize this)
            Image defaultIcon = new Image("icons8-document-50.png");
            iconView.setImage(defaultIcon);
        }

        // Create a label to display the file path
        Label filePathLabel = new Label(file.getAbsolutePath());



        ImageView removeIcon = new ImageView(new Image("icons8-close-50.png")); // Replace "remove-icon.png" with your remove icon file
        removeIcon.setFitWidth(16); // Adjust as needed
        removeIcon.setFitHeight(16); // Adjust as needed

       //removeIcon.setOnMouseEntered(event -> removeIcon.setStyle("-fx-background-color: lightgrey;"));
        //removeIcon.setOnMouseExited(event -> removeIcon.setStyle("-fx-background-color: transparent;"));


        // Create a VBox to hold the icon and file path label
        VBox fileBox = new VBox(iconView, filePathLabel,removeIcon);
        fileBox.setSpacing(5); // Set spacing between icon and label
        fileBox.getBorder();

        removeIcon.setOnMouseClicked(event -> {

            fileBox.getChildren().removeAll(iconView, filePathLabel, removeIcon);
        });

        // Add the VBox to the FlowPane

        selectedFilesPane.getChildren().add(fileBox);


    }






    public static List<File> stringToFileList(String filePaths, String delimiter) {
        List<File> fileList = new ArrayList<>();
        String[] paths = filePaths.split(delimiter);

        for (String path : paths) {
            File file = new File(path.trim()); // Trim to remove any leading or trailing spaces
            if (file.exists()) { // Check if the file exists
                fileList.add(file);
            } else {
                System.out.println(STR."File not found: \{path}");
            }
        }

        return fileList;
    }






}