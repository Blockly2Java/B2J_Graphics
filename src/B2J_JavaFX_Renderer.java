import java.util.HashMap;
import java.util.Map;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Handles JavaFX rendering for B2J graphics.
 * Manages JavaFX stages for each World and converts B2J shapes to JavaFX nodes.
 */
public class B2J_JavaFX_Renderer {
    
    private static final Map<World, JavaFXWindow> worldWindows = new HashMap<>();
    
    /**
     * Internal class to hold the JavaFX window for a World
     */
    private static class JavaFXWindow {
        Stage stage;
        Scene scene;
        javafx.scene.Group root;
        
        JavaFXWindow(String title, double width, double height, Color backgroundColor) {
            root = new javafx.scene.Group();
            scene = new Scene(root, width, height);
        
            stage = new Stage();
            stage.setTitle(title);
            
            // Set the scene background color to match the world background color
            scene.setFill(new javafx.scene.paint.Color(backgroundColor.red, backgroundColor.green, backgroundColor.blue, backgroundColor.alpha));
            
            stage.setScene(scene);
            stage.setWidth(width);
            stage.setHeight(height);
            stage.setMinWidth(width);
            stage.setMinHeight(height);
            stage.setMaxWidth(width);
            stage.setMaxHeight(height);
            stage.show();
        }
    }
    
    /**
     * Create a JavaFX window for a new World
     */
    public static void createWindow(World world, String title, double width, double height, Color backgroundColor) {
        Platform.runLater(() -> {
            JavaFXWindow window = new JavaFXWindow(title, width, height, backgroundColor);
            worldWindows.put(world, window);
        });
    }
    
    /**
     * Create a JavaFX window for a new World with default title
     */
    public static void createWindow(World world, double width, double height) {
        createWindow(world, "World", width, height, Color.BLACK);
    }  
    
    /**
     * Create a JavaFX window for a new World with default dimensions
     */
    public static void createWindow(World world) {
        createWindow(world, "World", world.getWidth(), world.getHeight(), world.getBackgroundColor());
    }
    
    /**
     * Add a shape to the JavaFX rendering
     */
    public static void addShape(Shape shape) {
        Platform.runLater(() -> {
            JavaFXWindow window = worldWindows.get(shape.getWorld());
            if (window != null && shape.isVisible()) {
                javafx.scene.Node node = createNodeForShape(shape);
                if (node != null) {
                    window.root.getChildren().add(node);
                }
            }
        });
    }
    
    /**
     * Remove a shape from the JavaFX rendering
     */
    public static void removeShape(Shape shape) {
        Platform.runLater(() -> {
            JavaFXWindow window = worldWindows.get(shape.getWorld());
            if (window != null) {
                javafx.scene.Node node = createNodeForShape(shape);
                if (node != null) {
                    window.root.getChildren().remove(node);
                }
            }
        });
    }
    
    /**
     * Update a shape in the JavaFX rendering
     */
    public static void updateShape(Shape shape) {
        Platform.runLater(() -> {
            JavaFXWindow window = worldWindows.get(shape.getWorld());
            if (window != null) {
                javafx.scene.Node node = createNodeForShape(shape);
                if (node != null) {
                    // Remove old node if it exists
                    window.root.getChildren().remove(node);
                    // Add updated node
                    window.root.getChildren().add(node);
                }
            }
        });
    }
    
    /**
     * Create a JavaFX node for a given B2J shape
     */
    private static javafx.scene.Node createNodeForShape(Shape shape) {
        if (shape instanceof Circle s) {
            return createNodeForCircle(s);
        } else if (shape instanceof Rectangle s) {
            return createNodeForRectangle(s);
        } else if (shape instanceof Line s) {
            return createNodeForLine(s);
        } else if (shape instanceof Polygon s) {
            return createNodeForPolygon(s);
        } else if (shape instanceof Triangle s) {
            return createNodeForTriangle(s);
        } else if (shape instanceof Ellipse s) {
            return createNodeForEllipse(s);
        } else if (shape instanceof Arc s) {
            return createNodeForArc(s);
        } else if (shape instanceof RoundedRectangle s) {
            return createNodeForRoundedRectangle(s);
        } else if (shape instanceof Sector s) {
            return createNodeForSector(s);
        } else if (shape instanceof Text s) {
            return createNodeForText(s);
        } else if (shape instanceof FilledShape s) {
            return createNodeForFilledShape(s);
        }
        return null;
    }
    
    private static javafx.scene.shape.Circle createNodeForCircle(Circle shape) {
        javafx.scene.shape.Circle circle = new javafx.scene.shape.Circle();
        circle.setCenterX(shape.getCenterX());
        circle.setCenterY(shape.getCenterY());
        circle.setRadius(shape.getRadius());
        applyCommonProperties(circle, shape);
        return circle;
    }
    
    private static javafx.scene.shape.Rectangle createNodeForRectangle(Rectangle shape) {
        javafx.scene.shape.Rectangle rect = new javafx.scene.shape.Rectangle();
        Shape.Bounds bounds = shape.getBounds();
        rect.setX(bounds.minX);
        rect.setY(bounds.minY);
        rect.setWidth(bounds.maxX - bounds.minX);
        rect.setHeight(bounds.maxY - bounds.minY);
        applyCommonProperties(rect, shape);
        return rect;
    }
    
    private static javafx.scene.shape.Line createNodeForLine(Line shape) {
        Shape.Bounds bounds = shape.getBounds();
        javafx.scene.shape.Line line = new javafx.scene.shape.Line();
        line.setStartX(bounds.minX);
        line.setStartY(bounds.minY);
        line.setEndX(bounds.maxX);
        line.setEndY(bounds.maxY);
        applyCommonProperties(line, shape);
        return line;
    }
    
    private static javafx.scene.shape.Polygon createNodeForPolygon(Polygon shape) {
        javafx.scene.shape.Polygon polygon = new javafx.scene.shape.Polygon();
        for (Shape.Point point : shape.getTransformedPoints()) {
            polygon.getPoints().addAll(point.x, point.y);
        }
        applyCommonProperties(polygon, shape);
        return polygon;
    }
    
    private static javafx.scene.shape.Polygon createNodeForTriangle(Triangle shape) {
        Polygon p = new Polygon(shape);
        return createNodeForPolygon(p);
    }
    
    private static javafx.scene.shape.Ellipse createNodeForEllipse(Ellipse shape) {
        javafx.scene.shape.Ellipse ellipse = new javafx.scene.shape.Ellipse();
        ellipse.setCenterX(shape.getCenterX());
        ellipse.setCenterY(shape.getCenterY());
        ellipse.setRadiusX(shape.getRadiusX());
        ellipse.setRadiusY(shape.getRadiusY());
        applyCommonProperties(ellipse, shape);
        return ellipse;
    }
    
    private static javafx.scene.shape.Arc createNodeForArc(Arc shape) {
        javafx.scene.shape.Arc arc = new javafx.scene.shape.Arc();
        arc.setCenterX(shape.getCenterX());
        arc.setCenterY(shape.getCenterY());
        arc.setRadiusX(shape.getOuterRadiusX());
        arc.setRadiusY(shape.getOuterRadiusY());
        arc.setStartAngle(shape.getStartAngleX());
        arc.setLength(shape.getLength());
        applyCommonProperties(arc, shape);
        return arc;
    }
    
    private static javafx.scene.shape.Rectangle createNodeForRoundedRectangle(RoundedRectangle shape) {
        javafx.scene.shape.Rectangle rect = new javafx.scene.shape.Rectangle();
        Shape.Bounds bounds = shape.getBounds();
        rect.setX(bounds.minX);
        rect.setY(bounds.minY);
        rect.setWidth(bounds.maxX - bounds.minX);
        rect.setHeight(bounds.maxY - bounds.minY);
        applyCommonProperties(rect, shape);
        return rect;
    }
    
    private static javafx.scene.shape.Arc createNodeForSector(Sector shape) {
        double mx = shape.getCenterX();
        double my = shape.getCenterY();
        double r = shape.getRadius();
        double sAngl = shape.getStartAngle();
        double eAngl = shape.getEndAngle();
        Arc a = new Arc(mx, my, 0, r, sAngl, eAngl);
        return createNodeForArc(a);
    }
    
    private static javafx.scene.text.Text createNodeForText(Text shape) {
        javafx.scene.text.Text text = new javafx.scene.text.Text();
        text.setX(shape.getCenterX());
        text.setY(shape.getCenterY());
        text.setText(shape.getText());
        return text;
    }
    
    private static javafx.scene.shape.Polygon createNodeForFilledShape(FilledShape shape) {
        return createNodeForPolygon(new Polygon(shape));
    }
    
    /**
     * Apply common properties (fill color, border, alpha, etc.) to a JavaFX shape
     */
    private static <T extends javafx.scene.shape.Shape> void applyCommonProperties(T node, Shape shape) {

        Color shapeFillColor = shape.getFillColor();
        if (shapeFillColor == null) {
            shapeFillColor = new Color(127, 127, 255);
        } 
        
        javafx.scene.paint.Color fxColor = toJavaFXColor(shapeFillColor, shape.getAlpha());
        node.setFill(fxColor);

        if (shape.getBorderColor() != null && shape.getBorderWidth() > 0) {
            fxColor = toJavaFXColor(shape.getBorderColor(), shape.getAlpha());
            node.setStroke(fxColor);
            node.setStrokeWidth(shape.getBorderWidth());
        } else {
            node.setStroke(null);
        }
        
        node.setOpacity(shape.getAlpha());
        node.setVisible(shape.isVisible());
    }
    
    /**
     * Convert B2J Color to JavaFX Color
     */
    private static javafx.scene.paint.Color toJavaFXColor(Color b2jColor, double alpha) {
        return javafx.scene.paint.Color.rgb(
            b2jColor.red,
            b2jColor.green,
            b2jColor.blue,
            alpha
        );
    }
    
    /**
     * Get the JavaFX root group for a world
     */
    public static javafx.scene.Group getRootGroup(World world) {
        JavaFXWindow window = worldWindows.get(world);
        return window != null ? window.root : null;
    }
    
    /**
     * Close the JavaFX window for a world
     */
    public static void closeWindow(World world) {
        Platform.runLater(() -> {
            JavaFXWindow window = worldWindows.remove(world);
            if (window != null) {
                window.stage.close();
            }
        });
    }
}