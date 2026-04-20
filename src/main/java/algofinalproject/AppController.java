package algofinalproject;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.*;
import javafx.scene.paint.Color;
import java.io.File;


public class AppController {
    private final AppView view;
    private final Stage stage;

    private Image currentImage;
    private PixelReader reader;

    public AppController(AppView view, Stage stage) {
        this.view = view;
        this.stage = stage;

        attachListeners();
    }

    private void attachListeners() {
        // File button: open a file picker dialog
        view.openFileButton.setOnAction(e -> openFile());

        // Run button: run the full algorithm
        view.runButton.setOnAction(e -> runAnalysis());
    }

    private void  openFile() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select an Image");

        chooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.bmp")
        );

        File file = chooser.showOpenDialog(stage);
        if (file == null) return;

        currentImage = new Image(file.toURI().toString());
        reader = currentImage.getPixelReader();

        // Resize canvases to match the new image
        double w = currentImage.getWidth();
        double h = currentImage.getHeight();
        view.originalCanvas.setWidth(w);
        view.originalCanvas.setHeight(h);
        view.resultCanvas.setWidth(w);
        view.resultCanvas.setHeight(h);

        // Draw the original image on the left canvas immediately
        view.originalCanvas.getGraphicsContext2D().drawImage(currentImage, 0, 0);

        // Clear the result canvas
        view.resultCanvas.getGraphicsContext2D().clearRect(0, 0, w, h);
    }

    private void runAnalysis() {
        if (currentImage == null) {
            System.out.println("No image loaded yet. Open an image first");
            return;
        }

        double threshold = view.thresholdSlider.getValue();
        int maxDepth = (int) view.depthSlider.getValue();

        int w = (int) currentImage.getWidth();
        int h = (int) currentImage.getHeight();

        // Start timer
        long startTime = System.nanoTime();


        // Run algorithm
        QuadtreeNode root = new QuadtreeNode(0, 0, w, h, 0);
        root.subdivide(reader, threshold, maxDepth);

        WritableImage output = new WritableImage(w, h);
        PixelWriter writer = output.getPixelWriter();
        root.applySobleToLeaves(reader, writer);

        // Stop timer 
        long endTime = System.nanoTime();
        long elapsedMs = (endTime - startTime) / 1_000_000;

        // Collect metricPixelsScanned
        AnalysisResult result = new AnalysisResult();
        result.executionTimeMs = elapsedMs;
        result.totalNodes = root.countAllNodes();
        result.leafNodes = root.countLeafNodes();
        result.pixelsScanned = root.countScannedPixels();
        result.totalPixels = w * h;
        result.maxDepthReached = root.getMaxDepthReached();

        // Draw result on right canvas
        GraphicsContext gc = view.resultCanvas.getGraphicsContext2D();
        gc.drawImage(output, 0, 0);

        // Draw the quadtree grid on top
        drawQuadtreeOverlay(gc, root);

        // Update metric labels 
        updateMetrics(result);
    }

    private void drawQuadtreeOverlay(GraphicsContext gc, QuadtreeNode node) {
        if (node.isLeaf()) {
            // High-variance leaf = interesting region → red tint
            // Low-variance leaf = uniform region → subtle green tint
            if (node.getVariance() >= view.thresholdSlider.getValue()) {
                gc.setStroke(Color.rgb(255, 80, 80, 0.7));
            } else {
                gc.setStroke(Color.rgb(80, 220, 120, 0.3));
            }
            gc.setLineWidth(0.5);
            gc.strokeRect(node.getX(), node.getY(), node.getWidth(), node.getHeight());
            return;
        }
        // Not a leaf — recurse into children
        for (QuadtreeNode child : node.getChildren()) {
            if (child != null) drawQuadtreeOverlay(gc, child);
        }
    }

    // Push all five metrics into the view's labels
    private void updateMetrics(AnalysisResult r) {
        view.metricTime.setText(String.valueOf(r.executionTimeMs));
        view.metricTotalNodes.setText(String.format("%,d", r.totalNodes));
        view.metricLeafNodes.setText(String.format("%,d", r.leafNodes));
        view.metricPixelsScanned.setText(String.format("%,d", r.pixelsScanned));
        view.metricReduction.setText(String.format("%.1f", r.reductionPercent()));
        view.metricDepth.setText(String.valueOf(r.maxDepthReached));
    }
}
