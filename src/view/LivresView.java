package view;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import services.LivreServices;
import model.Livres;

public class LivresView {

    private final LivreServices service = new LivreServices();
    private final VBox root;

    public LivresView() {
        // TITLE
        Label title = new Label("Gestion des Livres");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        // INPUT FIELDS
        TextField isbnField = new TextField();
        isbnField.setPromptText("ISBN");

        TextField titreField = new TextField();
        titreField.setPromptText("Titre");

        TextField auteurField = new TextField();
        auteurField.setPromptText("Auteur");

        TextField categorieField = new TextField();
        categorieField.setPromptText("Catégorie");

        CheckBox disponibiliteCheck = new CheckBox("Disponible");

        Button btnAdd = new Button("Ajouter Livre");
        btnAdd.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");

        // TABLE
        TableView<Livres> table = new TableView<>();
        TableColumn<Livres, String> colISBN = new TableColumn<>("ISBN");
        colISBN.setCellValueFactory(e -> new javafx.beans.property.SimpleStringProperty(e.getValue().getISBN()));

        TableColumn<Livres, String> colTitre = new TableColumn<>("Titre");
        colTitre.setCellValueFactory(e -> new javafx.beans.property.SimpleStringProperty(e.getValue().getTitre()));

        TableColumn<Livres, String> colAuteur = new TableColumn<>("Auteur");
        colAuteur.setCellValueFactory(e -> new javafx.beans.property.SimpleStringProperty(e.getValue().getAuteur()));

        TableColumn<Livres, String> colCategorie = new TableColumn<>("Catégorie");
        colCategorie.setCellValueFactory(e -> new javafx.beans.property.SimpleStringProperty(e.getValue().getCategorie()));

        TableColumn<Livres, String> colDisp = new TableColumn<>("Disponible");
        colDisp.setCellValueFactory(e -> new javafx.beans.property.SimpleStringProperty(
                e.getValue().isDisponibilite() ? "Oui" : "Non"
        ));

        table.getColumns().add(colISBN);
        table.getColumns().add(colTitre);
        table.getColumns().add(colAuteur);
        table.getColumns().add(colCategorie);
        table.getColumns().add(colDisp);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_SUBSEQUENT_COLUMNS);

        // LOAD DATA
        table.getItems().addAll(service.getAll());

        // BUTTON ACTION
        btnAdd.setOnAction(ev -> {
            Livres l = new Livres(
                    isbnField.getText(),
                    titreField.getText(),
                    auteurField.getText(),
                    categorieField.getText(),
                    disponibiliteCheck.isSelected()
            );

            service.addLivre(l);
            table.getItems().clear();
            table.getItems().addAll(service.getAll());
        });

        root = new VBox(12,
                title,
                new HBox(8, isbnField, titreField, auteurField),
                new HBox(8, categorieField, disponibiliteCheck),
                btnAdd,
                table
        );
        root.setPadding(new Insets(20));
    }

    public VBox getView() {
        return root;
    }
}
