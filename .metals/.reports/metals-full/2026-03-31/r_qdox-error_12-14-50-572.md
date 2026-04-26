error id: file:///C:/Users/Ashley%20Nicole/Documents/SCHOOL/2nd_Year_2nd_Sem/Algorithm/Intelligent_Feature_Detector/intelligent-feature-detector/src/main/java/algofinalproject/QuadtreeNode.java
file:///C:/Users/Ashley%20Nicole/Documents/SCHOOL/2nd_Year_2nd_Sem/Algorithm/Intelligent_Feature_Detector/intelligent-feature-detector/src/main/java/algofinalproject/QuadtreeNode.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[85,5]

error in qdox parser
file content:
```java
offset: 2566
uri: file:///C:/Users/Ashley%20Nicole/Documents/SCHOOL/2nd_Year_2nd_Sem/Algorithm/Intelligent_Feature_Detector/intelligent-feature-detector/src/main/java/algofinalproject/QuadtreeNode.java
text:
```scala
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

    if (depth >= maxDepth) return;
    if (width <= 1 || height <= 1) return;

    variance = VarianceCalculator.compute(reader, x, y, width, height);

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

    p@@ublic void applySobleToLeaves


    // Getters
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
}
```

```



#### Error stacktrace:

```
com.thoughtworks.qdox.parser.impl.Parser.yyerror(Parser.java:2025)
	com.thoughtworks.qdox.parser.impl.Parser.yyparse(Parser.java:2147)
	com.thoughtworks.qdox.parser.impl.Parser.parse(Parser.java:2006)
	com.thoughtworks.qdox.library.SourceLibrary.parse(SourceLibrary.java:232)
	com.thoughtworks.qdox.library.SourceLibrary.parse(SourceLibrary.java:190)
	com.thoughtworks.qdox.library.SourceLibrary.addSource(SourceLibrary.java:94)
	com.thoughtworks.qdox.library.SourceLibrary.addSource(SourceLibrary.java:89)
	com.thoughtworks.qdox.library.SortedClassLibraryBuilder.addSource(SortedClassLibraryBuilder.java:162)
	com.thoughtworks.qdox.JavaProjectBuilder.addSource(JavaProjectBuilder.java:174)
	scala.meta.internal.mtags.JavaMtags.indexRoot(JavaMtags.scala:49)
	scala.meta.internal.metals.SemanticdbDefinition$.foreachWithReturnMtags(SemanticdbDefinition.scala:99)
	scala.meta.internal.metals.Indexer.indexSourceFile(Indexer.scala:560)
	scala.meta.internal.metals.Indexer.$anonfun$reindexWorkspaceSources$3(Indexer.scala:691)
	scala.meta.internal.metals.Indexer.$anonfun$reindexWorkspaceSources$3$adapted(Indexer.scala:688)
	scala.collection.IterableOnceOps.foreach(IterableOnce.scala:630)
	scala.collection.IterableOnceOps.foreach$(IterableOnce.scala:628)
	scala.collection.AbstractIterator.foreach(Iterator.scala:1313)
	scala.meta.internal.metals.Indexer.reindexWorkspaceSources(Indexer.scala:688)
	scala.meta.internal.metals.MetalsLspService.$anonfun$onChange$2(MetalsLspService.scala:940)
	scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.scala:18)
	scala.concurrent.Future$.$anonfun$apply$1(Future.scala:691)
	scala.concurrent.impl.Promise$Transformation.run(Promise.scala:500)
	java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1090)
	java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:614)
	java.base/java.lang.Thread.run(Thread.java:1474)
```
#### Short summary: 

QDox parse error in file:///C:/Users/Ashley%20Nicole/Documents/SCHOOL/2nd_Year_2nd_Sem/Algorithm/Intelligent_Feature_Detector/intelligent-feature-detector/src/main/java/algofinalproject/QuadtreeNode.java