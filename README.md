# Intelligent Feature Detector
### Quadtree-Powered Image Analysis & ROI Extraction

> A Java + JavaFX application that uses a **Quadtree algorithm** to perform selective attention on images — identifying detail-rich regions, classifying image complexity, and exporting structured data for downstream processing.

---

## Table of Contents

1. [What This Application Does](#what-this-application-does)
2. [Why Quadtree?](#why-quadtree)
3. [How It Works — Algorithm Walkthrough](#how-it-works--algorithm-walkthrough)
4. [File Architecture](#file-architecture)
5. [Features](#features)
6. [Performance Metrics Explained](#performance-metrics-explained)
7. [Complexity Classifier](#complexity-classifier)
8. [Export Formats](#export-formats)
9. [Getting Started](#getting-started)
10. [Usage Guide](#usage-guide)
11. [Tech Stack](#tech-stack)

---

## What This Application Does

The Intelligent Feature Detector solves a real problem in image processing: **most of an image is boring.** A photo of a person against a white wall is 80% white wall. Running expensive algorithms (like edge detection) on every pixel wastes time on pixels that contain no useful information.

This application uses a **Quadtree** to find only the interesting parts first, then applies heavy processing exclusively to those regions. The result:

- **60–90% fewer pixels** passed to edge detection algorithms
- A **complexity classification** (SIMPLE / MODERATE / COMPLEX) for the entire image
- **Exportable bounding boxes** (JSON/CSV) describing every region of interest
- **Auto-cropped PNG files** of each detail-rich region, ready for downstream use

This mimics how the human eye works — it focuses attention on points of interest rather than processing the entire visual field at maximum resolution.

---

## Why Quadtree?

Traditional image scanning checks every pixel regardless of content — that is O(N) work for N pixels, even if 90% of those pixels are a blank sky.

A Quadtree applies **Selective Attention**:

```
If a region is UNIFORM (low variance) → skip it entirely
If a region is INTERESTING (high variance) → subdivide into 4 smaller regions
Repeat recursively until regions are too small or max depth is reached
```

This turns an O(N) brute-force scan into an O(log N) hierarchical approach for locating feature clusters. Only the smallest, most detail-rich leaf nodes ever touch the Sobel edge detector.

---

## How It Works — Algorithm Walkthrough

### Step 1 — Load Image
The user opens any JPG, PNG, JPEG, or BMP file. JavaFX's `PixelReader` provides per-pixel RGBA access to the image data.

### Step 2 — Compute Variance
`VarianceCalculator.compute()` scans all pixels in a rectangular region and computes the **brightness variance** — a statistical measure of how much pixel values differ from the mean.

```
mean = sum of all pixel brightness values / pixel count
variance = sum of (brightness - mean)² / pixel count
```

High variance = lots of different pixel values = detail, edges, texture.
Low variance = similar pixel values = uniform background, flat color.

### Step 3 — Threshold Check
The variance is compared to a user-controlled threshold:

- `variance < threshold` → region is **UNIFORM** → this becomes a leaf node, no further subdivision
- `variance >= threshold` → region is **INTERESTING** → subdivide

### Step 4 — Recursive Subdivision
The node splits into 4 child `QuadtreeNode` objects covering the NW, NE, SW, and SE quadrants. Each child independently calls `subdivide()` on itself. Recursion stops when:
- The node reaches `maxDepth`
- The node's width or height is 1 pixel
- The node's variance is below the threshold

### Step 5 — Apply Sobel Edge Detection
`applySobelToLeaves()` traverses the completed tree. At every leaf node, `SobelOperator.apply()` runs the Sobel X and Y convolution kernels across that region's pixels, producing edge magnitude values that highlight where intensity changes sharply.

### Step 6 — Collect & Export ROIs
`getAllLeavesSortedByVariance()` gathers all leaf nodes and sorts them by variance descending. The user selects how many to export (Top N or Top X%), then `ROIExporter` writes the output files.

---

## File Architecture

The project follows the **MVC (Model-View-Controller)** pattern, separating UI, logic, and algorithm concerns into distinct files.

```
algofinalproject/
├── Main.java                  # Entry point
├── AppView.java               # UI Layout (View)
├── AppController.java         # Event logic (Controller)
├── AnalysisResult.java        # Metrics data container (Model)
├── QuadtreeNode.java          # Core algorithm
├── VarianceCalculator.java    # Variance computation tool
├── SobelOperator.java         # Edge detection tool
└── ROIExporter.java           # Export engine
```

### `Main.java` — Entry Point
**Role:** Starts the JavaFX application.

Creates an `AppView` instance (the layout) and an `AppController` instance (the logic), then hands `AppView.root` to JavaFX as the scene. Contains no algorithm or UI construction logic — it is intentionally kept to ~10 lines so the separation of concerns is clear.

```java
AppView view = new AppView();
AppController controller = new AppController(view, primaryStage);
Scene scene = new Scene(view.root, 1280, 800);
```

---

### `AppView.java` — UI Layout
**Role:** Builds and owns every visual element in the application.

Constructs the full `BorderPane` layout containing:
- **Top bar** — title and Open Image button
- **Center** — two side-by-side `Canvas` elements (original + result), each centered in a fixed `StackPane` container so images of any size display cleanly
- **Bottom** — controls panel (threshold slider, depth slider, Run/Export buttons) and metrics panel (6 metric cards + complexity classifier card)

`AppView` contains no logic — it only builds structure. The controller accesses its public fields (sliders, canvases, labels, buttons) to read values and update the display.

Key design: `MAX_CANVAS_SIZE = 600.0` — all images scale to fit this boundary, both up and down, so the display is consistent regardless of source image dimensions.

---

### `AppController.java` — Logic / Brain
**Role:** Wires all events to actions and orchestrates the full analysis pipeline.

Responsibilities:
- `openFile()` — launches a `FileChooser`, loads the image, computes display scale, resizes canvases, draws original image
- `runAnalysis()` — reads slider values, builds and subdivides the quadtree, runs Sobel, times execution, fills `AnalysisResult`, draws the result canvas + overlay, updates all metric labels
- `drawQuadtreeOverlay()` — recursively draws the quadtree grid on the result canvas: red tint for high-variance leaves, green tint for uniform leaves, scaled to match the display resolution
- `exportROIs()` — shows the export selection dialog, takes the top-N or top-X% of leaves by variance, opens a `FileChooser` for JSON or CSV output
- `exportCrops()` — opens a `DirectoryChooser`, calls `ROIExporter.toCroppedPNGs()`

Fields `displayScale`, `lastROIs`, `lastResult`, and `lastImageName` persist between method calls to avoid redundant recomputation.

---

### `AnalysisResult.java` — Data Container
**Role:** A plain data object (POJO) that holds all metrics from one analysis run.

Fields:
| Field | Type | Description |
|---|---|---|
| `executionTimeMs` | `long` | Wall-clock time for subdivide + Sobel in milliseconds |
| `totalNodes` | `int` | All quadtree nodes created (internal + leaf) |
| `leafNodes` | `int` | Leaf nodes only — where Sobel actually ran |
| `pixelsScanned` | `int` | Total pixel area covered by leaf nodes |
| `totalPixels` | `int` | Full image pixel count (width × height) |
| `maxDepthReached` | `int` | Deepest recursion level reached |
| `maxDepth` | `int` | The slider value used for this run |

Methods:
- `reductionPercent()` — `(1 - pixelsScanned / totalPixels) × 100`
- `complexityScore()` — weighted formula combining leaf ratio, scan factor, depth factor
- `complexityLabel()` — maps score to SIMPLE / MODERATE / COMPLEX
- `complexityColor()` — returns a hex color for UI display

---

### `QuadtreeNode.java` — Algorithm Core
**Role:** The recursive tree node that implements the quadtree algorithm.

Each node stores its spatial region (`x, y, width, height`), its `depth`, its computed `variance`, and an array of 4 children (null if leaf).

Key methods:

| Method | Description |
|---|---|
| `subdivide(reader, threshold, maxDepth)` | Recursively builds the quadtree. Computes variance, returns if uniform or at depth limit, otherwise creates 4 children |
| `traverse(gc)` | Walks the tree, drawing each leaf's region on the canvas |
| `applySobelToLeaves(reader, writer)` | Runs `SobelOperator` on every leaf node |
| `getAllLeavesSortedByVariance()` | Collects all leaves into a `List` sorted by variance descending — the input to ROI export |
| `countAllNodes()` | Recursive count of every node in the tree |
| `countLeafNodes()` | Recursive count of leaf nodes only |
| `countScannedPixels()` | Sum of `width × height` for all leaf nodes |
| `getMaxDepthReached()` | Deepest `depth` value found anywhere in the tree |

The **accumulator pattern** is used throughout: a `List` is passed down the recursion and each qualifying node adds itself with `result.add(this)`, so the caller receives a populated list after one top-level call.

---

### `VarianceCalculator.java` — Variance Computation
**Role:** A stateless utility that computes pixel brightness variance for any rectangular region.

Algorithm:
1. Iterate all pixels in the region, reading RGB values via `PixelReader.getColor()`
2. Convert to brightness: `(red + green + blue) / 3.0`
3. Compute mean brightness across all pixels
4. Compute variance: `Σ(brightness - mean)² / pixelCount`

Returns a `double` in the range 0.0–~0.04 for typical photographic images. This value is what the threshold slider is calibrated against.

Called once per node during `subdivide()` — the result is stored in `QuadtreeNode.variance` for later use in export and overlay rendering.

---

### `SobelOperator.java` — Edge Detection
**Role:** Applies the Sobel edge detection algorithm to a single `QuadtreeNode` region.

The Sobel operator uses two 3×3 convolution kernels:

```
Sobel X (vertical edges):    Sobel Y (horizontal edges):
  -1  0  +1                    -1  -2  -1
  -2  0  +2                     0   0   0
  -1  0  +1                    +1  +2  +1
```

Implementation:
1. Pre-compute a 2D grayscale buffer for the node's pixel region (avoids repeated `PixelReader` calls)
2. For each interior pixel (avoiding the 1-pixel border), apply both kernels
3. Compute edge magnitude: `√(Gx² + Gy²)`, clamped to 255
4. Write the magnitude as a gray pixel to the `WritableImage` output

Runs only on leaf nodes with high variance — that is the core efficiency claim. Uniform leaf nodes never call this method.

---

### `ROIExporter.java` — Export Engine
**Role:** Writes the analysis results to disk in three formats.

**`toJSON(rois, imageName, complexityLabel, complexityScore, file)`**
Builds a JSON string using `StringBuilder` and writes it with `FileWriter`. Output structure:
```json
{
  "imageName": "sample.jpg",
  "complexityLabel": "COMPLEX",
  "complexityScore": 0.7821,
  "totalROIs": 47,
  "rois": [
    { "id": 0, "x": 120, "y": 45, "width": 16, "height": 16, "variance": 0.031200, "depth": 7 },
    ...
  ]
}
```

**`toCSV(rois, imageName, complexityLabel, file)`**
Writes a header row followed by one data row per ROI. Opens directly in Excel or Google Sheets. Columns: `id, imageName, complexityLabel, x, y, width, height, area, variance, depth`.

**`toCroppedPNGs(rois, source, outputFolder)`**
For each ROI (minimum 4×4 pixels), copies the source image pixels into a new `WritableImage` using the ROI's `x, y, width, height` as the crop window. Saves each crop as a PNG using `ImageIO.write(SwingFXUtils.fromFXImage(...))`.

Filename convention: `roi_0001_x120_y45_16x16.png` — encodes position and size so downstream systems can reconstruct spatial context without reading the file contents.

---

## Features

### Side-by-Side View
The original image appears on the left canvas. The right canvas shows the Sobel edge detection result overlaid with the quadtree grid — red borders on high-variance (interesting) leaf regions, green borders on uniform regions.

### Adaptive Image Scaling
Images of any resolution are scaled to fit the `MAX_CANVAS_SIZE` display container — scaled down if too large, scaled up if too small. The algorithm always runs at full resolution; only the display is scaled. The quadtree overlay is offset-corrected to align precisely with the displayed image.

### Live Slider Controls
- **Variance Threshold** (0.0001 – 0.04): Lower values = more regions classified as interesting = finer subdivision. Higher values = fewer interesting regions = coarser, faster analysis.
- **Max Depth** (1 – 10): Maximum recursion depth. Higher depth = smaller minimum region size = more precise localization, longer runtime.

### Export Selection Dialog
Before exporting, the user chooses either **Top N regions** (exact count) or **Top X%** (percentage of all leaves), both ranked by variance descending. This lets the user control the signal-to-noise ratio of the export.

---

## Performance Metrics Explained

After each analysis run, 6 metrics are displayed:

| Metric | What it measures |
|---|---|
| **Exec. Time (ms)** | Wall-clock duration of `subdivide()` + `applySobelToLeaves()` combined |
| **Total Nodes** | Every `QuadtreeNode` created — measures tree size |
| **Leaf Nodes** | Nodes with no children — the regions Sobel actually processed |
| **Pixels Scanned** | Total area (px²) of all leaf nodes — how much data Sobel touched |
| **Reduction %** | `(1 - Pixels Scanned / Total Pixels) × 100` — the efficiency gain |
| **Max Depth Reached** | The deepest level the tree reached in this run |

A typical complex image achieves **70–85% pixel reduction**, meaning Sobel runs on less than 30% of the image's pixels while still detecting all significant edges.

---

## Complexity Classifier

After analysis, the image is classified into one of three tiers using a weighted formula:

```
score = 0.4 × (leafNodes / totalNodes)
      + 0.4 × (1 − reductionPercent / 100)
      + 0.2 × (depthReached / maxDepth)
```

| Factor | Weight | What it captures |
|---|---|---|
| Leaf ratio | 40% | How fractured the tree is — many leaves = complex image |
| Scan factor | 40% | Inverse of reduction — low reduction = lots of interesting area |
| Depth factor | 20% | How deep the tree needed to go to resolve detail |

| Score | Label | Color |
|---|---|---|
| < 0.35 | **SIMPLE** | Green |
| 0.35 – 0.65 | **MODERATE** | Amber |
| ≥ 0.65 | **COMPLEX** | Red |

---

## Export Formats

### JSON — For Downstream Programs
Machine-readable structured data. Any language (Python, JavaScript, Java, R) can parse it directly. Use cases:
- Feed bounding boxes to an OCR engine
- Store ROI metadata in a database
- Input to a machine learning pipeline for region classification
- API response in a web service built on top of this system

### CSV — For Human Analysis
Opens directly in Excel or Google Sheets. Use cases:
- Sort ROIs by variance to find the most detail-rich regions
- Filter by size to ignore tiny noise artifacts
- Chart variance distribution across an image
- Share results with non-technical stakeholders

### Cropped PNG Files — For Direct Processing
Actual image patches, one file per ROI. Use cases:
- Feed directly to an OCR engine (no coordinate math needed)
- Input to an image classifier (e.g., "is this a defect?")
- Manual review by a human inspector
- Training data for a machine learning model

The filename `roi_0001_x120_y45_16x16.png` encodes the ROI's position (`x=120, y=45`) and dimensions (`16×16 px`) so spatial context is preserved without reading the file.

---

## Getting Started

### Prerequisites

- Java 17 or higher
- JavaFX 17 (must match your Java version)
- Maven 3.8+

### Maven Dependencies

Ensure your `pom.xml` includes:

```xml
<dependencies>
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-controls</artifactId>
        <version>17.0.6</version>
    </dependency>
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-fxml</artifactId>
        <version>17.0.6</version>
    </dependency>
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-swing</artifactId>
        <version>17.0.6</version>
    </dependency>
</dependencies>
```

### Build & Run

```bash
# Clone or open in your IDE
mvn clean javafx:run
```

Or run `Main.java` directly from IntelliJ IDEA / Eclipse with JavaFX configured on the module path.

---

## Usage Guide

### 1. Open an Image
Click **Open Image...** and select any JPG, PNG, JPEG, or BMP file. The original image appears on the left canvas, scaled to fit the display area.

### 2. Adjust Parameters
- **Variance Threshold** — Start at `0.002`. Lower the value to capture more regions; raise it to focus only on the highest-detail areas.
- **Max Depth** — Start at `6`. Increase for finer-grained subdivision on high-resolution images.

### 3. Run Analysis
Click **▶ Run Analysis**. The right canvas updates with the Sobel edge detection result and quadtree grid overlay. Metrics fill in below both canvases.

### 4. Read the Metrics
Check the complexity label (SIMPLE / MODERATE / COMPLEX) and reduction percentage. A high reduction % means the quadtree successfully skipped large uniform areas.

### 5. Export ROIs
Click **Export ROIs**. Choose Top N or Top X% in the dialog, then save as `.json` or `.csv`.

### 6. Export Crops
Click **🖼 Export Crops** and select a folder. One PNG file per ROI is saved to that folder, named with position and size information.

---

## Tech Stack

| Component | Technology |
|---|---|
| Language | Java 17 |
| UI Framework | JavaFX |
| Image I/O | JavaFX WritableImage + SwingFXUtils + ImageIO |
| Build Tool | Maven |
| Algorithm | Quadtree (custom implementation) |
| Edge Detection | Sobel operator (custom implementation) |
| Export Formats | JSON (manual StringBuilder), CSV (FileWriter), PNG (ImageIO) |

---

*Algorithms Final Project — Quadtree: Intelligent Feature Detector*