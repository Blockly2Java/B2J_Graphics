public class Triangle extends Shape {
    private double x1;
    private double y1;
    private double x2;
    private double y2;
    private double x3;
    private double y3;

    public Triangle() {
        this(0, 0, 100, 0, 0, 100);
    }

    public Triangle(double x1, double y1, double x2, double y2, double x3, double y3) {
        super((x1 + x2 + x3) / 3.0, (y1 + y2 + y3) / 3.0);
        setLocalPointsFromWorld(x1, y1, x2, y2, x3, y3);
        registerWithWorld();
    }

    public Triangle setPoints(double x1, double y1, double x2, double y2, double x3, double y3) {
        centerX = (x1 + x2 + x3) / 3.0;
        centerY = (y1 + y2 + y3) / 3.0;
        setLocalPointsFromWorld(x1, y1, x2, y2, x3, y3);
        return this;
    }

    @Override
    public Triangle copy() {
        Triangle copy = new Triangle(0, 0, 0, 0, 0, 0);
        copy.x1 = this.x1;
        copy.y1 = this.y1;
        copy.x2 = this.x2;
        copy.y2 = this.y2;
        copy.x3 = this.x3;
        copy.y3 = this.y3;
        copyBaseTo(copy);
        return copy;
    }

    @Override
    protected java.util.List<Point> getLocalPoints() {
        java.util.List<Point> points = new java.util.ArrayList<>(3);
        points.add(new Point(x1, y1));
        points.add(new Point(x2, y2));
        points.add(new Point(x3, y3));
        return points;
    }

    @Override
    public boolean containsPoint(double x, double y) {
        Point local = inverseTransformPoint(x, y);
        return pointInTriangle(local.x, local.y, x1, y1, x2, y2, x3, y3);
    }

    @Override
    public String toString() {
        Point p1 = transformPoint(new Point(x1, y1));
        Point p2 = transformPoint(new Point(x2, y2));
        Point p3 = transformPoint(new Point(x3, y3));
        return "{(" + p1.x + ", " + p1.y + "), (" + p2.x + ", " + p2.y + "), (" + p3.x + ", " + p3.y + ")}";
    }

    private void setLocalPointsFromWorld(double wx1, double wy1, double wx2, double wy2, double wx3, double wy3) {
        Point p1 = inverseTransformPoint(wx1, wy1);
        Point p2 = inverseTransformPoint(wx2, wy2);
        Point p3 = inverseTransformPoint(wx3, wy3);
        this.x1 = p1.x;
        this.y1 = p1.y;
        this.x2 = p2.x;
        this.y2 = p2.y;
        this.x3 = p3.x;
        this.y3 = p3.y;
    }

    private static boolean pointInTriangle(double px, double py,
        double ax, double ay, double bx, double by, double cx, double cy) {
        double v0x = cx - ax;
        double v0y = cy - ay;
        double v1x = bx - ax;
        double v1y = by - ay;
        double v2x = px - ax;
        double v2y = py - ay;

        double dot00 = v0x * v0x + v0y * v0y;
        double dot01 = v0x * v1x + v0y * v1y;
        double dot02 = v0x * v2x + v0y * v2y;
        double dot11 = v1x * v1x + v1y * v1y;
        double dot12 = v1x * v2x + v1y * v2y;

        double invDenom = (dot00 * dot11 - dot01 * dot01);
        if (invDenom == 0) {
            return false;
        }
        invDenom = 1.0 / invDenom;
        double u = (dot11 * dot02 - dot01 * dot12) * invDenom;
        double v = (dot00 * dot12 - dot01 * dot02) * invDenom;
        return u >= 0 && v >= 0 && (u + v) <= 1.0;
    }
}
