package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import services.EmprunteService;
import model.Emprunt;

import java.time.LocalDate;

public class EmpruntView {

    private final EmprunteService service = new EmprunteService();
    private final VBox root;

    public EmpruntView() {

        Label title = new Label("Loans Management");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #f5f5dc; -fx-font-style: italic;");

        // Form container
        VBox formContainer = new VBox(15);
        formContainer.setPadding(new Insets(20));
        formContainer.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.95);" +
            "-fx-background-radius: 10;" +
            "-fx-border-color: #66bb6a;" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 10;"
        );

        TextField isbnField = new TextField();
        isbnField.setPromptText("Book ISBN");
        isbnField.setStyle("-fx-background-color: #fafafa; -fx-text-fill: #000000; -fx-font-size: 14px; -fx-padding: 10;");

        TextField adherentField = new TextField();
        adherentField.setPromptText("Member Number");
        adherentField.setStyle("-fx-background-color: #fafafa; -fx-text-fill: #000000; -fx-font-size: 14px; -fx-padding: 10;");

        DatePicker dateEmprunt = new DatePicker(LocalDate.now());
        dateEmprunt.setPromptText("Loan Date");
        dateEmprunt.setStyle("-fx-background-color: #fafafa; -fx-font-size: 14px;");

        DatePicker dateRetour = new DatePicker();
        dateRetour.setPromptText("Return Date");
        dateRetour.setStyle("-fx-background-color: #fafafa; -fx-font-size: 14px;");

        HBox fieldsRow = new HBox(10, isbnField, adherentField, dateEmprunt, dateRetour);
        fieldsRow.setAlignment(Pos.CENTER);

        Button btnAdd = new Button("Record Loan");
        btnAdd.setStyle(
            "-fx-background-color: #66bb6a;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 12 30;" +
            "-fx-background-radius: 5;" +
            "-fx-cursor: hand;"
        );
        btnAdd.setOnMouseEntered(e -> btnAdd.setStyle(
            "-fx-background-color: #2e7d32;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 12 30;" +
            "-fx-background-radius: 5;" +
            "-fx-cursor: hand;"
        ));
        btnAdd.setOnMouseExited(e -> btnAdd.setStyle(
            "-fx-background-color: #66bb6a;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 12 30;" +
            "-fx-background-radius: 5;" +
            "-fx-cursor: hand;"
        ));

        formContainer.getChildren().addAll(fieldsRow, btnAdd);
        formContainer.setAlignment(Pos.CENTER);

        TableView<Emprunt> table = new TableView<>();
        table.setStyle("-fx-background-color: #ffffff;");

        TableColumn<Emprunt, String> colIsbn = new TableColumn<>("Book ISBN");
        colIsbn.setCellValueFactory(e -> new javafx.beans.property.SimpleStringProperty(e.getValue().getIsbnLivre()));
        colIsbn.setStyle("-fx-font-size: 13px;");

        TableColumn<Emprunt, String> colAdherent = new TableColumn<>("Member");
        colAdherent.setCellValueFactory(e -> new javafx.beans.property.SimpleStringProperty(e.getValue().getNumeroAdherent()));
        colAdherent.setStyle("-fx-font-size: 13px;");

        TableColumn<Emprunt, String> colDateEmprunt = new TableColumn<>("Loan Date");
        colDateEmprunt.setCellValueFactory(e -> new javafx.beans.property.SimpleStringProperty(e.getValue().getDateEmprunt().toString()));
        colDateEmprunt.setStyle("-fx-font-size: 13px;");

        TableColumn<Emprunt, String> colDateRetour = new TableColumn<>("Return Date");
        colDateRetour.setCellValueFactory(e -> {
            if (e.getValue().getDateRetour() == null)
                return new javafx.beans.property.SimpleStringProperty("Not returned");
            return new javafx.beans.property.SimpleStringProperty(e.getValue().getDateRetour().toString());
        });
        colDateRetour.setStyle("-fx-font-size: 13px;");

        table.getColumns().add(colIsbn);
        table.getColumns().add(colAdherent);
        table.getColumns().add(colDateEmprunt);
        table.getColumns().add(colDateRetour);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        // LOAD DATA
        table.getItems().addAll(service.getEmpruntsEnCours());

        // BUTTON ACTION
        btnAdd.setOnAction(ev -> {
            if (isbnField.getText().isEmpty() || adherentField.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("Missing Information");
                alert.setContentText("Please fill in both ISBN and Member Number.");
                alert.showAndWait();
                return;
            }

            Emprunt e = new Emprunt(
                    isbnField.getText(),
                    adherentField.getText(),
                    java.sql.Date.valueOf(dateEmprunt.getValue()),
                    dateRetour.getValue() == null ? null : java.sql.Date.valueOf(dateRetour.getValue())
            );

            service.addEmprunt(e);
            table.getItems().clear();
            table.getItems().addAll(service.getEmpruntsEnCours());
            
            // Clear fields
            isbnField.clear();
            adherentField.clear();
            dateEmprunt.setValue(LocalDate.now());
            dateRetour.setValue(null);
        });

        root = new VBox(20, title, formContainer, table);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #6B9071;");
    }

    public VBox getView() {
        return root;
    }
}
