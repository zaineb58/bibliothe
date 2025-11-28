package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import model.Livres;
import services.LivreServices;

public class AddLivreView {

    private final VBox root;
    private final LivreServices livreService = new LivreServices();
    @SuppressWarnings("unused")
    private final Runnable onSuccess;

    public AddLivreView(Runnable onSuccess) {
        this.onSuccess = onSuccess;

        root = new VBox(20);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #6B9071;");

        // Title
        Label title = new Label("Add a Book");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setStyle("-fx-text-fill: #f5f5dc; -fx-font-style: italic;");

        // Form container
        VBox formBox = new VBox(15);
        formBox.setPadding(new Insets(30));
        formBox.setMaxWidth(500);
        formBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.95); -fx-background-radius: 10; -fx-border-color: #66bb6a; -fx-border-width: 2; -fx-border-radius: 10;");

        // ISBN field
        Label lblISBN = new Label("ISBN:");
        lblISBN.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        TextField txtISBN = new TextField();
        txtISBN.setPromptText("Enter ISBN");
        txtISBN.setStyle("-fx-font-size: 14px;");

        // Titre field
        Label lblTitre = new Label("Title:");
        lblTitre.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        TextField txtTitre = new TextField();
        txtTitre.setPromptText("Enter title");
        txtTitre.setStyle("-fx-font-size: 14px;");

        // Auteur field
        Label lblAuteur = new Label("Author:");
        lblAuteur.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        TextField txtAuteur = new TextField();
        txtAuteur.setPromptText("Enter author");
        txtAuteur.setStyle("-fx-font-size: 14px;");

        // Catégorie field
        Label lblCategorie = new Label("Category:");
        lblCategorie.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        TextField txtCategorie = new TextField();
        txtCategorie.setPromptText("Enter category");
        txtCategorie.setStyle("-fx-font-size: 14px;");

        // Disponibilité checkbox
        CheckBox chkDisponibilite = new CheckBox("Book available");
        chkDisponibilite.setSelected(true);
        chkDisponibilite.setFont(Font.font("Arial", 14));

        // Button container
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);

        Button btnAjouter = new Button("Add");
        btnAjouter.setStyle(
            "-fx-background-color: #4CAF50;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 14 30;" +
            "-fx-cursor: hand;"
        );

        Button btnAnnuler = new Button("Cancel");
        btnAnnuler.setStyle(
            "-fx-background-color: #f44336;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 14 30;" +
            "-fx-cursor: hand;"
        );

        btnAjouter.setOnAction(e -> {
            String isbn = txtISBN.getText().trim();
            String titre = txtTitre.getText().trim();
            String auteur = txtAuteur.getText().trim();
            String categorie = txtCategorie.getText().trim();
            boolean disponibilite = chkDisponibilite.isSelected();

            if (isbn.isEmpty() || titre.isEmpty() || auteur.isEmpty() || categorie.isEmpty()) {
                showAlert("Error", "Please fill in all fields");
                return;
            }

            Livres livre = new Livres(isbn, titre, auteur, categorie, disponibilite);
            livreService.addLivre(livre);
            
            showAlert("Success", "Book added successfully!");
            onSuccess.run(); // Return to list
        });

        btnAnnuler.setOnAction(e -> onSuccess.run());

        buttonBox.getChildren().addAll(btnAjouter, btnAnnuler);

        formBox.getChildren().addAll(
            lblISBN, txtISBN,
            lblTitre, txtTitre,
            lblAuteur, txtAuteur,
            lblCategorie, txtCategorie,
            chkDisponibilite,
            buttonBox
        );

        root.getChildren().addAll(title, formBox);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public VBox getView() {
        return root;
    }
}
