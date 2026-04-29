package algofinalproject;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class Main extends Application {
    
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Build UI layout
        AppView view = new AppView();

        // Connect the logic 
        AppController controller = new AppController(view, primaryStage);

        // Hand the layout to javafx
        Scene scene = new Scene(view.root, 980, 720);
        primaryStage.setTitle("Intelligent Feature Detector");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setFullScreen(true);
    }
}

