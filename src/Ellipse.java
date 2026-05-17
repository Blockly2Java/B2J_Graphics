public class Ellipse extends FilledShape {
    private double radiusX;
    private double radiusY;

    public Ellipse() {
        this(200, 100, 100, 50);
    }

    public Ellipse(double x, double y, double rX, double rY) {
        super(x, y);
        this.radiusX = rX;
        this.radiusY = rY;
        registerWithWorld();
    }

    public Ellipse setRadiusX(double radiusX) {
        if (radiusX < 0) {
            return this;
        }
        this.radiusX = radiusX / scaleFactor;
        return this;
    }

    public Ellipse setRadiusY(double radiusY) {
        if (radiusY < 0) {
            return this;
        }
        this.radiusY = radiusY / scaleFactor;
        return this;
    }

    public double getRadiusX() {
        return radiusX * scaleFactor;
    }

    public double getRadiusY() {
        return radiusY * scaleFactor;
    }

    @Override
    public Ellipse copy() {
        Ellipse copy = new Ellipse(centerX, centerY, radiusX, radiusY);
        copyBaseTo(copy);
        return copy;
    }

    @Override
    public boolean containsPoint(double x, double y) {
        Point local = inverseTransformPoint(x, y);
        if (radiusX == 0 || radiusY == 0) {
            return false;
        }
        double dx = local.x / radiusX;
        double dy = local.y / radiusY;
        return dx * dx + dy * dy <= 1.0;
    }

    @Override
    protected Bounds getBounds() {
        double rx = Math.abs(radiusX * scaleFactor);
        double ry = Math.abs(radiusY * scaleFactor);
        if (angleDeg == 0) {
            return new Bounds(centerX - rx, centerY - ry, centerX + rx, centerY + ry);
        }
        return super.getBounds();
    }

    @Override
    protected java.util.List<Point> getLocalPoints() {
        int steps = 16;
        java.util.List<Point> points = new java.util.ArrayList<>(steps);
        double delta = Math.PI * 2.0 / steps;
        for (int i = 0; i < steps; i++) {
            double a = delta * i;
            points.add(new Point(radiusX * Math.cos(a), radiusY * Math.sin(a)));
        }
        return points;
    }

    @Override
    public String toString() {
        return "{rx: " + getRadiusX() + ", ry: " + getRadiusY() + ", centerX: " + getCenterX() + ", centerY: " + getCenterY() + "}";
    }
}
