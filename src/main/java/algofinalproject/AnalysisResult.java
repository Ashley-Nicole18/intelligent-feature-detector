package algofinalproject;

public class AnalysisResult {
    // How long the full analysis took
    public long executionTimeMs;
    
    // How many QuadtreeNode objects were created in total
    public int totalNodes;

    // How many of those are leaf nodes 
    public int leafNodes;

    // How many pixels the leaf nodes cover (the scanned pixels)
    public int pixelsScanned;

    // The image's full pixel count
    public int totalPixels;

    // The deepest level the tree reached
    public int maxDepthReached;

    public double reductionPercent() {
        if (totalPixels == 0) return 0;
        return (1.0 - (double) pixelsScanned / totalPixels) * 100;
    }

}
