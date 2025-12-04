package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import model.Adherent;
import model.Emprunt;
import model.Livres;
import services.EmprunteService;
import services.LivreServices;

import java.time.LocalDate;
import java.util.List;

public class AdherentDetailView {

    private final VBox root;
    private final Adherent adherent;
    private final EmprunteService empruntService = new EmprunteService();
    private final LivreServices livreService = new LivreServices();
    private final Runnable onBack;

    @SuppressWarnings("unchecked")
    public AdherentDetailView(Adherent adherent, Runnable onBack) {
        this.adherent = adherent;
        this.onBack = onBack;

        root = new VBox(20);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.TOP_CENTER);
        root.setStyle("-fx-background-color: #6B9071;");

        // Title
        Label title = new Label("👤 Member Details");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setStyle("-fx-text-fill: #f5f5dc; -fx-font-style: italic;");

        // Info container
        VBox infoBox = new VBox(12);
        infoBox.setPadding(new Insets(25));
        infoBox.setMaxWidth(700);
        infoBox.setStyle("-fx-background-color: #faf8f3; -fx-background-radius: 10; -fx-border-color: #9ccc65; -fx-border-radius: 10; -fx-border-width: 2;");

        Label lblNumero = new Label("📋 Number: " + adherent.getNumero());
        lblNumero.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        Label lblNom = new Label("👤 Last Name: " + adherent.getNom());
        lblNom.setFont(Font.font("Arial", 15));

        Label lblPrenom = new Label("👤 First Name: " + adherent.getPrenom());
        lblPrenom.setFont(Font.font("Arial", 15));

        Label lblDateNaissance = new Label("🎂 Birth Date: " + 
            (adherent.getDatenaissance() != null ? adherent.getDatenaissance().toString() : "Not defined"));
        lblDateNaissance.setFont(Font.font("Arial", 15));

        Label lblPremium = new Label("⭐ Status: " + (adherent.isPremium() ? "Premium" : "Standard"));
        lblPremium.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        lblPremium.setStyle("-fx-text-fill: " + (adherent.isPremium() ? "#f57c00" : "#f5f5dc") + ";");

        Label lblDateAjout = new Label("📅 Date Added: " + 
            (adherent.getDateAjout() != null ? adherent.getDateAjout().toString() : "Not defined"));
        lblDateAjout.setFont(Font.font("Arial", 15));
        
        // Signalements
        String signalementText = "⚠️ Warnings: " + adherent.getSignalements();
        String signalementColor = "#4caf50"; // green by default
        if (adherent.getSignalements() == 1) {
            signalementText += " ⚠️";
            signalementColor = "#ff9800"; // orange
        } else if (adherent.getSignalements() == 2) {
            signalementText += " ⚠️⚠️ (WARNING: 1 more and account will be deleted!)";
            signalementColor = "#ff5722"; // deep orange
        } else if (adherent.getSignalements() >= 3) {
            signalementText += " 🚫 (Should be deleted!)";
            signalementColor = "#f44336"; // red
        } else {
            signalementText += " ✓";
        }
        
        Label lblSignalements = new Label(signalementText);
        lblSignalements.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        lblSignalements.setStyle("-fx-text-fill: " + signalementColor + ";");

        // Count emprunts and get borrowed books
        List<Emprunt> activeEmprunts = getActiveEmprunts(adherent.getNumero());
        int nombreEmprunts = activeEmprunts.size();
        int maxEmprunts = adherent.isPremium() ? 5 : 3;
        
        Label lblEmprunts = new Label("📚 Number of borrowed books: " + nombreEmprunts + " / " + maxEmprunts);
        lblEmprunts.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        lblEmprunts.setStyle("-fx-text-fill: " + (nombreEmprunts >= maxEmprunts ? "#d32f2f" : "#1976d2") + ";");

        infoBox.getChildren().addAll(lblNumero, lblNom, lblPrenom, lblDateNaissance, lblPremium, lblDateAjout, lblSignalements, lblEmprunts);
        
        // Table of borrowed books
        VBox empruntsSection = new VBox(15);
        empruntsSection.setPadding(new Insets(20));
        empruntsSection.setStyle(
            "-fx-background-color: " + "rgba(255, 255, 255, 0.95)" + "; " +
            "-fx-background-radius: 15; " +
            "-fx-border-color: " + "#66bb6a" + "; " +
            "-fx-border-radius: 15; " +
            "-fx-border-width: 3;"
        );
        
        Label lblEmpruntsTitle = new Label("📚 List of Borrowed Books (" + activeEmprunts.size() + ")");
        lblEmpruntsTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        lblEmpruntsTitle.setStyle("-fx-text-fill: " + "#375534" + ";");
        
        if (!activeEmprunts.isEmpty()) {
            TableView<Emprunt> tableEmprunts = new TableView<>();
            tableEmprunts.setStyle(
                "-fx-background-color: " + "#ffffff" + "; " +
                "-fx-text-fill: " + "#000000" + "; " +
                "-fx-background-radius: 10; " +
                "-fx-border-color: " + "#66bb6a" + "; " +
                "-fx-border-radius: 10; " +
                "-fx-border-width: 2;"
            );
            tableEmprunts.setPrefHeight(250);
            tableEmprunts.setMaxWidth(1300);
            
            // Colonne ISBN
            TableColumn<Emprunt, String> colISBN = new TableColumn<>("📖 ISBN");
            colISBN.setCellValueFactory(e -> new javafx.beans.property.SimpleStringProperty(e.getValue().getIsbnLivre()));
            colISBN.setStyle("-fx-font-size: 14px; -fx-alignment: CENTER;");
            colISBN.setPrefWidth(150);
            
            // Colonne Titre
            TableColumn<Emprunt, String> colTitre = new TableColumn<>("📚 Book Title");
            colTitre.setCellValueFactory(e -> {
                Livres livre = livreService.findByISBN(e.getValue().getIsbnLivre());
                return new javafx.beans.property.SimpleStringProperty(
                    livre != null ? livre.getTitre() : "Book not found"
                );
            });
            colTitre.setStyle("-fx-font-size: 14px; -fx-alignment: CENTER;");
            colTitre.setPrefWidth(300);
            
            // Colonne Auteur
            TableColumn<Emprunt, String> colAuteur = new TableColumn<>("✍️ Author");
            colAuteur.setCellValueFactory(e -> {
                Livres livre = livreService.findByISBN(e.getValue().getIsbnLivre());
                return new javafx.beans.property.SimpleStringProperty(
                    livre != null ? livre.getAuteur() : "N/A"
                );
            });
            colAuteur.setStyle("-fx-font-size: 14px; -fx-alignment: CENTER;");
            colAuteur.setPrefWidth(200);
            
            // Colonne Date d'emprunt
            TableColumn<Emprunt, String> colDateEmprunt = new TableColumn<>("📅 Loan Date");
            colDateEmprunt.setCellValueFactory(e -> {
                java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("dd/MM/yyyy");
                return new javafx.beans.property.SimpleStringProperty(
                    e.getValue().getDateEmprunt() != null ? dateFormat.format(e.getValue().getDateEmprunt()) : "N/A"
                );
            });
            colDateEmprunt.setStyle("-fx-font-size: 14px; -fx-alignment: CENTER;");
            colDateEmprunt.setPrefWidth(150);
            
            // Colonne Date de retour
            TableColumn<Emprunt, String> colDateRetour = new TableColumn<>("🔙 Return Date");
            colDateRetour.setCellValueFactory(e -> {
                java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("dd/MM/yyyy");
                String dateStr = e.getValue().getDateRetour() != null ? 
                    dateFormat.format(e.getValue().getDateRetour()) : "Not defined";
                    
                // Vérifier si en retard
                if (e.getValue().getDateRetour() != null) {
                    java.util.Date aujourdhui = new java.util.Date();
                    if (e.getValue().getDateRetour().before(aujourdhui)) {
                        dateStr += " ⚠️ OVERDUE";
                    }
                }
                return new javafx.beans.property.SimpleStringProperty(dateStr);
            });
            colDateRetour.setStyle("-fx-font-size: 14px; -fx-alignment: CENTER;");
            colDateRetour.setPrefWidth(200);
            
            tableEmprunts.getColumns().addAll(colISBN, colTitre, colAuteur, colDateEmprunt, colDateRetour);
            tableEmprunts.getItems().addAll(activeEmprunts);
            
            empruntsSection.getChildren().addAll(lblEmpruntsTitle, tableEmprunts);
        } else {
            Label lblNoEmprunts = new Label("No books currently borrowed");
            lblNoEmprunts.setFont(Font.font("Arial", 16));
            lblNoEmprunts.setStyle("-fx-text-fill: #757575; -fx-padding: 10 0 0 0;");
            empruntsSection.getChildren().addAll(lblEmpruntsTitle, lblNoEmprunts);
        }

        // Emprunter button
        Button btnEmprunter = new Button("📖 Borrow a Book");
        btnEmprunter.setStyle(
            "-fx-background-color: #f5f5dc;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 16 30;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;"
        );
        btnEmprunter.setOnMouseEntered(e -> btnEmprunter.setStyle(
            "-fx-background-color: #689f38;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 16 30;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;"
        ));
        btnEmprunter.setOnMouseExited(e -> btnEmprunter.setStyle(
            "-fx-background-color: #f5f5dc;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 16 30;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;"
        ));

        btnEmprunter.setOnAction(e -> showLivreSelection());

        root.getChildren().addAll(title, infoBox, empruntsSection, btnEmprunter);
    }

    private List<Emprunt> getActiveEmprunts(String numeroAdherent) {
        List<Emprunt> allEmprunts = empruntService.getEmpruntsEnCours();
        List<Emprunt> activeEmprunts = new java.util.ArrayList<>();
        LocalDate today = LocalDate.now();
        
        for (Emprunt e : allEmprunts) {
            if (e.getNumeroAdherent().equals(numeroAdherent) && e.getDateRetour() != null) {
                LocalDate returnDate = new java.sql.Date(e.getDateRetour().getTime()).toLocalDate();
                // Only count as active if return date is AFTER today (future return = not yet returned)
                if (returnDate.isAfter(today)) {
                    activeEmprunts.add(e);
                }
            }
        }
        return activeEmprunts;
    }

    private void showLivreSelection() {
        Stage dialog = new Stage();
        dialog.setTitle("Select a Book");

        VBox dialogBox = new VBox(15);
        dialogBox.setPadding(new Insets(20));
        dialogBox.setStyle("-fx-background-color: #f1f8e9;");

        Label lblTitle = new Label("📚 Choose an available book:");
        lblTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        // Search bar for ISBN
        HBox searchBox = new HBox(10);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.setPadding(new Insets(5, 0, 5, 0));
        
        TextField searchISBNField = new TextField();
        searchISBNField.setPromptText("🔍 Search by ISBN...");
        searchISBNField.setPrefWidth(300);
        searchISBNField.setStyle(
            "-fx-font-size: 14px; " +
            "-fx-padding: 10; " +
            "-fx-background-color: " + "#fafafa" + "; " +
            "-fx-text-fill: " + "#000000" + "; " +
            "-fx-background-radius: 8; " +
            "-fx-border-color: " + "#66bb6a" + "; " +
            "-fx-border-radius: 8; " +
            "-fx-border-width: 2;"
        );
        
        searchBox.getChildren().add(searchISBNField);

        TableView<Livres> tableLivres = new TableView<>();
        tableLivres.setPrefHeight(350);
        tableLivres.setStyle(
            "-fx-background-color: " + "#ffffff" + "; " +
            "-fx-text-fill: " + "#000000" + "; " +
            "-fx-background-radius: 8; " +
            "-fx-border-color: " + "#66bb6a" + "; " +
            "-fx-border-radius: 8; " +
            "-fx-border-width: 2;"
        );

        TableColumn<Livres, String> colISBN = new TableColumn<>("ISBN");
        colISBN.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getISBN()));
        colISBN.setPrefWidth(120);

        TableColumn<Livres, String> colTitre = new TableColumn<>("Title");
        colTitre.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getTitre()));
        colTitre.setPrefWidth(250);

        TableColumn<Livres, String> colAuteur = new TableColumn<>("Author");
        colAuteur.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getAuteur()));
        colAuteur.setPrefWidth(150);

        tableLivres.getColumns().add(colISBN);
        tableLivres.getColumns().add(colTitre);
        tableLivres.getColumns().add(colAuteur);

        // Get all available books
        List<Livres> availableBooks = new java.util.ArrayList<>();
        List<Livres> allLivres = livreService.getAll();
        for (Livres livre : allLivres) {
            if (livre.isDisponibilite()) {
                availableBooks.add(livre);
            }
        }
        
        // Initial load
        tableLivres.getItems().addAll(availableBooks);
        
        // Search functionality
        searchISBNField.textProperty().addListener((obs, oldVal, newVal) -> {
            String searchText = newVal != null ? newVal.toLowerCase().trim() : "";
            tableLivres.getItems().clear();
            
            for (Livres livre : availableBooks) {
                if (searchText.isEmpty() || livre.getISBN().toLowerCase().contains(searchText)) {
                    tableLivres.getItems().add(livre);
                }
            }
        });

        Button btnConfirm = new Button("Confirm Loan");
        btnConfirm.setStyle(
            "-fx-background-color: #f5f5dc;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 14 25;" +
            "-fx-background-radius: 8;"
        );

        btnConfirm.setOnAction(e -> {
            Livres selectedLivre = tableLivres.getSelectionModel().getSelectedItem();
            if (selectedLivre == null) {
                showAlert("Please select a book");
                return;
            }

            // Check borrowing limit
            List<Emprunt> currentEmprunts = getActiveEmprunts(adherent.getNumero());
            int maxEmprunts = adherent.isPremium() ? 5 : 3;
            
            if (currentEmprunts.size() >= maxEmprunts) {
                showAlert("Limit reached!\n" +
                          "This member has already borrowed " + currentEmprunts.size() + " book(s).\n" +
                          "Maximum allowed: " + maxEmprunts + " book(s) (" + 
                          (adherent.isPremium() ? "Premium" : "Standard") + ")");
                return;
            }

            // Calculate return date based on adherent status
            int daysToReturn = adherent.isPremium() ? 15 : 10;
            LocalDate dateRetourAuto = LocalDate.now().plusDays(daysToReturn);
            
            Emprunt newEmprunt = new Emprunt(
                selectedLivre.getISBN(),
                adherent.getNumero(),
                java.sql.Date.valueOf(LocalDate.now()),
                java.sql.Date.valueOf(dateRetourAuto)
            );

            empruntService.addEmprunt(newEmprunt);
            
            // Update livre availability
            selectedLivre.setDisponibilite(false);
            livreService.updateLivre(selectedLivre);

            showAlert("Book borrowed successfully!\n" +
                      "Return date: " + dateRetourAuto + " (" + daysToReturn + " days)");
            dialog.close();
            onBack.run(); // Refresh view
        });

        dialogBox.getChildren().addAll(lblTitle, searchBox, tableLivres, btnConfirm);

        Scene dialogScene = new Scene(dialogBox, 600, 500);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg);
        alert.showAndWait();
    }

    public VBox getView() {
        return root;
    }
}
