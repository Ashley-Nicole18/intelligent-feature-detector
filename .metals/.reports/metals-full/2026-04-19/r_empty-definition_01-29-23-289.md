error id: file:///C:/Users/Ashley%20Nicole/Documents/SCHOOL/2nd_Year_2nd_Sem/Algorithm/Intelligent_Feature_Detector/intelligent-feature-detector/src/main/java/algofinalproject/AppView.java:algofinalproject/AppView#bigMetricLabel#
file:///C:/Users/Ashley%20Nicole/Documents/SCHOOL/2nd_Year_2nd_Sem/Algorithm/Intelligent_Feature_Detector/intelligent-feature-detector/src/main/java/algofinalproject/AppView.java
empty definition using pc, found symbol in pc: algofinalproject/AppView#bigMetricLabel#
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 4364
uri: file:///C:/Users/Ashley%20Nicole/Documents/SCHOOL/2nd_Year_2nd_Sem/Algorithm/Intelligent_Feature_Detector/intelligent-feature-detector/src/main/java/algofinalproject/AppView.java
text:
```scala
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

    // metric labels
    public Label metricTime;
    public Label metricTotalNodes;
    public Label metricLeafNodes;
    public Label metricPixelsScanned;
    public Label metricReduction;
    public Label metricDepth;

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

        VBox lefBox = labeledCanvas("Original Image", originalCanvas);
        VBox rightBox = labeledCanvas("Quadtree + Sobel Result", resultCanvas);

        HBox canvasRow = new HBox(20, lefBox, rightBox);
        canvasRow.setAlignment(Pos.CENTER);
        root.setCenter(canvasRow);

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
        VBox box = new VBox(6, lbl, canvas);
        box.setAlignment(Pos.TOP_CENTER);
        return box;
    }

    private HBox buildControlsPanel() {
        thresholdSlider = new Slider(0.001, 0.05, 0.009);
        thresholdSlider.setPrefWidth(260);
        thresholdSlider.setShowTickLabels(true);
        thresholdSlider.setMajorTickUnit(0.001);

        depthSlider = new Slider(1, 10, 6);
        depthSlider.setPrefWidth(200);
        depthSlider.setShowTickLabels(true);
        depthSlider.setMajorTickUnit(1);
        depthSlider.setSnapToTicks(true);

        runButton = new Button("▶  Run Analysis");
        runButton.setDefaultButton(true);
        styleButton(runButton, "#0f3460", "#533483");

        Label threshLbl = styledLabel("Variance Threshold:");
        Label depthLbl = styledLabel("Max Depth:");
        Label threshVal = styledLabel("0.009");
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
            runButton
        );
        controls.setAlignment(Pos.CENTER_LEFT);
        controls.setPadding(new Insets(10, 12, 10, 12));
        controls.setStyle("-fx-background-color: #16213e; -fx-background-radius: 8;");
        return controls;
    }

    private HBox buildMetricsPanel() {
        metricTime = @@bigMetricLabel("—");
        
    }
}
```


#### Short summary: 

empty definition using pc, found symbol in pc: algofinalproject/AppView#bigMetricLabel#