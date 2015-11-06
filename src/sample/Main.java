package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;


public class Main extends Application {
    private ObservableList<Place> places = FXCollections.observableArrayList();
    public Stage newDialogStage = new Stage();
    public Stage editDialogStage = new Stage();
    public static final File data = new File("resources/databases/data.xml");
    @Override
    public void start(Stage primaryStage) throws Exception {
        final Image icon = new Image("file:resources/images/icon.png");
        if(data.exists())
            this.loadData();
        else
            try {
                //noinspection ResultOfMethodCallIgnored
                data.createNewFile();
            } catch (IOException e) {
                new Alert(Alert.AlertType.ERROR,"Database file can't read,write.").showAndWait();
                primaryStage.close();
            }
        Controller.setMainApp(this);
        DialogController.setMainApp(this);
        BorderPane root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Node people = FXMLLoader.load(getClass().getResource("people.fxml"));
        Parent newDialog = FXMLLoader.load(getClass().getResource("Popup.fxml"));
        Parent editDialog = FXMLLoader.load(getClass().getResource("Popup.fxml"));
        root.setCenter(people);
        primaryStage.setTitle("Outings");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.getIcons().add(icon);
        newDialogStage.setTitle("New Place");
        newDialogStage.setScene(new Scene(newDialog, 300, 300));
        newDialogStage.getIcons().add(icon);
        editDialogStage.setTitle("Edit Place");
        editDialogStage.setScene(new Scene(editDialog, 300, 300));
        editDialogStage.getIcons().add(icon);
        primaryStage.show();
    }

    public ObservableList<Place> getPlaces() {
        return this.places;
    }


    public static void main(String[] args) {
        launch(args);
    }

    public void loadData() {
        try {
            JAXBContext context = JAXBContext.newInstance(PlaceWrapper.class);
            Unmarshaller um = context.createUnmarshaller();
            PlaceWrapper wrapper = (PlaceWrapper) um.unmarshal(Main.data);
            places.clear();
            places.addAll(wrapper.getPlaces());
        } catch (Exception e) {
            e.printStackTrace();
            }
    }

    public void saveData() {
        try {
            JAXBContext context = JAXBContext
                    .newInstance(PlaceWrapper.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            PlaceWrapper wrapper = new PlaceWrapper();
            wrapper.setPlaces(places);
            m.marshal(wrapper, data);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not save data");
            alert.setContentText("Could not save data to file.");
            alert.showAndWait();
        }
    }
}
