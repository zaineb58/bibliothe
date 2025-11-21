import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.MainView;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Initialize main view (it creates all sub-views internally)
        MainView mainView = new MainView();

        Scene scene = new Scene(mainView.getView(), 900, 600);
        primaryStage.setTitle("Bibliothèque JavaFX");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
