error id: file:///C:/Users/Ashley%20Nicole/Documents/SCHOOL/2nd_Year_2nd_Sem/Algorithm/Final_Project/quadtreeproject/src/main/java/algofinalproject/Main.java
file:///C:/Users/Ashley%20Nicole/Documents/SCHOOL/2nd_Year_2nd_Sem/Algorithm/Final_Project/quadtreeproject/src/main/java/algofinalproject/Main.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[9,1]

error in qdox parser
file content:
```java
offset: 181
uri: file:///C:/Users/Ashley%20Nicole/Documents/SCHOOL/2nd_Year_2nd_Sem/Algorithm/Final_Project/quadtreeproject/src/main/java/algofinalproject/Main.java
text:
```scala
package algofinalproject;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx

p@@ublic class Main extends Application {
    private Image image;
    private PixelReader reader;

    double threshold = 0.01;
    int maxDepth = 6;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        run();
    }

    public void run() {
        image = new Image(getClass().getResourceAsStream("/sample.jpg"));
        image = new Image(getClass().getResourceAsStream("/sample.jpg"));

    if (image == null || image.isError()) {
        System.out.println("❌ ERROR: Image not found! Is it in src/main/resources/sample.jpg?");
        return; 
    }

    System.out.println("✅ Image loaded! Dimensions: " + image.getWidth() + "x" + image.getHeight());
        reader = image.getPixelReader();

        QuadtreeNode root = new QuadtreeNode(
        0, 
        0, 
        (int) image.getWidth(), 
        (int) image.getHeight(), 
        0
    );
        root.subdivide(reader, threshold, maxDepth);

        Canvas canvas = new Canvas(image.getWidth(), image.getHeight());
        root.traverse();
    }
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
	scala.meta.internal.metals.MetalsLspService.$anonfun$onChange$2(MetalsLspService.scala:936)
	scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.scala:18)
	scala.concurrent.Future$.$anonfun$apply$1(Future.scala:691)
	scala.concurrent.impl.Promise$Transformation.run(Promise.scala:500)
	java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1090)
	java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:614)
	java.base/java.lang.Thread.run(Thread.java:1474)
```
#### Short summary: 

QDox parse error in file:///C:/Users/Ashley%20Nicole/Documents/SCHOOL/2nd_Year_2nd_Sem/Algorithm/Final_Project/quadtreeproject/src/main/java/algofinalproject/Main.java