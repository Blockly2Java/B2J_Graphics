import java.util.ArrayList;
import java.util.List;

import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;

public class MouseManager {
    private final World world;
    private final List<MouseListener> javaMouseListeners = new ArrayList<>();
    private final List<Shape> shapesWithMouseMethods = new ArrayList<>();

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

    public void addMouseListener(MouseListener listener) {
        if (listener != null) {
            javaMouseListeners.add(listener);
        }
    }

    public void removeMouseListener(MouseListener listener) {
        javaMouseListeners.remove(listener);
    }

    public void registerShape(Shape shape) {
        if (shape == null || shapesWithMouseMethods.contains(shape)) {
            return;
        }
        if (shape.hasMouseHandlers()) {
            shapesWithMouseMethods.add(shape);
        }
    }

    public void removeShape(Shape shape) {
        shapesWithMouseMethods.remove(shape);
    }

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
                // Always dispatch mouse down events to shapes that have the handler
                shape.onMouseDown(x, y, button);
                break;
            case UP:
                // Always dispatch mouse up events to shapes that have the handler
                shape.onMouseUp(x, y, button);
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
