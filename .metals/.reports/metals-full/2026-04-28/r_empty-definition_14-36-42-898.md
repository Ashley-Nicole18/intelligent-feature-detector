error id: file:///C:/Users/Ashley%20Nicole/Documents/SCHOOL/2nd_Year_2nd_Sem/Algorithm/Intelligent_Feature_Detector/intelligent-feature-detector/src/main/java/algofinalproject/AppController.java:_empty_/AppView#exportButton#
file:///C:/Users/Ashley%20Nicole/Documents/SCHOOL/2nd_Year_2nd_Sem/Algorithm/Intelligent_Feature_Detector/intelligent-feature-detector/src/main/java/algofinalproject/AppController.java
empty definition using pc, found symbol in pc: _empty_/AppView#exportButton#
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 1196
uri: file:///C:/Users/Ashley%20Nicole/Documents/SCHOOL/2nd_Year_2nd_Sem/Algorithm/Intelligent_Feature_Detector/intelligent-feature-detector/src/main/java/algofinalproject/AppController.java
text:
```scala
package algofinalproject;

import javafx.stage.FileChooser;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.*;
import javafx.scene.paint.Color;
import javafx.embed.swing.SwingFXUtils;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


public class AppController {
    private final AppView view;
    private final Stage stage;

    private Image currentImage;
    private PixelReader reader;
    private QuadtreeNode lastRoot;

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

        // Export button: save detected regions and metadata
        view.@@exportButton.setOnAction(e -> exportRegions());
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
        // keep reference for export operations
        lastRoot = root;

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

    // Collect leaves into list
    private void collectLeaves(QuadtreeNode node, List<QuadtreeNode> out) {
        if (node == null) return;
        if (node.isLeaf()) {
            out.add(node);
            return;
        }
        for (QuadtreeNode c : node.getChildren()) {
            if (c != null) collectLeaves(c, out);
        }
    }

    // Export high-variance leaf regions as PNGs and write regions.json manifest
    private void exportRegions() {
        if (currentImage == null || lastRoot == null) {
            System.out.println("No analysis available to export. Run analysis first.");
            return;
        }

        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Export Detected Regions");
        File dir = chooser.showDialog(stage);
        if (dir == null) return;

        List<QuadtreeNode> leaves = new ArrayList<>();
        collectLeaves(lastRoot, leaves);

        StringBuilder json = new StringBuilder();
        json.append("[\n");
        int exported = 0;
        double thresh = view.thresholdSlider.getValue();

        for (QuadtreeNode leaf : leaves) {
            if (leaf.getVariance() >= thresh) {
                int lx = leaf.getX();
                int ly = leaf.getY();
                int lw = Math.max(1, leaf.getWidth());
                int lh = Math.max(1, leaf.getHeight());

                WritableImage crop = new WritableImage(reader, lx, ly, lw, lh);
                BufferedImage bimg = SwingFXUtils.fromFXImage(crop, null);
                File outFile = new File(dir, String.format("region-%03d.png", ++exported));
                try {
                    ImageIO.write(bimg, "png", outFile);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                json.append(String.format("  { \"id\": %d, \"x\": %d, \"y\": %d, \"width\": %d, \"height\": %d }", exported, lx, ly, lw, lh));
                json.append("\n");
            }
        }

        json.append("]\n");

        Path manifest = Path.of(dir.getAbsolutePath(), "regions.json");
        try {
            Files.writeString(manifest, json.toString());
            System.out.println("Exported " + exported + " regions to " + dir.getAbsolutePath());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
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

```


#### Short summary: 

empty definition using pc, found symbol in pc: _empty_/AppView#exportButton#