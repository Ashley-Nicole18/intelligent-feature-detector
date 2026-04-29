package algofinalproject;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class AppView {
    // canvases
    public Canvas originalCanvas;
    public Canvas resultCanvas;

    // controls
    public Slider thresholdSlider;
    public Slider depthSlider;
    public Button openFileButton;
    public Button runButton;
    public Button exportButton;
    public Button exportCropsButton;

    // metric labels
    public Label metricTime;
    public Label metricTotalNodes;
    public Label metricLeafNodes;
    public Label metricPixelsScanned;
    public Label metricReduction;
    public Label metricDepth;
    public Label classifierLabel;
    public Label classifierScore;

    private Spinner<Integer> topNSpinner;
    private Spinner<Integer> topPercentSpinner;
    public boolean lastExportByPercent = false; 
    private static final double MAX_CANVAS_SIZE = 600.0;

    public BorderPane root;

    public AppView() {
        buildUI();
    }

    private void buildUI() {
        root = new BorderPane();
        root.setPadding(new Insets(14));
        root.setStyle("-fx-background-color: #1a1a2e;");

        // Top Bar 
        Text title = new Text("Intelligent Feature Detector");
        title.setFont(Font.font("System", FontWeight.BOLD, 18));
        title.setFill(Color.WHITE);

        openFileButton = new Button("Open Image...");
        styleButton(openFileButton, "#16213e", "#0f3460");

        HBox topBar = new HBox(16, title, openFileButton);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(0, 0, 14, 0));
        root.setTop(topBar);

        // two canvases displayed side-by-side
        originalCanvas = new Canvas(400, 380);
        resultCanvas = new Canvas(400, 380);

        VBox leftBox = labeledCanvas("Original Image", originalCanvas);
        VBox rightBox = labeledCanvas("Quadtree + Sobel Result", resultCanvas);

        HBox canvasRow = new HBox(20, leftBox, rightBox);
        canvasRow.setAlignment(Pos.CENTER);
        
        StackPane centerHolder = new StackPane(canvasRow);
        centerHolder.setAlignment(Pos.CENTER);
        root.setCenter(centerHolder);

        // Bottom bar: controls + metricPixelsScanned
        VBox bottomArea = new VBox(14,
            buildControlsPanel(),
            buildMetricsPanel()
        );
        bottomArea.setPadding(new Insets(14, 0, 0, 0));
        root.setBottom(bottomArea);
    }

    // Helper: wraps a canvas with a title label above it 
    private VBox labeledCanvas(String title, Canvas canvas) {
        Label lbl = new Label(title);
        lbl.setStyle("-fx-text-fill: #a0a8c0; -fx-font-size: 12px;");

        StackPane canvasHolder = new StackPane(canvas);
        canvasHolder.setAlignment(Pos.CENTER);
        canvasHolder.setPrefSize(MAX_CANVAS_SIZE, MAX_CANVAS_SIZE);
        canvasHolder.setStyle(
            "-fx-background-color: #12122a;" +
            "-fx-background-radius: 6;" +
            "-fx-border-color: #0f3460;" +
            "-fx-border-radius: 6;" +
            "-fx-border-width: 1;"
        );
        VBox box = new VBox(8, lbl, canvasHolder);
        box.setAlignment(Pos.TOP_CENTER);
        return box;
    }

    private HBox buildControlsPanel() {
        thresholdSlider = new Slider(0.0001, 0.04, 0.002);
        thresholdSlider.setPrefWidth(260);
        thresholdSlider.setShowTickLabels(true);
        thresholdSlider.setMajorTickUnit(0.005);

        depthSlider = new Slider(1, 10, 6);
        depthSlider.setPrefWidth(200);
        depthSlider.setShowTickLabels(true);
        depthSlider.setMajorTickUnit(1);
        depthSlider.setSnapToTicks(true);

        runButton = new Button("▶  Run Analysis");
        runButton.setDefaultButton(true);
        styleButton(runButton, "#0f3460", "#533483");

        exportButton = new Button("Export ROIs");
        styleButton(exportButton, "#533483", "#6a3fa0");
        exportButton.setDisable(true);

        exportCropsButton = new Button("🖼 Export Crops");
        styleButton(exportCropsButton, "#1a6b3c", "#1D9E75");
        exportCropsButton.setDisable(true);

        Label threshLbl = styledLabel("Variance Threshold:");
        Label depthLbl = styledLabel("Max Depth:");
        Label threshVal = styledLabel("0.002");
        Label depthVal = styledLabel("6");

        // Keep the value labels updates as sliders move
        thresholdSlider.valueProperty().addListener((obs, old, newVal) ->
            threshVal.setText(String.format("%.4f", newVal.doubleValue()))
        );

        depthSlider.valueProperty().addListener((obs, old, newVal) ->
            depthVal.setText(String.valueOf(newVal.intValue()))
        );

        HBox controls = new HBox(16,
            threshLbl, thresholdSlider, threshVal,
            new Separator(javafx.geometry.Orientation.VERTICAL),
            depthLbl, depthSlider, depthVal,
            runButton,
            exportButton,
            exportCropsButton
        );
        controls.setAlignment(Pos.CENTER_LEFT);
        controls.setPadding(new Insets(10, 12, 10, 12));
        controls.setStyle("-fx-background-color: #16213e; -fx-background-radius: 8;");
        return controls;
    }

    private HBox buildMetricsPanel() {
        metricTime = bigMetricLabel("—");
        metricTotalNodes = bigMetricLabel("—");
        metricLeafNodes = bigMetricLabel("—");
        metricPixelsScanned = bigMetricLabel("—");
        metricReduction = bigMetricLabel("—");
        metricDepth = bigMetricLabel("—");

        classifierLabel = new Label("—");
        classifierLabel.setStyle(
            "-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #888;"
        );
        classifierScore = new Label("score: —");
        classifierScore.setStyle("-fx-text-fill: #6c7a96; -fx-font-size: 10px;");

        VBox classifierCard = new VBox(4,
            styledLabel("Image Complexity"),
            classifierLabel,
            classifierScore
        );

        classifierCard.setAlignment(javafx.geometry.Pos.CENTER);
        classifierCard.setPadding(new javafx.geometry.Insets(10, 20, 10, 20));
        classifierCard.setMinWidth(140);
        classifierCard.setStyle(
            "-fx-background-color: #16213e;" +
            "-fx-background-radius: 8;" +
            "-fx-border-color: #0f3460;" +
            "-fx-border-radius: 8;" +
            "-fx-border-width: 1.5;"
        );

        Separator sep = new Separator(javafx.geometry.Orientation.VERTICAL);
        sep.setPadding(new javafx.geometry.Insets(0, 6, 0, 6));

        HBox row = new HBox(10,
            classifierCard,
            sep,
            metricCard("Exec. Time", metricTime, "ms"),
            metricCard("Total Nodes", metricTotalNodes, "nodes"),
            metricCard("Leaf Nodes", metricLeafNodes, "regions"),
            metricCard("Pixels Scanned", metricPixelsScanned, "px"),
            metricCard("Reduction", metricReduction, "%"),
            metricCard("Max Depth Reached", metricDepth, "levels")
        );
        row.setAlignment(javafx.geometry.Pos.CENTER);
        return row;
    }

    private VBox metricCard(String title, Label valueLabel, String unit) {
        Label titleLbl = new Label(title);
        titleLbl.setStyle("-fx-text-fill: #6c7a96; -fx-font-size: 10px;");

        Label unitLbl = new Label(unit);
        unitLbl.setStyle("-fx-text-fill: #6c7a96; -fx-font-size: 10px;");

        VBox card = new VBox(2, titleLbl, valueLabel, unitLbl);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(10, 14, 10, 14));
        card.setMinWidth(100);
        card.setStyle(
            "-fx-background-color: #16213e;" +
            "-fx-background-radius: 8;" +
            "-fx-border-color: #0f3460;" +
            "-fx-border-radius: 8;" +
            "-fx-border-width: 1;"
        );
        return card;
    }

    private Label bigMetricLabel(String text) {
        Label lbl = new Label(text);
        lbl.setStyle("-fx-text-fill: #e94560; -fx-font-size: 20px; -fx-font-weight: bold;");
        return lbl;
    }

    private Label styledLabel(String text) {
        Label lbl = new Label(text);
        lbl.setStyle("-fx-text-fill: #a0a8c0; -fx-font-size: 12px;");
        return lbl;
    }

    // Dialog to select how many top regions to export
    public int showExportSelectionDialog(int totalLeaves) {
        javafx.scene.control.Dialog<Integer> dialog = new javafx.scene.control.Dialog<>();
        dialog.setTitle("Export Top Regions");
        dialog.setHeaderText("Select high-variance regions to export (Total leaves: " + totalLeaves + ")");

        // Mode selection: Top N or Top X%
        javafx.scene.control.RadioButton byCountRadio = new javafx.scene.control.RadioButton("Top N Regions");
        javafx.scene.control.RadioButton byPercentRadio = new javafx.scene.control.RadioButton("Top X% of Regions");
        ToggleGroup modeGroup = new ToggleGroup();
        byCountRadio.setToggleGroup(modeGroup);
        byPercentRadio.setToggleGroup(modeGroup);
        byCountRadio.setSelected(true);

        topNSpinner = new Spinner<>(1, totalLeaves, Math.min(50, totalLeaves));
        topPercentSpinner = new Spinner<>(1, 100, 25);

        // Show/hide spinners based on selection
        topNSpinner.setDisable(false);
        topPercentSpinner.setDisable(true);

        byCountRadio.setOnAction(e -> {
            topNSpinner.setDisable(false);
            topPercentSpinner.setDisable(true);
        });

        byPercentRadio.setOnAction(e -> {
            topNSpinner.setDisable(true);
            topPercentSpinner.setDisable(false);
        });

        VBox content = new VBox(12);
        content.setPadding(new Insets(10));
        content.getChildren().addAll(
            byCountRadio,
            topNSpinner,
            new Separator(),
            byPercentRadio,
            topPercentSpinner
        );

        javafx.scene.control.ButtonType okButton = javafx.scene.control.ButtonType.OK;
        javafx.scene.control.ButtonType cancelButton = javafx.scene.control.ButtonType.CANCEL;
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == okButton) {
                if (byCountRadio.isSelected()) {
                    lastExportByPercent = false;
                    return topNSpinner.getValue();
                } else {
                    lastExportByPercent = true;
                    int percent = topPercentSpinner.getValue();
                    return Math.max(1, (percent * totalLeaves) / 100);
                }
            }
            return -1;
        });

        java.util.Optional<Integer> result = dialog.showAndWait();
        return result.orElse(-1);
    }

    private void styleButton(Button btn, String bg, String hover) {
        btn.setStyle(
            "-fx-background-color: " + bg + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 12px;" +
            "-fx-background-radius: 6;" +
            "-fx-padding: 6 14 6 14;"
        );
        btn.setOnMouseEntered(e -> btn.setStyle(
            "-fx-background-color: " + hover + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 12px;" +
            "-fx-background-radius: 6;" +
            "-fx-padding: 6 14 6 14;"
        ));
        btn.setOnMouseExited(e -> btn.setStyle(
            "-fx-background-color: " + bg + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 12px;" +
            "-fx-background-radius: 6;" +
            "-fx-padding: 6 14 6 14;"
        ));
    }
}