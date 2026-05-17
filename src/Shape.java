import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Shape extends Actor {
    protected static final class Point {
        final double x;
        final double y;

        Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    protected static final class Bounds {
        double minX;
        double minY;
        double maxX;
        double maxY;

        Bounds(double minX, double minY, double maxX, double maxY) {
            this.minX = minX;
            this.minY = minY;
            this.maxX = maxX;
            this.maxY = maxY;
        }

        boolean intersects(Bounds other) {
            if (other == null) {
                return false;
            }
            return !(maxX < other.minX || other.maxX < minX || maxY < other.minY || other.maxY < minY);
        }

        boolean contains(double x, double y) {
            return x >= minX && x <= maxX && y >= minY && y <= maxY;
        }
    }

    protected static boolean defaultVisibility = true;

    protected double centerX;
    protected double centerY;
    protected double angleDeg;
    protected double scaleFactor = 1.0;
    protected boolean mirroredX;
    protected boolean mirroredY;
    protected double directionRad;
    protected double lastMoveDx;
    protected double lastMoveDy;
    protected boolean visible = defaultVisibility;
    protected boolean isStatic;
    protected boolean destroyed;
    protected boolean trackMouseMove;
    protected boolean reactToMouseEventsWhenInvisible;
    protected boolean mouseLastSeenInsideObject;
    private Boolean cachedHasMouseHandlers;

    protected Color fillColor;
    protected double fillAlpha = 1.0;
    protected Color borderColor;
    protected double borderAlpha = 1.0;
    protected double borderWidth;
    protected Integer tint;

    protected World world;
    protected Group<? extends Shape> belongsToGroup;

    protected Shape() {
        this(0, 0);
    }

    protected Shape(double x, double y) {
        this.centerX = x;
        this.centerY = y;
        this.world = World.getWorld();
    }

    protected final void registerWithWorld() {
        if (world != null) {
            world.registerShape(this);
        }
    }

    public abstract Shape copy();

    protected void copyBaseTo(Shape target) {
        target.centerX = this.centerX;
        target.centerY = this.centerY;
        target.angleDeg = this.angleDeg;
        target.scaleFactor = this.scaleFactor;
        target.mirroredX = this.mirroredX;
        target.mirroredY = this.mirroredY;
        target.directionRad = this.directionRad;
        target.lastMoveDx = this.lastMoveDx;
        target.lastMoveDy = this.lastMoveDy;
        target.visible = this.visible;
        target.isStatic = this.isStatic;
        target.fillAlpha = this.fillAlpha;
        target.borderAlpha = this.borderAlpha;
        target.borderWidth = this.borderWidth;
        target.tint = this.tint;
        target.fillColor = this.fillColor == null ? null
            : new Color(this.fillColor.red, this.fillColor.green, this.fillColor.blue, this.fillColor.alpha);
        target.borderColor = this.borderColor == null ? null
            : new Color(this.borderColor.red, this.borderColor.green, this.borderColor.blue, this.borderColor.alpha);
    }

    public Shape setFillColor(Color color) {
        if (color == null) {
            this.fillColor = null;
            return this;
        }
        this.fillColor = new Color(color.red, color.green, color.blue, fillAlpha);
        return this;
    }

    public Shape setFillColor(Color color, double alpha) {
        this.fillAlpha = clampAlpha(alpha);
        if (color == null) {
            this.fillColor = null;
            return this;
        }
        this.fillColor = new Color(color.red, color.green, color.blue, fillAlpha);
        return this;
    }

    public Shape setFillColor(int color) {
        this.fillColor = Color.fromInt(color);
        this.fillColor.alpha = fillAlpha;
        return this;
    }

    public Shape setFillColor(int color, double alpha) {
        this.fillAlpha = clampAlpha(alpha);
        this.fillColor = Color.fromInt(color);
        this.fillColor.alpha = fillAlpha;
        return this;
    }

    public Shape setFillColor(String color) {
        Color parsed = Color.parse(color);
        if (parsed == null) {
            this.fillColor = null;
            return this;
        }
        parsed.alpha = fillAlpha;
        this.fillColor = parsed;
        return this;
    }

    public Shape setFillColor(String color, double alpha) {
        this.fillAlpha = clampAlpha(alpha);
        Color parsed = Color.parse(color);
        if (parsed == null) {
            this.fillColor = null;
            return this;
        }
        parsed.alpha = fillAlpha;
        this.fillColor = parsed;
        return this;
    }

    public Color getFillColor() {
        return fillColor;
    }

    public int getFillColorAsInt() {
        return fillColor == null ? 0 : fillColor.toInt();
    }

    public Shape setBorderColor(Color color) {
        if (color == null) {
            this.borderColor = null;
            return this;
        }
        this.borderColor = new Color(color.red, color.green, color.blue, borderAlpha);
        return this;
    }

    public Shape setBorderColor(Color color, double alpha) {
        this.borderAlpha = clampAlpha(alpha);
        if (color == null) {
            this.borderColor = null;
            return this;
        }
        this.borderColor = new Color(color.red, color.green, color.blue, borderAlpha);
        return this;
    }

    public Shape setBorderColor(int color) {
        this.borderColor = Color.fromInt(color);
        this.borderColor.alpha = borderAlpha;
        return this;
    }

    public Shape setBorderColor(int color, double alpha) {
        this.borderAlpha = clampAlpha(alpha);
        this.borderColor = Color.fromInt(color);
        this.borderColor.alpha = borderAlpha;
        return this;
    }

    public Shape setBorderColor(String color) {
        Color parsed = Color.parse(color);
        if (parsed == null) {
            this.borderColor = null;
            return this;
        }
        parsed.alpha = borderAlpha;
        this.borderColor = parsed;
        return this;
    }

    public Shape setBorderColor(String color, double alpha) {
        this.borderAlpha = clampAlpha(alpha);
        Color parsed = Color.parse(color);
        if (parsed == null) {
            this.borderColor = null;
            return this;
        }
        parsed.alpha = borderAlpha;
        this.borderColor = parsed;
        return this;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public int getBorderColorAsInt() {
        return borderColor == null ? 0 : borderColor.toInt();
    }

    public Shape setBorderWidth(double width) {
        if (width < 0) {
            return this;
        }
        this.borderWidth = width;
        return this;
    }

    public double getBorderWidth() {
        return borderWidth;
    }

    public Shape setAlpha(double alpha) {
        double a = clampAlpha(alpha);
        this.fillAlpha = a;
        this.borderAlpha = a;
        if (fillColor != null) {
            fillColor.alpha = a;
        }
        if (borderColor != null) {
            borderColor.alpha = a;
        }
        return this;
    }

    public double getAlpha() {
        return fillAlpha;
    }

    public static void setDefaultVisibility(boolean visible) {
        defaultVisibility = visible;
    }

    public Shape setVisible(boolean visible) {
        this.visible = visible;
        return this;
    }

    public boolean isVisible() {
        return visible;
    }

    public Shape setStatic(boolean isStatic) {
        this.isStatic = isStatic;
        return this;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public Shape bringToFront() {
        if (belongsToGroup != null) {
            belongsToGroup.bringChildToFront(this);
        } else if (world != null) {
            world.bringToFront(this);
        }
        return this;
    }

    public Shape sendToBack() {
        if (belongsToGroup != null) {
            belongsToGroup.sendChildToBack(this);
        } else if (world != null) {
            world.sendToBack(this);
        }
        return this;
    }

    public double getCenterX() {
        return centerX;
    }

    public double getCenterY() {
        return centerY;
    }

    public Shape move(double dx, double dy) {
        if (dx != 0 || dy != 0) {
            lastMoveDx = dx;
            lastMoveDy = dy;
        }
        centerX += dx;
        centerY += dy;
        return this;
    }

    public Shape moveTo(double x, double y) {
        return move(x - centerX, y - centerY);
    }

    public Shape setX(double x) {
        return move(x - centerX, 0);
    }

    public Shape setY(double y) {
        return move(0, y - centerY);
    }

    public Shape rotate(double angleDeg) {
        return rotate(angleDeg, centerX, centerY);
    }

    public Shape rotate(double angleDeg, double pivotX, double pivotY) {
        double rad = Math.toRadians(angleDeg);
        double cos = Math.cos(rad);
        double sin = Math.sin(rad);
        double dx = centerX - pivotX;
        double dy = centerY - pivotY;
        double rx = dx * cos - dy * sin;
        double ry = dx * sin + dy * cos;
        centerX = pivotX + rx;
        centerY = pivotY + ry;
        this.angleDeg = normalizeAngle(this.angleDeg + angleDeg);
        return this;
    }

    public Shape scale(double factor) {
        return scale(factor, centerX, centerY);
    }

    public Shape setScale(double newScale) {
        if (newScale <= 0) {
            return this;
        }
        double factor = newScale / scaleFactor;
        return scale(factor, centerX, centerY);
    }

    public Shape scale(double factor, double pivotX, double pivotY) {
        if (factor == 0) {
            return this;
        }
        if (factor < 0) {
            mirroredX = !mirroredX;
            mirroredY = !mirroredY;
            factor = Math.abs(factor);
        }
        centerX = pivotX + (centerX - pivotX) * factor;
        centerY = pivotY + (centerY - pivotY) * factor;
        scaleFactor *= factor;
        return this;
    }

    public Shape mirrorX() {
        mirroredX = !mirroredX;
        return this;
    }

    public Shape mirrorY() {
        mirroredY = !mirroredY;
        return this;
    }

    public Shape defineDirection(double angleDeg) {
        directionRad = Math.toRadians(angleDeg);
        return this;
    }

    public Shape forward(double distance) {
        double dx = distance * Math.cos(directionRad);
        double dy = -distance * Math.sin(directionRad);
        return move(dx, dy);
    }

    public double getAngle() {
        return angleDeg;
    }

    public Shape setAngle(double newAngle) {
        return rotate(newAngle - angleDeg);
    }

    public boolean containsPoint(double x, double y) {
        return getBounds().contains(x, y);
    }

    public boolean isOutsideView() {
        if (world == null) {
            return false;
        }
        Bounds b = getBounds();
        double right = world.getLeft() + world.getWidth();
        double bottom = world.getTop() + world.getHeight();
        return b.maxX < world.getLeft() || b.minX > right || b.maxY < world.getTop() || b.minY > bottom;
    }

    public Shape tint(int color) {
        this.tint = color & 0x00ffffff;
        return this;
    }

    public Shape tint(String color) {
        Color parsed = Color.parse(color);
        if (parsed != null) {
            this.tint = parsed.toInt();
        }
        return this;
    }

    public Shape tint(Color color) {
        this.tint = color == null ? null : color.toInt();
        return this;
    }

    public Direction directionRelativeTo(Shape other) {
        if (other == null) {
            return Direction.top;
        }
        Bounds bb = getBounds();
        Bounds bb1 = other.getBounds();

        double dx1 = bb1.minX - bb.maxX;
        double dx2 = bb.minX - bb1.maxX;
        double dy1 = bb1.minY - bb.maxY;
        double dy2 = bb.minY - bb1.maxY;

        List<DirectionChoice> choices = new ArrayList<>();
        if (lastMoveDx > 0) {
            choices.add(new DirectionChoice(dx1, Direction.left));
        } else if (lastMoveDx < 0) {
            choices.add(new DirectionChoice(dx2, Direction.right));
        }

        if (lastMoveDy > 0) {
            choices.add(new DirectionChoice(dy1, Direction.top));
        } else if (lastMoveDy < 0) {
            choices.add(new DirectionChoice(dy2, Direction.bottom));
        }

        if (choices.isEmpty()) {
            choices.add(new DirectionChoice(dx1, Direction.left));
            choices.add(new DirectionChoice(dx2, Direction.right));
            choices.add(new DirectionChoice(dy1, Direction.top));
            choices.add(new DirectionChoice(dy2, Direction.bottom));
        }

        DirectionChoice best = choices.get(0);
        for (int i = 1; i < choices.size(); i++) {
            DirectionChoice c = choices.get(i);
            if (c.distance > best.distance) {
                best = c;
            }
        }

        return best.direction;
    }

    public Shape moveBackFrom(Shape other, boolean keepColliding) {
        if (other == null) {
            return this;
        }
        double dx = lastMoveDx;
        double dy = lastMoveDy;
        double length = Math.sqrt(dx * dx + dy * dy);
        if (length < 1e-6) {
            return this;
        }

        if (!collidesWith(other)) {
            return this;
        }

        double parameterMax = 0.0;
        move(-dx, -dy);

        double currentParameter = -1.0;
        while (collidesWith(other)) {
            parameterMax = currentParameter;
            double newParameter = currentParameter * 2.0;
            move(dx * (newParameter - currentParameter), dy * (newParameter - currentParameter));
            currentParameter = newParameter;
            if ((currentParameter + 1.0) * length < -100.0) {
                move(dx * (-1.0 - currentParameter), dy * (-1.0 - currentParameter));
                return this;
            }
        }

        double parameterMin = currentParameter;
        boolean isColliding = false;
        while ((parameterMax - parameterMin) * length > 1.0) {
            double next = (parameterMax + parameterMin) / 2.0;
            move(dx * (next - currentParameter), dy * (next - currentParameter));
            isColliding = collidesWith(other);
            if (isColliding) {
                parameterMax = next;
            } else {
                parameterMin = next;
            }
            currentParameter = next;
        }

        if (keepColliding && !isColliding) {
            move(dx * (parameterMax - currentParameter), dy * (parameterMax - currentParameter));
        } else if (isColliding && !keepColliding) {
            move(dx * (parameterMin - currentParameter), dy * (parameterMin - currentParameter));
        }

        lastMoveDx = dx;
        lastMoveDy = dy;
        return this;
    }

    public boolean collidesWith(Shape other) {
        if (other == null || destroyed || other.destroyed) {
            return false;
        }
        return getBounds().intersects(other.getBounds());
    }

    public boolean collidesWithAnyShape() {
        if (world == null || destroyed) {
            return false;
        }
        for (Shape shape : world.getAllShapes()) {
            if (shape == this || shape.destroyed) {
                continue;
            }
            if (collidesWith(shape)) {
                return true;
            }
        }
        return false;
    }

    public Shape getFirstCollidingShape() {
        if (world == null || destroyed) {
            return null;
        }
        for (Shape shape : world.getAllShapes()) {
            if (shape == this || shape.destroyed) {
                continue;
            }
            if (collidesWith(shape)) {
                return shape;
            }
        }
        return null;
    }

    public List<Shape> getCollidingShapes(Group<? extends Shape> group) {
        if (group == null) {
            return Collections.emptyList();
        }
        List<Shape> result = new ArrayList<>();
        for (Shape shape : group.getChildren()) {
            if (shape != this && collidesWith(shape)) {
                result.add(shape);
            }
        }
        return result;
    }

    public World getWorld() {
        return world == null ? World.getWorld() : world;
    }

    @Override
    public void destroy() {
        if (destroyed) {
            return;
        }
        destroyed = true;
        if (belongsToGroup != null) {
            belongsToGroup.remove(this);
        }
        if (world != null) {
            world.deregisterShape(this);
        }
        super.destroy();
    }

    /**
     * Wird aufgerufen, wenn eine Maustaste über der Form losgelassen wird.
     *
     * @param x Welt-X-Koordinate des Ereignisses
     * @param y Welt-Y-Koordinate des Ereignisses
     * @param button ganzzahliger Tasten-Code. Verwendet JavaFX MouseButton.ordinal():
     *               0 = NONE, 1 = PRIMARY (normalerweise links), 2 = MIDDLE, 3 = SECONDARY (normalerweise rechts),
     *               höhere Werte für zusätzliche Tasten.
     */
    public void onMouseUp(double x, double y, int button) {
    }

    /**
     * Wird aufgerufen, wenn eine Maustaste über der Form gedrückt wird.
     *
     * @param x Welt-X-Koordinate des Ereignisses
     * @param y Welt-Y-Koordinate des Ereignisses
     * @param button ganzzahliger Tasten-Code. Verwendet JavaFX MouseButton.ordinal():
     *               0 = NONE, 1 = PRIMARY (normalerweise links), 2 = MIDDLE, 3 = SECONDARY (normalerweise rechts),
     *               höhere Werte für zusätzliche Tasten.
     */
    public void onMouseDown(double x, double y, int button) {
    }

    /**
     * Wird aufgerufen, wenn sich die Maus bewegt. Formen erhalten Bewegungsereignisse,
     * wenn sich der Zeiger innerhalb der Form befindet oder wenn {@link #trackMouseMove(boolean)}
     * aktiviert ist.
     *
     * @param x Welt-X-Koordinate der Maus
     * @param y Welt-Y-Koordinate der Maus
     */
    public void onMouseMove(double x, double y) {
    }

    /**
     * Wird einmal ausgelöst, wenn die Maus den Bereich der Form betritt.
     *
     * @param x Welt-X-Koordinate der Maus
     * @param y Welt-Y-Koordinate der Maus
     */
    public void onMouseEnter(double x, double y) {
    }

    /**
     * Wird einmal ausgelöst, wenn die Maus den Bereich der Form verlässt.
     *
     * @param x Welt-X-Koordinate der Maus
     * @param y Welt-Y-Koordinate der Maus
     */
    public void onMouseLeave(double x, double y) {
    }

    /**
     * Aktiviert oder deaktiviert die Verfolgung von Mausbewegungsereignissen,
     * auch wenn sich der Zeiger nicht strikt innerhalb der Form befindet. Wenn
     * aktiviert, erhält die Form kontinuierlich {@link #onMouseMove(double,double)}
     * mit der aktuellen Mausposition.
     *
     * @param enabled true, um die kontinuierliche Verfolgung von Mausbewegungen zu aktivieren
     * @return diese Form zur Verkettung
     */
    public Shape trackMouseMove(boolean enabled) {
        this.trackMouseMove = enabled;
        return this;
    }

    /**
     * Wenn auf true gesetzt, erhält die Form weiterhin Mausereignisse, selbst wenn
     * sie nicht sichtbar ist. Nützlich für unsichtbare Trefferbereiche.
     *
     * @param enabled ob auf Mausereignisse reagiert werden soll, wenn die Form unsichtbar ist
     * @return diese Form zur Verkettung
     */
    public Shape reactToMouseEventsWhenInvisible(boolean enabled) {
        this.reactToMouseEventsWhenInvisible = enabled;
        return this;
    }

    boolean hasMouseHandlers() {
        if (cachedHasMouseHandlers == null) {
            cachedHasMouseHandlers = isMouseMethodOverridden("onMouseUp", double.class, double.class, int.class)
                || isMouseMethodOverridden("onMouseDown", double.class, double.class, int.class)
                || isMouseMethodOverridden("onMouseMove", double.class, double.class)
                || isMouseMethodOverridden("onMouseEnter", double.class, double.class)
                || isMouseMethodOverridden("onMouseLeave", double.class, double.class);
        }
        return cachedHasMouseHandlers;
    }

    private boolean isMouseMethodOverridden(String methodName, Class<?>... paramTypes) {
        try {
            return getClass().getMethod(methodName, paramTypes).getDeclaringClass() != Shape.class;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    public Shape defineCenter(double x, double y) {
        centerX = x;
        centerY = y;
        return this;
    }

    public Shape defineCenterRelative(double relX, double relY) {
        Bounds b = getBounds();
        double x = b.minX + (b.maxX - b.minX) * relX;
        double y = b.minY + (b.maxY - b.minY) * relY;
        return defineCenter(x, y);
    }

    protected Bounds getBounds() {
        List<Point> points = getTransformedPoints();
        if (points.isEmpty()) {
            return new Bounds(centerX, centerY, centerX, centerY);
        }
        double minX = points.get(0).x;
        double maxX = points.get(0).x;
        double minY = points.get(0).y;
        double maxY = points.get(0).y;
        for (Point p : points) {
            minX = Math.min(minX, p.x);
            maxX = Math.max(maxX, p.x);
            minY = Math.min(minY, p.y);
            maxY = Math.max(maxY, p.y);
        }
        return new Bounds(minX, minY, maxX, maxY);
    }

    protected List<Point> getLocalPoints() {
        return Collections.emptyList();
    }

    protected List<Point> getTransformedPoints() {
        List<Point> local = getLocalPoints();
        if (local.isEmpty()) {
            return Collections.emptyList();
        }
        List<Point> transformed = new ArrayList<>(local.size());
        for (Point p : local) {
            transformed.add(transformPoint(p));
        }
        return transformed;
    }

    protected Point transformPoint(Point local) {
        double scaleX = mirroredX ? -scaleFactor : scaleFactor;
        double scaleY = mirroredY ? -scaleFactor : scaleFactor;
        double x = local.x * scaleX;
        double y = local.y * scaleY;
        double rad = Math.toRadians(angleDeg);
        double cos = Math.cos(rad);
        double sin = Math.sin(rad);
        double rx = x * cos - y * sin;
        double ry = x * sin + y * cos;
        return new Point(centerX + rx, centerY + ry);
    }

    protected Point inverseTransformPoint(double x, double y) {
        double dx = x - centerX;
        double dy = y - centerY;
        double rad = Math.toRadians(-angleDeg);
        double cos = Math.cos(rad);
        double sin = Math.sin(rad);
        double rx = dx * cos - dy * sin;
        double ry = dx * sin + dy * cos;
        double scaleX = mirroredX ? -scaleFactor : scaleFactor;
        double scaleY = mirroredY ? -scaleFactor : scaleFactor;
        if (scaleX == 0 || scaleY == 0) {
            return new Point(0, 0);
        }
        return new Point(rx / scaleX, ry / scaleY);
    }

    protected static double clampAlpha(double value) {
        return Math.max(0.0, Math.min(1.0, value));
    }

    protected static double normalizeAngle(double angle) {
        double a = angle % 360.0;
        if (a < 0) {
            a += 360.0;
        }
        return a;
    }

    private static final class DirectionChoice {
        final double distance;
        final Direction direction;

        private DirectionChoice(double distance, Direction direction) {
            this.distance = distance;
            this.direction = direction;
        }
    }
}
