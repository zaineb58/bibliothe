package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Livres;
import services.LivreServices;

public class LivresController {

    @FXML private TextField txtISBN;
    @FXML private TextField txtTitre;
    @FXML private TextField txtAuteur;
    @FXML private TextField txtCategorie;
    @FXML private CheckBox chkDisponibilite;

    @FXML private TableView<Livres> table;
    @FXML private TableColumn<Livres, String> colISBN;
    @FXML private TableColumn<Livres, String> colTitre;
    @FXML private TableColumn<Livres, String> colAuteur;
    @FXML private TableColumn<Livres, String> colCategorie;
    @FXML private TableColumn<Livres, Boolean> colDisponibilite;

    private LivreServices service = new LivreServices();

    @FXML
    public void initialize() {
        colISBN.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getISBN()));
        colTitre.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTitre()));
        colAuteur.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getAuteur()));
        colCategorie.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCategorie()));
        colDisponibilite.setCellValueFactory(data -> new javafx.beans.property.SimpleBooleanProperty(data.getValue().isDisponibilite()).asObject());

        loadTable();
    }

    private void loadTable() {
        table.getItems().setAll(service.getAll());
    }

    @FXML
    public void add() {
        Livres l = new Livres(
            txtISBN.getText(),
            txtTitre.getText(),
            txtAuteur.getText(),
            txtCategorie.getText(),
            chkDisponibilite.isSelected()
        );
        service.addLivre(l);
        loadTable();
        clearFields();
    }

    @FXML
    public void update() {
        Livres l = new Livres(
            txtISBN.getText(),
            txtTitre.getText(),
            txtAuteur.getText(),
            txtCategorie.getText(),
            chkDisponibilite.isSelected()
        );
        service.updateLivre(l);
        loadTable();
        clearFields();
    }

    @FXML
    public void delete() {
        service.deleteLivre(txtISBN.getText());
        loadTable();
        clearFields();
    }

    @FXML
    public void search() {
        Livres l = service.findByISBN(txtISBN.getText());
        if (l != null) {
            txtTitre.setText(l.getTitre());
            txtAuteur.setText(l.getAuteur());
            txtCategorie.setText(l.getCategorie());
            chkDisponibilite.setSelected(l.isDisponibilite());
        } else {
            showAlert("Livre non trouvé !");
        }
    }

    private void clearFields() {
        txtISBN.clear();
        txtTitre.clear();
        txtAuteur.clear();
        txtCategorie.clear();
        chkDisponibilite.setSelected(false);
    }

    private void showAlert(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg).show();
    }
}
