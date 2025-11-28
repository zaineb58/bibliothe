package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.Scene;
import model.Adherent;
import model.Livres;
import model.Emprunt;
import services.LivreServices;
import services.EmprunteService;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

public class MemberDetailView {

    private final Stage stage;
    private final Adherent adherent;
    private final LivreServices livreService = new LivreServices();
    private final EmprunteService empruntService = new EmprunteService();
    private final Runnable onClose;
    private List<Emprunt> memberLoans;

    public MemberDetailView(Adherent adherent, Runnable onClose) {
        this.adherent = adherent;
        this.onClose = onClose;
        this.stage = new Stage();
        stage.setTitle("Member Details - " + adherent.getNom() + " " + adherent.getPrenom());
        stage.setScene(new Scene(createContent(), 900, 750));
    }

    @SuppressWarnings("unchecked")
    private ScrollPane createContent() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #6B9071;");

        // Title
        Label title = new Label("👤 " + adherent.getNom() + " " + adherent.getPrenom());
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #f5f5dc; -fx-font-style: italic;");
        title.setWrapText(true);

        // Member Info Container
        VBox infoContainer = new VBox(15);
        infoContainer.setPadding(new Insets(20));
        infoContainer.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.95);" +
            "-fx-background-radius: 10;" +
            "-fx-border-color: #66bb6a;" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 10;"
        );

        Label infoTitle = new Label("Member Information");
        infoTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2e7d32;");

        GridPane infoGrid = new GridPane();
        infoGrid.setHgap(20);
        infoGrid.setVgap(12);
        infoGrid.setPadding(new Insets(10, 0, 0, 0));

        addInfoRow(infoGrid, 0, "Number:", adherent.getNumero());
        addInfoRow(infoGrid, 1, "Last Name:", adherent.getNom());
        addInfoRow(infoGrid, 2, "First Name:", adherent.getPrenom());
        addInfoRow(infoGrid, 3, "Birth Date:", adherent.getDatenaissance() != null ? adherent.getDatenaissance().toString() : "N/A");
        addInfoRow(infoGrid, 4, "Status:", adherent.isPremium() ? "⭐ Premium" : "📌 Standard");
        addInfoRow(infoGrid, 5, "Registration Date:", adherent.getDateAjout() != null ? adherent.getDateAjout().toString() : "N/A");

        // Warnings display
        int warnings = adherent.getSignalements();
        String warningText = warnings + " / 3";
        String warningColor = warnings == 0 ? "#4caf50" : warnings < 3 ? "#ff9800" : "#f44336";
        Label warningLabel = new Label("Warnings:");
        warningLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #2e7d32;");
        Label warningValue = new Label(warningText);
        warningValue.setStyle("-fx-font-size: 15px; -fx-text-fill: " + warningColor + "; -fx-font-weight: bold;");
        infoGrid.add(warningLabel, 0, 6);
        infoGrid.add(warningValue, 1, 6);

        infoContainer.getChildren().addAll(infoTitle, infoGrid);

        // Loan Statistics Container
        VBox statsContainer = new VBox(15);
        statsContainer.setPadding(new Insets(20));
        statsContainer.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.95);" +
            "-fx-background-radius: 10;" +
            "-fx-border-color: #66bb6a;" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 10;"
        );

        Label statsTitle = new Label("Loan Statistics");
        statsTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2e7d32;");

        // Get current loans
        memberLoans = getCurrentLoans();
        int currentLoansCount = memberLoans.size();
        int maxLoans = adherent.isPremium() ? 5 : 3;
        boolean canBorrow = currentLoansCount < maxLoans;

        GridPane statsGrid = new GridPane();
        statsGrid.setHgap(20);
        statsGrid.setVgap(12);
        statsGrid.setPadding(new Insets(10, 0, 0, 0));

        addInfoRow(statsGrid, 0, "Books Borrowed:", currentLoansCount + " / " + maxLoans);
        
        String limitStatus = canBorrow ? "✅ Can borrow more" : "❌ Limit reached";
        String limitColor = canBorrow ? "#4caf50" : "#f44336";
        Label limitLabel = new Label("Limit Status:");
        limitLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #2e7d32;");
        Label limitValue = new Label(limitStatus);
        limitValue.setStyle("-fx-font-size: 15px; -fx-text-fill: " + limitColor + "; -fx-font-weight: bold;");
        statsGrid.add(limitLabel, 0, 1);
        statsGrid.add(limitValue, 1, 1);

        statsContainer.getChildren().addAll(statsTitle, statsGrid);

        // Borrow button (if can borrow)
        if (canBorrow) {
            Button btnBorrow = new Button("📚 Borrow New Book");
            btnBorrow.setPrefWidth(250);
            btnBorrow.setPrefHeight(50);
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
            
            HBox borrowBox = new HBox(btnBorrow);
            borrowBox.setAlignment(Pos.CENTER);
            borrowBox.setPadding(new Insets(10, 0, 0, 0));
            statsContainer.getChildren().add(borrowBox);
        }

        // Current Loans Container
        VBox loansContainer = new VBox(15);
        loansContainer.setPadding(new Insets(20));
        loansContainer.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.95);" +
            "-fx-background-radius: 10;" +
            "-fx-border-color: #66bb6a;" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 10;"
        );

        Label loansTitle = new Label("Current Borrowed Books");
        loansTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2e7d32;");

        if (memberLoans.isEmpty()) {
            Label noLoans = new Label("No books currently borrowed");
            noLoans.setStyle("-fx-font-size: 14px; -fx-text-fill: #757575; -fx-font-style: italic;");
            loansContainer.getChildren().addAll(loansTitle, noLoans);
        } else {
            TableView<Emprunt> loansTable = new TableView<>();
            loansTable.setPrefHeight(250);
            loansTable.setStyle("-fx-background-color: #ffffff;");

            TableColumn<Emprunt, String> colISBN = new TableColumn<>("ISBN");
            colISBN.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getIsbnLivre()));
            colISBN.setPrefWidth(120);

            TableColumn<Emprunt, String> colTitle = new TableColumn<>("Book Title");
            colTitle.setCellValueFactory(c -> {
                Livres livre = livreService.findByISBN(c.getValue().getIsbnLivre());
                return new javafx.beans.property.SimpleStringProperty(livre != null ? livre.getTitre() : "N/A");
            });
            colTitle.setPrefWidth(250);

            TableColumn<Emprunt, String> colLoanDate = new TableColumn<>("Loan Date");
            colLoanDate.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getDateEmprunt().toString()));
            colLoanDate.setPrefWidth(100);

            TableColumn<Emprunt, String> colReturnDate = new TableColumn<>("Return Date");
            colReturnDate.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getDateRetour().toString()));
            colReturnDate.setPrefWidth(100);

            TableColumn<Emprunt, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(c -> {
            java.sql.Date sqlDate = new java.sql.Date(c.getValue().getDateRetour().getTime());
            LocalDate returnDate = sqlDate.toLocalDate();
            LocalDate today = LocalDate.now();                if (today.isAfter(returnDate)) {
                    long daysLate = ChronoUnit.DAYS.between(returnDate, today);
                    return new javafx.beans.property.SimpleStringProperty("⚠️ " + daysLate + " days late");
                } else {
                    long daysLeft = ChronoUnit.DAYS.between(today, returnDate);
                    return new javafx.beans.property.SimpleStringProperty("⏰ " + daysLeft + " days left");
                }
            });
            colStatus.setPrefWidth(150);

            // Custom cell factory for status column to color text
            colStatus.setCellFactory(column -> new TableCell<Emprunt, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(item);
                        if (item.contains("late")) {
                            setStyle("-fx-text-fill: #f44336; -fx-font-weight: bold;");
                        } else {
                            setStyle("-fx-text-fill: #4caf50;");
                        }
                    }
                }
            });

            loansTable.getColumns().addAll(colISBN, colTitle, colLoanDate, colReturnDate, colStatus);
            loansTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
            loansTable.getItems().addAll(memberLoans);

            loansContainer.getChildren().addAll(loansTitle, loansTable);
        }

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

        root.getChildren().addAll(title, infoContainer, statsContainer, loansContainer, buttonBox);
        
        ScrollPane scrollPane = new ScrollPane(root);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        return scrollPane;
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

    private List<Emprunt> getCurrentLoans() {
        List<Emprunt> allLoans = empruntService.getEmpruntsEnCours();
        LocalDate today = LocalDate.now();
        
        // Only count loans where return date is in the future (not yet returned)
        return allLoans.stream()
            .filter(e -> e.getNumeroAdherent().equals(adherent.getNumero()))
            .filter(e -> e.getDateRetour() != null)
            .filter(e -> e.getDateRetour().toLocalDate().isAfter(today))
            .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    private void showBorrowDialog() {
        Stage dialog = new Stage();
        dialog.setTitle("Borrow Book");
        
        VBox dialogContent = new VBox(20);
        dialogContent.setPadding(new Insets(30));
        dialogContent.setStyle("-fx-background-color: linear-gradient(to bottom, #e8f5e9, #c8e6c9);");
        
        Label title = new Label("Select Book to Borrow");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #1b5e20;");
        
        // Get available books
        List<Livres> availableBooks = livreService.getAll().stream()
            .filter(Livres::isDisponibilite)
            .collect(Collectors.toList());
        
        TableView<Livres> booksTable = new TableView<>();
        booksTable.setPrefHeight(350);
        
        TableColumn<Livres, String> colISBN = new TableColumn<>("ISBN");
        colISBN.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getISBN()));
        colISBN.setPrefWidth(120);
        
        TableColumn<Livres, String> colTitle = new TableColumn<>("Title");
        colTitle.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getTitre()));
        colTitle.setPrefWidth(250);
        
        TableColumn<Livres, String> colAuthor = new TableColumn<>("Author");
        colAuthor.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getAuteur()));
        colAuthor.setPrefWidth(150);
        
        TableColumn<Livres, String> colCategory = new TableColumn<>("Category");
        colCategory.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getCategorie()));
        colCategory.setPrefWidth(120);
        
        booksTable.getColumns().addAll(colISBN, colTitle, colAuthor, colCategory);
        booksTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        booksTable.getItems().addAll(availableBooks);
        
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        
        Button btnConfirm = new Button("✓ Confirm");
        btnConfirm.setStyle("-fx-background-color: #4caf50; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 14 30; -fx-cursor: hand;");
        btnConfirm.setOnAction(e -> {
            Livres selected = booksTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                borrowBook(selected);
                dialog.close();
                stage.close();
                if (onClose != null) onClose.run();
            } else {
                showAlert("Please select a book.");
            }
        });
        
        Button btnCancel = new Button("✖ Cancel");
        btnCancel.setStyle("-fx-background-color: #757575; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 14 30; -fx-cursor: hand;");
        btnCancel.setOnAction(e -> dialog.close());
        
        buttonBox.getChildren().addAll(btnConfirm, btnCancel);
        
        dialogContent.getChildren().addAll(title, booksTable, buttonBox);
        
        dialog.setScene(new Scene(dialogContent, 700, 550));
        dialog.show();
    }

    private void borrowBook(Livres livre) {
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
        
        showSuccess("Book borrowed successfully!\nReturn date: " + returnDate + " (" + days + " days for " + (adherent.isPremium() ? "Premium" : "Standard") + " member)");
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
