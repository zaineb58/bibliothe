package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.Scene;
import model.Livres;
import model.Adherent;
import model.Emprunt;
import services.LivreServices;
import services.AdherentService;
import services.EmprunteService;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class BookDetailView {

    private final Stage stage;
    private final Livres livre;
    private final LivreServices livreService = new LivreServices();
    private final AdherentService adherentService = new AdherentService();
    private final EmprunteService empruntService = new EmprunteService();
    private final Runnable onClose;

    public BookDetailView(Livres livre, Runnable onClose) {
        this.livre = livre;
        this.onClose = onClose;
        this.stage = new Stage();
        stage.setTitle("Book Details - " + livre.getTitre());
        stage.setScene(new Scene(createContent(), 800, 700));
    }

    private VBox createContent() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #6B9071;");

        // Title
        Label title = new Label("📖 " + livre.getTitre());
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #f5f5dc; -fx-font-style: italic;");
        title.setWrapText(true);

        // Book Info Container
        VBox infoContainer = new VBox(15);
        infoContainer.setPadding(new Insets(20));
        infoContainer.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.95);" +
            "-fx-background-radius: 10;" +
            "-fx-border-color: #66bb6a;" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 10;"
        );

        Label infoTitle = new Label("Book Information");
        infoTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2e7d32;");

        GridPane infoGrid = new GridPane();
        infoGrid.setHgap(20);
        infoGrid.setVgap(12);
        infoGrid.setPadding(new Insets(10, 0, 0, 0));

        addInfoRow(infoGrid, 0, "ISBN:", livre.getISBN());
        addInfoRow(infoGrid, 1, "Title:", livre.getTitre());
        addInfoRow(infoGrid, 2, "Author:", livre.getAuteur());
        addInfoRow(infoGrid, 3, "Category:", livre.getCategorie());
        addInfoRow(infoGrid, 4, "Status:", livre.isDisponibilite() ? "✅ Available" : "❌ Unavailable");

        infoContainer.getChildren().addAll(infoTitle, infoGrid);

        // Loan Container
        VBox loanContainer = new VBox(15);
        loanContainer.setPadding(new Insets(20));
        loanContainer.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.95);" +
            "-fx-background-radius: 10;" +
            "-fx-border-color: #66bb6a;" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 10;"
        );

        Label loanTitle = new Label("Loan Status");
        loanTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2e7d32;");

        VBox loanContent = getLoanStatus();

        loanContainer.getChildren().addAll(loanTitle, loanContent);

        // Close Button
        Button btnClose = new Button("✖ Close");
        btnClose.setPrefWidth(150);
        btnClose.setStyle(
            "-fx-background-color: #757575;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 16 30;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;"
        );
        btnClose.setOnAction(e -> {
            stage.close();
            if (onClose != null) onClose.run();
        });

        HBox buttonBox = new HBox(btnClose);
        buttonBox.setAlignment(Pos.CENTER);

        root.getChildren().addAll(title, infoContainer, loanContainer, buttonBox);
        return root;
    }

    private void addInfoRow(GridPane grid, int row, String label, String value) {
        Label lblLabel = new Label(label);
        lblLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #2e7d32;");
        
        Label lblValue = new Label(value);
        lblValue.setStyle("-fx-font-size: 15px; -fx-text-fill: #424242;");
        lblValue.setWrapText(true);
        lblValue.setMaxWidth(600);
        
        grid.add(lblLabel, 0, row);
        grid.add(lblValue, 1, row);
    }

    private VBox getLoanStatus() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(10, 0, 0, 0));

        if (livre.isDisponibilite()) {
            // Book is available
            Label statusLabel = new Label("📗 This book is available for borrowing");
            statusLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #2e7d32; -fx-font-weight: bold;");

            Button btnBorrow = new Button("📚 Borrow Book");
            btnBorrow.setPrefWidth(200);
            btnBorrow.setPrefHeight(45);
            btnBorrow.setStyle(
                "-fx-background-color: #4caf50;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 12 30;" +
                "-fx-background-radius: 8;" +
                "-fx-cursor: hand;"
            );
            btnBorrow.setOnMouseEntered(e -> btnBorrow.setOpacity(0.8));
            btnBorrow.setOnMouseExited(e -> btnBorrow.setOpacity(1.0));
            btnBorrow.setOnAction(e -> showBorrowDialog());

            content.getChildren().addAll(statusLabel, btnBorrow);
        } else {
            // Book is borrowed
            Emprunt currentLoan = getCurrentLoan();
            if (currentLoan != null) {
                VBox loanInfo = new VBox(10);

                Label statusLabel = new Label("📕 This book is currently borrowed");
                statusLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #f44336; -fx-font-weight: bold;");

                List<Adherent> borrowerList = adherentService.chercherParNumero(currentLoan.getNumeroAdherent());
                Adherent borrower = (borrowerList != null && !borrowerList.isEmpty()) ? borrowerList.get(0) : null;
                if (borrower != null) {
                    Label borrowerLabel = new Label("Borrower: " + borrower.getNom() + " " + borrower.getPrenom() + " (#" + borrower.getNumero() + ")");
                    borrowerLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: #424242;");

                    Label typeLabel = new Label("Type: " + (borrower.isPremium() ? "⭐ Premium" : "📌 Standard"));
                    typeLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: #424242;");

                    loanInfo.getChildren().addAll(borrowerLabel, typeLabel);
                }

                Label loanDateLabel = new Label("Loan Date: " + currentLoan.getDateEmprunt());
                loanDateLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: #424242;");

                Label returnDateLabel = new Label("Expected Return: " + currentLoan.getDateRetour());
                returnDateLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: #424242;");

                // Check if overdue
                LocalDate today = LocalDate.now();
                java.sql.Date sqlDate = new java.sql.Date(currentLoan.getDateRetour().getTime());
                LocalDate returnDate = sqlDate.toLocalDate();
                boolean isOverdue = today.isAfter(returnDate);

                if (isOverdue) {
                    long daysOverdue = ChronoUnit.DAYS.between(returnDate, today);
                    Label overdueLabel = new Label("⚠️ OVERDUE by " + daysOverdue + " day(s)!");
                    overdueLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #f44336; -fx-font-weight: bold;");
                    loanInfo.getChildren().add(overdueLabel);
                } else {
                    long daysLeft = ChronoUnit.DAYS.between(today, returnDate);
                    Label daysLeftLabel = new Label("⏰ " + daysLeft + " day(s) remaining");
                    daysLeftLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #4caf50; -fx-font-weight: bold;");
                    loanInfo.getChildren().add(daysLeftLabel);
                }

                // Return button
                Button btnReturn = new Button("↩️ Return Book");
                btnReturn.setPrefWidth(200);
                btnReturn.setPrefHeight(45);
                btnReturn.setStyle(
                    "-fx-background-color: #2196f3;" +
                    "-fx-text-fill: white;" +
                    "-fx-font-size: 16px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-padding: 12 30;" +
                    "-fx-background-radius: 8;" +
                    "-fx-cursor: hand;"
                );
                btnReturn.setOnMouseEntered(e -> btnReturn.setOpacity(0.8));
                btnReturn.setOnMouseExited(e -> btnReturn.setOpacity(1.0));
                btnReturn.setOnAction(e -> returnBook(currentLoan));

                content.getChildren().addAll(statusLabel, loanInfo, loanDateLabel, returnDateLabel, btnReturn);
            }
        }

        return content;
    }

    private Emprunt getCurrentLoan() {
        LocalDate today = LocalDate.now();
        List<Emprunt> allLoans = empruntService.getEmpruntsEnCours();
        
        return allLoans.stream()
            .filter(e -> e.getIsbnLivre().equals(livre.getISBN()))
            .filter(e -> e.getDateRetour() != null)
            .filter(e -> {
                java.sql.Date sqlDate = new java.sql.Date(e.getDateRetour().getTime());
                return sqlDate.toLocalDate().isAfter(today.minusDays(1));
            })
            .findFirst()
            .orElse(null);
    }

    @SuppressWarnings("unchecked")
    private void showBorrowDialog() {
        Stage dialog = new Stage();
        dialog.setTitle("Borrow Book");
        
        VBox dialogContent = new VBox(20);
        dialogContent.setPadding(new Insets(30));
        dialogContent.setStyle("-fx-background-color: linear-gradient(to bottom, #e8f5e9, #c8e6c9);");
        
        Label title = new Label("Select Member to Borrow");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #1b5e20;");
        
        TableView<Adherent> memberTable = new TableView<>();
        memberTable.setPrefHeight(350);
        
        TableColumn<Adherent, String> colNum = new TableColumn<>("Number");
        colNum.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNumero()));
        colNum.setPrefWidth(100);
        
        TableColumn<Adherent, String> colName = new TableColumn<>("Name");
        colName.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNom() + " " + c.getValue().getPrenom()));
        colName.setPrefWidth(200);
        
        TableColumn<Adherent, String> colType = new TableColumn<>("Type");
        colType.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().isPremium() ? "⭐ Premium" : "📌 Standard"));
        colType.setPrefWidth(120);
        
        memberTable.getColumns().addAll(colNum, colName, colType);
        memberTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        memberTable.getItems().addAll(adherentService.getAll());
        
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        
        Button btnConfirm = new Button("✓ Confirm");
        btnConfirm.setStyle("-fx-background-color: #4caf50; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 14 30; -fx-cursor: hand;");
        btnConfirm.setOnAction(e -> {
            Adherent selected = memberTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                borrowBook(selected);
                dialog.close();
                stage.close();
                if (onClose != null) onClose.run();
            } else {
                showAlert("Please select a member.");
            }
        });
        
        Button btnCancel = new Button("✖ Cancel");
        btnCancel.setStyle("-fx-background-color: #757575; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 14 30; -fx-cursor: hand;");
        btnCancel.setOnAction(e -> dialog.close());
        
        buttonBox.getChildren().addAll(btnConfirm, btnCancel);
        
        dialogContent.getChildren().addAll(title, memberTable, buttonBox);
        
        dialog.setScene(new Scene(dialogContent, 500, 550));
        dialog.show();
    }

    private void borrowBook(Adherent adherent) {
        // Check if member has reached borrowing limit
        int maxLoans = adherent.isPremium() ? 5 : 3;
        int currentLoans = countActiveLoans(adherent.getNumero());
        
        if (currentLoans >= maxLoans) {
            showAlert("⚠️ This member has reached their borrowing limit (" + currentLoans + "/" + maxLoans + ").\n" +
                     "Please return a book before borrowing another one.");
            return;
        }
        
        LocalDate today = LocalDate.now();
        int days = adherent.isPremium() ? 15 : 10;
        LocalDate returnDate = today.plusDays(days);
        
        Emprunt emprunt = new Emprunt(
            livre.getISBN(),
            adherent.getNumero(),
            Date.valueOf(today),
            Date.valueOf(returnDate)
        );
        
        empruntService.addEmprunt(emprunt);
        livre.setDisponibilite(false);
        livreService.modifierLivre(livre);
        
        showSuccess("Book borrowed successfully!\nReturn date: " + returnDate + " (" + days + " days)");
    }
    
    private int countActiveLoans(String numeroAdherent) {
        List<Emprunt> allLoans = empruntService.getEmpruntsEnCours();
        LocalDate today = LocalDate.now();
        
        int count = 0;
        for (Emprunt e : allLoans) {
            if (e.getNumeroAdherent().equals(numeroAdherent) && e.getDateRetour() != null) {
                LocalDate returnDate = new java.sql.Date(e.getDateRetour().getTime()).toLocalDate();
                // Only count loans where return date is in the future (not yet returned)
                if (returnDate.isAfter(today)) {
                    count++;
                }
            }
        }
        return count;
    }

    private void returnBook(Emprunt loan) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Return");
        confirm.setHeaderText("Return this book?");
        confirm.setContentText("Are you sure you want to return this book?");
        
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                String result = empruntService.enregistrerRetour(
                    livre.getISBN(),
                    loan.getNumeroAdherent(),
                    Date.valueOf(LocalDate.now())
                );
                
                livre.setDisponibilite(true);
                livreService.modifierLivre(livre);
                
                showSuccess(result);
                stage.close();
                if (onClose != null) onClose.run();
            }
        });
    }

    private void showAlert(String msg) {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setTitle("Warning");
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    private void showSuccess(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Success");
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    public void show() {
        stage.show();
    }
}
