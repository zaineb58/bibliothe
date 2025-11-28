import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Adherent;
import model.Livres;
import view.*;
import java.util.List;

public class Main extends Application {

    private Scene scene;
    private Stage primaryStage;
    private String userType;
    private String clientName;
    private String clientPhone;
    private Adherent currentAdherent;
    private LoginView loginView;
    private LivresView livresView;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        
        // Show login view first
        showLoginView();
        
        primaryStage.setTitle("The Knowledge Hub");
        primaryStage.show();
    }

    private void showLoginView() {
        loginView = new LoginView(() -> {
            userType = loginView.getUserType();
            // On successful login, show appropriate view
            if (userType.equals("admin")) {
                showAdminHome();
            } else {
                clientName = loginView.getClientName();
                clientPhone = loginView.getClientPhone();
                showClientView();
            }
        });
        
        scene = new Scene(loginView.getView(), 1400, 850);
        primaryStage.setScene(scene);
    }

    private void showAdminHome() {
        AdminHomeView adminHome = new AdminHomeView(
            () -> showLivresView(),
            () -> showAdherentsView()
        );
        scene = new Scene(addBackButton(adminHome.getView(), () -> showLoginView()), 1400, 850);
        primaryStage.setScene(scene);
    }

    private void showLivresView() {
        livresView = new LivresView();
        scene = new Scene(addBackButton(livresView.getView(), () -> showAdminHome()), 1400, 850);
        primaryStage.setScene(scene);
    }

    @SuppressWarnings("unused")
    private void showAddLivreView() {
        AddLivreView addView = new AddLivreView(() -> showLivresView());
        scene = new Scene(addBackButton(addView.getView(), () -> showLivresView()), 1400, 850);
        primaryStage.setScene(scene);
    }

    @SuppressWarnings("unused")
    private void showLivreDetail(Livres livre) {
        LivreDetailView detailView = new LivreDetailView(livre, () -> showLivresView());
        scene = new Scene(addBackButton(detailView.getView(), () -> showLivresView()), 1400, 850);
        primaryStage.setScene(scene);
    }

    private void showAdherentsView() {
        AdherentView adherentView = new AdherentView();
        scene = new Scene(addBackButton(adherentView.getView(), () -> showAdminHome()), 1400, 850);
        primaryStage.setScene(scene);
    }

    @SuppressWarnings("unused")
    private void showEmpruntsView() {
        EmpruntView empruntView = new EmpruntView();
        scene = new Scene(addBackButton(empruntView.getView(), () -> showAdminHome()), 1400, 850);
        primaryStage.setScene(scene);
    }

    @SuppressWarnings("unused")
    private void showAddAdherentView() {
        AddAdherentView addView = new AddAdherentView(() -> showAdherentsView());
        scene = new Scene(addBackButton(addView.getView(), () -> showAdherentsView()), 1400, 850);
        primaryStage.setScene(scene);
    }

    @SuppressWarnings("unused")
    private void showAdherentDetail(Adherent adherent) {
        AdherentDetailView detailView = new AdherentDetailView(adherent, () -> showAdherentsView());
        scene = new Scene(addBackButton(detailView.getView(), () -> showAdherentsView()), 1400, 850);
        primaryStage.setScene(scene);
    }

    private javafx.scene.layout.StackPane addBackButton(javafx.scene.layout.Region content, Runnable backAction) {
        javafx.scene.control.Button btnBack = new javafx.scene.control.Button("← Back");
        btnBack.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-text-fill: #f5f5dc;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 10 20;" +
            "-fx-cursor: hand;"
        );
        btnBack.setOnAction(e -> backAction.run());

        javafx.scene.layout.StackPane layout = new javafx.scene.layout.StackPane();
        layout.getChildren().addAll(content, btnBack);
        javafx.scene.layout.StackPane.setAlignment(btnBack, javafx.geometry.Pos.TOP_LEFT);
        javafx.scene.layout.StackPane.setMargin(btnBack, new javafx.geometry.Insets(5, 10, 10, 10));

        return layout;
    }

    private void showClientView() {
        // Récupérer l'adhérent connecté depuis la base de données par nom
        services.AdherentService adherentService = new services.AdherentService();
        List<model.Adherent> adherents = adherentService.chercherParNom(clientName);
        
        if (!adherents.isEmpty()) {
            currentAdherent = adherents.get(0); // Prendre le premier résultat
        } else {
            System.err.println("ERROR: Member not found with name: " + clientName);
        }
        
        ClientLivresView clientView = new ClientLivresView(clientName, clientPhone);
        clientView.setOnLivreClick(livre -> showClientLivreDetail(livre));
        clientView.setOnMesInfosClick(() -> showClientInfo());
        scene = new Scene(addBackButton(clientView.getView(), () -> showLoginView()), 1400, 850);
        primaryStage.setScene(scene);
    }

    private void showClientLivreDetail(Livres livre) {
        ClientLivreDetailView detailView = new ClientLivreDetailView(livre, clientName, clientPhone, () -> showClientView());
        scene = new Scene(addBackButton(detailView.getView(), () -> showClientView()), 1400, 850);
        primaryStage.setScene(scene);
    }

    private void showClientInfo() {
        if (currentAdherent == null) {
            // Si l'adhérent n'est pas encore chargé, le récupérer par nom
            services.AdherentService adherentService = new services.AdherentService();
            List<Adherent> adherents = adherentService.chercherParNom(clientName);
            
            if (!adherents.isEmpty()) {
                currentAdherent = adherents.get(0);
            } else {
                System.err.println("ERROR: Member not found with name: " + clientName);
                // Retourner à la vue client si l'adhérent n'est pas trouvé
                showClientView();
                return;
            }
        }
        
        ClientInfoView infoView = new ClientInfoView(currentAdherent);
        scene = new Scene(addBackButton(infoView.getView(), () -> showClientView()), 1400, 850);
        primaryStage.setScene(scene);
    }

    public static void main(String[] args) {
        // Set system properties BEFORE JavaFX starts for better text rendering
        System.setProperty("prism.text", "t2k");
        System.setProperty("prism.lcdtext", "true");
        System.setProperty("prism.subpixeltext", "true");
        System.setProperty("awt.useSystemAAFontSettings", "lcd");
        System.setProperty("swing.aatext", "true");
        
        launch(args);
    }
}
