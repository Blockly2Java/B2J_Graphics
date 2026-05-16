import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class World {
        private static World currentWorld;

    private double currentLeft;
    private double currentTop;
    private double currentWidth;
    private double currentHeight;

    private double worldRotationDeg;
    private boolean flippedY;

    private Color backgroundColor = new Color(0, 0, 0);

    private final List<Shape> allShapes = new ArrayList<>();
    private final List<Shape> rootShapes = new ArrayList<>();

    private Group<? extends Shape> defaultGroup;
    
    private javafx.stage.Stage stage;
    private javafx.scene.Group rootGroup;

    public World() {
        this(800, 600);
    }

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
        javafx.application.Platform.runLater(() -> {
            stage = new javafx.stage.Stage();
            stage.setTitle("World");
            
            rootGroup = new javafx.scene.Group();
            javafx.scene.Scene scene = new javafx.scene.Scene(rootGroup, currentWidth, currentHeight);
            
            // Set the scene background color to match the world background color
            scene.setFill(new javafx.scene.paint.Color(backgroundColor.red, backgroundColor.green, backgroundColor.blue, backgroundColor.alpha));
            
            stage.setScene(scene);
            stage.setWidth(currentWidth);
            stage.setHeight(currentHeight);
            stage.setMinWidth(currentWidth);
            stage.setMinHeight(currentHeight);
            stage.setMaxWidth(currentWidth);
            stage.setMaxHeight(currentHeight);
            stage.show();
        });
    }
    
    /**
     * Set the title of the JavaFX window
     */
    public void setWindowTitle(String title) {
        javafx.application.Platform.runLater(() -> {
            if (stage != null) {
                stage.setTitle(title);
            }
        });
    }
    
    /**
     * Get the JavaFX root group for adding shapes directly
     */
    public javafx.scene.Group getJavaFXRootGroup() {
        return rootGroup;
    }
    
    /**
     * Get the JavaFX stage
     */
    public javafx.stage.Stage getJavaFXStage() {
        return stage;
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
    }

    void unregisterFromDefaultList(Shape shape) {
        rootShapes.remove(shape);
    }

    void deregisterShape(Shape shape) {
        rootShapes.remove(shape);
        allShapes.remove(shape);
        // Remove shape from JavaFX rendering
        B2J_JavaFX_Renderer.removeShape(shape);
    }

    public static World getWorld() {
        if (currentWorld == null) {
            currentWorld = new World(800, 600);
        }
        return currentWorld;
    }

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

    public double getWidth() {
        return Math.round(currentWidth);
    }

    public double getHeight() {
        return Math.round(currentHeight);
    }

    public double getTop() {
        return Math.round(currentTop);
    }

    public double getLeft() {
        return Math.round(currentLeft);
    }

    public Group<? extends Shape> getDefaultGroup() {
        return defaultGroup;
    }

    public void setDefaultGroup(Group<? extends Shape> defaultGroup) {
        this.defaultGroup = defaultGroup;
    }

    public void setBackgroundColor(Color color) {
        if (color != null) {
            backgroundColor = new Color(color.red, color.green, color.blue, color.alpha);
        }
    }

    public void setBackgroundColor(int color) {
        backgroundColor = Color.fromInt(color);
    }

    public void setBackgroundColor(String color) {
        Color parsed = Color.parse(color);
        if (parsed != null) {
            backgroundColor = parsed;
        }
    }

    public Color getBackgroundColor() {
        return new Color(backgroundColor.red, backgroundColor.green, backgroundColor.blue, backgroundColor.alpha);
    }

    public void move(double dx, double dy) {
        currentLeft += dx;
        currentTop += dy;
    }

    public void rotate(double angleInDeg, double centerX, double centerY) {
        worldRotationDeg = (worldRotationDeg + angleInDeg) % 360.0;
    }

    public void scale(double factor, double centerX, double centerY) {
        if (factor == 0) {
            return;
        }
        currentLeft = centerX + (currentLeft - centerX) / factor;
        currentTop = centerY + (currentTop - centerY) / factor;
        currentWidth = currentWidth / factor;
        currentHeight = currentHeight / factor;
    }

    public void flipY() {
        flippedY = !flippedY;
    }

    public void setCoordinateSystem(double left, double top, double width, double height) {
        currentLeft = left;
        currentTop = top;
        currentWidth = width;
        currentHeight = height;
    }

    public void setCursor(String cursor) {
    }

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

    public void addMouseListener(Object mouseListener) {
    }

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

    public List<Shape> getRootShapes() {
        return Collections.unmodifiableList(rootShapes);
    }
}
