package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import model.Adherent;
import services.AdherentService;

import java.sql.Date;
import java.time.LocalDate;

public class AddAdherentView {

    private final VBox root;
    private final AdherentService adherentService = new AdherentService();
    @SuppressWarnings("unused")
    private final Runnable onSuccess;

    public AddAdherentView(Runnable onSuccess) {
        this.onSuccess = onSuccess;

        root = new VBox(20);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #6B9071;");

        // Title
        Label title = new Label("➕ Add a Member");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setStyle("-fx-text-fill: #f5f5dc; -fx-font-style: italic;");

        // Form container
        VBox formBox = new VBox(15);
        formBox.setPadding(new Insets(30));
        formBox.setMaxWidth(500);
        formBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.95); -fx-background-radius: 10; -fx-border-color: #66bb6a; -fx-border-radius: 10; -fx-border-width: 2;");

        // Numéro field
        Label lblNumero = new Label("📋 Number:");
        lblNumero.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        TextField txtNumero = new TextField();
        txtNumero.setPromptText("Enter number");
        txtNumero.setStyle("-fx-font-size: 14px; -fx-padding: 8;");

        // Nom field
        Label lblNom = new Label("👤 Last Name:");
        lblNom.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        TextField txtNom = new TextField();
        txtNom.setPromptText("Enter last name");
        txtNom.setStyle("-fx-font-size: 14px; -fx-padding: 8;");

        // Prénom field
        Label lblPrenom = new Label("👤 First Name:");
        lblPrenom.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        TextField txtPrenom = new TextField();
        txtPrenom.setPromptText("Enter first name");
        txtPrenom.setStyle("-fx-font-size: 14px; -fx-padding: 8;");

        // Date de naissance field
        Label lblDateNaissance = new Label("🎂 Birth Date:");
        lblDateNaissance.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Select date");
        datePicker.setStyle("-fx-font-size: 14px;");

        // Premium checkbox
        CheckBox chkPremium = new CheckBox("⭐ Premium Member");
        chkPremium.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        chkPremium.setStyle("-fx-text-fill: #f57c00;");

        // Button container
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);

        Button btnAjouter = new Button("✓ Add");
        btnAjouter.setStyle(
            "-fx-background-color: #558b2f;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 16 30;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;"
        );

        Button btnAnnuler = new Button("✗ Cancel");
        btnAnnuler.setStyle(
            "-fx-background-color: #d32f2f;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 16 30;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;"
        );

        btnAjouter.setOnAction(e -> {
            String numero = txtNumero.getText().trim();
            String nom = txtNom.getText().trim();
            String prenom = txtPrenom.getText().trim();
            LocalDate dateNaissance = datePicker.getValue();
            boolean premium = chkPremium.isSelected();

            if (numero.isEmpty() || nom.isEmpty() || prenom.isEmpty() || dateNaissance == null) {
                showAlert("Error", "Please fill in all fields");
                return;
            }

            Adherent adherent = new Adherent(
                numero,
                nom,
                prenom,
                Date.valueOf(dateNaissance),
                premium
            );
            
            adherentService.AjouterAdherent(adherent);
            showAlert("Success", "Member added successfully!\nDate added: " + new java.util.Date());
            onSuccess.run();
        });

        btnAnnuler.setOnAction(e -> onSuccess.run());

        buttonBox.getChildren().addAll(btnAjouter, btnAnnuler);

        formBox.getChildren().addAll(
            lblNumero, txtNumero,
            lblNom, txtNom,
            lblPrenom, txtPrenom,
            lblDateNaissance, datePicker,
            chkPremium,
            buttonBox
        );

        root.getChildren().addAll(title, formBox);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public VBox getView() {
        return root;
    }
}
