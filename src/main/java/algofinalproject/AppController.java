package algofinalproject;

import javafx.stage.FileChooser;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.*;
import javafx.scene.paint.Color;
import java.io.File;
import java.util.List;


public class AppController {
    private final AppView view;
    private final Stage stage;
    private double displayScale = 1.0;
    private static final double MAX_CANVAS_SIZE = 800.0;

    private Image currentImage;
    private PixelReader reader;
    private List<QuadtreeNode> allLeavesSorted;
    private AnalysisResult lastResult;
    private String lastImageName = "image";

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

        view.exportButton.setOnAction(e -> exportROIs());

        view.exportCropsButton.setOnAction(e -> exportCrops());
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
        double imgW = currentImage.getWidth();
        double imgH = currentImage.getHeight();

        displayScale = computeScale(imgW, imgH);

        double displayW = imgW * displayScale;
        double displayH = imgH * displayScale;
        double offsetX = (MAX_CANVAS_SIZE - displayW) / 2.0;
        double offsetY = (MAX_CANVAS_SIZE - displayH) / 2.0;
        
        view.originalCanvas.setWidth(MAX_CANVAS_SIZE);
        view.originalCanvas.setHeight(MAX_CANVAS_SIZE);
        view.resultCanvas.setWidth(MAX_CANVAS_SIZE);
        view.resultCanvas.setHeight(MAX_CANVAS_SIZE);

        // Clear first, then draw center
        GraphicsContext gcOrig = view.originalCanvas.getGraphicsContext2D();
        gcOrig.clearRect(0, 0, MAX_CANVAS_SIZE, MAX_CANVAS_SIZE);
        gcOrig.drawImage(currentImage, offsetX, offsetY, displayW, displayH);

        // Clear the result canvas
        view.resultCanvas.getGraphicsContext2D().clearRect(0, 0, MAX_CANVAS_SIZE, MAX_CANVAS_SIZE);
        lastImageName = file.getName();
    }

    private double computeScale(double imageWidth, double imageHeight) {
        double scaleW = MAX_CANVAS_SIZE / imageWidth;
        double scaleH = MAX_CANVAS_SIZE / imageHeight;
        return Math.min(scaleW, scaleH);
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

        // Get all leaves sorted by variance (for export)
        allLeavesSorted = root.getAllLeavesSortedByVariance();
        System.out.println("Found " + allLeavesSorted.size() + " total leaves (highest variance: " 
            + (allLeavesSorted.isEmpty() ? "N/A" : String.format("%.6f", allLeavesSorted.get(0).getVariance())) + ")");
        view.exportButton.setDisable(false);
        view.exportCropsButton.setDisable(false);

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
        result.maxDepth = maxDepth;

        lastResult = result;

        double displayW = currentImage.getWidth() * displayScale;
        double displayH = currentImage.getHeight() * displayScale;
        double offsetX = (MAX_CANVAS_SIZE - displayW) / 2.0;
        double offsetY = (MAX_CANVAS_SIZE - displayH) / 2.0;

        // Draw result on right canvas
        GraphicsContext gc = view.resultCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, MAX_CANVAS_SIZE, MAX_CANVAS_SIZE);
        gc.drawImage(output, offsetX, offsetY, displayW, displayH);

        // Draw the quadtree grid on top
        drawQuadtreeOverlay(gc, root, displayScale, offsetX, offsetY);

        // Update metric labels 
        updateMetrics(result);
    }

    private void drawQuadtreeOverlay(GraphicsContext gc, QuadtreeNode node, double scale, double offsetX, double offsetY) {
        if (node.isLeaf()) {
            // High-variance leaf = interesting region → red tint
            // Low-variance leaf = uniform region → subtle green tint
            if (node.getVariance() >= view.thresholdSlider.getValue()) {
                gc.setStroke(Color.rgb(255, 80, 80, 0.7));
            } else {
                gc.setStroke(Color.rgb(80, 220, 120, 0.3));
            }
            gc.setLineWidth(0.5);
            gc.strokeRect(
                offsetX + node.getX() * scale,
                offsetY + node.getY() * scale,
                node.getWidth() * scale,
                node.getHeight() * scale
            );
            return;
        }
        // Not a leaf — recurse into children
        for (QuadtreeNode child : node.getChildren()) {
            if (child != null) drawQuadtreeOverlay(gc, child, scale, offsetX, offsetY);
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

        String label = r.complexityLabel();
        double score = r.complexityScore();
        String color = r.complexityColor();

        view.classifierLabel.setText(label);
        view.classifierLabel.setStyle(
            "-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: " + color + ";"
        );
        view.classifierScore.setText(String.format("score: %.2f", score));
    }

    private void exportROIs() {
        if (allLeavesSorted == null || allLeavesSorted.isEmpty()) {
            System.out.println("No regions to export. Run analysis first.");
            return;
        }

        // Show dialog to select top-N or top-X%
        int topN = view.showExportSelectionDialog(allLeavesSorted.size());
        if (topN <= 0) {
            System.out.println("Export cancelled.");
            return;
        }

        // Export top-N regions
        List<QuadtreeNode> selectedROIs = allLeavesSorted.subList(0, Math.min(topN, allLeavesSorted.size()));

        FileChooser chooser = new FileChooser();
        String modeLabel = view.lastExportByPercent ? "top" + (topN * 100 / allLeavesSorted.size()) + "pct" : "top" + topN;
        chooser.setTitle("Export " + selectedROIs.size() + " High-Variance Regions (" + modeLabel + ")");
        chooser.setInitialFileName("rois_" + modeLabel);

        chooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("JSON File", "*.json"),
            new FileChooser.ExtensionFilter("CSV File", "*.csv")
        );

        File file = chooser.showSaveDialog(stage);
        if (file == null) return;

        String name = file.getName().toLowerCase();
        String label = (lastResult != null) ? lastResult.complexityLabel() : "UNKNOWN";
        double score = (lastResult != null) ? lastResult.complexityScore() : 0.0;

        if (name.endsWith(".json")) {
            ROIExporter.toJSON(selectedROIs, lastImageName, label, score, file);
            System.out.println("Exported " + selectedROIs.size() + " high-variance regions to " + file.getAbsolutePath());
        } else if (name.endsWith(".csv")) {
            ROIExporter.toCSV(selectedROIs, lastImageName, label, file);
            System.out.println("Exported " + selectedROIs.size() + " high-variance regions to " + file.getAbsolutePath());
        }
    }

    private void exportCrops() {
        if (allLeavesSorted == null || allLeavesSorted.isEmpty()) {
            System.out.println("No regions to export. Run analysis first.");
            return;
        }

        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Select Output Folder for Cropped Regions");

        File folder = chooser.showDialog(stage);
        if (folder == null) return;

        ROIExporter.toCroppedPNGs(allLeavesSorted, currentImage, folder);
    }
}
