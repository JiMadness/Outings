package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class DialogController {
    private static Main mainApp;
    private static Place currentSelection;
    @FXML
    private TextField nameField;
    @FXML
    private TextField areaField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField phoneField;
    @FXML
    private Spinner<Integer> minField;
    @FXML
    private ChoiceBox<String> chooseBox;
    @FXML
    private Slider slider;
    @FXML
    private Button ok;
    @FXML
    private Button cancel;
    @FXML
    private void initialize() {
        Controller.getDialogInstance(this);
        ObservableList<String> chooseBoxOptions = FXCollections.observableArrayList();
        chooseBoxOptions.addAll("1-Star", "2-Stars", "3-Stars", "4-Stars", "5-Stars");
        minField.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, 0, 10));
        chooseBox.setItems(chooseBoxOptions);
    }

    @FXML
    private void okPressed() {
        Place newPlace = new Place();
        if (mainApp.newDialogStage.isShowing()) {
            newPlace.setName(new SimpleStringProperty(nameField.getText()));
            newPlace.setArea(new SimpleStringProperty(areaField.getText()));
            newPlace.setAddress(addressField.getText());
            newPlace.setPhoneNumber(phoneField.getText());
            newPlace.setPlaceRating(chooseBox.getValue());
            newPlace.setDoWeLikeIt(String.valueOf((int) slider.getValue()) + "%");
            newPlace.setMinCharge(String.valueOf(minField.getValueFactory().getValue()));
            mainApp.getPlaces().add(newPlace);
            clearFields();
            mainApp.newDialogStage.close();
        } else {
            mainApp.getPlaces().remove(currentSelection);
            newPlace.setName(new SimpleStringProperty(nameField.getText()));
            newPlace.setArea(new SimpleStringProperty(areaField.getText()));
            newPlace.setAddress(addressField.getText());
            newPlace.setPhoneNumber(phoneField.getText());
            newPlace.setPlaceRating(chooseBox.getValue());
            newPlace.setDoWeLikeIt(String.valueOf((int) slider.getValue()) + "%");
            newPlace.setMinCharge(String.valueOf(minField.getValueFactory().getValue()));
            mainApp.getPlaces().add(newPlace);
            clearFields();
            mainApp.editDialogStage.close();
        }
        mainApp.saveData();
        try {
            mainApp.sendFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void cancelPressed() {
        if (mainApp.newDialogStage.isShowing())
            mainApp.newDialogStage.close();
        else
            mainApp.editDialogStage.close();
    }

    public static void setMainApp(Main mainApps) {
        mainApp = mainApps;
    }

    private void clearFields() {
        nameField.setText("");
        areaField.setText("");
        addressField.setText("");
        phoneField.setText("");
        slider.setValue(0);
        minField.getValueFactory().setValue(0);
        chooseBox.getSelectionModel().clearSelection();
    }

    public void start(TableView tableView) {
        DialogController.currentSelection = (Place) tableView.getSelectionModel().getSelectedItem();
        if (DialogController.currentSelection == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "You need to select a place first to edit it.");
            alert.showAndWait();
        } else {
            mainApp.editDialogStage.show();
            nameField.setText(currentSelection.getName().get());
            areaField.setText(currentSelection.getArea().get());
            addressField.setText(currentSelection.getAddress());
            phoneField.setText(currentSelection.getPhoneNumber());
            chooseBox.setValue(currentSelection.getPlaceRating() != null ? currentSelection.getPlaceRating() : "");
            slider.setValue(Integer.valueOf(currentSelection.getDoWeLikeIt().substring(0, currentSelection.getDoWeLikeIt().length() - 1)));
            minField.getValueFactory().setValue(Integer.valueOf(currentSelection.getMinCharge()));
        }
    }
}
