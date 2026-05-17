/**
 * Ein Rechteck mit abgerundeten Ecken.
 */
public class RoundedRectangle extends FilledShape {
    private double radius;
    private double width;
    private double height;

    public RoundedRectangle() {
        this(0, 0, 100, 100, 10);
    }

    public RoundedRectangle(double left, double top, double width, double height, double radius) {
        super(left + width / 2.0, top + height / 2.0);
        this.width = width;
        this.height = height;
        this.radius = radius;
        registerWithWorld();
    }

    public RoundedRectangle setWidth(double width) {
        if (width < 0) {
            return this;
        }
        double left = getLeft();
        this.width = width / scaleFactor;
        this.centerX = left + width / 2.0;
        return this;
    }

    public RoundedRectangle setHeight(double height) {
        if (height < 0) {
            return this;
        }
        double top = getTop();
        this.height = height / scaleFactor;
        this.centerY = top + height / 2.0;
        return this;
    }

    public RoundedRectangle setRadius(double radius) {
        if (radius < 0) {
            return this;
        }
        this.radius = radius / scaleFactor;
        return this;
    }

    public double getWidth() {
        return width * scaleFactor;
    }

    public double getHeight() {
        return height * scaleFactor;
    }

    public double getRadius() {
        return radius * scaleFactor;
    }

    @Override
    public RoundedRectangle moveTo(double x, double y) {
        this.centerX = x + getWidth() / 2.0;
        this.centerY = y + getHeight() / 2.0;
        return this;
    }

    @Override
    public RoundedRectangle copy() {
        RoundedRectangle copy = new RoundedRectangle(getLeft(), getTop(), width, height, radius);
        copyBaseTo(copy);
        return copy;
    }

    @Override
    protected java.util.List<Point> getLocalPoints() {
        double hw = width / 2.0;
        double hh = height / 2.0;
        java.util.List<Point> points = new java.util.ArrayList<>(4);
        points.add(new Point(-hw, -hh));
        points.add(new Point(-hw, hh));
        points.add(new Point(hw, hh));
        points.add(new Point(hw, -hh));
        return points;
    }

    @Override
    public boolean containsPoint(double x, double y) {
        Point local = inverseTransformPoint(x, y);
        double hw = width / 2.0;
        double hh = height / 2.0;
        if (Math.abs(local.x) > hw || Math.abs(local.y) > hh) {
            return false;
        }
        double r = Math.min(radius, Math.min(hw, hh));
        double cx = Math.abs(local.x) - (hw - r);
        double cy = Math.abs(local.y) - (hh - r);
        if (cx <= 0 || cy <= 0) {
            return true;
        }
        return cx * cx + cy * cy <= r * r;
    }

    @Override
    public String toString() {
        return "{width: " + getWidth() + ", height: " + getHeight() + ", radius: " + getRadius()
            + ", centerX: " + getCenterX() + ", centerY: " + getCenterY() + "}";
    }

    private double getLeft() {
        return centerX - getWidth() / 2.0;
    }

    private double getTop() {
        return centerY - getHeight() / 2.0;
    }
}
