import java.util.List;
import java.util.function.Consumer;

/**
 * Thin delegation layer for all JavaFX operations.
 * Uses reflection to avoid direct JavaFX imports that would fail in headless environments.
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
            Consumer<Object> onReady) {
        if (!JavaFXInit.FX_AVAILABLE) {
            return;
        }
        JavaFXInit.loadRendererClasses();
        try {
            java.lang.reflect.Method method = B2J_JavaFX_Renderer.class.getMethod("createWindow", World.class, String.class, double.class, double.class, Color.class, java.util.function.Consumer.class);
            method.invoke(null, world, title, width, height, backgroundColor, (java.util.function.Consumer) onReady);
        } catch (Exception e) {
            // ignore
        }
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
    static Object getRootGroup(World world) {
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
        try {
            Class<?> cursorClass = Class.forName("javafx.scene.Cursor");
            java.lang.reflect.Method cursorMethod = cursorClass.getMethod("cursor", String.class);
            Object fxCursor = cursorMethod.invoke(null, cursor);
            java.lang.reflect.Method setCursorMethod = sceneObj.getClass().getMethod("setCursor", cursorClass);
            setCursorMethod.invoke(sceneObj, fxCursor);
        } catch (Exception e) {
            // ignore
        }
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
    static Object startSpriteAnimation(Sprite shape, int imagesPerSecond, List<Integer> sequence, RepeatType repeatType) {
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
    static Object loadSpriteImage(SpriteLibrary lib, int index) {
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
        try {
            java.lang.reflect.Method getWidthMethod = img.getClass().getMethod("getWidth");
            return (Double) getWidthMethod.invoke(img);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Get image height.
     */
    static double getImageHeight(Object img) {
        if (img == null) {
            return 0;
        }
        try {
            java.lang.reflect.Method getHeightMethod = img.getClass().getMethod("getHeight");
            return (Double) getHeightMethod.invoke(img);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Create a Turtle line node.
     */
    static Object createTurtleLineNode(Turtle shape) {
        if (!JavaFXInit.FX_AVAILABLE) {
            return null;
        }
        try {
            Class<?> groupClass = Class.forName("javafx.scene.Group");
            Object group = groupClass.getDeclaredConstructor().newInstance();
            java.lang.reflect.Method getChildrenMethod = groupClass.getMethod("getChildren");
            java.lang.reflect.Method addMethod = getChildrenMethod.getReturnType().getMethod("add", java.lang.Object.class);

            Class<?> lineClass = Class.forName("javafx.scene.shape.Line");
            java.lang.reflect.Constructor<?> lineConstructor = lineClass.getDeclaredConstructor();
            java.lang.reflect.Method setX1Method = lineClass.getMethod("setX1", double.class);
            java.lang.reflect.Method setY1Method = lineClass.getMethod("setY1", double.class);
            java.lang.reflect.Method setX2Method = lineClass.getMethod("setX2", double.class);
            java.lang.reflect.Method setY2Method = lineClass.getMethod("setY2", double.class);
            java.lang.reflect.Method setStrokeMethod = lineClass.getMethod("setStroke", Class.forName("javafx.scene.paint.Color"));
            java.lang.reflect.Method setStrokeWidthMethod = lineClass.getMethod("setStrokeWidth", double.class);

            for (Turtle.LineSegment segment : shape.getSegments()) {
                Object line = lineConstructor.newInstance();
                setX1Method.invoke(line, segment.x1);
                setY1Method.invoke(line, segment.y1);
                setX2Method.invoke(line, segment.x2);
                setY2Method.invoke(line, segment.y2);
                if (segment.color != null) {
                    Object fxColor = toFxColor(segment.color, segment.alpha);
                    setStrokeMethod.invoke(line, fxColor);
                }
                setStrokeWidthMethod.invoke(line, segment.width);
                addMethod.invoke(getChildrenMethod.invoke(group), line);
            }
            return group;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Create a Turtle polygon node.
     */
    static Object createTurtlePolygonNode(Turtle shape) {
        if (!JavaFXInit.FX_AVAILABLE || !shape.isShowTurtle()) {
            return null;
        }
        try {
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

            Class<?> polygonClass = Class.forName("javafx.scene.shape.Polygon");
            Object polygon = polygonClass.getDeclaredConstructor().newInstance();
            java.lang.reflect.Method getPointsMethod = polygonClass.getMethod("getPoints");
            java.lang.reflect.Method addAllMethod = getPointsMethod.getReturnType().getMethod("addAll", double[].class);
            java.lang.reflect.Method setFillMethod = polygonClass.getMethod("setFill", Class.forName("javafx.scene.paint.Color"));

            double[] points = {x1, y1, x2, y2, x3, y3};
            addAllMethod.invoke(getPointsMethod.invoke(polygon), (Object) points);
            setFillMethod.invoke(polygon, toFxColor(Color.GREEN.toInt(), 1.0));
            return polygon;
        } catch (Exception e) {
            return null;
        }
    }

    private static Object toFxColor(int rgb, double alpha) {
        try {
            Class<?> colorClass = Class.forName("javafx.scene.paint.Color");
            java.lang.reflect.Method rgbMethod = colorClass.getMethod("rgb", int.class, int.class, int.class, double.class);
            return rgbMethod.invoke(null, (rgb >> 16) & 0xff, (rgb >> 8) & 0xff, rgb & 0xff, alpha);
        } catch (Exception e) {
            return null;
        }
    }
}