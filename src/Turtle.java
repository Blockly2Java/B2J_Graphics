// AUTO-GENERATED FIX: Turtle.java rewrite to resolve corruption
import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
import java.util.List;

/**
 * Turtle-Grafik-Helfer, der beim Bewegen zeichnet.
 */
class Turtle extends FilledShape {

    private static final boolean FX_AVAILABLE = !GraphicsEnvironment.isHeadless();
    private static final double DEFAULT_TURTLE_SIZE = 20.0;

    final List<LineSegment> segments = new ArrayList<>();
    boolean showTurtle = true;
    boolean penIsDown = true;
    private double turtleAngleDeg = 0.0;
    double turtleSize = DEFAULT_TURTLE_SIZE;

    public Turtle() {
        this(100, 200, true);
    }

    public Turtle(double x, double y) {
        this(x, y, true);
    }

    public Turtle(double x, double y, boolean showTurtle) {
        super(x, y);
        this.showTurtle = showTurtle;
        segments.add(new LineSegment(x, y, x, y, borderColor == null ? null : borderColor.toInt(), borderAlpha, borderWidth));
        registerWithWorld();
    }

    @Override
    public Turtle forward(double length) {
        double rad = Math.toRadians(turtleAngleDeg);
        double newX = centerX + length * Math.cos(rad);
        double newY = centerY - length * Math.sin(rad);
        if (penIsDown) {
            segments.add(new LineSegment(centerX, centerY, newX, newY,
                borderColor == null ? null : borderColor.toInt(), borderAlpha, borderWidth));
        }
        centerX = newX;
        centerY = newY;
        if (FX_AVAILABLE && world != null) {
            JavaFXBridge.updateShape(this);
        }
        return this;
    }

    public void turn(double angleInDeg) {
        turtleAngleDeg = normalizeAngle(turtleAngleDeg + angleInDeg);
        if (FX_AVAILABLE && world != null) {
            JavaFXBridge.updateShape(this);
        }
    }

    public void penUp() {
        penIsDown = false;
    }

    public void penDown() {
        penIsDown = true;
    }

    public void closeAndFill(boolean closeAndFill) {
        if (!closeAndFill || segments.size() < 2) {
            return;
        }
        LineSegment first = segments.get(0);
        LineSegment last = segments.get(segments.size() - 1);
        segments.add(new LineSegment(last.x2, last.y2, first.x1, first.y1,
            borderColor == null ? null : borderColor.toInt(), borderAlpha, borderWidth));
        if (FX_AVAILABLE && world != null) {
            JavaFXBridge.updateShape(this);
        }
    }

    public void showTurtle(boolean show) {
        this.showTurtle = show;
        if (FX_AVAILABLE && world != null) {
            JavaFXBridge.updateShape(this);
        }
    }

    public void clear() {
        segments.clear();
        segments.add(new LineSegment(centerX, centerY, centerX, centerY,
            borderColor == null ? null : borderColor.toInt(), borderAlpha, borderWidth));
        if (FX_AVAILABLE && world != null) {
            JavaFXBridge.updateShape(this);
        }
    }

    public double getLastSegmentLength() {
        if (segments.isEmpty()) {
            return 0.0;
        }
        LineSegment seg = segments.get(segments.size() - 1);
        return Math.hypot(seg.x2 - seg.x1, seg.y2 - seg.y1);
    }

    public double getTurtleAngle() {
        return turtleAngleDeg;
    }

    public double getTurtleAngleDeg() {
        return turtleAngleDeg;
    }

    public double getTurtleSize() {
        return turtleSize;
    }

    public boolean isShowTurtle() {
        return showTurtle;
    }

    public List<LineSegment> getSegments() {
        return segments;
    }

    @Override
    public Turtle moveTo(double x, double y) {
        centerX = x;
        centerY = y;
        if (FX_AVAILABLE && world != null) {
            JavaFXBridge.updateShape(this);
        }
        return this;
    }

    @Override
    public Turtle copy() {
        Turtle copy = new Turtle(centerX, centerY, showTurtle);
        copy.penIsDown = penIsDown;
        copy.turtleAngleDeg = turtleAngleDeg;
        copy.turtleSize = turtleSize;
        for (LineSegment segment : segments) {
            copy.segments.add(segment.copy());
        }
        copyBaseTo(copy);
        return copy;
    }

    public Object createLineNode() {
        if (!FX_AVAILABLE) {
            return null;
        }
        return JavaFXBridge.createTurtleLineNode(this);
    }

    public Object createTurtleNode() {
        if (!FX_AVAILABLE || !showTurtle) {
            return null;
        }
        return JavaFXBridge.createTurtlePolygonNode(this);
    }

    @Override
    protected Bounds getBounds() {
        if (segments.isEmpty()) {
            return new Bounds(centerX, centerY, centerX, centerY);
        }
        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double maxX = -Double.MAX_VALUE;
        double maxY = -Double.MAX_VALUE;
        for (LineSegment segment : segments) {
            minX = Math.min(minX, Math.min(segment.x1, segment.x2));
            minY = Math.min(minY, Math.min(segment.y1, segment.y2));
            maxX = Math.max(maxX, Math.max(segment.x1, segment.x2));
            maxY = Math.max(maxY, Math.max(segment.y1, segment.y2));
        }
        return new Bounds(minX, minY, maxX, maxY);
    }

    static class LineSegment {
        final double x1;
        final double y1;
        final double x2;
        final double y2;
        final Integer color;
        final double alpha;
        final double width;

        LineSegment(double x1, double y1, double x2, double y2, Integer color, double alpha, double width) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
            this.color = color;
            this.alpha = alpha;
            this.width = width;
        }

        LineSegment copy() {
            return new LineSegment(x1, y1, x2, y2, color, alpha, width);
        }
    }
}