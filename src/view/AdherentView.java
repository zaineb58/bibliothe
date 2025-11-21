package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import model.Adherent;
import services.AdherentService;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class AdherentView {

    private final VBox root;
    private final TextField txtNumero = new TextField();
    private final TextField txtNom = new TextField();
    private final TextField txtPrenom = new TextField();
    private final DatePicker datePicker = new DatePicker();
    private final CheckBox chkPremium = new CheckBox("Premium");
    private final TableView<Adherent> table = new TableView<>();
    private final AdherentService service = new AdherentService();

    @SuppressWarnings("unchecked")
    public AdherentView() {
        root = new VBox(10);
        root.setPadding(new Insets(12));

        // Top form
        HBox form = new HBox(8);
        txtNumero.setPromptText("Numéro");
        txtNom.setPromptText("Nom");
        txtPrenom.setPromptText("Prénom");
        datePicker.setPromptText("Date Naissance");
        Button btnAdd = new Button("Ajouter");
        Button btnUpdate = new Button("Modifier");
        Button btnDelete = new Button("Supprimer");
        Button btnSearch = new Button("Chercher");

        form.getChildren().addAll(txtNumero, txtNom, txtPrenom, datePicker, chkPremium, btnAdd, btnUpdate, btnDelete, btnSearch);

        // Table columns
        TableColumn<Adherent, String> colNumero = new TableColumn<>("Numéro");
        colNumero.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNumero()));
        TableColumn<Adherent, String> colNom = new TableColumn<>("Nom");
        colNom.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNom()));
        TableColumn<Adherent, String> colPrenom = new TableColumn<>("Prénom");
        colPrenom.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getPrenom()));

        table.getColumns().addAll(colNumero, colNom, colPrenom);
        table.setPrefHeight(400);

        // Button actions
        btnAdd.setOnAction(e -> addAdherent());
        btnUpdate.setOnAction(e -> updateAdherent());
        btnDelete.setOnAction(e -> deleteAdherent());
        btnSearch.setOnAction(e -> searchAdherent());

        root.getChildren().addAll(form, table);

        loadTable();
    }

    public Pane getRoot() { return root; }
    
    public VBox getView() { return root; }

    private void loadTable() {
        List<Adherent> list = service.getAll();
        ObservableList<Adherent> obs = FXCollections.observableArrayList(list);
        table.setItems(obs);
    }

    private boolean validateFields() {
        if (txtNumero.getText().isEmpty() || txtNom.getText().isEmpty() || txtPrenom.getText().isEmpty() || datePicker.getValue() == null) {
            showAlert("Remplissez tous les champs");
            return false;
        }
        return true;
    }

    private void addAdherent() {
        if (!validateFields()) return;
        Adherent a = new Adherent(
            txtNumero.getText(),
            txtNom.getText(),
            txtPrenom.getText(),
            Date.valueOf(datePicker.getValue()),
            chkPremium.isSelected()
        );
        service.AjouterAdherent(a);
        loadTable();
        clear();
    }

    private void updateAdherent() {
        if (!validateFields()) return;
        Adherent a = new Adherent(
            txtNumero.getText(),
            txtNom.getText(),
            txtPrenom.getText(),
            Date.valueOf(datePicker.getValue()),
            chkPremium.isSelected()
        );
        service.modifierAdherent(a);
        loadTable();
        clear();
    }

    private void deleteAdherent() {
        String num = txtNumero.getText();
        if (num.isEmpty()) { showAlert("Entrez le numéro"); return; }
        service.supprimerAdherent(num);
        loadTable();
        clear();
    }

    private void searchAdherent() {
        String num = txtNumero.getText();
        if (num.isEmpty()) { showAlert("Entrez le numéro"); return; }
        var results = service.chercherParNumero(num);
        if (!results.isEmpty()) {
            Adherent a = results.get(0);
            txtNom.setText(a.getNom());
            txtPrenom.setText(a.getPrenom());
            if (a.getDatenaissance() != null) {
                LocalDate localDate = new java.sql.Date(a.getDatenaissance().getTime()).toLocalDate();
                datePicker.setValue(localDate);
            }
            chkPremium.setSelected(a.isPremium());
            table.getSelectionModel().select(a);
        } else {
            showAlert("Aucun adhérent trouvé");
        }
    }

    private void clear() {
        txtNumero.clear(); txtNom.clear(); txtPrenom.clear();
        datePicker.setValue(null); chkPremium.setSelected(false);
        table.getSelectionModel().clearSelection();
    }

    private void showAlert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg);
        a.showAndWait();
    }
    
}
