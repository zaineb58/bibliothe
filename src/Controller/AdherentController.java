package Controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Adherent;
import services.AdherentService;

import java.sql.Date;
import java.util.List;

public class AdherentController {

    @FXML private TextField txtNumero;
    @FXML private TextField txtNom;
    @FXML private TextField txtPrenom;
    @FXML private DatePicker txtDateNaissance;
    @FXML private CheckBox chkPremium;

    @FXML private TableView<Adherent> table;
    @FXML private TableColumn<Adherent, String> colNumero;
    @FXML private TableColumn<Adherent, String> colNom;
    @FXML private TableColumn<Adherent, String> colPrenom;

    private final AdherentService service = new AdherentService();

    @FXML
    public void initialize() {
        colNumero.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNumero()));
        colNom.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNom()));
        colPrenom.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPrenom()));

        loadTable();
    }

    private void loadTable() {
        List<Adherent> adherents = service.getAll();
        table.getItems().setAll(adherents);
    }

    @FXML
    public void add() {
        if (!validateFields()) return;

        Adherent a = new Adherent(
                txtNumero.getText(),
                txtNom.getText(),
                txtPrenom.getText(),
                Date.valueOf(txtDateNaissance.getValue()),
                chkPremium.isSelected()
        );

        service.AjouterAdherent(a);
        loadTable();
        clearFields();
        showAlert("Adhérent ajouté avec succès !");
    }

    @FXML
    public void update() {
        if (!validateFields()) return;

        Adherent a = new Adherent(
                txtNumero.getText(),
                txtNom.getText(),
                txtPrenom.getText(),
                Date.valueOf(txtDateNaissance.getValue()),
                chkPremium.isSelected()
        );

        service.modifierAdherent(a);
        loadTable();
        clearFields();
        showAlert("Adhérent modifié avec succès !");
    }

    @FXML
    public void delete() {
        String numero = txtNumero.getText();
        if (numero.isEmpty()) {
            showAlert("Veuillez entrer le numéro de l'adhérent à supprimer !");
            return;
        }

        service.supprimerAdherent(numero);
        loadTable();
        clearFields();
        showAlert("Adhérent supprimé avec succès !");
    }

    @FXML
    public void search() {
        String numero = txtNumero.getText();
        if (numero.isEmpty()) {
            showAlert("Veuillez entrer le numéro à rechercher !");
            return;
        }

        List<Adherent> results = service.chercherParNumero(numero);

        if (!results.isEmpty()) {
            Adherent a = results.get(0);
            txtNom.setText(a.getNom());
            txtPrenom.setText(a.getPrenom());
            txtDateNaissance.setValue(((java.sql.Date) a.getDatenaissance()).toLocalDate());
            chkPremium.setSelected(a.isPremium());

            table.getSelectionModel().select(a);
        } else {
            showAlert("Aucun adhérent trouvé !");
        }
    }

    private boolean validateFields() {
        if (txtNumero.getText().isEmpty() || txtNom.getText().isEmpty() || txtPrenom.getText().isEmpty()) {
            showAlert("Veuillez remplir tous les champs !");
            return false;
        }

        if (txtDateNaissance.getValue() == null) {
            showAlert("Veuillez sélectionner une date de naissance !");
            return false;
        }

        return true;
    }

    private void clearFields() {
        txtNumero.clear();
        txtNom.clear();
        txtPrenom.clear();
        txtDateNaissance.setValue(null);
        chkPremium.setSelected(false);
        table.getSelectionModel().clearSelection();
    }

    private void showAlert(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg).showAndWait();
    }
}
