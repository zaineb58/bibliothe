package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

public class AdminHomeView {

    private final VBox root;

    public AdminHomeView(Runnable onLivresClick, Runnable onAdherentsClick) {
        root = new VBox(40);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(50));
        root.setStyle("-fx-background-color: #6B9071;");

        // Title with decorative elements
        VBox titleBox = new VBox(10);
        titleBox.setAlignment(Pos.CENTER);
        
        Label mainTitle = new Label("📖 Library Management System");
        mainTitle.setStyle("-fx-font-size: 42px; -fx-font-weight: bold; -fx-text-fill: #f5f5dc; -fx-font-style: italic; -fx-font-family: 'Georgia'; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 8, 0, 0, 2);");
        
        Label subtitle = new Label("Admin Dashboard");
        subtitle.setStyle("-fx-font-size: 24px; -fx-text-fill: #f5f5dc; -fx-font-style: italic; -fx-opacity: 0.9;");
        
        titleBox.getChildren().addAll(mainTitle, subtitle);

        // Buttons container
        HBox buttonBox = new HBox(50);
        buttonBox.setAlignment(Pos.CENTER);

        // Livres button
        VBox btnLivresBox = createMenuButton("📚", "Books", "", onLivresClick);

        // Adherents button
        VBox btnAdherentsBox = createMenuButton("👤", "Members", "", onAdherentsClick);

        buttonBox.getChildren().addAll(btnLivresBox, btnAdherentsBox);

        root.getChildren().addAll(titleBox, buttonBox);
    }

    private VBox createMenuButton(String icon, String title, String description, Runnable action) {
        VBox container = new VBox(15);
        container.setAlignment(Pos.CENTER);
        container.setPrefSize(450, 350);
        container.setPadding(new Insets(30));
        container.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.98);" +
            "-fx-background-radius: 20;" +
            "-fx-border-color: #66bb6a;" +
            "-fx-border-width: 3;" +
            "-fx-border-radius: 20;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 5);"
        );

        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 80px;");

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #2e7d32; -fx-font-style: italic;");

        Label descLabel = new Label(description);
        descLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #558b2f; -fx-text-alignment: center;");
        descLabel.setWrapText(true);
        descLabel.setMaxWidth(380);

        container.getChildren().addAll(iconLabel, titleLabel, descLabel);

        // Hover effects
        container.setOnMouseEntered(e -> {
            container.setStyle(
                "-fx-background-color: linear-gradient(to bottom right, #c8e6c9, #a5d6a7);" +
                "-fx-background-radius: 20;" +
                "-fx-border-color: #2e7d32;" +
                "-fx-border-width: 4;" +
                "-fx-border-radius: 20;" +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 15, 0, 0, 8);" +
                "-fx-scale-x: 1.03;" +
                "-fx-scale-y: 1.03;"
            );
        });

        container.setOnMouseExited(e -> {
            container.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.98);" +
                "-fx-background-radius: 20;" +
                "-fx-border-color: #66bb6a;" +
                "-fx-border-width: 3;" +
                "-fx-border-radius: 20;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 5);"
            );
        });

        container.setOnMouseClicked(e -> action.run());

        return container;
    }

    public VBox getView() {
        return root;
    }
}
