package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import services.LivreServices;
import services.EmprunteService;
import model.Livres;
import model.Emprunt;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDate;

public class LivresView {

    private final LivreServices service = new LivreServices();
    private final EmprunteService empruntService = new EmprunteService();
    private final VBox root;
    private final TableView<Livres> table = new TableView<>();
    private final TextField searchByISBN = new TextField();
    private final TextField searchByTitle = new TextField();
    private final ComboBox<String> filterCombo = new ComboBox<>();
    private List<Livres> allBooks;
    private final TextField txtISBN = new TextField();
    private final TextField txtTitle = new TextField();
    private final TextField txtAuthor = new TextField();
    private final TextField txtCategory = new TextField();
    private final CheckBox chkAvailable = new CheckBox("Available");

    @SuppressWarnings("unchecked")
    public LivresView() {
        root = new VBox(20);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #6B9071;");

        // Title
        Label title = new Label("📚 Books Management");
        title.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #f5f5dc; -fx-font-style: italic;");

        // Search and Filter Container
        HBox searchBox = new HBox(15);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.setPadding(new Insets(20));
        searchBox.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.95);" +
            "-fx-background-radius: 10;" +
            "-fx-border-color: #66bb6a;" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 10;"
        );

        Label searchLabel = new Label("🔍 Search:");
        searchLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2e7d32;");

        searchByISBN.setPromptText("By ISBN");
        searchByISBN.setPrefWidth(180);
        searchByISBN.setStyle(
            "-fx-background-color: #f1f8e9;" +
            "-fx-text-fill: #000000;" +
            "-fx-font-size: 14px;" +
            "-fx-padding: 10;" +
            "-fx-background-radius: 5;" +
            "-fx-border-color: #81c784;" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 5;"
        );

        searchByTitle.setPromptText("By Title");
        searchByTitle.setPrefWidth(180);
        searchByTitle.setStyle(
            "-fx-background-color: #f1f8e9;" +
            "-fx-text-fill: #000000;" +
            "-fx-font-size: 14px;" +
            "-fx-padding: 10;" +
            "-fx-background-radius: 5;" +
            "-fx-border-color: #81c784;" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 5;"
        );

        Label filterLabel = new Label("Filter:");
        filterLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2e7d32;");

        filterCombo.getItems().addAll("All", "Available", "Unavailable", "Overdue");
        filterCombo.setValue("All");
        filterCombo.setPrefWidth(150);
        filterCombo.setStyle(
            "-fx-background-color: #f1f8e9;" +
            "-fx-font-size: 14px;" +
            "-fx-background-radius: 5;"
        );

        searchBox.getChildren().addAll(searchLabel, searchByISBN, searchByTitle, filterLabel, filterCombo);

        // Add Book Button
        HBox addBookButtonContainer = new HBox();
        addBookButtonContainer.setAlignment(Pos.CENTER);
        addBookButtonContainer.setPadding(new Insets(10, 0, 10, 0));
        
        Button btnShowAddForm = createActionButton("➕ Add New Book", "#4caf50");
        addBookButtonContainer.getChildren().add(btnShowAddForm);

        // Form Container (initially hidden)
        VBox formContainer = new VBox(15);
        formContainer.setPadding(new Insets(20));
        formContainer.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.95);" +
            "-fx-background-radius: 10;" +
            "-fx-border-color: #66bb6a;" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 10;"
        );
        formContainer.setVisible(false);
        formContainer.setManaged(false);

        Label formTitle = new Label("Book Information");
        formTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2e7d32;");

        GridPane formGrid = new GridPane();
        formGrid.setHgap(15);
        formGrid.setVgap(15);
        formGrid.setAlignment(Pos.CENTER_LEFT);

        txtISBN.setPromptText("ISBN");
        txtTitle.setPromptText("Title");
        txtAuthor.setPromptText("Author");
        txtCategory.setPromptText("Category");

        String inputStyle = "-fx-background-color: #f1f8e9; -fx-text-fill: #000000; -fx-font-size: 14px; -fx-padding: 8; -fx-background-radius: 5; -fx-border-color: #81c784; -fx-border-width: 1; -fx-border-radius: 5;";
        txtISBN.setStyle(inputStyle);
        txtISBN.setPrefWidth(250);
        txtTitle.setStyle(inputStyle);
        txtTitle.setPrefWidth(250);
        txtAuthor.setStyle(inputStyle);
        txtAuthor.setPrefWidth(250);
        txtCategory.setStyle(inputStyle);
        txtCategory.setPrefWidth(250);

        chkAvailable.setStyle("-fx-font-size: 14px; -fx-text-fill: #2e7d32; -fx-font-weight: bold;");
        chkAvailable.setSelected(true);

        formGrid.add(new Label("ISBN:"), 0, 0);
        formGrid.add(txtISBN, 1, 0);
        formGrid.add(new Label("Title:"), 0, 1);
        formGrid.add(txtTitle, 1, 1);
        formGrid.add(new Label("Author:"), 2, 0);
        formGrid.add(txtAuthor, 3, 0);
        formGrid.add(new Label("Category:"), 2, 1);
        formGrid.add(txtCategory, 3, 1);
        formGrid.add(chkAvailable, 0, 2, 2, 1);

        // Style form labels
        formGrid.getChildren().stream()
            .filter(node -> node instanceof Label && !node.equals(formTitle))
            .forEach(node -> ((Label)node).setStyle("-fx-font-size: 14px; -fx-text-fill: #2e7d32; -fx-font-weight: bold;"));

        // Buttons
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10, 0, 0, 0));

        Button btnSave = createStyledButton("💾 Save", "#4caf50");
        Button btnCancel = createStyledButton("❌ Cancel", "#757575");

        buttonBox.getChildren().addAll(btnSave, btnCancel);

        formContainer.getChildren().addAll(formTitle, formGrid, buttonBox);
        
        // Toggle form visibility
        btnShowAddForm.setOnAction(e -> {
            formContainer.setVisible(true);
            formContainer.setManaged(true);
            clearForm();
        });
        
        btnCancel.setOnAction(e -> {
            formContainer.setVisible(false);
            formContainer.setManaged(false);
            clearForm();
        });

        btnSave.setOnAction(e -> addBook(formContainer));

        // Table Container
        VBox tableContainer = new VBox(10);
        tableContainer.setPadding(new Insets(20));
        tableContainer.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.98);" +
            "-fx-background-radius: 10;" +
            "-fx-border-color: #66bb6a;" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 10;"
        );

        // Table
        table.setStyle("-fx-background-color: #ffffff;");
        table.setPrefHeight(500);

        TableColumn<Livres, String> colISBN = new TableColumn<>("ISBN");
        colISBN.setCellValueFactory(e -> new javafx.beans.property.SimpleStringProperty(e.getValue().getISBN()));
        colISBN.setStyle("-fx-font-size: 13px;");
        colISBN.setPrefWidth(150);

        TableColumn<Livres, String> colTitre = new TableColumn<>("Title");
        colTitre.setCellValueFactory(e -> new javafx.beans.property.SimpleStringProperty(e.getValue().getTitre()));
        colTitre.setStyle("-fx-font-size: 13px;");
        colTitre.setPrefWidth(300);

        TableColumn<Livres, String> colAuteur = new TableColumn<>("Author");
        colAuteur.setCellValueFactory(e -> new javafx.beans.property.SimpleStringProperty(e.getValue().getAuteur()));
        colAuteur.setStyle("-fx-font-size: 13px;");
        colAuteur.setPrefWidth(200);

        TableColumn<Livres, String> colCategorie = new TableColumn<>("Category");
        colCategorie.setCellValueFactory(e -> new javafx.beans.property.SimpleStringProperty(e.getValue().getCategorie()));
        colCategorie.setStyle("-fx-font-size: 13px;");
        colCategorie.setPrefWidth(150);

        TableColumn<Livres, String> colDisp = new TableColumn<>("Status");
        colDisp.setCellValueFactory(e -> new javafx.beans.property.SimpleStringProperty(
                e.getValue().isDisponibilite() ? "✅ Available" : "❌ Unavailable"
        ));
        colDisp.setStyle("-fx-font-size: 13px;");
        colDisp.setPrefWidth(120);

        table.getColumns().addAll(colISBN, colTitre, colAuteur, colCategorie, colDisp);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        // Table click handler to show book details
        table.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Double-click
                Livres selected = table.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    BookDetailView detailView = new BookDetailView(selected, () -> refreshTable());
                    detailView.show();
                }
            }
        });

        tableContainer.getChildren().add(table);

        // Load initial data
        loadTable();

        // Search and filter listeners
        searchByISBN.textProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        searchByTitle.textProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        filterCombo.setOnAction(e -> applyFilters());

        root.getChildren().addAll(title, searchBox, addBookButtonContainer, formContainer, tableContainer);
    }

    private void loadTable() {
        allBooks = service.getAll();
        ObservableList<Livres> obs = FXCollections.observableArrayList(allBooks);
        table.setItems(obs);
    }

    private void applyFilters() {
        String searchISBN = searchByISBN.getText().toLowerCase().trim();
        String searchTitle = searchByTitle.getText().toLowerCase().trim();
        String filterValue = filterCombo.getValue();

        List<Livres> filtered = allBooks.stream()
            .filter(livre -> {
                // Search filter by ISBN
                boolean matchesISBN = searchISBN.isEmpty() || 
                    livre.getISBN().toLowerCase().contains(searchISBN);

                // Search filter by Title
                boolean matchesTitle = searchTitle.isEmpty() || 
                    livre.getTitre().toLowerCase().contains(searchTitle);

                // Status filter
                boolean matchesFilter = true;
                switch (filterValue) {
                    case "Available":
                        matchesFilter = livre.isDisponibilite();
                        break;
                    case "Unavailable":
                        matchesFilter = !livre.isDisponibilite();
                        break;
                    case "Overdue":
                        // Check if book has an active loan that is overdue
                        matchesFilter = isBookOverdue(livre.getISBN());
                        break;
                    case "All":
                    default:
                        matchesFilter = true;
                }

                return matchesISBN && matchesTitle && matchesFilter;
            })
            .collect(Collectors.toList());

        ObservableList<Livres> obs = FXCollections.observableArrayList(filtered);
        table.setItems(obs);
    }

    private void addBook(VBox formContainer) {
        if (!validateFields()) return;
        
        Livres livre = new Livres(
            txtISBN.getText(),
            txtTitle.getText(),
            txtAuthor.getText(),
            txtCategory.getText(),
            chkAvailable.isSelected()
        );
        
        service.addLivre(livre);
        showSuccess("Book added successfully!");
        loadTable();
        applyFilters();
        clearForm();
        formContainer.setVisible(false);
        formContainer.setManaged(false);
    }

    private boolean validateFields() {
        if (txtISBN.getText().isEmpty() || txtTitle.getText().isEmpty() || 
            txtAuthor.getText().isEmpty() || txtCategory.getText().isEmpty()) {
            showAlert("Please fill in all fields.");
            return false;
        }
        return true;
    }

    private void clearForm() {
        txtISBN.clear();
        txtTitle.clear();
        txtAuthor.clear();
        txtCategory.clear();
        chkAvailable.setSelected(true);
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

    private Button createStyledButton(String text, String color) {
        Button btn = new Button(text);
        btn.setPrefWidth(130);
        btn.setStyle(
            "-fx-background-color: " + color + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 14 20;" +
            "-fx-background-radius: 5;" +
            "-fx-cursor: hand;"
        );
        btn.setOnMouseEntered(e -> btn.setOpacity(0.8));
        btn.setOnMouseExited(e -> btn.setOpacity(1.0));
        return btn;
    }

    private Button createActionButton(String text, String color) {
        Button btn = new Button(text);
        btn.setPrefWidth(250);
        btn.setPrefHeight(50);
        btn.setStyle(
            "-fx-background-color: " + color + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 18px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 18 30;" +
            "-fx-background-radius: 10;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 8, 0, 0, 3);"
        );
        
        String hoverColor = "#45a049";
        
        btn.setOnMouseEntered(e -> btn.setStyle(
            "-fx-background-color: " + hoverColor + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 18px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 15 30;" +
            "-fx-background-radius: 10;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 12, 0, 0, 5);"
        ));
        btn.setOnMouseExited(e -> btn.setStyle(
            "-fx-background-color: " + color + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 18px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 15 30;" +
            "-fx-background-radius: 10;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 8, 0, 0, 3);"
        ));
        return btn;
    }

    public void refreshTable() {
        loadTable();
        applyFilters();
    }
    
    private boolean isBookOverdue(String isbn) {
        LocalDate today = LocalDate.now();
        List<Emprunt> allLoans = empruntService.getEmpruntsEnCours();
        
        // Check if this book has an active loan that is overdue
        for (Emprunt emprunt : allLoans) {
            if (emprunt.getIsbnLivre().equals(isbn) && emprunt.getDateRetour() != null) {
                LocalDate returnDate = new java.sql.Date(emprunt.getDateRetour().getTime()).toLocalDate();
                // Check if the return date is in the future (active loan) and before today (overdue)
                if (returnDate.isBefore(today) && returnDate.isAfter(today.minusYears(1))) {
                    return true;
                }
            }
        }
        return false;
    }

    public VBox getView() {
        return root;
    }
}
