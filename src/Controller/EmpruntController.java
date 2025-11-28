package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Emprunt;
import services.EmprunteService;

import java.time.ZoneId;
import java.util.Date;

public class EmpruntController {

    @FXML private TextField txtISBNLivre;
    @FXML private TextField txtNumeroAdherent;
    @FXML private DatePicker dpDateEmprunt;
    @FXML private DatePicker dpDateRetour;

    @FXML private TableView<Emprunt> table;
    @FXML private TableColumn<Emprunt, String> colISBNLivre;
    @FXML private TableColumn<Emprunt, String> colNumeroAdherent;
    @FXML private TableColumn<Emprunt, Date> colDateEmprunt;
    @FXML private TableColumn<Emprunt, Date> colDateRetour;

    private EmprunteService service = new EmprunteService();

    @FXML
    public void initialize() {
        colISBNLivre.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getIsbnLivre()));
        colNumeroAdherent.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNumeroAdherent()));
        colDateEmprunt.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getDateEmprunt()));
        colDateRetour.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getDateRetour()));

        loadTable();
    }

    private void loadTable() {
        table.getItems().setAll(service.getEmpruntsEnCours());
    }

    @FXML
    public void add() {
        java.sql.Date dateEmprunt = new java.sql.Date(Date.from(dpDateEmprunt.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime());
        java.sql.Date dateRetour = dpDateRetour.getValue() != null ? 
            new java.sql.Date(Date.from(dpDateRetour.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime()) : null;

        Emprunt e = new Emprunt(
                txtISBNLivre.getText(),
                txtNumeroAdherent.getText(),
                dateEmprunt,
                dateRetour
        );
        service.addEmprunt(e);
        loadTable();
        clearFields();
    }

    @FXML
    public void enregistrerRetour() {
        if (dpDateRetour.getValue() == null) {
            showAlert("Veuillez sélectionner une date de retour!");
            return;
        }
        
        java.sql.Date dateRetour = new java.sql.Date(
            Date.from(dpDateRetour.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime()
        );
        
        service.enregistrerRetour(txtISBNLivre.getText(), txtNumeroAdherent.getText(), dateRetour);
        loadTable();
        clearFields();
        showAlert("Retour enregistré avec succès!");
    }

    private void clearFields() {
        txtISBNLivre.clear();
        txtNumeroAdherent.clear();
        dpDateEmprunt.setValue(null);
        dpDateRetour.setValue(null);
    }

    private void showAlert(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg).show();
    }
}
