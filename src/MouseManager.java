import java.util.ArrayList;
import java.util.List;

import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;

/**
 * Übersetzt JavaFX-Mausereignisse in Weltkoordinaten und verteilt sie an Listener und Formen.
 *
 * <p>Maustastenwerte werden als Ganzzahlen übergeben und folgen der JavaFX-Logik
 * {@code MouseButton.ordinal()}: 0 = NONE, 1 = PRIMARY (normalerweise links), 2 = MIDDLE,
 * 3 = SECONDARY (normalerweise rechts). Höhere Werte stehen für zusätzliche Tasten zur Verfügung.</p>
 */
public class MouseManager {
    private final World world;
    private final List<MouseListener> javaMouseListeners = new ArrayList<>();
    private final List<Shape> shapesWithMouseMethods = new ArrayList<>();

    /**
     * Creates a mouse manager for the given world and scene.
     */
    public MouseManager(World world, Scene scene) {
        this.world = world;
        if (scene != null) {
            registerListeners(scene);
        }
    }

    private void registerListeners(Scene scene) {
        scene.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> handleMouseEvent(event, MouseEventKind.DOWN));
        scene.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> handleMouseEvent(event, MouseEventKind.UP));
        scene.addEventHandler(MouseEvent.MOUSE_MOVED, event -> handleMouseEvent(event, MouseEventKind.MOVE));
        scene.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> handleMouseEvent(event, MouseEventKind.ENTER));
        scene.addEventHandler(MouseEvent.MOUSE_EXITED, event -> handleMouseEvent(event, MouseEventKind.LEAVE));
    }

    /**
     * Adds a listener that receives raw mouse callbacks.
     */
    public void addMouseListener(MouseListener listener) {
        if (listener != null) {
            javaMouseListeners.add(listener);
        }
    }

    /**
     * Removes a previously registered mouse listener.
     */
    public void removeMouseListener(MouseListener listener) {
        javaMouseListeners.remove(listener);
    }

    /**
     * Beginnt mit der Verfolgung einer Form, wenn sie Maus-Callback-Methoden implementiert.
     */
    public void registerShape(Shape shape) {
        if (shape == null || shapesWithMouseMethods.contains(shape)) {
            return;
        }
        if (shape.hasMouseHandlers()) {
            shapesWithMouseMethods.add(shape);
        }
    }

    /**
     * Stoppt die Verfolgung von Mauserereignissen für eine Form.
     */
    public void removeShape(Shape shape) {
        shapesWithMouseMethods.remove(shape);
    }

    /**
     * Gibt true zurück, wenn Maus-Listener oder mausbereite Formen registriert sind.
     */
    public boolean hasMouseListeners() {
        return !shapesWithMouseMethods.isEmpty() || !javaMouseListeners.isEmpty();
    }

    private void handleMouseEvent(MouseEvent event, MouseEventKind kind) {
        double x = world.screenToWorldX(event.getX(), world.getWidth());
        double y = world.screenToWorldY(event.getY(), world.getHeight());

        for (MouseListener listener : new ArrayList<>(javaMouseListeners)) {
            dispatchListener(listener, kind, x, y, event.getButton().ordinal());
        }

        for (Shape shape : new ArrayList<>(shapesWithMouseMethods)) {
            if (!shape.reactToMouseEventsWhenInvisible && !shape.isVisible()) {
                continue;
            }
            dispatchShape(shape, kind, x, y, event.getButton().ordinal());
        }
    }

    private void dispatchListener(MouseListener listener, MouseEventKind kind, double x, double y, int button) {
        switch (kind) {
            case DOWN:
                listener.onMouseDown(x, y, button);
                break;
            case UP:
                listener.onMouseUp(x, y, button);
                break;
            case MOVE:
                listener.onMouseMove(x, y);
                break;
            case ENTER:
                listener.onMouseEnter(x, y);
                break;
            case LEAVE:
                listener.onMouseLeave(x, y);
                break;
            default:
                break;
        }
    }

    private void dispatchShape(Shape shape, MouseEventKind kind, double x, double y, int button) {
        boolean contains = shape.containsPoint(x, y);
        switch (kind) {
            case DOWN:
                if (contains || shape.trackMouseMove) {
                    shape.onMouseDown(x, y, button);
                }
                break;
            case UP:
                if (contains || shape.trackMouseMove) {
                    shape.onMouseUp(x, y, button);
                }
                break;
            case MOVE:
                if (shape.trackMouseMove) {
                    shape.onMouseMove(x, y);
                } else if (contains) {
                    shape.onMouseMove(x, y);
                }
                if (contains && !shape.mouseLastSeenInsideObject) {
                    shape.mouseLastSeenInsideObject = true;
                    shape.onMouseEnter(x, y);
                } else if (!contains && shape.mouseLastSeenInsideObject) {
                    shape.mouseLastSeenInsideObject = false;
                    shape.onMouseLeave(x, y);
                }
                break;
            case ENTER:
                if (contains && !shape.mouseLastSeenInsideObject) {
                    shape.mouseLastSeenInsideObject = true;
                    shape.onMouseEnter(x, y);
                }
                break;
            case LEAVE:
                if (shape.mouseLastSeenInsideObject) {
                    shape.mouseLastSeenInsideObject = false;
                    shape.onMouseLeave(x, y);
                }
                break;
            default:
                break;
        }
    }

    private enum MouseEventKind {
        DOWN,
        UP,
        MOVE,
        ENTER,
        LEAVE
    }
}
