package algofinalproject;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

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
    private Color avgColor;

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
    }

    // traverses a tree until it reaches a leaf node
    public void traverse(GraphicsContext gc) {
        // if there's no graphics context, nothing to draw
        if (gc == null) {
            return;
        }
        // if this node has no children (leaf node), draw a filled rectangle for this region
        if (isLeaf()) {
            Color fill = (avgColor != null) ? avgColor : Color.MAGENTA;

            gc.setFill(fill);

            gc.fillRect(x, y, width, height);

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


    // Getters
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
}