package view;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;

public class MainView {

    private BorderPane root = new BorderPane();

    // The three views
    AdherentView adherentView = new AdherentView();
    LivresView livresView = new LivresView();
    EmpruntView empruntView = new EmpruntView();

    public MainView() {
        // Sidebar
        VBox menu = new VBox(10);
        Button btnAdherent = new Button("Adherents");
        Button btnLivres = new Button("Livres");
        Button btnEmprunts = new Button("Emprunts");

        menu.getChildren().addAll(btnAdherent, btnLivres, btnEmprunts);

        root.setLeft(menu);

        // Default view
        root.setCenter(adherentView.getView());

        // Actions
        btnAdherent.setOnAction(e -> root.setCenter(adherentView.getView()));
        btnLivres.setOnAction(e -> root.setCenter(livresView.getView()));
        btnEmprunts.setOnAction(e -> root.setCenter(empruntView.getView()));
    }

    public BorderPane getRoot() {
        return root;
    }
    public BorderPane getView() {
        return root;
    }
}
