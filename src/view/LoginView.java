package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import model.Adherent;
import services.AdherentService;

import java.util.List;

public class LoginView {

    private final VBox root;
    private String userType = "admin"; // default
    private String clientName = "";
    private String clientPhone = "";
    @SuppressWarnings("unused")
    private final Runnable onLoginSuccess;
    private final AdherentService adherentService = new AdherentService();

    public LoginView(Runnable onLoginSuccess) {
        this.onLoginSuccess = onLoginSuccess;
        
        root = new VBox(30);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(50));
        root.setStyle("-fx-background-color: #6B9071;");

        // Welcome section
        VBox welcomeBox = new VBox(10);
        welcomeBox.setAlignment(Pos.CENTER);
        
        Label welcomeLabel = new Label("Welcome");
        welcomeLabel.setFont(Font.font("Segoe UI", FontWeight.LIGHT, 24));
        welcomeLabel.setStyle("-fx-text-fill: #f5f5dc; -fx-font-style: italic;");
        
        Label title = new Label("📚 THE KNOWLEDGE HUB");
        title.setFont(Font.font("Georgia", FontWeight.BOLD, 42));
        title.setStyle("-fx-text-fill: #f5f5dc; -fx-font-style: italic; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 8, 0, 0, 2);");
        
        Label subtitle = new Label("Management System");
        subtitle.setFont(Font.font("Segoe UI", FontWeight.LIGHT, 18));
        subtitle.setStyle("-fx-text-fill: #f5f5dc; -fx-font-style: italic; -fx-opacity: 0.9;");
        
        welcomeBox.getChildren().addAll(welcomeLabel, title, subtitle);

        // Login form container
        VBox loginForm = new VBox(20);
        loginForm.setAlignment(Pos.CENTER);
        loginForm.setPadding(new Insets(40, 50, 40, 50));
        loginForm.setMaxWidth(450);
        loginForm.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.95);" +
            "-fx-background-radius: 20;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 20, 0, 0, 5);"
        );

        // User type selection
        Label typeLabel = new Label("I am:");
        typeLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        typeLabel.setStyle("-fx-text-fill: #375534;");
        
        ToggleGroup userTypeGroup = new ToggleGroup();
        RadioButton rbAdmin = new RadioButton("👔 Administrator");
        RadioButton rbClient = new RadioButton("👤 Member");
        rbAdmin.setToggleGroup(userTypeGroup);
        rbClient.setToggleGroup(userTypeGroup);
        rbAdmin.setSelected(true);
        rbAdmin.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 15));
        rbClient.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 15));
        rbAdmin.setStyle("-fx-text-fill: #424242;");
        rbClient.setStyle("-fx-text-fill: #424242;");
        
        rbAdmin.setOnAction(e -> userType = "admin");
        rbClient.setOnAction(e -> userType = "client");
        
        HBox typeBox = new HBox(30, rbAdmin, rbClient);
        typeBox.setAlignment(Pos.CENTER);
        typeBox.setPadding(new Insets(10, 0, 10, 0));
        typeBox.setStyle(
            "-fx-background-color: #f1f8e9;" +
            "-fx-background-radius: 10;" +
            "-fx-padding: 15;"
        );

        // Input fields with labels
        VBox usernameBox = new VBox(8);
        Label usernameLabel = new Label("📝 Name");
        usernameLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        usernameLabel.setStyle("-fx-text-fill: #558b2f;");
        
        TextField txtUsername = new TextField();
        txtUsername.setPrefHeight(45);
        txtUsername.setPromptText("Enter your name");
        txtUsername.setFont(Font.font("Segoe UI", 14));
        txtUsername.setStyle(
            "-fx-background-color: #faf8f3;" +
            "-fx-border-color: #9ccc65;" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 10;"
        );
        usernameBox.getChildren().addAll(usernameLabel, txtUsername);

        VBox passwordBox = new VBox(8);
        Label passwordLabel = new Label("🔒 Security Code");
        passwordLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        passwordLabel.setStyle("-fx-text-fill: #558b2f;");
        
        PasswordField txtPassword = new PasswordField();
        txtPassword.setPrefHeight(45);
        txtPassword.setPromptText("Enter security code");
        txtPassword.setFont(Font.font("Segoe UI", 14));
        txtPassword.setStyle(
            "-fx-background-color: #faf8f3;" +
            "-fx-border-color: #9ccc65;" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 10;"
        );
        passwordBox.getChildren().addAll(passwordLabel, txtPassword);
        
        // Update prompts and labels based on user type
        rbAdmin.setOnAction(e -> {
            userType = "admin";
            usernameBox.setVisible(false);
            usernameBox.setManaged(false);
            passwordLabel.setText("🔒 Security Code");
            txtPassword.setPromptText("Enter security code");
            rbAdmin.setStyle("-fx-text-fill: #375534; -fx-font-weight: bold;");
            rbClient.setStyle("-fx-text-fill: #424242;");
        });
        
        rbClient.setOnAction(e -> {
            userType = "client";
            usernameBox.setVisible(true);
            usernameBox.setManaged(true);
            usernameLabel.setText("📝 Name");
            txtUsername.setPromptText("Enter your name");
            passwordLabel.setText("📱 Phone Number");
            txtPassword.setPromptText("Enter your number");
            rbClient.setStyle("-fx-text-fill: #375534; -fx-font-weight: bold;");
            rbAdmin.setStyle("-fx-text-fill: #424242;");
        });
        
        // Set initial state for admin
        usernameBox.setVisible(false);
        usernameBox.setManaged(false);
        rbAdmin.setStyle("-fx-text-fill: #375534; -fx-font-weight: bold;");

        Button btnLogin = new Button("🔓 LOGIN");
        btnLogin.setPrefWidth(250);
        btnLogin.setPrefHeight(50);
        btnLogin.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        btnLogin.setStyle(
            "-fx-background-color: #faf8f3;" +
            "-fx-text-fill: #375534;" +
            "-fx-background-radius: 25;" +
            "-fx-cursor: hand;" +
            "-fx-border-color: #66bb6a;" +
            "-fx-border-width: 3;" +
            "-fx-border-radius: 25;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 3);"
        );
        
        btnLogin.setOnMouseEntered(e -> btnLogin.setStyle(
            "-fx-background-color: #a5d6a7;" +
            "-fx-text-fill: #1b5e20;" +
            "-fx-background-radius: 25;" +
            "-fx-cursor: hand;" +
            "-fx-border-color: #375534;" +
            "-fx-border-width: 3;" +
            "-fx-border-radius: 25;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 15, 0, 0, 4);" +
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;"
        ));
        
        btnLogin.setOnMouseExited(e -> btnLogin.setStyle(
            "-fx-background-color: #faf8f3;" +
            "-fx-text-fill: #375534;" +
            "-fx-background-radius: 25;" +
            "-fx-cursor: hand;" +
            "-fx-border-color: #66bb6a;" +
            "-fx-border-width: 3;" +
            "-fx-border-radius: 25;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 3);" +
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;"
        ));

        Label lblMessage = new Label();
        lblMessage.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        lblMessage.setStyle("-fx-text-fill: #d32f2f; -fx-padding: 5;");
        lblMessage.setWrapText(true);
        lblMessage.setMaxWidth(350);
        lblMessage.setAlignment(Pos.CENTER);

        loginForm.getChildren().addAll(typeLabel, typeBox, usernameBox, passwordBox, btnLogin, lblMessage);

        // Login button action
        btnLogin.setOnAction(e -> {
            String username = txtUsername.getText();
            String password = txtPassword.getText();

            // Check fields based on user type
            if (userType.equals("admin")) {
                if (password.isEmpty()) {
                    lblMessage.setText("Please enter security code");
                    return;
                }
            } else {
                if (username.isEmpty() || password.isEmpty()) {
                    lblMessage.setText("Please fill in all fields");
                    return;
                }
            }

            // Simple authentication logic
            if (authenticate(username, password)) {
                if (userType.equals("client")) {
                    clientName = username;
                    clientPhone = password;
                    System.out.println("DEBUG LOGIN: Client connecté - Nom: " + clientName + ", Téléphone: " + clientPhone);
                }
                onLoginSuccess.run();
            } else {
                if (userType.equals("admin")) {
                    lblMessage.setText("Incorrect security code");
                } else {
                    lblMessage.setText("Incorrect name or phone number");
                }
            }
        });

        // Press Enter to login
        txtPassword.setOnAction(e -> btnLogin.fire());
        txtUsername.setOnAction(e -> btnLogin.fire());

        root.getChildren().addAll(welcomeBox, loginForm);
    }

    private boolean authenticate(String username, String password) {
        if (userType.equals("admin")) {
            // Admin: only security code needed
            return password.equals("27062005");
        } else {
            // Client: check in database if adherent exists with matching name and phone number
            if (username.isEmpty() || password.isEmpty()) {
                return false;
            }
            
            List<Adherent> adherents = adherentService.chercherParNom(username);
            
            // Check if any adherent has matching name and phone number (stored in numero field)
            for (Adherent a : adherents) {
                if (a.getNumero().equals(password)) {
                    return true;
                }
            }
            
            return false; // No matching adherent found
        }
    }

    public VBox getView() {
        return root;
    }

    public String getUserType() {
        return userType;
    }

    public String getClientName() {
        return clientName;
    }

    public String getClientPhone() {
        return clientPhone;
    }
}
