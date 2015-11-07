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
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;

public class Main extends Application {
    private ObservableList<Place> places = FXCollections.observableArrayList();
    public Stage primaryStage;
    public Stage newDialogStage = new Stage();
    public Stage editDialogStage = new Stage();
    public static final File data = new File("resources/databases/data.xml");
    private static final File password = new File("resources/databases/admin.pwd");
    public static String adminPassword;
    private static FTPClient ftp = new FTPClient();

    @Override
    public void start(Stage primaryStage) throws Exception {
        final Image icon = new Image("file:resources/images/icon.png");
        this.primaryStage = primaryStage;
        this.initializeData();
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

    public boolean receiveFile(File localFile, String remoteFileName) throws Exception {
        FileOutputStream stream = new FileOutputStream(localFile, false);
        if (!ftp.isConnected()) {
            new Alert(Alert.AlertType.ERROR, "Can't read database.\nServer busy, or no internet connection.").showAndWait();
            return false;
        }
        try {
            ftp.retrieveFile(remoteFileName, stream);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Can't read database from server.").showAndWait();
            return false;
        }
        return true;
    }

    public boolean sendFile() throws Exception {
        FileInputStream stream = new FileInputStream(data);
        if (!ftp.isConnected()) {
            new Alert(Alert.AlertType.ERROR, "Can't write to database.\nServer busy, or no internet connection.").showAndWait();
            return false;
        }
        try {
            ftp.storeFile("data.xml", stream);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Can't write database to server.").showAndWait();
            return false;
        }
        return true;
    }

    public static void getPassword() throws Exception {
        InputStream stream = new FileInputStream(password);
        char[] buffer = new char[10];
        for (int i = 0; stream.available() != 0; i++) {
            buffer[i] = (char) stream.read();
        }
        adminPassword = new String(buffer);
    }

    public void initializeData() throws Exception {
        try {
            ftp.connect("f12-preview.awardspace.net");
            ftp.login("1987916", "OutingsAdmin101");
            ftp.enterLocalPassiveMode();
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            this.receiveFile(data, "data.xml");
            this.receiveFile(password, "admin.pwd");
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Can't connect to server.\nPlease check your internet connection.").show();
        }
        Main.getPassword();
        password.deleteOnExit();
        if (data.exists())
            this.loadData();
        else {
            new Alert(Alert.AlertType.ERROR, "Can't read database.\nPlease check your internet connection.").showAndWait();
            primaryStage.close();
        }
    }
}
