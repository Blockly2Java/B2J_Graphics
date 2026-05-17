import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.application.Platform;

/**
 * Represents the drawing area that owns all shapes, actors, and mouse handling.
 *
 * <p>Novices usually start here: create a world, add shapes, and then let actors
 * animate or respond to keyboard and mouse input.</p>
 */
public class World implements IWorld {
    static {
        // Ensure JavaFX Toolkit is initialized before any World instance is created.
        // This allows the library to be used as a dependency without requiring
        // consumers to extend javafx.application.Application.
        try {
            Platform.startup(() -> {
                // No-op — only here to initialize the JavaFX Toolkit
            });
        } catch (IllegalStateException e) {
            // Toolkit already initialized (e.g., via Application.launch() in B2J_Graphics_Main)
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

    private javafx.scene.Scene scene;
    private ActorManager actorManager;
    private MouseManager mouseManager;

    /**
     * Creates a world with the default size of 800 by 600 pixels.
     */
    public World() {
        this(800, 600);
    }

    /**
     * Creates a world with the given size.
     */
    public World(double width, double height) {
        this.currentLeft = 0;
        this.currentTop = 0;
        this.currentWidth = width;
        this.currentHeight = height;
        currentWorld = this;
        // Create JavaFX window for this world
        createJavaFXWindow();
    }
    
    /**
     * Create a JavaFX window for this world
     */
    private void createJavaFXWindow() {
        B2J_JavaFX_Renderer.createWindow(this, "World", currentWidth, currentHeight, backgroundColor, this::onWindowReady);
    }

    private void onWindowReady(javafx.scene.Scene createdScene) {
        this.scene = createdScene;
        this.actorManager = new ActorManager(createdScene);
        this.mouseManager = new MouseManager(this, createdScene);
        for (Shape shape : allShapes) {
            if (shape != null) {
                shape.ensureRegistration();
                mouseManager.registerShape(shape);
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
        B2J_JavaFX_Renderer.addShape(shape);
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
        B2J_JavaFX_Renderer.removeShape(shape);
        if (mouseManager != null) {
            mouseManager.removeShape(shape);
        }
    }

    /**
     * Returns the current world, creating a default world if needed.
     */
    public static World getWorld() {
        if (currentWorld == null) {
            currentWorld = new World(800, 600);
        }
        return currentWorld;
    }

    /**
     * Removes all root shapes from the current world.
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
     * Returns the current world width.
     */
    public double getWidth() {
        return Math.round(currentWidth);
    }

    @Override
    /**
     * Returns the current world height.
     */
    public double getHeight() {
        return Math.round(currentHeight);
    }

    @Override
    /**
     * Returns the top edge of the active coordinate system.
     */
    public double getTop() {
        return Math.round(currentTop);
    }

    @Override
    /**
     * Returns the left edge of the active coordinate system.
     */
    public double getLeft() {
        return Math.round(currentLeft);
    }

    /**
     * Returns the group that new shapes are attached to by default, if one was set.
     */
    public Group<? extends Shape> getDefaultGroup() {
        return defaultGroup;
    }

    /**
     * Sets the default group for newly created shapes.
     */
    public void setDefaultGroup(Group<? extends Shape> defaultGroup) {
        this.defaultGroup = defaultGroup;
    }

    /**
     * Changes the background color of the world.
     */
    public void setBackgroundColor(Color color) {
        if (color != null) {
            backgroundColor = new Color(color.red, color.green, color.blue, color.alpha);
        }
    }

    /**
     * Changes the background color using an RGB integer.
     */
    public void setBackgroundColor(int color) {
        backgroundColor = Color.fromInt(color);
    }

    /**
     * Changes the background color using a color string such as "#ffcc00" or "red".
     */
    public void setBackgroundColor(String color) {
        Color parsed = Color.parse(color);
        if (parsed != null) {
            backgroundColor = parsed;
        }
    }

    /**
     * Returns a copy of the current background color.
     */
    public Color getBackgroundColor() {
        return new Color(backgroundColor.red, backgroundColor.green, backgroundColor.blue, backgroundColor.alpha);
    }

    /**
     * Moves the visible coordinate system by the given offset.
     */
    public void move(double dx, double dy) {
        currentLeft += dx;
        currentTop += dy;
    }

    /**
     * Rotates the world coordinate system around the given point.
     */
    public void rotate(double angleInDeg, double centerX, double centerY) {
        worldRotationDeg = (worldRotationDeg + angleInDeg) % 360.0;
    }

    /**
     * Scales the world coordinate system around the given point.
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
     * Flips the Y axis so world coordinates grow upward instead of downward.
     */
    public void flipY() {
        flippedY = !flippedY;
    }

    /**
     * Replaces the current coordinate system with a new rectangle.
     */
    public void setCoordinateSystem(double left, double top, double width, double height) {
        currentLeft = left;
        currentTop = top;
        currentWidth = width;
        currentHeight = height;
    }

    @Override
    public void setCursor(String cursor) {
        if (scene != null) {
            javafx.scene.Cursor fxCursor = javafx.scene.Cursor.cursor(cursor);
            scene.setCursor(fxCursor);
        }
    }

    /**
     * Moves the world so the given shape stays inside the visible area when possible.
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
     * Registers a mouse listener object that implements {@link MouseListener}.
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
     * Returns the actor manager used by this world.
     */
    public ActorManager getActorManager() {
        return actorManager;
    }

    @Override
    /**
     * Returns true when the world still contains active actors.
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
     * Returns all shapes in the world, including shapes inside groups.
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
     * Returns the top-level shapes that are directly attached to the world.
     */
    public List<Shape> getRootShapes() {
        return Collections.unmodifiableList(rootShapes);
    }
}
