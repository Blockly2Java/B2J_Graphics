import javafx.scene.Node;
import javafx.scene.Parent;

/**
 * Hilfsfunktionen, die JavaFX-Knoten dazu zwingen, ihr CSS und ihre Transformationsdaten zu aktualisieren.
 */
public class PixiHelper {
    private PixiHelper() {
    }

    public static void updateWorldTransformRecursively(Node node, boolean includeChildren) {
        if (node == null) {
            return;
        }
        node.applyCss();
        if (includeChildren && node instanceof Parent) {
            Parent parent = (Parent) node;
            for (Node child : parent.getChildrenUnmodifiable()) {
                updateWorldTransformRecursively(child, true);
            }
        }
    }
}
