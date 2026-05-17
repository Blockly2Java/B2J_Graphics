import java.util.ArrayList;
import java.util.List;

/**
 * Ein Polygon, definiert durch eine Liste von Punkten.
 */
public class Polygon extends FilledShape {
    private final List<Point> points = new ArrayList<>();
    private boolean closeAndFill;
    private boolean isClosed;

    public Polygon() {
        this(true, defaultPoints());
    }

    public Polygon(boolean closeAndFill) {
        this(closeAndFill, defaultPoints());
    }

    public Polygon(boolean closeAndFill, double... coordinates) {
        super(0, 0);
        this.closeAndFill = closeAndFill;
        setPointsInternal(coordinates);
        registerWithWorld();
    }

    public Polygon(Shape shape) {
        super(0, 0);
        this.closeAndFill = true;
        if (shape != null) {
            Bounds b = shape.getBounds();
            setPointsInternal(new double[] { b.minX, b.minY, b.minX, b.maxY, b.maxX, b.maxY, b.maxX, b.minY });
        } else {
            setPointsInternal(new double[0]);
        }
        registerWithWorld();
    }

    public void addPoint(double x, double y) {
        Point local = inverseTransformPoint(x, y);
        points.add(local);
    }

    public void setPoints(double[] points) {
        setPointsInternal(points);
    }

    public void addPoints(double[] points) {
        if (points == null) {
            return;
        }
        for (int i = 0; i + 1 < points.length; i += 2) {
            addPoint(points[i], points[i + 1]);
        }
    }

    public void insertPoint(double x, double y, int index) {
        if (index < 0) {
            index = 0;
        }
        if (index > points.size()) {
            index = points.size();
        }
        Point local = inverseTransformPoint(x, y);
        points.add(index, local);
    }

    public void movePointTo(double x, double y, int index) {
        if (index < 0 || index >= points.size()) {
            return;
        }
        Point local = inverseTransformPoint(x, y);
        points.set(index, local);
    }

    public void open() {
        isClosed = false;
    }

    public void close() {
        isClosed = true;
    }

    @Override
    public boolean containsPoint(double x, double y) {
        if (points.isEmpty()) {
            return false;
        }
        Point local = inverseTransformPoint(x, y);
        if (closeAndFill || isClosed) {
            return pointInPolygon(local.x, local.y);
        }
        return distanceToPolyline(local.x, local.y) <= Math.max(1.0, borderWidth / 2.0);
    }

    @Override
    protected List<Point> getLocalPoints() {
        return new ArrayList<>(points);
    }

    @Override
    public Polygon copy() {
        Polygon copy = new Polygon(closeAndFill, new double[0]);
        copy.points.clear();
        copy.points.addAll(this.points);
        copy.isClosed = this.isClosed;
        copy.centerX = this.centerX;
        copy.centerY = this.centerY;
        copyBaseTo(copy);
        return copy;
    }

    @Override
    public String toString() {
        if (points.isEmpty()) {
            return "{points: []}";
        }
        StringBuilder builder = new StringBuilder("{points: [");
        for (int i = 0; i < points.size(); i++) {
            Point p = transformPoint(points.get(i));
            builder.append("(").append(p.x).append(", ").append(p.y).append(")");
            if (i < points.size() - 1) {
                builder.append(", ");
            }
        }
        builder.append("]}");
        return builder.toString();
    }

    protected void setPointsInternal(double[] coordinates) {
        points.clear();
        if (coordinates == null || coordinates.length < 2) {
            return;
        }
        int count = coordinates.length / 2;
        double sumX = 0.0;
        double sumY = 0.0;
        for (int i = 0; i < count; i++) {
            sumX += coordinates[i * 2];
            sumY += coordinates[i * 2 + 1];
        }
        centerX = sumX / count;
        centerY = sumY / count;
        for (int i = 0; i < count; i++) {
            double x = coordinates[i * 2] - centerX;
            double y = coordinates[i * 2 + 1] - centerY;
            points.add(new Point(x, y));
        }
    }

    private boolean pointInPolygon(double x, double y) {
        boolean inside = false;
        int n = points.size();
        for (int i = 0, j = n - 1; i < n; j = i++) {
            Point pi = points.get(i);
            Point pj = points.get(j);
            boolean intersect = ((pi.y > y) != (pj.y > y))
                && (x < (pj.x - pi.x) * (y - pi.y) / (pj.y - pi.y + 1e-12) + pi.x);
            if (intersect) {
                inside = !inside;
            }
        }
        return inside;
    }

    private double distanceToPolyline(double x, double y) {
        if (points.size() < 2) {
            return Double.MAX_VALUE;
        }
        double min = Double.MAX_VALUE;
        for (int i = 0; i < points.size() - 1; i++) {
            Point a = points.get(i);
            Point b = points.get(i + 1);
            min = Math.min(min, distancePointToSegment(x, y, a.x, a.y, b.x, b.y));
        }
        if (isClosed || closeAndFill) {
            Point a = points.get(points.size() - 1);
            Point b = points.get(0);
            min = Math.min(min, distancePointToSegment(x, y, a.x, a.y, b.x, b.y));
        }
        return min;
    }

    private double distancePointToSegment(double px, double py, double ax, double ay, double bx, double by) {
        double dx = bx - ax;
        double dy = by - ay;
        double lenSq = dx * dx + dy * dy;
        if (lenSq == 0) {
            double cx = px - ax;
            double cy = py - ay;
            return Math.sqrt(cx * cx + cy * cy);
        }
        double t = ((px - ax) * dx + (py - ay) * dy) / lenSq;
        t = Math.max(0.0, Math.min(1.0, t));
        double cx = ax + t * dx;
        double cy = ay + t * dy;
        double ddx = px - cx;
        double ddy = py - cy;
        return Math.sqrt(ddx * ddx + ddy * ddy);
    }

    private static double[] defaultPoints() {
        double[] points = new double[12];
        for (int i = 0; i < 6; i++) {
            double angle = Math.PI / 3.0 * i;
            points[i * 2] = 100 + 50 * Math.cos(angle);
            points[i * 2 + 1] = 100 - 50 * Math.sin(angle);
        }
        return points;
    }
}
