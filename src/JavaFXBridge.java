import java.util.List;
import java.util.function.Consumer;

import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.image.Image;

/**
 * Thin delegation layer for all JavaFX operations.
 * All JavaFX imports live here, so headless environments never load JavaFX classes.
 */
class JavaFXBridge {

    /**
     * Create a JavaFX window for a new World.
     */
    static void createWindow(World world, String title, double width, double height, Color backgroundColor) {
        if (!JavaFXInit.FX_AVAILABLE) {
            return;
        }
        JavaFXInit.loadRendererClasses();
        B2J_JavaFX_Renderer.createWindow(world, title, width, height, backgroundColor);
    }

    /**
     * Create a JavaFX window for a new World with onReady callback.
     */
    static void createWindow(World world, String title, double width, double height, Color backgroundColor,
            Consumer<Scene> onReady) {
        if (!JavaFXInit.FX_AVAILABLE) {
            return;
        }
        JavaFXInit.loadRendererClasses();
        B2J_JavaFX_Renderer.createWindow(world, title, width, height, backgroundColor, onReady);
    }

    /**
     * Add a shape to the JavaFX rendering.
     */
    static void addShape(Shape shape) {
        if (!JavaFXInit.FX_AVAILABLE) {
            return;
        }
        JavaFXInit.loadRendererClasses();
        B2J_JavaFX_Renderer.addShape(shape);
    }

    /**
     * Remove a shape from the JavaFX rendering.
     */
    static void removeShape(Shape shape) {
        if (!JavaFXInit.FX_AVAILABLE) {
            return;
        }
        JavaFXInit.loadRendererClasses();
        B2J_JavaFX_Renderer.removeShape(shape);
    }

    /**
     * Update a shape in the JavaFX rendering.
     */
    static void updateShape(Shape shape) {
        if (!JavaFXInit.FX_AVAILABLE) {
            return;
        }
        JavaFXInit.loadRendererClasses();
        B2J_JavaFX_Renderer.updateShape(shape);
    }

    /**
     * Close the JavaFX window for a world.
     */
    static void closeWindow(World world) {
        if (!JavaFXInit.FX_AVAILABLE) {
            return;
        }
        JavaFXInit.loadRendererClasses();
        B2J_JavaFX_Renderer.closeWindow(world);
    }

    /**
     * Get the JavaFX root group for a world.
     */
    static javafx.scene.Group getRootGroup(World world) {
        if (!JavaFXInit.FX_AVAILABLE) {
            return null;
        }
        JavaFXInit.loadRendererClasses();
        return B2J_JavaFX_Renderer.getRootGroup(world);
    }

    /**
     * Set the cursor on the JavaFX scene.
     */
    static void setCursor(Object sceneObj, String cursor) {
        if (!JavaFXInit.FX_AVAILABLE || sceneObj == null) {
            return;
        }
        JavaFXInit.loadRendererClasses();
        Scene scene = (Scene) sceneObj;
        javafx.scene.Cursor fxCursor = javafx.scene.Cursor.cursor(cursor);
        scene.setCursor(fxCursor);
    }

    /**
     * Update a Sprite shape in the JavaFX rendering.
     */
    static void updateSpriteShape(Sprite shape) {
        if (!JavaFXInit.FX_AVAILABLE) {
            return;
        }
        JavaFXInit.loadRendererClasses();
        B2J_JavaFX_Renderer.updateShape(shape);
    }

    /**
     * Start a sprite animation timeline.
     */
    static Timeline startSpriteAnimation(Sprite shape, int imagesPerSecond, List<Integer> sequence, RepeatType repeatType) {
        if (!JavaFXInit.FX_AVAILABLE) {
            return null;
        }
        JavaFXInit.loadRendererClasses();
        return B2J_JavaFX_Renderer.startSpriteAnimation(shape, imagesPerSecond, sequence, repeatType);
    }

    /**
     * Stop a sprite animation.
     */
    static void stopSpriteAnimation(Sprite shape) {
        if (!JavaFXInit.FX_AVAILABLE) {
            return;
        }
        JavaFXInit.loadRendererClasses();
        B2J_JavaFX_Renderer.stopSpriteAnimation(shape);
    }

    /**
     * Pause a sprite animation.
     */
    static void pauseSpriteAnimation(Sprite shape) {
        if (!JavaFXInit.FX_AVAILABLE) {
            return;
        }
        JavaFXInit.loadRendererClasses();
        B2J_JavaFX_Renderer.pauseSpriteAnimation(shape);
    }

    /**
     * Resume a sprite animation.
     */
    static void resumeSpriteAnimation(Sprite shape) {
        if (!JavaFXInit.FX_AVAILABLE) {
            return;
        }
        JavaFXInit.loadRendererClasses();
        B2J_JavaFX_Renderer.resumeSpriteAnimation(shape);
    }

    /**
     * Get pixel color from a sprite image.
     */
    static int getSpritePixelColor(Sprite shape, int x, int y) {
        if (!JavaFXInit.FX_AVAILABLE) {
            return 0;
        }
        JavaFXInit.loadRendererClasses();
        return B2J_JavaFX_Renderer.getSpritePixelColor(shape, x, y);
    }

    /**
     * Get pixel alpha from a sprite image.
     */
    static double getSpritePixelAlpha(Sprite shape, int x, int y) {
        if (!JavaFXInit.FX_AVAILABLE) {
            return 0.0;
        }
        JavaFXInit.loadRendererClasses();
        return B2J_JavaFX_Renderer.getSpritePixelAlpha(shape, x, y);
    }

    /**
     * Load a sprite image from resources.
     */
    static Image loadSpriteImage(SpriteLibrary lib, int index) {
        if (!JavaFXInit.FX_AVAILABLE) {
            return null;
        }
        JavaFXInit.loadRendererClasses();
        return B2J_JavaFX_Renderer.loadSpriteImage(lib, index);
    }

    /**
     * Get image width.
     */
    static double getImageWidth(Object img) {
        if (img == null) {
            return 0;
        }
        return ((Image) img).getWidth();
    }

    /**
     * Get image height.
     */
    static double getImageHeight(Object img) {
        if (img == null) {
            return 0;
        }
        return ((Image) img).getHeight();
    }

    /**
     * Create a Turtle line node.
     */
    static javafx.scene.Group createTurtleLineNode(Turtle shape) {
        if (!JavaFXInit.FX_AVAILABLE) {
            return null;
        }
        javafx.scene.Group group = new javafx.scene.Group();
        for (Turtle.LineSegment segment : shape.getSegments()) {
            javafx.scene.shape.Line line = new javafx.scene.shape.Line(segment.x1, segment.y1, segment.x2, segment.y2);
            if (segment.color != null) {
                javafx.scene.paint.Color base = toFxColor(segment.color, segment.alpha);
                line.setStroke(base);
            }
            line.setStrokeWidth(segment.width);
            group.getChildren().add(line);
        }
        return group;
    }

    /**
     * Create a Turtle polygon node.
     */
    static javafx.scene.shape.Polygon createTurtlePolygonNode(Turtle shape) {
        if (!JavaFXInit.FX_AVAILABLE || !shape.isShowTurtle()) {
            return null;
        }
        double size = shape.getTurtleSize();
        double rad = Math.toRadians(shape.getTurtleAngleDeg());
        double cos = Math.cos(rad);
        double sin = Math.sin(rad);

        double x1 = shape.centerX + cos * size;
        double y1 = shape.centerY - sin * size;
        double x2 = shape.centerX + Math.cos(rad + Math.toRadians(140)) * size * 0.6;
        double y2 = shape.centerY - Math.sin(rad + Math.toRadians(140)) * size * 0.6;
        double x3 = shape.centerX + Math.cos(rad - Math.toRadians(140)) * size * 0.6;
        double y3 = shape.centerY - Math.sin(rad - Math.toRadians(140)) * size * 0.6;

        javafx.scene.shape.Polygon turtle = new javafx.scene.shape.Polygon();
        turtle.getPoints().addAll(x1, y1, x2, y2, x3, y3);
        turtle.setFill(javafx.scene.paint.Color.GREEN);
        return turtle;
    }

    private static javafx.scene.paint.Color toFxColor(int rgb, double alpha) {
        return javafx.scene.paint.Color.rgb((rgb >> 16) & 0xff, (rgb >> 8) & 0xff, rgb & 0xff, alpha);
    }
}