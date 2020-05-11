package paint;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage){
        ShapesGraph shapesGraph = new ShapesGraph();
        shapesGraph.start(stage);

    }

    public static void main(String[] args){
        launch(args);
    }
}
