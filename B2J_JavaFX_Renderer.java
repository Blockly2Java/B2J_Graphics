import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

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
        Group root;
        
        JavaFXWindow(String title, double width, double height) {
            stage = new Stage();
            stage.setTitle(title);
            root = new Group();
            scene = new Scene(root, width, height);
            stage.setScene(scene);
            stage.show();
        }
    }
    
    /**
     * Create a JavaFX window for a new World
     */
    public static void createWindow(World world, String title, double width, double height) {
        Platform.runLater(() -> {
            JavaFXWindow window = new JavaFXWindow(title, width, height);
            worldWindows.put(world, window);
        });
    }
    
    /**
     * Create a JavaFX window for a new World with default title
     */
    public static void createWindow(World world, double width, double height) {
        createWindow(world, "World", width, height);
    }
    
    /**
     * Create a JavaFX window for a new World with default dimensions
     */
    public static void createWindow(World world) {
        createWindow(world, "World", 800, 600);
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
        if (shape instanceof Circle) {
            return createNodeForCircle((Circle) shape);
        } else if (shape instanceof Rectangle) {
            return createNodeForRectangle((Rectangle) shape);
        } else if (shape instanceof Line) {
            return createNodeForLine((Line) shape);
        } else if (shape instanceof Polygon) {
            return createNodeForPolygon((Polygon) shape);
        } else if (shape instanceof Triangle) {
            return createNodeForTriangle((Triangle) shape);
        } else if (shape instanceof Ellipse) {
            return createNodeForEllipse((Ellipse) shape);
        } else if (shape instanceof Arc) {
            return createNodeForArc((Arc) shape);
        } else if (shape instanceof RoundedRectangle) {
            return createNodeForRoundedRectangle((RoundedRectangle) shape);
        } else if (shape instanceof Sector) {
            return createNodeForSector((Sector) shape);
        } else if (shape instanceof Text) {
            return createNodeForText((Text) shape);
        } else if (shape instanceof FilledShape) {
            return createNodeForFilledShape((FilledShape) shape);
        }
        return null;
    }
    
    private static Circle createNodeForCircle(Circle shape) {
        Circle circle = new Circle();
        circle.setCenterX(shape.getCenterX());
        circle.setCenterY(shape.getCenterY());
        circle.setRadius(shape.getRadius());
        applyCommonProperties(circle, shape);
        return circle;
    }
    
    private static Rectangle createNodeForRectangle(Rectangle shape) {
        Rectangle rect = new Rectangle();
        Bounds bounds = shape.getBounds();
        rect.setX(bounds.minX);
        rect.setY(bounds.minY);
        rect.setWidth(bounds.maxX - bounds.minX);
        rect.setHeight(bounds.maxY - bounds.minY);
        applyCommonProperties(rect, shape);
        return rect;
    }
    
    private static Line createNodeForLine(Line shape) {
        Bounds bounds = shape.getBounds();
        Line line = new Line();
        line.setStartX(bounds.minX);
        line.setStartY(bounds.minY);
        line.setEndX(bounds.maxX);
        line.setEndY(bounds.maxY);
        applyCommonProperties(line, shape);
        return line;
    }
    
    private static Polygon createNodeForPolygon(Polygon shape) {
        Polygon polygon = new Polygon();
        for (Shape.Point point : shape.getTransformedPoints()) {
            polygon.getPoints().addAll(point.x, point.y);
        }
        applyCommonProperties(polygon, shape);
        return polygon;
    }
    
    private static Triangle createNodeForTriangle(Triangle shape) {
        return createNodeForPolygon(shape);
    }
    
    private static Ellipse createNodeForEllipse(Ellipse shape) {
        Ellipse ellipse = new Ellipse();
        ellipse.setCenterX(shape.getCenterX());
        ellipse.setCenterY(shape.getCenterY());
        ellipse.setRadiusX(shape.getRadiusX());
        ellipse.setRadiusY(shape.getRadiusY());
        applyCommonProperties(ellipse, shape);
        return ellipse;
    }
    
    private static Arc createNodeForArc(Arc shape) {
        Arc arc = new Arc();
        arc.setCenterX(shape.getCenterX());
        arc.setCenterY(shape.getCenterY());
        arc.setRadiusX(shape.getRadiusX());
        arc.setRadiusY(shape.getRadiusY());
        arc.setStartAngle(shape.getStartAngle());
        arc.setLength(shape.getLength());
        applyCommonProperties(arc, shape);
        return arc;
    }
    
    private static RoundedRectangle createNodeForRoundedRectangle(RoundedRectangle shape) {
        Bounds bounds = shape.getBounds();
        RoundedRectangle rect = new RoundedRectangle();
        rect.setX(bounds.minX);
        rect.setY(bounds.minY);
        rect.setWidth(bounds.maxX - bounds.minX);
        rect.setHeight(bounds.maxY - bounds.minY);
        rect.setArcWidth(shape.getArcWidth());
        rect.setArcHeight(shape.getArcHeight());
        applyCommonProperties(rect, shape);
        return rect;
    }
    
    private static Sector createNodeForSector(Sector shape) {
        return createNodeForArc(shape);
    }
    
    private static Text createNodeForText(Text shape) {
        Text text = new Text();
        text.setX(shape.getX());
        text.setY(shape.getY());
        text.setText(shape.getText());
        text.setFont(shape.getFont());
        return text;
    }
    
    private static Shape createNodeForFilledShape(FilledShape shape) {
        return createNodeForPolygon(shape);
    }
    
    /**
     * Apply common properties (fill color, border, alpha, etc.) to a JavaFX shape
     */
    private static <T extends javafx.scene.shape.Shape> void applyCommonProperties(T node, Shape shape) {
        if (shape.getFillColor() != null) {
            javafx.scene.paint.Color fxColor = toJavaFXColor(shape.getFillColor(), shape.getFillAlpha());
            node.setFill(fxColor);
        } else {
            node.setFill(null);
        }
        
        if (shape.getBorderColor() != null && shape.getBorderWidth() > 0) {
            javafx.scene.paint.Color fxColor = toJavaFXColor(shape.getBorderColor(), shape.getBorderAlpha());
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
            (int) (b2jColor.red * 255),
            (int) (b2jColor.green * 255),
            (int) (b2jColor.blue * 255),
            alpha
        );
    }
    
    /**
     * Get the JavaFX root group for a world
     */
    public static Group getRootGroup(World world) {
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