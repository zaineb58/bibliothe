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
import services.ReservationService;

import java.time.LocalDate;
import java.util.List;

public class ClientLivreDetailView {

    private final VBox root;
    @SuppressWarnings("unused")
    private Livres livre;
    private final String clientName;
    private final String clientPhone;
    private final LivreServices livreService = new LivreServices();
    private final EmprunteService empruntService = new EmprunteService();
    private final AdherentService adherentService = new AdherentService();
    private final ReservationService reservationService = new ReservationService();
    @SuppressWarnings("unused")
    private final Runnable onBack;

    public ClientLivreDetailView(Livres livre, String clientName, String clientPhone, Runnable onBack) {
        this.livre = livre;
        this.clientName = clientName;
        this.clientPhone = clientPhone;
        this.onBack = onBack;

        root = new VBox(20);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.TOP_CENTER);
        root.setStyle("-fx-background-color: #6B9071;");

        // Title
        Label title = new Label("📖 Book Details");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setStyle("-fx-text-fill: #f5f5dc; -fx-font-style: italic;");

        // Info container
        VBox infoBox = new VBox(12);
        infoBox.setPadding(new Insets(25));
        infoBox.setMaxWidth(700);
        infoBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.95); -fx-background-radius: 10; -fx-border-color: #66bb6a; -fx-border-radius: 10; -fx-border-width: 2;");

        Label lblISBN = new Label("📋 ISBN: " + livre.getISBN());
        lblISBN.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        Label lblTitre = new Label("📚 Title: " + livre.getTitre());
        lblTitre.setFont(Font.font("Arial", 15));

        Label lblAuteur = new Label("✍️ Author: " + livre.getAuteur());
        lblAuteur.setFont(Font.font("Arial", 15));

        Label lblCategorie = new Label("🏷️ Category: " + livre.getCategorie());
        lblCategorie.setFont(Font.font("Arial", 15));

        infoBox.getChildren().addAll(lblISBN, lblTitre, lblAuteur, lblCategorie);

        // Get client adherent info
        Adherent client = getClientAdherent();
        
        // Action box
        VBox actionBox = new VBox(15);
        actionBox.setPadding(new Insets(20));
        actionBox.setMaxWidth(700);
        actionBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.95); -fx-background-radius: 10; -fx-border-color: #66bb6a; -fx-border-radius: 10; -fx-border-width: 2;");

        if (client == null) {
            Label lblError = new Label("❌ Client not found in database");
            lblError.setFont(Font.font("Arial", FontWeight.BOLD, 16));
            lblError.setStyle("-fx-text-fill: #d32f2f;");
            actionBox.getChildren().add(lblError);
        } else {
            // Check current emprunt
            Emprunt currentEmprunt = getCurrentEmprunt(livre.getISBN());

            if (currentEmprunt != null && !livre.isDisponibilite()) {
                // Book is borrowed - show return date and reservation option
                Label lblStatus = new Label("📕 Book not available");
                lblStatus.setFont(Font.font("Arial", FontWeight.BOLD, 16));
                lblStatus.setStyle("-fx-text-fill: #d32f2f;");

                if (currentEmprunt != null) {
                    LocalDate returnDate = new java.sql.Date(currentEmprunt.getDateRetour().getTime()).toLocalDate();
                    Label lblReturnDate = new Label("📅 Next expected return date: " + returnDate);
                    lblReturnDate.setFont(Font.font("Arial", FontWeight.BOLD, 15));
                    lblReturnDate.setStyle("-fx-text-fill: #1976d2;");

                    Label lblReservationInfo = new Label("You can reserve this book from this date.");
                    lblReservationInfo.setFont(Font.font("Arial", 14));

                    // Check if client can reserve (not at limit)
                    int currentEmprunts = countActiveEmprunts(client.getNumero());
                    int maxEmprunts = client.isPremium() ? 5 : 3;

                    if (currentEmprunts >= maxEmprunts) {
                        Label lblLimitReached = new Label("⚠️ You have reached your loan limit (" + maxEmprunts + " book(s)).\nYou cannot reserve at this time.");
                        lblLimitReached.setFont(Font.font("Arial", FontWeight.BOLD, 14));
                        lblLimitReached.setStyle("-fx-text-fill: #f57c00;");
                        actionBox.getChildren().addAll(lblStatus, lblReturnDate, lblLimitReached);
                    } else {
                        Button btnReserver = new Button("📌 Reserve this Book");
                        btnReserver.setStyle(
                            "-fx-background-color: #f57c00;" +
                            "-fx-text-fill: white;" +
                            "-fx-font-size: 16px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-padding: 16 30;" +
                            "-fx-background-radius: 8;" +
                            "-fx-cursor: hand;"
                        );

                        btnReserver.setOnAction(e -> creerReservation(livre.getISBN(), client.getNumero(), returnDate));

                        actionBox.getChildren().addAll(lblStatus, lblReturnDate, lblReservationInfo, btnReserver);
                    }
                }

            } else if (livre.isDisponibilite()) {
                // Book is available - allow borrowing
                Label lblStatus = new Label("✅ Book available");
                lblStatus.setFont(Font.font("Arial", FontWeight.BOLD, 16));
                lblStatus.setStyle("-fx-text-fill: #388e3c;");

                // Check if client can borrow
                int currentEmprunts = countActiveEmprunts(client.getNumero());
                int maxEmprunts = client.isPremium() ? 5 : 3;
                int daysToReturn = client.isPremium() ? 15 : 10;

                Label lblEmpruntsInfo = new Label("📊 Your loans: " + currentEmprunts + " / " + maxEmprunts);
                lblEmpruntsInfo.setFont(Font.font("Arial", 15));
                lblEmpruntsInfo.setStyle("-fx-text-fill: " + (currentEmprunts >= maxEmprunts ? "#d32f2f" : "#1976d2") + ";");

                if (currentEmprunts >= maxEmprunts) {
                    Label lblLimitReached = new Label("⚠️ You have reached your loan limit.\nPlease return a book before borrowing again.");
                    lblLimitReached.setFont(Font.font("Arial", FontWeight.BOLD, 14));
                    lblLimitReached.setStyle("-fx-text-fill: #d32f2f;");
                    actionBox.getChildren().addAll(lblStatus, lblEmpruntsInfo, lblLimitReached);
                } else {
                    LocalDate dateRetour = LocalDate.now().plusDays(daysToReturn);
                    Label lblReturnInfo = new Label("📅 Return date: " + dateRetour + " (" + daysToReturn + " days)");
                    lblReturnInfo.setFont(Font.font("Arial", 15));

                    Button btnEmprunter = new Button("📚 Borrow this book");
                    btnEmprunter.setStyle(
                        "-fx-background-color: #215e00ff;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 16 30;" +
                        "-fx-background-radius: 8;" +
                        "-fx-cursor: hand;"
                    );

                    btnEmprunter.setOnAction(e -> {
                        // Create emprunt
                        Emprunt newEmprunt = new Emprunt(
                            livre.getISBN(),
                            client.getNumero(),
                            java.sql.Date.valueOf(LocalDate.now()),
                            java.sql.Date.valueOf(dateRetour)
                        );

                        empruntService.addEmprunt(newEmprunt);
                        
                        // Update livre availability
                        livre.setDisponibilite(false);
                        livreService.updateLivre(livre);

                        showAlert("Book borrowed successfully!\n" +
                                  "Return date: " + dateRetour + "\n" +
                                  "Please respect this date.");
                        onBack.run();
                    });

                    actionBox.getChildren().addAll(lblStatus, lblEmpruntsInfo, lblReturnInfo, btnEmprunter);
                }
            }
        }

        root.getChildren().addAll(title, infoBox, actionBox);
    }

    private Adherent getClientAdherent() {
        // Search by name
        List<Adherent> adherents = adherentService.chercherParNom(clientName);
        for (Adherent a : adherents) {
            // Match by phone (stored as numero in this simple implementation)
            if (a.getNumero().equals(clientPhone)) {
                return a;
            }
        }
        return null;
    }

    private Emprunt getCurrentEmprunt(String isbn) {
        List<Emprunt> emprunts = empruntService.getEmpruntsEnCours();
        LocalDate today = LocalDate.now();
        
        for (Emprunt e : emprunts) {
            if (e.getIsbnLivre().equals(isbn) && e.getDateRetour() != null) {
                LocalDate returnDate = new java.sql.Date(e.getDateRetour().getTime()).toLocalDate();
                if (returnDate.isAfter(today)) {
                    return e;
                }
            }
        }
        return null;
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

    private void creerReservation(String isbnLivre, String numeroAdherent, LocalDate expectedReturnDate) {
        // Confirm reservation
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Reservation");
        confirm.setHeaderText("Reserve this book?");
        confirm.setContentText("You will be added to the reservation queue.\n" +
                              "Expected availability: " + expectedReturnDate + "\n\n" +
                              "You will have 3 days to borrow the book once it's assigned to you.");
        
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                String result = reservationService.creerReservation(isbnLivre, numeroAdherent);
                
                if (result.startsWith("SUCCESS")) {
                    Alert success = new Alert(Alert.AlertType.INFORMATION);
                    success.setTitle("Reservation Created");
                    success.setHeaderText("✅ Reservation successful!");
                    success.setContentText(result.replace("SUCCESS: ", ""));
                    success.showAndWait();
                } else {
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setTitle("Reservation Failed");
                    error.setHeaderText("❌ Could not create reservation");
                    error.setContentText(result.replace("ERROR: ", ""));
                    error.showAndWait();
                }
            }
        });
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg);
        alert.showAndWait();
    }

    public VBox getView() {
        return root;
    }
}
