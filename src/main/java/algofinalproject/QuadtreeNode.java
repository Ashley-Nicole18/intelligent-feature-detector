package algofinalproject;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelReader;

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
        
    }

    // accepts a pixel reader type, threshold, and max depth to decide whether a node can be subdivided or when to stop subdividing
    public void subdivide(PixelReader reader, double threshold, int maxDepth) {
    }

    // traverses a tree until it reaches a leaf node
    public void traverse(GraphicsContext gc) {
    }


    // Getters
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
}