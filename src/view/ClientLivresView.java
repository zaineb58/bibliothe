package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import services.LivreServices;
import model.Livres;

@SuppressWarnings("unused")
public class ClientLivresView {

    private final LivreServices service = new LivreServices();
    private final VBox root;
    private final TableView<Livres> table = new TableView<>();
    @SuppressWarnings("unused")
    private final String clientName;
    @SuppressWarnings("unused")
    private final String clientPhone;
    private java.util.function.Consumer<Livres> onLivreClick;
    private Runnable onMesInfosClick;

    public ClientLivresView(String clientName, String clientPhone) {
        this.clientName = clientName;
        this.clientPhone = clientPhone;
        root = new VBox(20);
        root.setPadding(new Insets(25));
        root.setStyle("-fx-background-color: #6B9071;");

        // TITLE
        Label title = new Label("📚 Book Catalog");
        title.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #f5f5dc; -fx-font-style: italic; -fx-padding: 20 0 20 0;");

        // Search and filter bar
        HBox searchBox = new HBox(15);
        searchBox.setPadding(new Insets(15));
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.setStyle("-fx-background-color: " + "#d4e7c5" + "; -fx-background-radius: 10;");

        TextField searchTitreField = new TextField();
        searchTitreField.setPromptText("🔍 Search by title...");
        searchTitreField.setPrefWidth(280);
        searchTitreField.setStyle(
            "-fx-font-size: 15px; " +
            "-fx-padding: 10; " +
            "-fx-background-color: " + "#fafafa" + "; " +
            "-fx-text-fill: " + "#000000" + "; " +
            "-fx-background-radius: 8; " +
            "-fx-border-color: " + "#7cb342" + "; " +
            "-fx-border-radius: 8; " +
            "-fx-border-width: 2;"
        );

        TextField searchAuteurField = new TextField();
        searchAuteurField.setPromptText("🔍 Search by author...");
        searchAuteurField.setPrefWidth(280);
        searchAuteurField.setStyle(
            "-fx-font-size: 15px; " +
            "-fx-padding: 10; " +
            "-fx-background-color: " + "#fafafa" + "; " +
            "-fx-text-fill: " + "#000000" + "; " +
            "-fx-background-radius: 8; " +
            "-fx-border-color: " + "#7cb342" + "; " +
            "-fx-border-radius: 8; " +
            "-fx-border-width: 2;"
        );

        ComboBox<String> filterCombo = new ComboBox<>();
        filterCombo.getItems().addAll("All books", "Available only");
        filterCombo.setValue("All books");
        filterCombo.setStyle(
            "-fx-font-size: 15px; " +
            "-fx-background-color: " + "#fafafa" + "; " +
            "-fx-text-fill: " + "#000000" + "; " +
            "-fx-background-radius: 8; " +
            "-fx-border-color: " + "#7cb342" + "; " +
            "-fx-border-radius: 8; " +
            "-fx-border-width: 2;"
        );
        filterCombo.setPrefWidth(220);

        Button btnMesInfos = new Button("👤 My Information");
        btnMesInfos.setStyle(
            "-fx-background-color: #558b2f; " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 16px; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 14 20; " +
            "-fx-background-radius: 8; " +
            "-fx-cursor: hand;"
        );
        btnMesInfos.setOnMouseEntered(e -> btnMesInfos.setStyle(
            "-fx-background-color: #689f38; " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 16px; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 14 20; " +
            "-fx-background-radius: 8; " +
            "-fx-cursor: hand;"
        ));
        btnMesInfos.setOnMouseExited(e -> btnMesInfos.setStyle(
            "-fx-background-color: #558b2f; " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 16px; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 14 20; " +
            "-fx-background-radius: 8; " +
            "-fx-cursor: hand;"
        ));

        searchBox.getChildren().addAll(searchTitreField, searchAuteurField, filterCombo, btnMesInfos);

        // TABLE (Read-only for clients)
        table.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 10; " +
            "-fx-border-color: #9ccc65; " +
            "-fx-border-radius: 10; " +
            "-fx-border-width: 2;"
        );
        table.setEditable(false);

        TableColumn<Livres, String> colISBN = new TableColumn<>("📖 ISBN");
        colISBN.setCellValueFactory(e -> new javafx.beans.property.SimpleStringProperty(e.getValue().getISBN()));
        colISBN.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-alignment: CENTER;");

        TableColumn<Livres, String> colTitre = new TableColumn<>("📚 Title");
        colTitre.setCellValueFactory(e -> new javafx.beans.property.SimpleStringProperty(e.getValue().getTitre()));
        colTitre.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-alignment: CENTER;");
        colTitre.setPrefWidth(250);

        TableColumn<Livres, String> colAuteur = new TableColumn<>("✍️ Author");
        colAuteur.setCellValueFactory(e -> new javafx.beans.property.SimpleStringProperty(e.getValue().getAuteur()));
        colAuteur.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-alignment: CENTER;");

        TableColumn<Livres, String> colCategorie = new TableColumn<>("🏷️ Category");
        colCategorie.setCellValueFactory(e -> new javafx.beans.property.SimpleStringProperty(e.getValue().getCategorie()));
        colCategorie.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-alignment: CENTER;");

        TableColumn<Livres, String> colDisp = new TableColumn<>("✅ Available");
        colDisp.setCellValueFactory(e -> new javafx.beans.property.SimpleStringProperty(
                e.getValue().isDisponibilite() ? "✓ Yes" : "✗ No"
        ));
        colDisp.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-alignment: CENTER;");

        table.getColumns().add(colISBN);
        table.getColumns().add(colTitre);
        table.getColumns().add(colAuteur);
        table.getColumns().add(colCategorie);
        table.getColumns().add(colDisp);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_SUBSEQUENT_COLUMNS);

        // Style table rows with click handler
        table.setRowFactory(tv -> {
            TableRow<Livres> row = new TableRow<Livres>() {
                @Override
                protected void updateItem(Livres livre, boolean empty) {
                    super.updateItem(livre, empty);
                    if (livre == null || empty) {
                        setStyle("");
                    } else {
                        if (livre.isDisponibilite()) {
                            setStyle("-fx-background-color: #e8f5e9; -fx-font-size: 13px; -fx-cursor: hand;");
                        } else {
                            setStyle("-fx-background-color: #ffebee; -fx-font-size: 13px; -fx-cursor: hand;");
                        }
                    }
                }
            };
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && onLivreClick != null) {
                    onLivreClick.accept(row.getItem());
                }
            });
            return row;
        });

        // LOAD DATA
        refreshTable();

        // Search and filter functionality
        Runnable applyFilters = () -> {
            String searchTitre = searchTitreField.getText();
            String searchAuteur = searchAuteurField.getText();
            String filterValue = filterCombo.getValue();
            
            table.getItems().clear();
            for (Livres livre : service.getAll()) {
                // Apply search filter (Titre and Auteur)
                boolean matchesTitre = searchTitre == null || searchTitre.trim().isEmpty() || 
                    livre.getTitre().toLowerCase().contains(searchTitre.toLowerCase());
                
                boolean matchesAuteur = searchAuteur == null || searchAuteur.trim().isEmpty() || 
                    livre.getAuteur().toLowerCase().contains(searchAuteur.toLowerCase());
                
                // Apply availability filter
                boolean matchesAvailability = true;
                if (filterValue.equals("Available only")) {
                    matchesAvailability = livre.isDisponibilite();
                }
                
                if (matchesTitre && matchesAuteur && matchesAvailability) {
                    table.getItems().add(livre);
                }
            }
        };
        
        searchTitreField.textProperty().addListener((obs, oldVal, newVal) -> applyFilters.run());
        searchAuteurField.textProperty().addListener((obs, oldVal, newVal) -> applyFilters.run());
        filterCombo.setOnAction(e -> applyFilters.run());

        // Button action
        btnMesInfos.setOnAction(e -> {
            if (onMesInfosClick != null) {
                onMesInfosClick.run();
            }
        });

        root.getChildren().addAll(title, searchBox, table);
    }

    public void refreshTable() {
        table.getItems().clear();
        table.getItems().addAll(service.getAll());
    }

    public VBox getView() {
        return root;
    }

    public void setOnLivreClick(java.util.function.Consumer<Livres> action) {
        this.onLivreClick = action;
    }

    public void setOnMesInfosClick(Runnable action) {
        this.onMesInfosClick = action;
    }
}
