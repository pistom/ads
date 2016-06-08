package ads;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;
import java.util.Optional;


public class Controller {

    @FXML ListView adsList;
    @FXML Button deleteAdBtn;
    @FXML Button addAdBtn;
    @FXML Button saveAdBtn;
    @FXML ChoiceBox adCategoryList;
    @FXML TextField adReferenceTxt;
    @FXML TextArea adContentTxt;
    @FXML Label editionDateLabel;
    @FXML Label editionNumberLabel;
    @FXML MenuItem saveEditionMenu;
    @FXML MenuItem saveAsEditionMenu;

    Edition ed = null;
    String filePath = null;

    ObservableList<String> data = FXCollections.observableArrayList();
    final FileChooser fileChooser = new FileChooser();

    @FXML
    private void newEditionMenu() {
        // Alert before override Edition
        if(ed != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Ogłoszenia drobne");
            alert.setHeaderText("Niezapisane dane zostaną utracone.");
            alert.setContentText("Jeśli dokument nie został zapisany dane zostaną utracone.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK)
                editionCreate();
        }
        else {
            editionCreate();
        }
    }

    private void editionCreate() {
        ed = new Edition();
        clearEditionData();
        // If values of Numer et Date of Edition are wrong reset Edition
        if(!(setEditionNumber() && setEditionDate()))
            ed = null;
        else {
            disableEdition(false);
            showEditionDateAndNumber();
        }
    }

    private boolean setEditionNumber() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Ogłoszenia drobne");
        dialog.setHeaderText("Tworzenie nowej wkładki");
        dialog.setContentText("Podaj numer wydania:");
        Optional<String> result = dialog.showAndWait();
        boolean success = false;
        if (result.isPresent()) {
            try {
                ed.setEditionNumber(Integer.parseInt(result.get()));
                success = true;
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Ogłoszenia drobne");
                alert.setHeaderText("Podana wartość nie jest prawidłowym numerem");
                alert.setContentText("Podczas tworzenia nowego dokumentu należy podać wartość liczbową (np. 17)");
                alert.showAndWait();
            }
        }
        return success;
    }
    private boolean setEditionDate() {
        Dialog dialog = new Dialog();
        dialog.setTitle("Ogłoszenia drobne");
        dialog.setHeaderText("Tworzenie nowej wkładki");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CLOSE);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        DatePicker editionDate = new DatePicker();
        editionDate.setValue(ed.getEditionDateAsLocalDate());
        editionDate.setPromptText("Data wydania");
        grid.add(new Label("Username:"), 0, 0);
        grid.add(editionDate, 1, 0);
        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return editionDate.getValue();
            }
            return null;
        });

        Optional<LocalDate> result = dialog.showAndWait();

        final boolean[] success = {false};
        result.ifPresent(date -> {
            try {
                ed.setEditionDate(result.get().format(DateTimeFormatter.ISO_DATE));
                success[0] = true;
            } catch (Exception e) {
                System.out.println(result);
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Ogłoszenia drobne");
                alert.setHeaderText("Podana wartość nie jest prawidłową datą");
                alert.setContentText("Podczas tworzenia nowego dokumentu należy podać wartość liczbową (np. 17)");
                alert.showAndWait();
                ed = null;
            }
        });
        return success[0];
    }


    @FXML
    private void openEditionMenu() {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            filePath = selectedFile.getAbsolutePath();
            openEdition();
        }
    }

    private void openEdition(){
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        File file = new File(filePath);
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            System.err.println("Nie ma takiego pliku " + e);
            System.exit(-1);
        }
        try {
            ois = new ObjectInputStream(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }

        DataInputStream sd = new DataInputStream(ois);

        try {
            ed = (Edition) ois.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            sd.close();
        }
        catch (IOException e) {
            System.err.println("Nieudane zamknięcie strumienia zapisu");
            System.exit(-1);
        }

        showEditionDateAndNumber();
    }

    @FXML
    private void saveEditionMenu() {
        if(filePath == null)
            saveAsEditionMenu();
        else
            saveEdition();
    }

    @FXML
    private void saveAsEditionMenu() {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showSaveDialog(null);
        if (selectedFile != null) {
            filePath = selectedFile.getAbsolutePath();
            saveEdition();
        }
    }

    private void saveEdition(){
        System.out.println("Zapisywanie pliku");
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;

        File file = new File(filePath);

        try {
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            System.err.println("Nie ma takiego pliku " + e);
            System.exit(-1);
        }
        try {
            oos = new ObjectOutputStream(fos);
        } catch (IOException e) {
            e.printStackTrace();
        }

        DataOutputStream sd = new DataOutputStream(fos);

        try {
            oos.writeObject(ed);
        }
        catch (IOException e) {
            System.err.println("NIEUDANY ZAPIS: " + e);
            System.exit(-1);
        }

        try {
            sd.close();
        }
        catch (IOException e) {
            System.err.println("Nieudane zamknięcie strumienia zapisu");
            System.exit(-1);
        }
    }

    // Enable buttons and fields
    private void disableEdition(boolean status) {
        adsList.setDisable(status);
        deleteAdBtn.setDisable(status);
        addAdBtn.setDisable(status);
        saveAdBtn.setDisable(status);
        adCategoryList.setDisable(status);
        adReferenceTxt.setDisable(status);
        adContentTxt.setDisable(status);
        saveEditionMenu.setDisable(status);
        saveAsEditionMenu.setDisable(status);
    }

    // Show Edition Date and Number
    private void showEditionDateAndNumber() {
        editionDateLabel.setText(ed.getEditionDateAsString());
        editionNumberLabel.setText(String.valueOf(ed.getEditionNumber()));
    }
    // Clear all Edition Data
    private void clearEditionData() {
        editionDateLabel.setText("");
        editionNumberLabel.setText("");
    }


}
