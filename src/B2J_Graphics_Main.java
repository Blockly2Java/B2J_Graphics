import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Kleiner Starter, der eine Demo-Welt öffnet, wenn die Grafikbibliothek direkt gestartet wird.
 */
public class B2J_Graphics_Main extends Application {
    @Override
    /**
    * Startet die JavaFX-Anwendung und erstellt die Beispielszene.
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