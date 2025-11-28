package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import services.EmprunteService;
import services.LivreServices;
import services.ReservationService;
import model.Adherent;
import model.Emprunt;
import model.Livres;
import model.Reservation;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class ClientInfoView {

    private final VBox root;
    private final Adherent adherent;
    private final EmprunteService empruntService = new EmprunteService();
    private final LivreServices livreService = new LivreServices();
    private final ReservationService reservationService = new ReservationService();

    public ClientInfoView(Adherent adherent) {
        this.adherent = adherent;
        root = new VBox(20);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #6B9071;");
        root.setAlignment(Pos.TOP_CENTER);

        // TITLE
        Label title = new Label("👤 My Information");
        title.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: #f5f5dc; -fx-font-style: italic; -fx-padding: 10 0 20 0;");

        // Container pour centrer le contenu avec largeur maximale
        VBox contentContainer = new VBox(20);
        contentContainer.setMaxWidth(1100);
        contentContainer.setAlignment(Pos.TOP_CENTER);
        
        // Informations personnelles panel
        VBox infoPanel = createInfoPanel();
        
        // Reservations panel
        VBox reservationsPanel = createReservationsPanel();
        
        // Emprunts panel
        VBox empruntsPanel = createEmpruntsPanel();

        contentContainer.getChildren().addAll(infoPanel, reservationsPanel, empruntsPanel);
        
        ScrollPane scrollPane = new ScrollPane(contentContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        root.getChildren().addAll(title, scrollPane);
    }

    private VBox createReservationsPanel() {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(20));
        panel.setMaxWidth(1100);
        panel.setStyle(
            "-fx-background-color: #ffffff; " +
            "-fx-background-radius: 12; " +
            "-fx-border-color: #f57c00; " +
            "-fx-border-radius: 12; " +
            "-fx-border-width: 2; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 8, 0, 0, 2);"
        );

        Label sectionTitle = new Label("📌 My Reservations");
        sectionTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #f57c00;");

        // Get active reservations
        List<Reservation> reservations = reservationService.getReservationsActives(adherent.getNumero());

        if (reservations.isEmpty()) {
            Label noReservations = new Label("No active reservations");
            noReservations.setStyle("-fx-font-size: 16px; -fx-text-fill: #757575; -fx-font-style: italic;");
            panel.getChildren().addAll(sectionTitle, noReservations);
        } else {
            VBox reservationsList = new VBox(10);
            
            for (Reservation reservation : reservations) {
                HBox reservationBox = new HBox(15);
                reservationBox.setPadding(new Insets(15));
                reservationBox.setStyle(
                    "-fx-background-color: white; " +
                    "-fx-border-color: #ffb74d; " +
                    "-fx-border-width: 2; " +
                    "-fx-border-radius: 8; " +
                    "-fx-background-radius: 8;"
                );
                reservationBox.setAlignment(Pos.CENTER_LEFT);

                // Get book info
                Livres livre = livreService.findByISBN(reservation.getIsbnLivre());

                VBox infoBox = new VBox(5);
                Label bookTitle = new Label("📚 " + (livre != null ? livre.getTitre() : "Book not found"));
                bookTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));
                
                Label bookAuthor = new Label("✍️ " + (livre != null ? livre.getAuteur() : ""));
                bookAuthor.setFont(Font.font("Arial", 14));
                
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Label dateReserved = new Label("📅 Reserved on: " + dateFormat.format(reservation.getDateReservation()));
                dateReserved.setFont(Font.font("Arial", 14));
                dateReserved.setStyle("-fx-text-fill: #757575;");

                // Get position in queue
                int position = reservationService.getPositionInQueue(reservation.getIsbnLivre(), adherent.getNumero());
                Label queuePosition = new Label("🔢 Position in queue: #" + position);
                queuePosition.setFont(Font.font("Arial", FontWeight.BOLD, 14));
                queuePosition.setStyle("-fx-text-fill: #f57c00;");

                infoBox.getChildren().addAll(bookTitle, bookAuthor, dateReserved, queuePosition);

                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                Button cancelButton = new Button("❌ Cancel");
                cancelButton.setStyle(
                    "-fx-background-color: #f44336; " +
                    "-fx-text-fill: white; " +
                    "-fx-font-size: 14px; " +
                    "-fx-font-weight: bold; " +
                    "-fx-padding: 10 20; " +
                    "-fx-background-radius: 5; " +
                    "-fx-cursor: hand;"
                );
                cancelButton.setOnAction(e -> annulerReservation(reservation.getId()));

                reservationBox.getChildren().addAll(infoBox, spacer, cancelButton);
                reservationsList.getChildren().add(reservationBox);
            }

            ScrollPane scrollPane = new ScrollPane(reservationsList);
            scrollPane.setFitToWidth(true);
            scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
            scrollPane.setPrefHeight(250);
            scrollPane.setMaxHeight(300);

            panel.getChildren().addAll(sectionTitle, scrollPane);
        }

        return panel;
    }

    private void annulerReservation(int reservationId) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Cancel Reservation");
        confirm.setHeaderText("Are you sure?");
        confirm.setContentText("Do you want to cancel this reservation?");
        
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                String result = reservationService.annulerReservation(reservationId, adherent.getNumero());
                
                if (result.startsWith("SUCCESS")) {
                    Alert success = new Alert(Alert.AlertType.INFORMATION);
                    success.setTitle("Success");
                    success.setHeaderText("✅ Reservation canceled");
                    success.setContentText(result.replace("SUCCESS: ", ""));
                    success.showAndWait();
                    
                    // Refresh the view
                    root.getChildren().clear();
                    Label title = new Label("👤 My Information");
                    title.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: #375534; -fx-padding: 15 0 20 0;");
                    root.getChildren().addAll(title, createInfoPanel(), createReservationsPanel(), createEmpruntsPanel());
                } else {
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setTitle("Error");
                    error.setHeaderText("❌ Could not cancel reservation");
                    error.setContentText(result.replace("ERROR: ", ""));
                    error.showAndWait();
                }
            }
        });
    }

    private VBox createInfoPanel() {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(25));
        panel.setStyle(
            "-fx-background-color: #faf8f3; " +
            "-fx-background-radius: 15; " +
            "-fx-border-color: #66bb6a; " +
            "-fx-border-radius: 15; " +
            "-fx-border-width: 3; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 10, 0, 0, 3);"
        );

        Label sectionTitle = new Label("📋 Personal Information");
        sectionTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: " + "#375534" + ";");

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        GridPane grid = new GridPane();
        grid.setHgap(30);
        grid.setVgap(15);
        grid.setPadding(new Insets(15, 0, 0, 0));

        // Numéro
        addInfoRow(grid, 0, "Number:", adherent.getNumero());
        
        // Nom
        addInfoRow(grid, 1, "Last Name:", adherent.getNom());
        
        // Prénom
        addInfoRow(grid, 2, "First Name:", adherent.getPrenom());
        
        // Date d'ajout
        addInfoRow(grid, 3, "Date Added:", adherent.getDateAjout() != null ? dateFormat.format(adherent.getDateAjout()) : "N/A");
        
        // Type de compte
        addInfoRow(grid, 4, "Account Type:", adherent.isPremium() ? "Premium ⭐" : "Standard");
        
        // Nombre de livres empruntés (non retournés)
        java.time.LocalDate today = java.time.LocalDate.now();
        long activeLoansCount = empruntService.getEmpruntsEnCours().stream()
            .filter(e -> e.getNumeroAdherent().equals(adherent.getNumero()))
            .filter(e -> e.getDateRetour() != null)
            .filter(e -> new java.sql.Date(e.getDateRetour().getTime()).toLocalDate().isAfter(today))
            .count();
        int maxLoans = adherent.isPremium() ? 5 : 3;
        String loansText = activeLoansCount + " / " + maxLoans;
        String loansColor = (activeLoansCount >= maxLoans) ? "#d32f2f" : "#1976d2";
        
        Label loansLabel = new Label("Books Borrowed:");
        loansLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #424242;");
        
        Label loansValue = new Label(loansText);
        loansValue.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + loansColor + ";");
        
        grid.add(loansLabel, 0, 5);
        grid.add(loansValue, 1, 5);
        
        // Signalements
        String signalementText = adherent.getSignalements() + " warning(s)";
        String signalementColor = "";
        if (adherent.getSignalements() == 0) {
            signalementText += " ✓";
            signalementColor = "#4caf50"; // green
        } else if (adherent.getSignalements() == 1) {
            signalementText += " ⚠️";
            signalementColor = "#ff9800"; // orange
        } else if (adherent.getSignalements() == 2) {
            signalementText += " ⚠️⚠️";
            signalementColor = "#ff5722"; // deep orange
        } else {
            signalementText += " 🚫";
            signalementColor = "#f44336"; // red
        }
        
        Label signalementLabel = new Label("Warnings:");
        signalementLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #424242;");
        
        Label signalementValue = new Label(signalementText);
        signalementValue.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + signalementColor + ";");
        
        grid.add(signalementLabel, 0, 6);
        grid.add(signalementValue, 1, 6);
        
        // Message d'avertissement si signalements > 0
        if (adherent.getSignalements() > 0) {
            Label warningMessage = new Label();
            if (adherent.getSignalements() >= 2) {
                warningMessage.setText("⚠️ WARNING: You have " + adherent.getSignalements() + " warning(s). At 3 warnings, your account will be automatically deleted!");
                warningMessage.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #d32f2f; -fx-padding: 15 0 0 0; -fx-background-color: " + "#ffebee" + "; -fx-padding: 10; -fx-border-radius: 5; -fx-background-radius: 5;");
            } else {
                warningMessage.setText("⚠️ You have " + adherent.getSignalements() + " warning for late book return. Please return books on time.");
                warningMessage.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #f57c00; -fx-padding: 15 0 0 0; -fx-background-color: " + "#fff3e0" + "; -fx-padding: 10; -fx-border-radius: 5; -fx-background-radius: 5;");
            }
            warningMessage.setWrapText(true);
            panel.getChildren().add(warningMessage);
        }

        panel.getChildren().addAll(sectionTitle, grid);
        return panel;
    }

    private void addInfoRow(GridPane grid, int row, String labelText, String valueText) {
        Label label = new Label(labelText);
        label.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #424242;");
        
        Label value = new Label(valueText);
        value.setStyle("-fx-font-size: 18px; -fx-text-fill: #616161;");
        
        grid.add(label, 0, row);
        grid.add(value, 1, row);
    }

    @SuppressWarnings("unchecked")
    private VBox createEmpruntsPanel() {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(20));
        panel.setMaxWidth(1100);
        panel.setStyle(
            "-fx-background-color: #ffffff; " +
            "-fx-background-radius: 12; " +
            "-fx-border-color: #66bb6a; " +
            "-fx-border-radius: 12; " +
            "-fx-border-width: 2; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 8, 0, 0, 2);"
        );

        // Récupérer les emprunts actifs de cet adhérent (non encore retournés)
        java.time.LocalDate today = java.time.LocalDate.now();
        List<Emprunt> mesEmprunts = empruntService.getEmpruntsEnCours().stream()
            .filter(e -> e.getNumeroAdherent().equals(adherent.getNumero()))
            .filter(e -> e.getDateRetour() != null)
            .filter(e -> new java.sql.Date(e.getDateRetour().getTime()).toLocalDate().isAfter(today))
            .collect(Collectors.toList());

        Label sectionTitle = new Label("📚 My Loans (" + mesEmprunts.size() + " book(s))");
        sectionTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: " + "#375534" + ";");

        if (mesEmprunts.isEmpty()) {
            Label noEmprunts = new Label("No loans in progress");
            noEmprunts.setStyle("-fx-font-size: 18px; -fx-text-fill: #757575; -fx-padding: 20 0 0 0;");
            panel.getChildren().addAll(sectionTitle, noEmprunts);
        } else {
            // Table des emprunts
            TableView<Emprunt> table = new TableView<>();
            table.setStyle(
                "-fx-background-color: #f5f5f5; " +
                "-fx-background-radius: 8; " +
                "-fx-border-color: #e0e0e0; " +
                "-fx-border-radius: 8; " +
                "-fx-border-width: 1;"
            );
            table.setPrefHeight(300);
            table.setMaxHeight(400);

            // Colonne ISBN
            TableColumn<Emprunt, String> colISBN = new TableColumn<>("📖 ISBN");
            colISBN.setCellValueFactory(e -> new javafx.beans.property.SimpleStringProperty(e.getValue().getIsbnLivre()));
            colISBN.setStyle("-fx-font-size: 14px; -fx-alignment: CENTER;");
            colISBN.setPrefWidth(150);

            // Colonne Titre du livre
            TableColumn<Emprunt, String> colTitre = new TableColumn<>("📚 Book Title");
            colTitre.setCellValueFactory(e -> {
                String isbn = e.getValue().getIsbnLivre();
                Livres livre = livreService.getAll().stream()
                    .filter(l -> l.getISBN().equals(isbn))
                    .findFirst()
                    .orElse(null);
                return new javafx.beans.property.SimpleStringProperty(
                    livre != null ? livre.getTitre() : "Book not found"
                );
            });
            colTitre.setStyle("-fx-font-size: 14px; -fx-alignment: CENTER;");
            colTitre.setPrefWidth(280);

            // Colonne Date d'emprunt
            TableColumn<Emprunt, String> colDateEmprunt = new TableColumn<>("📅 Loan Date");
            colDateEmprunt.setCellValueFactory(e -> {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                return new javafx.beans.property.SimpleStringProperty(
                    e.getValue().getDateEmprunt() != null ? dateFormat.format(e.getValue().getDateEmprunt()) : "N/A"
                );
            });
            colDateEmprunt.setStyle("-fx-font-size: 14px; -fx-alignment: CENTER;");
            colDateEmprunt.setPrefWidth(150);

            // Colonne Date de retour
            TableColumn<Emprunt, String> colDateRetour = new TableColumn<>("🔙 Return Date");
            colDateRetour.setCellValueFactory(e -> {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                return new javafx.beans.property.SimpleStringProperty(
                    e.getValue().getDateRetour() != null ? dateFormat.format(e.getValue().getDateRetour()) : "In progress"
                );
            });
            colDateRetour.setStyle("-fx-font-size: 14px; -fx-alignment: CENTER;");
            colDateRetour.setPrefWidth(150);

            table.getColumns().addAll(colISBN, colTitre, colDateEmprunt, colDateRetour);
            table.getItems().addAll(mesEmprunts);

            panel.getChildren().addAll(sectionTitle, table);
        }

        return panel;
    }

    public VBox getView() {
        return root;
    }
}
