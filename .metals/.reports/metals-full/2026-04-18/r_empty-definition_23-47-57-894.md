error id: file:///C:/Users/Ashley%20Nicole/Documents/SCHOOL/2nd_Year_2nd_Sem/Algorithm/Intelligent_Feature_Detector/intelligent-feature-detector/src/main/java/algofinalproject/Main.java:_empty_/AppView#
file:///C:/Users/Ashley%20Nicole/Documents/SCHOOL/2nd_Year_2nd_Sem/Algorithm/Intelligent_Feature_Detector/intelligent-feature-detector/src/main/java/algofinalproject/Main.java
empty definition using pc, found symbol in pc: _empty_/AppView#
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 355
uri: file:///C:/Users/Ashley%20Nicole/Documents/SCHOOL/2nd_Year_2nd_Sem/Algorithm/Intelligent_Feature_Detector/intelligent-feature-detector/src/main/java/algofinalproject/Main.java
text:
```scala
package algofinalproject;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class Main extends Application {
    
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Build UI layout
        AppView view = new App@@View();

        // Connect the logic 
        AppController controller = new AppController(view, primaryStage);

        // Hand the layout to javafx
        Scene scene = new Scene(view.root, 900, 600);
    }
}


```


#### Short summary: 

empty definition using pc, found symbol in pc: _empty_/AppView#