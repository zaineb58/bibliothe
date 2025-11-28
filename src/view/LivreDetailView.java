package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import model.Adherent;
import model.Emprunt;
import model.Livres;
import services.AdherentService;
import services.EmprunteService;
import services.LivreServices;

import java.time.LocalDate;
import java.util.List;

public class LivreDetailView {

    private final VBox root;
    @SuppressWarnings("unused")
    private final Livres livre;
    private final LivreServices livreService = new LivreServices();
    private final EmprunteService empruntService = new EmprunteService();
    private final AdherentService adherentService = new AdherentService();
    @SuppressWarnings("unused")
    private final Runnable onBack;

    public LivreDetailView(Livres livre, Runnable onBack) {
        this.livre = livre;
        this.onBack = onBack;

        root = new VBox(20);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.TOP_CENTER);
        root.setStyle("-fx-background-color: #6B9071;");

        // Title
        Label title = new Label("Book Details");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setStyle("-fx-text-fill: #f5f5dc; -fx-font-style: italic;");

        // Info container
        VBox infoBox = new VBox(10);
        infoBox.setPadding(new Insets(20));
        infoBox.setMaxWidth(600);
        infoBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.95); -fx-background-radius: 10; -fx-border-color: #66bb6a; -fx-border-width: 2; -fx-border-radius: 10;");

        Label lblISBN = new Label("ISBN: " + livre.getISBN());
        lblISBN.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        Label lblTitre = new Label("Title: " + livre.getTitre());
        lblTitre.setFont(Font.font("Arial", 14));

        Label lblAuteur = new Label("Author: " + livre.getAuteur());
        lblAuteur.setFont(Font.font("Arial", 14));

        Label lblCategorie = new Label("🏷️ Category: " + livre.getCategorie());
        lblCategorie.setFont(Font.font("Arial", 16));

        infoBox.getChildren().addAll(lblISBN, lblTitre, lblAuteur, lblCategorie);

        VBox empruntBox = new VBox(15);
        empruntBox.setPadding(new Insets(20));
        empruntBox.setMaxWidth(600);
        empruntBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.95); -fx-background-radius: 10; -fx-border-color: #66bb6a; -fx-border-width: 2; -fx-border-radius: 10;");

        Emprunt currentEmprunt = getCurrentEmprunt(livre.getISBN());

        if (currentEmprunt != null && !livre.isDisponibilite()) {
            // Book is borrowed - show info only, no button
            Label lblStatus = new Label("📕 Book not available");
            lblStatus.setFont(Font.font("Arial", FontWeight.BOLD, 16));
            lblStatus.setStyle("-fx-text-fill: #d32f2f;");

            Label lblEmprunteur = new Label("Borrowed by: " + getAdherentName(currentEmprunt.getNumeroAdherent()));
            lblEmprunteur.setFont(Font.font("Arial", 14));

            Label lblDateEmprunt = new Label("Loan date: " + currentEmprunt.getDateEmprunt());
            lblDateEmprunt.setFont(Font.font("Arial", 14));

            Label lblDateRetour = new Label("Expected return date: " + 
                (currentEmprunt.getDateRetour() != null ? currentEmprunt.getDateRetour().toString() : "Not defined"));
            lblDateRetour.setFont(Font.font("Arial", 14));

            Button btnRemettreDispo = new Button("Make Available");
            btnRemettreDispo.setStyle(
                "-fx-background-color: #FF9800;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 8 20;" +
                "-fx-cursor: hand;"
            );

            btnRemettreDispo.setOnAction(e -> {
                // Mark emprunt as returned by setting actual return date to today
                if (currentEmprunt != null) {
                    empruntService.enregistrerRetour(
                        currentEmprunt.getIsbnLivre(), 
                        currentEmprunt.getNumeroAdherent(), 
                        new java.sql.Date(System.currentTimeMillis())
                    );
                }
                
                livre.setDisponibilite(true);
                livreService.updateLivre(livre);
                
                showAlert("The book has been made available!");
                onBack.run();
            });

            empruntBox.getChildren().addAll(lblStatus, lblEmprunteur, lblDateEmprunt, lblDateRetour, btnRemettreDispo);

        } else if (livre.isDisponibilite()) {
            // Book is available - show adherent table and borrow button
            Label lblStatus = new Label("📗 Book available");
            lblStatus.setFont(Font.font("Arial", FontWeight.BOLD, 16));
            lblStatus.setStyle("-fx-text-fill: #388e3c;");

            Label lblInstruction = new Label("Select a member to borrow this book:");
            lblInstruction.setFont(Font.font("Arial", 14));

            // Table of adherents
            TableView<Adherent> tableAdherents = new TableView<>();
            tableAdherents.setPrefHeight(250);

            TableColumn<Adherent, String> colNumero = new TableColumn<>("Number");
            colNumero.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNumero()));
            colNumero.setPrefWidth(100);

            TableColumn<Adherent, String> colNom = new TableColumn<>("Last Name");
            colNom.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNom()));
            colNom.setPrefWidth(150);

            TableColumn<Adherent, String> colPrenom = new TableColumn<>("First Name");
            colPrenom.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getPrenom()));
            colPrenom.setPrefWidth(150);

            tableAdherents.getColumns().add(colNumero);
            tableAdherents.getColumns().add(colNom);
            tableAdherents.getColumns().add(colPrenom);
            tableAdherents.getItems().addAll(adherentService.getAll());

                // Date de retour info
                Label lblDateRetourInfo = new Label("📅 Loan duration: Premium (15 days) | Standard (10 days)");
                lblDateRetourInfo.setFont(Font.font("Arial", FontWeight.BOLD, 14));
                lblDateRetourInfo.setStyle("-fx-text-fill: #1976d2;");            // Emprunter button
            Button btnEmprunter = new Button("Borrow");
            btnEmprunter.setStyle(
                "-fx-background-color: #4CAF50;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 14 30;" +
                "-fx-cursor: hand;"
            );

            btnEmprunter.setOnAction(e -> {
                Adherent selectedAdherent = tableAdherents.getSelectionModel().getSelectedItem();
                if (selectedAdherent == null) {
                    showAlert("Please select a member");
                    return;
                }

                // Check borrowing limit
                int currentEmprunts = countActiveEmprunts(selectedAdherent.getNumero());
                int maxEmprunts = selectedAdherent.isPremium() ? 5 : 3;
                
                if (currentEmprunts >= maxEmprunts) {
                    showAlert("Limit reached! This member has already borrowed " + currentEmprunts + " book(s).\n" +
                              "Maximum allowed: " + maxEmprunts + " book(s) (" + 
                              (selectedAdherent.isPremium() ? "Premium" : "Standard") + ")");
                    return;
                }

                // Calculate return date based on adherent status
                int daysToReturn = selectedAdherent.isPremium() ? 15 : 10;
                LocalDate dateRetourCalculated = LocalDate.now().plusDays(daysToReturn);

                // Create emprunt with automatic return date
                Emprunt newEmprunt = new Emprunt(
                    livre.getISBN(),
                    selectedAdherent.getNumero(),
                    java.sql.Date.valueOf(LocalDate.now()),
                    java.sql.Date.valueOf(dateRetourCalculated)
                );

                empruntService.addEmprunt(newEmprunt);
                
                // Update livre availability
                livre.setDisponibilite(false);
                livreService.updateLivre(livre);

                showAlert("Book borrowed successfully!\nReturn date: " + dateRetourCalculated + 
                          " (" + daysToReturn + " days)");
                onBack.run(); // Go back to list
            });

            empruntBox.getChildren().addAll(lblStatus, lblInstruction, tableAdherents, lblDateRetourInfo, btnEmprunter);
        
        } else {
            // Book is not available (disponibilite = false)
            Label lblStatus = new Label("📕 Book not available");
            lblStatus.setFont(Font.font("Arial", FontWeight.BOLD, 16));
            lblStatus.setStyle("-fx-text-fill: #d32f2f;");

            Label lblRaison = new Label("This book is currently marked as unavailable.");
            lblRaison.setFont(Font.font("Arial", 14));

            empruntBox.getChildren().addAll(lblStatus, lblRaison);
        }

        root.getChildren().addAll(title, infoBox, empruntBox);
    }



    private int countActiveEmprunts(String numeroAdherent) {
        List<Emprunt> allEmprunts = empruntService.getEmpruntsEnCours();
        LocalDate today = LocalDate.now();
        int count = 0;
        
        for (Emprunt e : allEmprunts) {
            if (e.getNumeroAdherent().equals(numeroAdherent) && e.getDateRetour() != null) {
                LocalDate returnDate = new java.sql.Date(e.getDateRetour().getTime()).toLocalDate();
                if (returnDate.isAfter(today)) {
                    count++;
                }
            }
        }
        return count;
    }

    private Emprunt getCurrentEmprunt(String isbn) {
        List<Emprunt> emprunts = empruntService.getEmpruntsEnCours();
        LocalDate today = LocalDate.now();
        
        for (Emprunt e : emprunts) {
            if (e.getIsbnLivre().equals(isbn) && e.getDateRetour() != null) {
                // Check if return date has not passed yet
                LocalDate returnDate = new java.sql.Date(e.getDateRetour().getTime()).toLocalDate();
                if (returnDate.isAfter(today)) {
                    // Book is still borrowed (return date not passed)
                    return e;
                }
            }
        }
        return null;
    }

    private String getAdherentName(String numero) {
        List<Adherent> adherents = adherentService.chercherParNumero(numero);
        if (!adherents.isEmpty()) {
            Adherent a = adherents.get(0);
            return a.getNom() + " " + a.getPrenom();
        }
        return "Inconnu";
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg);
        alert.showAndWait();
    }

    public VBox getView() {
        return root;
    }
}
