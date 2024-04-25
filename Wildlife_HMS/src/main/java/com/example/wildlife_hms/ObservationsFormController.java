package com.example.wildlife_hms;
import io.github.palexdev.materialfx.utils.StringUtils;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import javafx.collections.FXCollections;
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
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
    private MFXTextField txtHabitatName;

    @FXML
    private MFXTextField txtSpeciesName;


    @FXML
    private TextField txtHabitatId;


    @FXML
    private TextField txtSpeciesId;

    @FXML
    private MFXFilterComboBox<UserModel> combObserverName;

    private static final String IMAGE_PATH_PREFIX = "file:///";

    int id = 0;


    DatabaseConnection connectNow = new DatabaseConnection();
    Connection connectDB = connectNow.getConnection();

    HabitatController habitatController = new HabitatController();
    SpeciesController speciesController = new SpeciesController();



    UserDetailsController userDetailsController=new UserDetailsController();

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


        List<UserModel> researcherUsers = userDetailsController.getUsers().stream()
                .filter(userModel -> userModel != null && "Researcher".equalsIgnoreCase(userModel.getRoll()))
                .collect(Collectors.toList());

        StringConverter<UserModel> converter2 = FunctionalStringConverter.to(userModel -> {
            if (userModel == null) {
                return "";
            } else {
                return STR."\{userModel.getFirstName()} \{userModel.getLastName()}";
            }
        });

        Function<String, Predicate<UserModel>> filterFunction2 = s -> userModel ->
                StringUtils.containsIgnoreCase(converter2.toString(userModel), s);

        combObserverName.setItems(FXCollections.observableArrayList(researcherUsers)); // Set researcher users as items
        combObserverName.setConverter(converter2);
        combObserverName.setFilterFunction(filterFunction2);






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
                        displayImageWithRemoveIcon(file);
                    }
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });


        // Load the background image
        Image backgroundImage = new Image("icons8-drag-and-drop-100.png");

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
        combObserverName.setText(observationsModel.getObserverName());
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
            displayImageWithRemoveIcon(file);
        }


        lookupHabitatNameForGetData();
        lookupSpeciesForGetData();


    }

    @FXML
    void clearObservations() {

        combObserverName.clearSelection();
        txtNotes.clear();
        selectedFilesPane.getChildren().clear(); // Clear all elements in selectedFilesPane
        dateObservationDate.setValue(null); // Clear date value
        combSpeciesID.clearSelection(); // Clear selected species
        combHabitatID.clearSelection(); // Clear selected habitat
        txtHabitatId.clear();
        txtSpeciesId.clear();
        txtHabitatName.clear();
        txtSpeciesName.clear();
        txtObservationID.clear();


    }

     


    void afterUpdateOperation() {

        clearObservations();
        Stage window = (Stage) txtHabitatId.getScene().getWindow();
        window.close();

    }


    private boolean validateFields() {
        return !combObserverName.getText().isEmpty() && !txtHabitatId.getText().isEmpty() && !txtNotes.getText().isEmpty() && !txtSpeciesId.getText().isEmpty() && !dateObservationDate.getText().isEmpty();
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

                statement.setString(1, combObserverName.getText());
                statement.setDate(2, Date.valueOf(dateObservationDate.getValue()));
                statement.setString(3, txtNotes.getText());
                statement.setInt(4, Integer.parseInt((txtHabitatId.getText())));
                statement.setInt(5, Integer.parseInt((txtSpeciesId.getText())));
                // Process attachments
                // Save attachment file paths
                String attachmentPaths = getAttachmentFilePaths();
                statement.setString(6, attachmentPaths);
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

                statement.setString(1, combObserverName.getText());
                statement.setDate(2, Date.valueOf(dateObservationDate.getValue()));
                statement.setString(3, txtNotes.getText());
                statement.setInt(4, Integer.parseInt(txtHabitatId.getText()));
                statement.setInt(5, Integer.parseInt(txtSpeciesId.getText()));

                // Process attachments
                // Save attachment file paths
                String attachmentPaths = getAttachmentFilePaths();
                statement.setString(6, attachmentPaths);

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


    public void OpenMultipleImageFileChooser() {

        // Check if the maximum limit of files has been reached
        if (selectedFilesPane.getChildren().size() >= 3) {
            showAlert(Alert.AlertType.WARNING, "Exceeded Limit", "You can only select a maximum of three images.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Images");

        // Limit file selection to images only
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif", "*.bmp", "*.jpeg")
        );

        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(new Stage()); // Show file chooser dialog

        if (selectedFiles != null) {
            // If more than three files are selected, take only the first three files
            if (selectedFiles.size() > 3) {
                selectedFiles = selectedFiles.subList(0, 3);
            }
            for (File selectedFile : selectedFiles) {
                //Create an ImageView to display the image
                ImageView imageView = new ImageView();
                imageView.setFitWidth(100); // Set width of the image view
                imageView.setFitHeight(100); // Set height of the image view

                // Try to load the image
                try {
                    Image image = new Image(selectedFile.toURI().toString());
                    imageView.setImage(image);
                } catch (Exception e) {
                    e.printStackTrace(); // Handle image loading failure
                    continue; // Skip to the next file
                }

                ImageView removeIcon = new ImageView(new Image("icons8-close-50.png"));
                removeIcon.setFitWidth(16); // Adjust as needed
                removeIcon.setFitHeight(16); // Adjust as needed

                // Create a VBox to hold the image and remove icon
                VBox imageBox = new VBox(imageView, removeIcon);
                imageBox.setSpacing(5); // Set spacing between image and remove icon

                removeIcon.setOnMouseClicked(event -> {
                    selectedFilesPane.getChildren().remove(imageBox);
                });

                // Add the VBox to the FlowPane
                selectedFilesPane.getChildren().add(imageBox);
            }
        } else {
            System.out.println("No image selected.");
        }
    }


    private void displayImageWithRemoveIcon(File file) {
        if (isImageFile(file)) {
            // Create an ImageView to display the image
            ImageView imageView = new ImageView();
            imageView.setFitWidth(100); // Set width of the image
            imageView.setFitHeight(100); // Set height of the image
            imageView.setPreserveRatio(true); // Preserve image aspect ratio

            // Try to load the image
            try {
                Image image = new Image(file.toURI().toString());
                imageView.setImage(image);
            } catch (Exception e) {
                e.printStackTrace(); // Handle image loading error
                return;
            }

            // Create a remove icon
            ImageView removeIcon = new ImageView(new Image("icons8-close-50.png"));
            removeIcon.setFitWidth(16); // Set width of the remove icon
            removeIcon.setFitHeight(16); // Set height of the remove icon

            // Create a VBox to hold the image and remove icon
            VBox imageBox = new VBox(imageView, removeIcon);
            imageBox.setSpacing(5); // Set spacing between image and remove icon

            // Add event handler to remove the image when the remove icon is clicked
            removeIcon.setOnMouseClicked(event -> selectedFilesPane.getChildren().remove(imageBox));

            // Add the VBox to the selectedFilesPane
            selectedFilesPane.getChildren().add(imageBox);
        } else {
            showAlert(Alert.AlertType.WARNING, "Invalid File Type", "Only image files are allowed.");
        }
    }

    private boolean isImageFile(File file) {
        String fileName = file.getName();
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex != -1) {
            String fileExtension = fileName.substring(lastDotIndex + 1).toLowerCase();
            // Check if the file extension corresponds to an image format
            return fileExtension.equals("jpg") || fileExtension.equals("jpeg") ||
                    fileExtension.equals("png") || fileExtension.equals("gif") ||
                    fileExtension.equals("bmp");
        }
        return false;
    }







    public static List<File> stringToFileList(String filePaths, String delimiter) {
        List<File> fileList = new ArrayList<>();
        String[] paths  = filePaths.split(delimiter);

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


    private String getAttachmentFilePaths() {
        StringBuilder attachmentPaths = new StringBuilder();

        for (Node attachment : selectedFilesPane.getChildren()) {
            if (attachment instanceof VBox) {
                VBox fileBox = (VBox) attachment;
                boolean firstImageFound = false; // Flag to track if the first image has been found
                for (Node node : fileBox.getChildren()) {
                    if (node instanceof ImageView && !firstImageFound) {
                        ImageView imageView = (ImageView) node;
                        String imagePath = getImagePathFromImageView(imageView);
                        if (imagePath != null) {
                            attachmentPaths.append(imagePath).append("\n");
                            firstImageFound = true; // Set the flag to true after finding the first image
                        }
                    }
                }
            }
        }

        return attachmentPaths.toString();
    }

    private String getImagePathFromImageView(ImageView imageView) {
        Image image = imageView.getImage();
        if (image != null && image.getUrl() != null) {
            String url = image.getUrl();
            try {
                // Convert the URL string to a URI
                URI uri = new URI(url);
                // Get the path component of the URI
                String path = uri.getPath();
                // Decode the path to handle any percent-encoded characters
                String decodedPath = URLDecoder.decode(path, StandardCharsets.UTF_8);
                return decodedPath;
            } catch (URISyntaxException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }




    public void OpenHabitatForm() throws IOException {
        habitatController.openHabitatForm();
    }

    public void OpenSpeciesForm() throws IOException {
        speciesController.openSpeciesForm();
    }

    



}

