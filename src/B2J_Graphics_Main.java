import javafx.application.Application;
import javafx.stage.Stage;

public class B2J_Graphics_Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        //World w = new World(1000, 1000);
        Circle c = new Circle(100,100,50);
    }

    public static void main(String[] args) {
        launch(args);
    }
}