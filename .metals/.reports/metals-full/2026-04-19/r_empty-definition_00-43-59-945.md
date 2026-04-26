error id: file:///C:/Users/Ashley%20Nicole/Documents/SCHOOL/2nd_Year_2nd_Sem/Algorithm/Intelligent_Feature_Detector/intelligent-feature-detector/src/main/java/algofinalproject/AppView.java:_empty_/HBox#
file:///C:/Users/Ashley%20Nicole/Documents/SCHOOL/2nd_Year_2nd_Sem/Algorithm/Intelligent_Feature_Detector/intelligent-feature-detector/src/main/java/algofinalproject/AppView.java
empty definition using pc, found symbol in pc: _empty_/HBox#
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 1426
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
import javafx.scene.text.Text;;

public class AppView {
    public Canvas originalCanvas;
    public Canvas resultCanvas;

    // controls the quadtree threshold - maps to the threshold variable
    public Slider thresholdSlider;

    // controls max recursion depth - maps to maxDepth variable
    public Slider depthSlider;

    public Button openFileButton;
    public Button runButton;

    // Labels that show the current slider values
    public Label thresholdLabel;
    public Label depthLabel;

    // Metric Labels
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
        // BorderPane divides the window into: TOP, LEFT, CENTER, RIGHT, BOTTOM
        root = new BorderPane();
        root.setPadding(new Insets(14));
        root.setStyle("-fx-background-color: #1a1a2e;");

        // Top Bar: title + file Button
        @@HBox topBar = new HBox(16, title, openFileButton);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(0, 0, 14, 0));

        Text title = new Text("Intelligent Feature Detector");
        title.setFont(Font.font("System", FontWeight.BOLD, 18));
        title.setFill(Color.WHITE);

        openFileButton = new Button("Open Image...");
        styleButton(openFileButton, "#16213e", "#0f3460");

        topBar.getChildren().addAll(title, openFileButton);
        root.setTop(topBar);

        // Center: two canvases side by side
        originalCanvas = new Canvas(400, 400);
        resultCanvas = new Canvas(400, 400);

        // Each canvas gets a label above int
        VBox leftPanel = new VBox(4, new Label("Original"), originalCanvas);
        VBox rightPanel = new VBox(4, new Label("Quadtree + Sobel"), resultCanvas);
        leftPanel.setAlignment(Pos.TOP_CENTER);
        rightPanel.setAlignment(Pos.TOP_CENTER);

        HBox canvasArea = new HBox(16, leftPanel, rightPanel);
        canvasArea.setAlignment(Pos.CENTER);
        root.setCenter(canvasArea);

        // Bottom: sliders + run button
        thresholdSlider = new Slider(0.001, 0.05, 0.009);
        thresholdSlider.setShowTickLabels(true);
        thresholdSlider.setMajorTickUnit(0.01);

        depthSlider = new Slider(2, 10, 6);
        depthSlider.setShowTickLabels(true);
        depthSlider.setMajorTickUnit(2);
        depthSlider.setSnapToTicks(true);

        thresholdLabel = new Label("0.009");
        depthLabel = new Label("6");

        runButton = new Button("Run Analysis");
        runButton.setDefaultButton(true);

        // GridPane to set things in rows and columns
        GridPane controls = new GridPane();
        controls.setHgap(10);
        controls.setVgap(8);
        controls.setPadding(new Insets(12, 0, 0, 0));

        controls.add(new Label("Variance Threshold"), 0, 0);
        controls.add(thresholdSlider, 1, 0);
        controls.add(thresholdLabel, 2, 0);

        controls.add(new Label("Max Depth:"), 0, 1);
        controls.add(depthSlider, 1, 1);
        controls.add(depthLabel, 2, 1);

        controls.add(runButton, 1, 2);

        root.setBottom(controls);
    }
}

```


#### Short summary: 

empty definition using pc, found symbol in pc: _empty_/HBox#