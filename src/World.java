import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 * Repräsentiert die Zeichenfläche, die alle Formen, Actoren und die Maussteuerung besitzt.
 *
 * <p>Einsteiger beginnen hier: Erstellen Sie eine Welt, fügen Sie Formen hinzu und
 * lassen Sie Actoren animieren oder auf Tastatur- und Maus-Eingaben reagieren.</p>
 */
public class World implements IWorld {

    /**
     * Returns true if JavaFX is available (not running on a headless system).
     */
    private static final boolean FX_AVAILABLE = !GraphicsEnvironment.isHeadless();

    static {
        // Lazily load JavaFX initialization class to avoid NoClassDefFoundError
        // in headless environments where JavaFX is not on the classpath.
        if (FX_AVAILABLE) {
            try {
                Class.forName("JavaFXInit");
            } catch (ClassNotFoundException e) {
                // JavaFX classes not available — that's fine for headless
            } catch (Exception e) {
                // JavaFX initialization failed
            }
        }
    }

    private static World currentWorld;

    private double currentLeft;
    private double currentTop;
    private double currentWidth;
    private double currentHeight;

    private double worldRotationDeg;
    private boolean flippedY;

    private Color backgroundColor = Color.BLACK;

    private final List<Shape> allShapes = new ArrayList<>();
    private final List<Shape> rootShapes = new ArrayList<>();

    private Group<? extends Shape> defaultGroup;

    // Use Object to avoid direct JavaFX import
    private Object scene;
    private ActorManager actorManager;
    private MouseManager mouseManager;

    /**
     * Erstellt eine Welt mit der Standardgröße 800x600 Pixel.
     */
    public World() {
        this(800, 600);
    }

    /**
     * Erstellt eine Welt mit der angegebenen Größe.
     */
    public World(double width, double height) {
        this.currentLeft = 0;
        this.currentTop = 0;
        this.currentWidth = width;
        this.currentHeight = height;
        currentWorld = this;
        // Create JavaFX window for this world (no-op on headless systems)
        if (FX_AVAILABLE) {
            createJavaFXWindow();
        }
    }

    /**
     * Erstellt ein JavaFX-Fenster für diese Welt.
     */
    private void createJavaFXWindow() {
        JavaFXBridge.createWindow(this, "World", currentWidth, currentHeight, backgroundColor, this::onWindowReady);
    }

    private void onWindowReady(Object createdScene) {
        this.scene = createdScene;
        if (createdScene != null) {
            this.actorManager = new ActorManager(createdScene);
            this.mouseManager = new MouseManager(this, createdScene);
            for (Shape shape : allShapes) {
                if (shape != null) {
                    shape.ensureRegistration();
                    mouseManager.registerShape(shape);
                }
            }
        }
    }


    void registerShape(Shape shape) {
        if (shape == null) {
            return;
        }
        if (!allShapes.contains(shape)) {
            allShapes.add(shape);
        }
        if (shape.belongsToGroup == null && !rootShapes.contains(shape)) {
            rootShapes.add(shape);
        }
        // Add shape to JavaFX rendering
        JavaFXBridge.addShape(shape);
        shape.ensureRegistration();
        if (mouseManager != null) {
            mouseManager.registerShape(shape);
        }
    }

    void unregisterFromDefaultList(Shape shape) {
        rootShapes.remove(shape);
    }

    void deregisterShape(Shape shape) {
        rootShapes.remove(shape);
        allShapes.remove(shape);
        // Remove shape from JavaFX rendering
        JavaFXBridge.removeShape(shape);
        if (mouseManager != null) {
            mouseManager.removeShape(shape);
        }
    }

    /**
     * Liefert die aktuelle Welt und erstellt ggf. eine Standardwelt, falls keine existiert.
     */
    public static World getWorld() {
        if (currentWorld == null) {
            currentWorld = new World(800, 600);
        }
        return currentWorld;
    }

    /**
     * Entfernt alle Root-Formen aus der aktuellen Welt.
     */
    public static void clear() {
        if (currentWorld == null) {
            return;
        }
        List<Shape> shapes = new ArrayList<>(currentWorld.rootShapes);
        for (Shape shape : shapes) {
            shape.destroy();
        }
        currentWorld.rootShapes.clear();
        currentWorld.allShapes.clear();
    }


    void bringToFront(Shape shape) {
        if (shape == null) {
            return;
        }
        if (rootShapes.remove(shape)) {
            rootShapes.add(shape);
        }
    }

    void sendToBack(Shape shape) {
        if (shape == null) {
            return;
        }
        if (rootShapes.remove(shape)) {
            rootShapes.add(0, shape);
        }
    }

    @Override
    /**
     * Gibt die aktuelle Breite der Welt zurück.
     */
    public double getWidth() {
        return Math.round(currentWidth);
    }

    @Override
    /**
     * Gibt die aktuelle Höhe der Welt zurück.
     */
    public double getHeight() {
        return Math.round(currentHeight);
    }

    @Override
    /**
     * Gibt die obere Kante des aktiven Koordinatensystems zurück.
     */
    public double getTop() {
        return Math.round(currentTop);
    }

    @Override
    /**
     * Gibt die linke Kante des aktiven Koordinatensystems zurück.
     */
    public double getLeft() {
        return Math.round(currentLeft);
    }

    /**
     * Gibt die Gruppe zurück, an die neue Formen standardmäßig angehängt werden, falls gesetzt.
     */
    public Group<? extends Shape> getDefaultGroup() {
        return defaultGroup;
    }

    /**
     * Setzt die Standardgruppe für neu erstellte Formen.
     */
    public void setDefaultGroup(Group<? extends Shape> defaultGroup) {
        this.defaultGroup = defaultGroup;
    }

    /**
     * Ändert die Hintergrundfarbe der Welt.
     */
    public void setBackgroundColor(Color color) {
        if (color != null) {
            backgroundColor = new Color(color.red, color.green, color.blue, color.alpha);
        }
    }

    /**
     * Ändert die Hintergrundfarbe anhand eines RGB-Integerwerts.
     */
    public void setBackgroundColor(int color) {
        backgroundColor = Color.fromInt(color);
    }

    /**
     * Ändert die Hintergrundfarbe mithilfe eines Farb-Strings wie "#ffcc00" oder "red".
     */
    public void setBackgroundColor(String color) {
        Color parsed = Color.parse(color);
        if (parsed != null) {
            backgroundColor = parsed;
        }
    }

    /**
     * Liefert eine Kopie der aktuellen Hintergrundfarbe.
     */
    public Color getBackgroundColor() {
        return new Color(backgroundColor.red, backgroundColor.green, backgroundColor.blue, backgroundColor.alpha);
    }

    /**
     * Verschiebt das sichtbare Koordinatensystem um den angegebenen Versatz.
     */
    public void move(double dx, double dy) {
        currentLeft += dx;
        currentTop += dy;
    }

    /**
     * Dreht das Welt-Koordinatensystem um den angegebenen Punkt.
     */
    public void rotate(double angleInDeg, double centerX, double centerY) {
        worldRotationDeg = (worldRotationDeg + angleInDeg) % 360.0;
    }

    /**
     * Skaliert das Welt-Koordinatensystem um den angegebenen Punkt.
     */
    public void scale(double factor, double centerX, double centerY) {
        if (factor == 0) {
            return;
        }
        currentLeft = centerX + (currentLeft - centerX) / factor;
        currentTop = centerY + (currentTop - centerY) / factor;
        currentWidth = currentWidth / factor;
        currentHeight = currentHeight / factor;
    }

    /**
     * Spiegelt die Y-Achse, sodass die Weltkoordinaten nach oben ansteigen statt nach unten.
     */
    public void flipY() {
        flippedY = !flippedY;
    }

    /**
     * Ersetzt das aktuelle Koordinatensystem durch ein neues Rechteck.
     */
    public void setCoordinateSystem(double left, double top, double width, double height) {
        currentLeft = left;
        currentTop = top;
        currentWidth = width;
        currentHeight = height;
    }

    @Override
    public void setCursor(String cursor) {
        if (!FX_AVAILABLE || scene == null) {
            return;
        }
        // Delegate to JavaFXBridge to avoid direct JavaFX import
        JavaFXBridge.setCursor(scene, cursor);
    }

    /**
     * Verschiebt die Welt so, dass die angegebene Form nach Möglichkeit im sichtbaren Bereich bleibt.
     */
    public void follow(Shape shape, double margin, double xMin, double xMax, double yMin, double yMax) {
        if (shape == null) {
            return;
        }
        double moveX = 0;
        double moveY = 0;

        double shapeX = shape.getCenterX();
        double shapeY = shape.getCenterY();

        double outsideRight = shapeX - (currentLeft + currentWidth - margin);
        if (outsideRight > 0 && currentLeft + currentWidth < xMax) {
            moveX = -outsideRight;
        }

        double outsideLeft = (currentLeft + margin) - shapeX;
        if (outsideLeft > 0 && currentLeft > xMin) {
            moveX = outsideLeft;
        }

        double outsideBottom = shapeY - (currentTop + currentHeight - margin);
        if (outsideBottom > 0 && currentTop + currentHeight <= yMax) {
            moveY = -outsideBottom;
        }

        double outsideTop = (currentTop + margin) - shapeY;
        if (outsideTop > 0 && currentTop >= yMin) {
            moveY = outsideTop;
        }

        if (moveX != 0 || moveY != 0) {
            move(moveX, moveY);
        }
    }

    /**
     * Registriert ein Maus-Listener-Objekt, das {@link MouseListener} implementiert.
     */
    public void addMouseListener(Object mouseListener) {
        if (mouseManager == null) {
            return;
        }
        if (mouseListener instanceof MouseListener) {
            mouseManager.addMouseListener((MouseListener) mouseListener);
        }
    }

    /**
     * Gibt den ActorManager zurück, der von dieser Welt verwendet wird.
     */
    public ActorManager getActorManager() {
        return actorManager;
    }

    @Override
    /**
     * Gibt true zurück, wenn die Welt noch aktive Actoren enthält.
     */
    public boolean hasActors() {
        return actorManager != null && actorManager.hasActors();
    }

    double screenToWorldX(double screenX, double sceneWidth) {
        if (sceneWidth <= 0) {
            return currentLeft + screenX;
        }
        return currentLeft + screenX * (currentWidth / sceneWidth);
    }

    double screenToWorldY(double screenY, double sceneHeight) {
        if (sceneHeight <= 0) {
            return currentTop + screenY;
        }
        if (flippedY) {
            return currentTop + currentHeight - screenY * (currentHeight / sceneHeight);
        }
        return currentTop + screenY * (currentHeight / sceneHeight);
    }

    /**
     * Gibt alle Formen in der Welt zurück, einschließlich Formen innerhalb von Gruppen.
     */
    public List<Shape> getAllShapes() {
        List<Shape> result = new ArrayList<>();
        for (Shape shape : rootShapes) {
            collectShapes(shape, result);
        }
        return result;
    }

    private void collectShapes(Shape shape, List<Shape> target) {
        if (shape == null) {
            return;
        }
        target.add(shape);
        if (shape instanceof Group) {
            for (Shape child : ((Group<?>) shape).getChildren()) {
                collectShapes(child, target);
            }
        }
    }

    /**
     * Gibt die obersten Formen zurück, die direkt an die Welt angehängt sind.
     */
    public List<Shape> getRootShapes() {
        return Collections.unmodifiableList(rootShapes);
    }
}