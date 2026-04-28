error id: file:///C:/Users/Ashley%20Nicole/Documents/SCHOOL/2nd_Year_2nd_Sem/Algorithm/Intelligent_Feature_Detector/intelligent-feature-detector/src/main/java/algofinalproject/QuadtreeNode.java:_empty_/VarianceCalculator#
file:///C:/Users/Ashley%20Nicole/Documents/SCHOOL/2nd_Year_2nd_Sem/Algorithm/Intelligent_Feature_Detector/intelligent-feature-detector/src/main/java/algofinalproject/QuadtreeNode.java
empty definition using pc, found symbol in pc: _empty_/VarianceCalculator#
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 1092
uri: file:///C:/Users/Ashley%20Nicole/Documents/SCHOOL/2nd_Year_2nd_Sem/Algorithm/Intelligent_Feature_Detector/intelligent-feature-detector/src/main/java/algofinalproject/QuadtreeNode.java
text:
```scala
package algofinalproject;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;

public class QuadtreeNode {

    // Spatial info
    private int x;
    private int y;
    private int width;
    private int height;

    // Tree info
    private QuadtreeNode[] children;
    private int depth;

    // Analysis
    private double variance;

    public QuadtreeNode(int x, int y, int width, int height, int depth) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.children = null;
    }

    // return a boolean value depending whether a node is a leaf node or not
    public boolean isLeaf() {
        return children == null;
    }

// accepts a pixel reader type, threshold, and max depth to decide whether a node can be subdivided or when to stop subdividing
public void subdivide(PixelReader reader, double threshold, int maxDepth) {

    if (depth >= maxDepth) return;
    if (width <= 1 || height <= 1) return;

    variance = @@VarianceCalculator.compute(reader, x, y, width, height);

    if (variance < threshold) return;

    children = new QuadtreeNode[4];

    int halfWidth = width / 2;
    int halfHeight = height / 2;

    children[0] = new QuadtreeNode(x, y, halfWidth, halfHeight, depth + 1);
    children[1] = new QuadtreeNode(x + halfWidth, y, halfWidth, halfHeight, depth + 1);
    children[2] = new QuadtreeNode(x, y + halfHeight, halfWidth, halfHeight, depth + 1);
    children[3] = new QuadtreeNode(x + halfWidth, y + halfHeight, halfWidth, halfHeight, depth + 1);

    for (QuadtreeNode child : children) {
        child.subdivide(reader, threshold, maxDepth);
    }
}

    // traverses a tree until it reaches a leaf node
    public void traverse(GraphicsContext gc) {
        // if there's no graphics context, nothing to draw
        if (gc == null) {
            return;
        }
        // if this node has children, recursively traverse each child
        if (children != null) {
            for (QuadtreeNode child : children) {
                if (child != null) {
                    child.traverse(gc);
                }
            }
        }
    }

    public void applySobleToLeaves(
        PixelReader reader,
        PixelWriter writer
    ) {
        if (isLeaf()) {
            SobelOperator.apply(reader, writer, this);
            return;
        }

        for (QuadtreeNode child : children) {
            child.applySobleToLeaves(reader, writer);
        }
    }

    // Counts every node in the tree 
    public int countAllNodes() {
        if (isLeaf()) return 1;
        int count = 1;
        for (QuadtreeNode child : children) {
            count += child.countAllNodes();
        }
        return count;
    }

    // Counts only leaf nodes
    public int countLeafNodes() {
        if (isLeaf()) return 1;
        int count = 0;
        for (QuadtreeNode child: children) {
            count += child.countLeafNodes();
        }
        return count;
    }

    // Counts total pixels covered by leaf nodes
    public int countScannedPixels() {
        if (isLeaf()) return width * height;
        int count = 0;
        for (QuadtreeNode child : children) {
            count += child.countScannedPixels();
        }
        return count;
    }

    public int getMaxDepthReached() {
        if (isLeaf()) return depth;
        int max = depth;
        for (QuadtreeNode child : children) {
            max = Math.max(max, child.getMaxDepthReached());
        }
        return max;
    }


    // Getters
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public double getVariance() { return variance; }
    public QuadtreeNode[] getChildren() { return children; }
}
```


#### Short summary: 

empty definition using pc, found symbol in pc: _empty_/VarianceCalculator#