package ads;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;


import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    @FXML Label adId;
    @FXML MenuItem saveEditionMenu;
    @FXML MenuItem saveAsEditionMenu;
    @FXML Button cancelAdBtn;
    @FXML Button editAdBtn;
    @FXML Button saveEditionBtn;
    @FXML Button exportXMLBtn;
    @FXML MenuItem addAdMenu;
    @FXML MenuItem deleteAdMenu;
    @FXML MenuItem editAdMenu;
    @FXML MenuItem exportXMLMenu;
    @FXML MenuItem sendToFTPMenu;
    @FXML ButtonBar clearSearchButtonBar;
    @FXML MenuItem setEditionDateMenu;
    @FXML MenuItem setEditionNumberMenu;


    private Edition ed = null;
    private String filePath = null;
    private String currentDirectory = null;
    private Ad.Category filterCategory = null;
    private String searchText = null;

    @FXML
    public void initialize() {
        ObservableList<String> items = FXCollections.observableArrayList();
        for (Ad.Category c: Ad.Category.values()) {
            String item = c.toString().replace("_"," ");
            items.add(item);
        }

        this.adCategoryList.setItems(items);
    }

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
                createEdition();
            }
        else {
            createEdition();
        }
    }

    private void createEdition() {
        filePath = null;
        ed = new Edition();
        clearEditionData();
        clearAdData();
        // If values of Numer et Date of Edition are wrong reset Edition
        if (!(setEditionNumber() && setEditionDate())) {
            ed = null;
            disableAdsList(true);
            disableAdDetails(true);
            disableSaveBtns(true);
            disableEditAdBtns(true);
        }
        else {
            disableAdsList(false);
            disableSaveBtns(false);
            showEditionDate();
            showEditionNumber();
        }
    }

    @FXML
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
                showEditionNumber();
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

    @FXML
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
                showEditionDate();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Ogłoszenia drobne");
                alert.setHeaderText("Podana wartość nie jest prawidłową datą");
                alert.setContentText("Data powinna mieć format dd/mm/rrrr");
                alert.showAndWait();
            }
        });
        return success[0];
    }


    @FXML
    private void openEditionMenu() throws IOException {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Wkładka do numeru GK","*.wkladka");
        fileChooser.getExtensionFilters().add(filter);
        if(ed != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Ogłoszenia drobne");
            alert.setHeaderText("Niezapisane dane zostaną utracone.");
            alert.setContentText("Jeśli dokument nie został zapisany dane zostaną utracone.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                File selectedFile = fileChooser.showOpenDialog(null);
                if (selectedFile != null) {
                    filePath = selectedFile.getAbsolutePath();
                    currentDirectory = selectedFile.getParent();
                    openEdition();
                }
            }
        } else {
            File selectedFile = fileChooser.showOpenDialog(null);
            if (selectedFile != null) {
                currentDirectory = selectedFile.getParent();
                filePath = selectedFile.getAbsolutePath();
                openEdition();
            }
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


        disableAdsList(false);
        disableSaveBtns(false);
        disableAdDetails(true);
        showEditionDate();
        showEditionNumber();
        refreshAdsList();
    }

    @FXML
    private void saveEditionMenu() {
        if(filePath == null)
            saveAsEditionMenu();
        else
            saveEdition();
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

    @FXML
    private void saveAsEditionMenu() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Wkładka do numeru GK","*.wkladka");
        fileChooser.getExtensionFilters().add(filter);
        File selectedFile = fileChooser.showSaveDialog(null);
        if (selectedFile != null) {
            filePath = selectedFile.getAbsolutePath();
            currentDirectory = selectedFile.getParent();
            saveEdition();
            exportXMLMenu.setDisable(false);
            exportXMLBtn.setDisable(false);
            setEditionDateMenu.setDisable(false);
            setEditionNumberMenu.setDisable(false);
            sendToFTPMenu.setDisable(false);
        }
    }

    // Disable/Enable save buttons
    private void disableSaveBtns(boolean status) {
        saveEditionMenu.setDisable(status);
        saveAsEditionMenu.setDisable(status);
        saveEditionBtn.setDisable(status);
        if(currentDirectory != null) {
            exportXMLMenu.setDisable(status);
            exportXMLBtn.setDisable(status);
            sendToFTPMenu.setDisable(status);
            setEditionDateMenu.setDisable(status);
            setEditionNumberMenu.setDisable(status);
        }
    }
    // Disable/Enable list of Ads
    private void disableAdsList(boolean status) {
        adsList.setDisable(status);
        addAdBtn.setDisable(status);
        addAdMenu.setDisable(status);
        if(adsList.getSelectionModel().getSelectedIndex() != -1){
            disableEditAdBtns(status);
        }
    }
    private void disableEditAdBtns(boolean status) {
        editAdBtn.setDisable(status);
        editAdMenu.setDisable(status);
    }

    // Disable/Enable Ad's details
    private void disableAdDetails(boolean status) {
        saveAdBtn.setDisable(status);
        deleteAdBtn.setDisable(status);
        adCategoryList.setDisable(status);
        adReferenceTxt.setDisable(status);
        adContentTxt.setDisable(status);
        cancelAdBtn.setDisable(status);
        deleteAdMenu.setDisable(status);
    }

    // Show Edition Date and Number
    private void showEditionDate() {
        editionDateLabel.setText(ed.getEditionDateAsString());
    }
    private void showEditionNumber() {
        editionNumberLabel.setText(String.valueOf(ed.getEditionNumber()));
    }
    // Clear all Edition Data
    private void clearEditionData() {
        editionDateLabel.setText("");
        editionNumberLabel.setText("");
        adsList.getItems().clear();
    }
    private void clearAdData() {
        adId.setText("");
        adReferenceTxt.setText("");
        adContentTxt.setText("");
        adCategoryList.getSelectionModel().select(-1);
    }

    @FXML
    private void addAd(){
        Ad ad = new Ad(ed.getNextId());
        ed.addAd(ad);
        clearAdData();
        refreshAdsList();
        adsList.getSelectionModel().selectLast();
        disableAdDetails(false);
        disableAdsList(true);
    }

    @FXML
    private void editAd(){
        disableAdDetails(false);
        disableAdsList(true);
        disableSaveBtns(true);
    }


    private void refreshAdsList(){
        ObservableList<String> items = FXCollections.observableArrayList();
        items.clear();
        for(int i=0; i<ed.ads.size(); i++){
            if(filterCategory == null)
                if(ed.ads.get(i).getReference() != "")
                    items.add(ed.ads.get(i).getCategoryName(true)+": "+ed.ads.get(i).toString()+" - id: "+ ed.ads.get(i).getId());
                else
                    items.add("--- wprowadź i zapisz dane --- "+" - id: "+ ed.ads.get(i).getId());
            else
                if(filterCategory == ed.ads.get(i).getCategory())
                    items.add(ed.ads.get(i).getCategoryName(true)+": "+ed.ads.get(i).toString()+" - id: "+ ed.ads.get(i).getId());
        }

        if(searchText != null){
            for(int i=items.size()-1; i>-1; i--){
                if (items.get(i).toUpperCase().indexOf(searchText.toUpperCase()) == -1 && items.get(i).indexOf("---") == -1) {
                    items.remove(i);
                }
            }
        }
        adsList.setItems(items);
    }


    @FXML
    private void showAd(){
        if(adsList.getSelectionModel().getSelectedIndex()!=-1){
            String selectedAd = adsList.getSelectionModel().getSelectedItem().toString();
            int aId = Integer.parseInt(selectedAd.substring(selectedAd.lastIndexOf(' ') + 1));
            int index = -1;
            for(int i=0; i<ed.ads.size(); i++){
                if (ed.ads.get(i).getId() == aId)
                    index = i;
            }
            adId.setText(Integer.toString(ed.ads.get(index).getId()));
            adReferenceTxt.setText(ed.ads.get(index).getReference());
            adContentTxt.setText(ed.ads.get(index).getContent());
            int categoryIndex = ed.ads.get(index).getCategory().ordinal();
            adCategoryList.getSelectionModel().select(categoryIndex);
            disableEditAdBtns(false);
        }
        else {
            disableEditAdBtns(true);
        }
    }

    @FXML
    private void saveAd(){
        // Conversion selected item index to enum
        int selectedItem = adsList.getSelectionModel().getSelectedIndex();
        String selectedAd = adsList.getSelectionModel().getSelectedItem().toString();
        int aId = Integer.parseInt(selectedAd.substring(selectedAd.lastIndexOf(' ') + 1));
        int index = -1;
        for(int i=0; i<ed.ads.size(); i++){
            if (ed.ads.get(i).getId() == aId)
                index = i;
        }
        System.out.println(aId);
        int categoryIndex = adCategoryList.getSelectionModel().getSelectedIndex();
        if(categoryIndex != -1) {
            Ad.Category category = Ad.Category.values()[categoryIndex];
            ed.ads.get(index).setCategory(category);
            ed.ads.get(index).setContent(adContentTxt.getText());
            ed.ads.get(index).setReference(adReferenceTxt.getText());
            disableAdsList(false);
            disableAdDetails(true);
            disableSaveBtns(false);
            refreshAdsList();
            adsList.getSelectionModel().select(selectedItem);
        }
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ogłoszenia drobne");
            alert.setHeaderText("Proszę uzupełnić dane formularza.");
            alert.showAndWait();
        }
    }

    @FXML
    private void deleteAd() {
        String selectedAd = adsList.getSelectionModel().getSelectedItem().toString();
        int aId = Integer.parseInt(selectedAd.substring(selectedAd.lastIndexOf(' ') + 1));
        int index = -1;
        for(int i=0; i<ed.ads.size(); i++){
            if (ed.ads.get(i).getId() == aId)
                index = i;
        }
        ed.ads.remove(index);
        refreshAdsList();
        adsList.getSelectionModel().select(-1);
        clearAdData();
        disableAdsList(false);
        disableAdDetails(true);
        disableSaveBtns(false);
    }
    @FXML
    private void cancelAd(){
        disableAdsList(false);
        disableAdDetails(true);
        disableSaveBtns(false);
    }

    @FXML
    private void aboutProgram(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Ogłoszenia drobne");
        alert.setHeaderText(null);
        alert.setContentText("Program Ogłoszenia drobne.\nTomasz Pisarek");

        alert.showAndWait();
    }

    @FXML
    private void closeProgram(){
        if(ed != null) saveEdition();
        Platform.exit();
    }

    @FXML
    private void exportXML(){
        if(Utils.generateXML(ed, currentDirectory)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Ogłoszenia drobne");
            alert.setHeaderText("Export pliku do formatu XML");
            alert.setContentText("Plik został prawidłowo zapisany.\n"+currentDirectory+File.separator+ed.getEditionDateAsLocalDate().toString()+".xml");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ogłoszenia drobne");
            alert.setHeaderText("Export pliku do formatu XML");
            alert.setContentText("Nie udało się zapisać pliku.");
            alert.showAndWait();
        }
    }

    @FXML
    private void sendToFTP() {
        if(Utils.generateXML(ed, currentDirectory) && Utils.uploadToFTP(currentDirectory+File.separator+ed.getEditionDateAsLocalDate().toString()+".xml")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Ogłoszenia drobne");
            alert.setHeaderText("Wysłanie pliku na serwer");
            alert.setContentText("Plik został prawidłowo wysłany.");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ogłoszenia drobne");
            alert.setHeaderText("Wysłanie pliku na serwer");
            alert.setContentText("Nie udało się wysłać pliku.");
            alert.showAndWait();
        }

    }

    @FXML
    protected void filterCategory(ActionEvent e) {
        MenuItem item = (MenuItem) e.getSource();
        switch (item.getId()){
            case "filterCategory0":
                filterCategory = Ad.Category.Szukam_Pracy;
                break;
            case "filterCategory1":
                filterCategory = Ad.Category.Oferty_Pracy;
                break;
            case "filterCategory2":
                filterCategory = Ad.Category.Szukam_Mieszkania;
                break;
            case "filterCategory3":
                filterCategory = Ad.Category.Wynajem_Mieszkania;
                break;
            case "filterCategory4":
                filterCategory = Ad.Category.Usługi;
                break;
            case "filterCategory5":
                filterCategory = Ad.Category.Inne;
                break;
        }
        clearSearchButtonBar.setVisible(true);
        refreshAdsList();
        adsList.getSelectionModel().selectLast();
        editAdBtn.setDisable(true);
    }

    @FXML
    private void searchText() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Ogłoszenia drobne");
        dialog.setHeaderText("Szukaj ogłoszenia");
        dialog.setContentText("Wprowadź szukaną frazę:");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            searchText = result.get();
            refreshAdsList();
            clearSearchButtonBar.setVisible(true);
            adsList.getSelectionModel().select(-1);
            clearAdData();
            editAdBtn.setDisable(true);
        }
    }

    @FXML
    private void clearSearch(){
        clearSearchButtonBar.setVisible(false);
        searchText = null;
        filterCategory = null;
        refreshAdsList();
        adsList.getSelectionModel().select(-1);
        clearAdData();
        editAdBtn.setDisable(true);
    }

}
