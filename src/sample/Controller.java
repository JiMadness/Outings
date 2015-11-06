package sample;


import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.Optional;

public class Controller {
    @FXML
    private TableView<Place> placeTable;
    @FXML
    private TableColumn<Place, String> placeName;
    @FXML
    private TableColumn<Place, String> placeArea;
    @FXML
    private Label nameLabel;
    @FXML
    private Label addressLabel;
    @FXML
    private Label phoneLabel;
    @FXML
    private Label minChargeLabel;
    @FXML
    private Label ratingLabel;
    @FXML
    private Label likeLabel;
    @FXML
    private Button newButton;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button chooseButton;

    private static Main mainApp;
    private static DialogController dialogInstance;

    @FXML
    private void initialize() {
        placeTable.setItems(mainApp.getPlaces());
        placeName.setCellValueFactory(c -> c.getValue().getName());
        placeArea.setCellValueFactory(c -> c.getValue().getArea());
        placeTable.getSelectionModel().selectedItemProperty().addListener((obs, oldv, newv) -> {
            if (newv != null) showPlaceDetails(newv);
            else clearPlaceDetails();
        });
    }

    @FXML
    private void newPressed() {
        mainApp.newDialogStage.show();
    }

    @FXML
    private void editPressed() {
        dialogInstance.start(placeTable);
    }

    @FXML
    private void choosePressed() {
        int availableValue=-2;
        ArrayList<Place> availablePlaces = new ArrayList<>();
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Choose a Place");
        dialog.setHeaderText("We'll choose a place for you now.");
        dialog.setContentText("How much money can you afford for today?");
        Optional<String> result = dialog.showAndWait();
        if (!result.isPresent())
            return;
        else if (result.get().equals("")) {
            new Alert(Alert.AlertType.ERROR, "No amount entered, please try again.").show();
            return;
        }
        try {
            availableValue = Integer.valueOf(result.get()) >= 0 ? Integer.valueOf(result.get()) : -1;
        }catch (Exception e){
            new Alert(Alert.AlertType.ERROR,"Invalid amount value").show();
        }
            if (availableValue == -1) {
            new Alert(Alert.AlertType.ERROR, "Invalid amount value.").show();
        }
        for (Place place : mainApp.getPlaces()) {
            if (Integer.valueOf(place.getMinCharge()) <= availableValue) {
                availablePlaces.add(place);
            }
        }
        availablePlaces.sort((p1,p2)->p2.getPlaceRating().compareTo(p1.getPlaceRating()));
        String buffer = "You can go to:\n\n";
        for(Place place:availablePlaces){
            buffer+=place.getName().get()+"\t"+place.getArea().get()+"\n";
        }
        if (buffer.equals("You can go to:\n\n"))
            new Alert(Alert.AlertType.INFORMATION,"Stay home, you're broke.",ButtonType.OK).show();
        else
        new Alert(Alert.AlertType.INFORMATION,buffer,ButtonType.OK).show();
    }

    public static void setMainApp(Main mainApps) {
        mainApp = mainApps;
    }

    private void showPlaceDetails(Place place) {
        if (!place.getName().get().isEmpty()||place.getName().get()==null)
            nameLabel.setText(place.getName().get());
        else nameLabel.setText("Not Specified");
        if (place.getAddress()==null||!place.getAddress().isEmpty())
            addressLabel.setText(place.getAddress());
        else addressLabel.setText("Not specified");
        if (place.getPhoneNumber()==null||!place.getPhoneNumber().isEmpty())
            phoneLabel.setText(place.getPhoneNumber());
        else phoneLabel.setText("Not specified");
        if (place.getMinCharge()==null||!place.getMinCharge().isEmpty())
            minChargeLabel.setText(place.getMinCharge());
        else minChargeLabel.setText("Not specified");
        if (place.getPlaceRating()==null||!place.getPlaceRating().isEmpty())
            ratingLabel.setText(place.getPlaceRating());
        else ratingLabel.setText("Not specified");
        if (place.getDoWeLikeIt()==null||!place.getDoWeLikeIt().isEmpty())
            likeLabel.setText(place.getDoWeLikeIt());
        else likeLabel.setText("Not specified");
    }

    public void clearPlaceDetails() {
        nameLabel.setText("Not Specified");
        addressLabel.setText("Not specified");
        phoneLabel.setText("Not specified");
        minChargeLabel.setText("Not specified");
        ratingLabel.setText("Not specified");
        likeLabel.setText("Not specified");
    }

    @FXML
    private void deletePlace() {
        if (placeTable.getSelectionModel().getSelectedItem() == null)
            new Alert(Alert.AlertType.ERROR, "You need to select a place first to delete it.").show();
        else {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Delete place");
            confirm.setContentText("Are you sure to delete this place?");
            Optional<ButtonType> result = confirm.showAndWait();
            if (result.get() == ButtonType.OK) {
                mainApp.getPlaces().remove(placeTable.getSelectionModel().getSelectedItem());
                mainApp.saveData();
            }
        }
    }

    public static void getDialogInstance(DialogController dialog) {
        dialogInstance = dialog;
    }
}
