public class Line extends FilledShape {
    private double x1;
    private double y1;
    private double x2;
    private double y2;

    public Line() {
        this(0, 0, 100, 100);
    }

    public Line(double x1, double y1, double x2, double y2) {
        super((x1 + x2) / 2.0, (y1 + y2) / 2.0);
        setLocalPointsFromWorld(x1, y1, x2, y2);
        if (borderColor == null) {
            borderColor = new Color(255, 255, 255, borderAlpha);
        }
        if (borderWidth == 0) {
            borderWidth = 10.0;
        }
        registerWithWorld();
    }

    public Line setPoints(double x1, double y1, double x2, double y2) {
        centerX = (x1 + x2) / 2.0;
        centerY = (y1 + y2) / 2.0;
        setLocalPointsFromWorld(x1, y1, x2, y2);
        return this;
    }

    @Override
    public Line copy() {
        Line copy = new Line(0, 0, 0, 0);
        copy.x1 = this.x1;
        copy.y1 = this.y1;
        copy.x2 = this.x2;
        copy.y2 = this.y2;
        copyBaseTo(copy);
        return copy;
    }

    @Override
    protected java.util.List<Point> getLocalPoints() {
        java.util.List<Point> points = new java.util.ArrayList<>(2);
        points.add(new Point(x1, y1));
        points.add(new Point(x2, y2));
        return points;
    }

    @Override
    public boolean containsPoint(double x, double y) {
        Point local = inverseTransformPoint(x, y);
        double px = local.x;
        double py = local.y;
        double dx = x2 - x1;
        double dy = y2 - y1;
        double lenSq = dx * dx + dy * dy;
        if (lenSq == 0) {
            double distSq = (px - x1) * (px - x1) + (py - y1) * (py - y1);
            double radius = Math.max(1.0, borderWidth / 2.0) / Math.max(1.0, scaleFactor);
            return distSq <= radius * radius;
        }
        double t = ((px - x1) * dx + (py - y1) * dy) / lenSq;
        t = Math.max(0.0, Math.min(1.0, t));
        double cx = x1 + t * dx;
        double cy = y1 + t * dy;
        double distSq = (px - cx) * (px - cx) + (py - cy) * (py - cy);
        double radius = Math.max(1.0, borderWidth / 2.0) / Math.max(1.0, scaleFactor);
        return distSq <= radius * radius;
    }

    @Override
    public String toString() {
        Point p1 = transformPoint(new Point(x1, y1));
        Point p2 = transformPoint(new Point(x2, y2));
        return "{x1: " + p1.x + ", y1: " + p1.y + ", x2: " + p2.x + ", y2: " + p2.y + "}";
    }

    private void setLocalPointsFromWorld(double wx1, double wy1, double wx2, double wy2) {
        Point p1 = inverseTransformPoint(wx1, wy1);
        Point p2 = inverseTransformPoint(wx2, wy2);
        this.x1 = p1.x;
        this.y1 = p1.y;
        this.x2 = p2.x;
        this.y2 = p2.y;
    }
}
