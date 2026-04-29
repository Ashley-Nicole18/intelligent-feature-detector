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

    public int maxDepth;

    public double reductionPercent() {
        if (totalPixels == 0) return 0;
        return (1.0 - (double) pixelsScanned / totalPixels) * 100;
    }

    public double complexityScore() {
        // fraction of leaf nodes among all nodes
        double leafRatio = (totalNodes == 0) ? 0 :
            (double) leafNodes / totalNodes;

        // how much of image not scanned
        double scanFactor = 1.0 - (reductionPercent() / 100.0);

        // how deep the tree go relative to its allowed maximum
        double depthFactor = (maxDepth == 0) ? 0 :
            (double) maxDepthReached / maxDepth;

        return (0.4 * leafRatio) + (0.4 * scanFactor) + (0.2 * depthFactor);
    }

    // convert score into human-readable label
    public String complexityLabel() {
        double score = complexityScore();
        if (score < 0.35) return "SIMPLE";
        if (score < 0.65) return "MODERATE";
        return "COMPLEX";
    }

    // color suggestion
    public String complexityColor() {
        switch (complexityLabel()) {
            case "SIMPLE": return "#4CAF50";
            case "MODERATE": return "#FF9800";
            default: return "#F44336";
        }
    }
}
