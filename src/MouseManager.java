import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
import java.util.List;

/**
 * Übersetzt JavaFX-Mausereignisse in Weltkoordinaten und verteilt sie an Listener und Formen.
 * Uses reflection to avoid direct JavaFX imports that would fail in headless environments.
 */
class MouseManager {

    private static final boolean FX_AVAILABLE = !GraphicsEnvironment.isHeadless();
    private final World world;
    private final List<MouseListener> javaMouseListeners = new ArrayList<>();
    private final List<Shape> shapesWithMouseMethods = new ArrayList<>();

    /**
     * Erzeugt einen Maus-Manager für die angegebene `World` und `Scene`.
     * Accepts Object to avoid direct JavaFX import in callers.
     */
    public MouseManager(World world, Object sceneObj) {
        this.world = world;
        if (FX_AVAILABLE && sceneObj != null) {
            try {
                registerListeners(sceneObj);
            } catch (Exception e) {
                // Mouse registration failed — continue without it
            }
        }
    }

    private void registerListeners(Object sceneObj) throws Exception {
        if (!FX_AVAILABLE || sceneObj == null) {
            return;
        }
        Class<?> sceneClass = sceneObj.getClass();
        java.lang.reflect.Method addEventHandlerMethod = sceneClass.getMethod("addEventHandler", java.lang.Object.class, java.lang.Object.class);

        Object mousePressedType = getMouseEventType("MOUSE_PRESSED");
        addEventHandlerMethod.invoke(sceneObj, mousePressedType, new java.util.function.Consumer<Object>() {
            public void accept(Object event) { handleMouseEvent(event, MouseEventKind.DOWN); }
        });

        Object mouseReleasedType = getMouseEventType("MOUSE_RELEASED");
        addEventHandlerMethod.invoke(sceneObj, mouseReleasedType, new java.util.function.Consumer<Object>() {
            public void accept(Object event) { handleMouseEvent(event, MouseEventKind.UP); }
        });

        Object mouseMovedType = getMouseEventType("MOUSE_MOVED");
        addEventHandlerMethod.invoke(sceneObj, mouseMovedType, new java.util.function.Consumer<Object>() {
            public void accept(Object event) { handleMouseEvent(event, MouseEventKind.MOVE); }
        });

        Object mouseEnteredType = getMouseEventType("MOUSE_ENTERED");
        addEventHandlerMethod.invoke(sceneObj, mouseEnteredType, new java.util.function.Consumer<Object>() {
            public void accept(Object event) { handleMouseEvent(event, MouseEventKind.ENTER); }
        });

        Object mouseExitedType = getMouseEventType("MOUSE_EXITED");
        addEventHandlerMethod.invoke(sceneObj, mouseExitedType, new java.util.function.Consumer<Object>() {
            public void accept(Object event) { handleMouseEvent(event, MouseEventKind.LEAVE); }
        });
    }

    private Object getMouseEventType(String typeName) throws Exception {
        Class<?> mouseEventClass = Class.forName("javafx.scene.input.MouseEvent");
        java.lang.reflect.Field field = mouseEventClass.getField(typeName);
        return field.get(null);
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

    private void handleMouseEvent(Object event, MouseEventKind kind) {
        try {
            double x = world.screenToWorldX((Double) event.getClass().getMethod("getX").invoke(event), world.getWidth());
            double y = world.screenToWorldY((Double) event.getClass().getMethod("getY").invoke(event), world.getHeight());

            Object buttonObj = event.getClass().getMethod("getButton").invoke(event);
            int button = 0;
            if (buttonObj != null) {
                java.lang.reflect.Method ordinalMethod = buttonObj.getClass().getMethod("ordinal");
                button = (Integer) ordinalMethod.invoke(buttonObj);
            }

            for (MouseListener listener : new ArrayList<>(javaMouseListeners)) {
                dispatchListener(listener, kind, x, y, button);
            }

            for (Shape shape : new ArrayList<>(shapesWithMouseMethods)) {
                if (!shape.reactToMouseEventsWhenInvisible && !shape.isVisible()) {
                    continue;
                }
                dispatchShape(shape, kind, x, y, button);
            }
        } catch (Exception e) {
            // ignore
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