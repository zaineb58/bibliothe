package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import model.Adherent;
import services.AdherentService;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class AdherentView {

    private final VBox root;
    private final TextField txtNumero = new TextField();
    private final TextField txtNom = new TextField();
    private final TextField txtPrenom = new TextField();
    private final DatePicker datePicker = new DatePicker();
    private final CheckBox chkPremium = new CheckBox("Premium");
    private final TableView<Adherent> table = new TableView<>();
    private final AdherentService service = new AdherentService();
    private final TextField searchField = new TextField();
    private final TextField searchByNumber = new TextField();
    private final ComboBox<String> filterCombo = new ComboBox<>();
    private List<Adherent> allAdherents;
    private boolean isEditing = false;
    private Adherent editingMember = null;

    @SuppressWarnings("unchecked")
    public AdherentView() {
        root = new VBox(20);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #6B9071;");

        // Title
        Label title = new Label("👤 Members Management");
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

        TextField searchByNumber = new TextField();
        searchByNumber.setPromptText("By Number");
        searchByNumber.setPrefWidth(180);
        searchByNumber.setStyle(
            "-fx-background-color: #f1f8e9;" +
            "-fx-text-fill: #000000;" +
            "-fx-font-size: 14px;" +
            "-fx-padding: 10;" +
            "-fx-background-radius: 5;" +
            "-fx-border-color: #81c784;" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 5;"
        );

        searchField.setPromptText("By Name");
        searchField.setPrefWidth(180);
        searchField.setStyle(
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

        filterCombo.getItems().addAll("All", "Premium", "Standard");
        filterCombo.setValue("All");
        filterCombo.setPrefWidth(150);
        filterCombo.setStyle(
            "-fx-background-color: #f1f8e9;" +
            "-fx-font-size: 14px;" +
            "-fx-background-radius: 5;"
        );

        searchBox.getChildren().addAll(searchLabel, searchByNumber, searchField, filterLabel, filterCombo);

        // Action Buttons Container (Add, Delete)
        HBox actionButtonsContainer = new HBox(20);
        actionButtonsContainer.setAlignment(Pos.CENTER);
        actionButtonsContainer.setPadding(new Insets(10, 0, 10, 0));
        
        Button btnShowAddForm = createActionButton("➕ Add New Member", "#4caf50");
        Button btnDeleteMember = createActionButton("🗑️ Delete Member", "#a70000ff");
        
        actionButtonsContainer.getChildren().addAll(btnShowAddForm, btnDeleteMember);

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

        Label formTitle = new Label("Member Information");
        formTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2e7d32;");

        GridPane formGrid = new GridPane();
        formGrid.setHgap(15);
        formGrid.setVgap(15);
        formGrid.setAlignment(Pos.CENTER_LEFT);

        txtNumero.setPromptText("Number");
        txtNom.setPromptText("Last Name");
        txtPrenom.setPromptText("First Name");
        datePicker.setPromptText("Birth Date");

        String inputStyle = "-fx-background-color: #f1f8e9; -fx-text-fill: #000000; -fx-font-size: 14px; -fx-padding: 8; -fx-background-radius: 5; -fx-border-color: #81c784; -fx-border-width: 1; -fx-border-radius: 5;";
        txtNumero.setStyle(inputStyle);
        txtNumero.setPrefWidth(180);
        txtNom.setStyle(inputStyle);
        txtNom.setPrefWidth(180);
        txtPrenom.setStyle(inputStyle);
        txtPrenom.setPrefWidth(180);
        datePicker.setStyle("-fx-background-color: #f1f8e9; -fx-font-size: 14px;");
        datePicker.setPrefWidth(180);

        chkPremium.setStyle("-fx-font-size: 14px; -fx-text-fill: #2e7d32; -fx-font-weight: bold;");

        formGrid.add(new Label("Number:"), 0, 0);
        formGrid.add(txtNumero, 1, 0);
        formGrid.add(new Label("Last Name:"), 2, 0);
        formGrid.add(txtNom, 3, 0);
        formGrid.add(new Label("First Name:"), 0, 1);
        formGrid.add(txtPrenom, 1, 1);
        formGrid.add(new Label("Birth Date:"), 2, 1);
        formGrid.add(datePicker, 3, 1);
        formGrid.add(chkPremium, 0, 2, 2, 1);

        // Style form labels
        formGrid.getChildren().stream()
            .filter(node -> node instanceof Label && !node.equals(formTitle))
            .forEach(node -> ((Label)node).setStyle("-fx-font-size: 14px; -fx-text-fill: #2e7d32; -fx-font-weight: bold;"));

        // Buttons
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10, 0, 0, 0));

        Button btnSave = createStyledButton("💾 Save", "#375534");
        Button btnCancel = createStyledButton("❌ Cancel", "#757575");

        buttonBox.getChildren().addAll(btnSave, btnCancel);

        formContainer.getChildren().addAll(formTitle, formGrid, buttonBox);
        
        // Toggle form visibility
        btnShowAddForm.setOnAction(e -> {
            formContainer.setVisible(true);
            formContainer.setManaged(true);
            clear();
        });
        
        
        btnCancel.setOnAction(e -> {
            formContainer.setVisible(false);
            formContainer.setManaged(false);
            clear();
        });

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

        table.setStyle("-fx-background-color: #ffffff;");
        table.setPrefHeight(300);

        // Table columns
        TableColumn<Adherent, String> colNumero = new TableColumn<>("Number");
        colNumero.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNumero()));
        colNumero.setStyle("-fx-font-size: 13px;");
        colNumero.setPrefWidth(120);

        TableColumn<Adherent, String> colNom = new TableColumn<>("Last Name");
        colNom.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNom()));
        colNom.setStyle("-fx-font-size: 13px;");
        colNom.setPrefWidth(200);

        TableColumn<Adherent, String> colPrenom = new TableColumn<>("First Name");
        colPrenom.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getPrenom()));
        colPrenom.setStyle("-fx-font-size: 13px;");
        colPrenom.setPrefWidth(200);

        TableColumn<Adherent, String> colDate = new TableColumn<>("Birth Date");
        colDate.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
            c.getValue().getDatenaissance() != null ? c.getValue().getDatenaissance().toString() : "N/A"
        ));
        colDate.setStyle("-fx-font-size: 13px;");
        colDate.setPrefWidth(150);

        TableColumn<Adherent, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
            c.getValue().isPremium() ? "⭐ Premium" : "📌 Standard"
        ));
        colStatus.setStyle("-fx-font-size: 13px;");
        colStatus.setPrefWidth(130);

        table.getColumns().addAll(colNumero, colNom, colPrenom, colDate, colStatus);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        // Table row double-click to open detail view
        table.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Double-click only
                Adherent selected = table.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    MemberDetailView detailView = new MemberDetailView(selected, () -> refreshTable());
                    detailView.show();
                }
            }
        });

        tableContainer.getChildren().add(table);

        // Button actions
        btnSave.setOnAction(e -> {
            // Determine if adding or updating based on editing mode
            if (isEditing && editingMember != null) {
                updateAdherent();
            } else {
                addAdherent();
            }
        });

        // Search and filter listeners
        searchByNumber.textProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        searchField.textProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        filterCombo.setOnAction(e -> applyFilters());

        root.getChildren().addAll(title, searchBox, actionButtonsContainer, formContainer, tableContainer);

        loadTable();
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
        btn.setPrefWidth(220);
        btn.setPrefHeight(50);
        btn.setStyle(
            "-fx-background-color: " + color + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 18 30;" +
            "-fx-background-radius: 10;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 8, 0, 0, 3);"
        );
        
        String hoverColor = color.equals("#375534") ? "#45a049" : 
                           color.equals("#2196f3") ? "#1976d2" : "#8B0000";
        
        btn.setOnMouseEntered(e -> btn.setStyle(
            "-fx-background-color: " + hoverColor + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 15 30;" +
            "-fx-background-radius: 10;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 12, 0, 0, 5);"
        ));
        btn.setOnMouseExited(e -> btn.setStyle(
            "-fx-background-color: " + color + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 15 30;" +
            "-fx-background-radius: 10;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 8, 0, 0, 3);"
        ));
        return btn;
    }

    @SuppressWarnings("unused")
    private void populateForm(Adherent a) {
        isEditing = true;
        editingMember = a;
        txtNumero.setText(a.getNumero());
        txtNumero.setDisable(true); // Disable numero field when editing
        txtNom.setText(a.getNom());
        txtPrenom.setText(a.getPrenom());
        if (a.getDatenaissance() != null) {
            LocalDate localDate = new java.sql.Date(a.getDatenaissance().getTime()).toLocalDate();
            datePicker.setValue(localDate);
        }
        chkPremium.setSelected(a.isPremium());
    }

    public Pane getRoot() { return root; }
    
    public VBox getView() { return root; }

    private void loadTable() {
        allAdherents = service.getAll();
        ObservableList<Adherent> obs = FXCollections.observableArrayList(allAdherents);
        table.setItems(obs);
    }

    private void applyFilters() {
        String searchByNum = searchByNumber.getText().toLowerCase().trim();
        String searchByName = searchField.getText().toLowerCase().trim();
        String filterValue = filterCombo.getValue();

        List<Adherent> filtered = allAdherents.stream()
            .filter(adherent -> {
                // Search filter by Number
                boolean matchesNumber = searchByNum.isEmpty() || 
                    adherent.getNumero().toLowerCase().contains(searchByNum);

                // Search filter by Name
                boolean matchesName = searchByName.isEmpty() || 
                    adherent.getNom().toLowerCase().contains(searchByName) ||
                    adherent.getPrenom().toLowerCase().contains(searchByName);

                // Status filter
                boolean matchesFilter = true;
                switch (filterValue) {
                    case "Premium":
                        matchesFilter = adherent.isPremium();
                        break;
                    case "Standard":
                        matchesFilter = !adherent.isPremium();
                        break;
                    case "All":
                    default:
                        matchesFilter = true;
                }

                return matchesNumber && matchesName && matchesFilter;
            })
            .collect(Collectors.toList());

        ObservableList<Adherent> obs = FXCollections.observableArrayList(filtered);
        table.setItems(obs);
    }

    public void refreshTable() {
        loadTable();
        applyFilters();
    }

    private boolean validateFields() {
        if (txtNumero.getText().isEmpty() || txtNom.getText().isEmpty() || txtPrenom.getText().isEmpty() || datePicker.getValue() == null) {
            showAlert("Remplissez tous les champs");
            return false;
        }
        return true;
    }

    private void addAdherent() {
        if (!validateFields()) return;
        Adherent a = new Adherent(
            txtNumero.getText(),
            txtNom.getText(),
            txtPrenom.getText(),
            Date.valueOf(datePicker.getValue()),
            chkPremium.isSelected()
        );
        service.AjouterAdherent(a);
        showSuccess("Member added successfully!");
        loadTable();
        applyFilters();
        clear();
        // Hide form after adding
        root.getChildren().stream()
            .filter(node -> node instanceof VBox && ((VBox)node).getChildren().stream()
                .anyMatch(child -> child instanceof Label && ((Label)child).getText().equals("Member Information")))
            .findFirst()
            .ifPresent(form -> {
                form.setVisible(false);
                form.setManaged(false);
            });
    }

    private void updateAdherent() {
        if (!validateFields()) return;
        
        if (editingMember == null) {
            showAlert("No member selected for update.");
            return;
        }
        
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Update");
        confirm.setHeaderText("Update Member");
        confirm.setContentText("Are you sure you want to update this member?");
        
        if (confirm.showAndWait().get() == ButtonType.OK) {
            try {
                // Create updated member preserving dateAjout and signalements
                Adherent a = new Adherent(
                    txtNumero.getText(),
                    txtNom.getText(),
                    txtPrenom.getText(),
                    Date.valueOf(datePicker.getValue()),
                    chkPremium.isSelected(),
                    editingMember.getDateAjout(),
                    editingMember.getSignalements()
                );
                service.modifierAdherent(a);
                showSuccess("Member updated successfully!");
                loadTable();
                applyFilters();
                clear();
            } catch (Exception ex) {
                showAlert("Error updating member: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    private void deleteAdherent() {
        Adherent selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) { 
            showAlert("Please select a member from the table to delete."); 
            return; 
        }
        
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Delete");
        confirm.setHeaderText("Delete Member");
        confirm.setContentText("Are you sure you want to delete member #" + selected.getNumero() + " - " + selected.getNom() + " " + selected.getPrenom() + "?");
        
        if (confirm.showAndWait().get() == ButtonType.OK) {
            service.supprimerAdherent(selected.getNumero());
            showSuccess("Member deleted successfully!");
            loadTable();
            applyFilters();
            clear();
            table.getSelectionModel().clearSelection();
        }
    }

    private void clear() {
        isEditing = false;
        editingMember = null;
        txtNumero.clear();
        txtNumero.setDisable(false); // Re-enable numero field
        txtNom.clear();
        txtPrenom.clear();
        datePicker.setValue(null);
        chkPremium.setSelected(false);
        table.getSelectionModel().clearSelection();
    }    private void showAlert(String msg) {
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
    
}
