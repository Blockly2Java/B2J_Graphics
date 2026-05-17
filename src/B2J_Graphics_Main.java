import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Small launcher that opens a demo world when the graphics library is started directly.
 */
public class B2J_Graphics_Main extends Application {
    @Override
    /**
     * Starts the JavaFX application and creates the sample scene.
     */
    public void start(Stage primaryStage) {
        World w = new World(1000, 1000);
        B2J_Graphics_DiscoKugel c = new B2J_Graphics_DiscoKugel(500,500);
    }

    /**
     * Launches the JavaFX application.
     */
    public static void main(String[] args) {
        launch(args);
    }
}