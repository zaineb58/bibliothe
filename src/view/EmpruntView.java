package view;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import services.EmprunteService;
import model.Emprunt;

import java.time.LocalDate;

public class EmpruntView {

    private final EmprunteService service = new EmprunteService();
    private final VBox root;

    public EmpruntView() {

        Label title = new Label("Gestion des Emprunts");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        TextField isbnField = new TextField();
        isbnField.setPromptText("ISBN Livre");

        TextField adherentField = new TextField();
        adherentField.setPromptText("Numéro Adhérent");

        DatePicker dateEmprunt = new DatePicker(LocalDate.now());
        DatePicker dateRetour = new DatePicker();

        Button btnAdd = new Button("Enregistrer Emprunt");
        btnAdd.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");

        TableView<Emprunt> table = new TableView<>();

        TableColumn<Emprunt, String> colIsbn = new TableColumn<>("ISBN Livre");
        colIsbn.setCellValueFactory(e -> new javafx.beans.property.SimpleStringProperty(e.getValue().getIsbnLivre()));

        TableColumn<Emprunt, String> colAdherent = new TableColumn<>("Adhérent");
        colAdherent.setCellValueFactory(e -> new javafx.beans.property.SimpleStringProperty(e.getValue().getNumeroAdherent()));

        TableColumn<Emprunt, String> colDateEmprunt = new TableColumn<>("Date Emprunt");
        colDateEmprunt.setCellValueFactory(e -> new javafx.beans.property.SimpleStringProperty(e.getValue().getDateEmprunt().toString()));

        TableColumn<Emprunt, String> colDateRetour = new TableColumn<>("Date Retour");
        colDateRetour.setCellValueFactory(e -> {
            if (e.getValue().getDateRetour() == null)
                return new javafx.beans.property.SimpleStringProperty("Non retourné");
            return new javafx.beans.property.SimpleStringProperty(e.getValue().getDateRetour().toString());
        });

        table.getColumns().add(colIsbn);
        table.getColumns().add(colAdherent);
        table.getColumns().add(colDateEmprunt);
        table.getColumns().add(colDateRetour);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        // LOAD DATA
        table.getItems().addAll(service.getEmpruntsEnCours());

        // BUTTON ACTION
        btnAdd.setOnAction(ev -> {
            Emprunt e = new Emprunt(
                    isbnField.getText(),
                    adherentField.getText(),
                    java.sql.Date.valueOf(dateEmprunt.getValue()),
                    dateRetour.getValue() == null ? null : java.sql.Date.valueOf(dateRetour.getValue())
            );

            service.addEmprunt(e);
            table.getItems().clear();
            table.getItems().addAll(service.getEmpruntsEnCours());
        });

        root = new VBox(12,
                title,
                new HBox(8, isbnField, adherentField, dateEmprunt, dateRetour),
                btnAdd,
                table
        );
        root.setPadding(new Insets(20));
    }

    public VBox getView() {
        return root;
    }
}
