/**
 * An axis-aligned rectangle.
 */
public class Rectangle extends FilledShape {
    private double width;
    private double height;

    public Rectangle() {
        this(0, 0, 100, 100);
    }

    public Rectangle(double left, double top, double width, double height) {
        super(left + width / 2.0, top + height / 2.0);
        this.width = width;
        this.height = height;
        registerWithWorld();
    }

    public Rectangle setWidth(double width) {
        if (width < 0) {
            return this;
        }
        double left = getLeft();
        this.width = width / scaleFactor;
        this.centerX = left + width / 2.0;
        return this;
    }

    public Rectangle setHeight(double height) {
        if (height < 0) {
            return this;
        }
        double top = getTop();
        this.height = height / scaleFactor;
        this.centerY = top + height / 2.0;
        return this;
    }

    public double getWidth() {
        return width * scaleFactor;
    }

    public double getHeight() {
        return height * scaleFactor;
    }

    @Override
    public Rectangle moveTo(double x, double y) {
        this.centerX = x + getWidth() / 2.0;
        this.centerY = y + getHeight() / 2.0;
        return this;
    }

    @Override
    public Rectangle copy() {
        Rectangle copy = new Rectangle(getLeft(), getTop(), width, height);
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
        return Math.abs(local.x) <= width / 2.0 && Math.abs(local.y) <= height / 2.0;
    }

    @Override
    public String toString() {
        return "{width: " + getWidth() + ", height: " + getHeight() + ", centerX: " + getCenterX()
            + ", centerY: " + getCenterY() + "}";
    }

    private double getLeft() {
        return centerX - getWidth() / 2.0;
    }

    private double getTop() {
        return centerY - getHeight() / 2.0;
    }
}
