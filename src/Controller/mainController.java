package Controller;

import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Label;

public class mainController {

    @FXML
    private StackPane contentArea;

    @FXML
    private void openAdherents() {
        contentArea.getChildren().setAll(new Label("Adherents View - FXML not loaded"));
    }

    @FXML
    private void openLivres() {
        contentArea.getChildren().setAll(new Label("Livres View - FXML not loaded"));
    }

    @FXML
    private void openEmprunts() {
        contentArea.getChildren().setAll(new Label("Emprunts View - FXML not loaded"));
    }
}
