package algofinalproject;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.layout.StackPane;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.Scene;

public class Main extends Application {
    private Image image;
    private PixelReader reader;

    double threshold = 0.01;
    int maxDepth = 6;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        run(primaryStage);
    }

    public void run(Stage primaryStage) {
        image = new Image(getClass().getResourceAsStream("/sample.jpg"));
        image = new Image(getClass().getResourceAsStream("/sample.jpg"));

    if (image == null || image.isError()) {
        System.out.println("❌ ERROR: Image not found! Is it in src/main/resources/sample.jpg?");
        return; 
    }

    System.out.println("✅ Image loaded! Dimensions: " + image.getWidth() + "x" + image.getHeight());
        reader = image.getPixelReader();

        QuadtreeNode root = new QuadtreeNode(
        0, 
        0, 
        (int) image.getWidth(), 
        (int) image.getHeight(), 
        0
    );
        root.subdivide(reader, threshold, maxDepth);

        Canvas canvas = new Canvas(image.getWidth(), image.getHeight());
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.drawImage(image, 0, 0);
        root.traverse(gc);

        StackPane rootPane = new StackPane();
        rootPane.getChildren().add(canvas);

        Scene scene = new Scene(rootPane);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Quadtree Test");
        primaryStage.show();

        
    }
}
