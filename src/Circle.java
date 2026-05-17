/**
 * Ein Kreis, zentriert an einer Welt-Position.
 */
public class Circle extends FilledShape {
    private double radius;

    public Circle() {
        this(50, 50, 50);
    }

    public Circle(double x, double y, double r) {
        super(x, y);
        this.radius = r;
        registerWithWorld();
    }

    public Circle setRadius(double radius) {
        if (radius < 0) {
            return this;
        }
        this.radius = radius / scaleFactor;
        return this;
    }

    public double getRadius() {
        return radius * scaleFactor;
    }

    @Override
    public Circle copy() {
        Circle copy = new Circle(centerX, centerY, radius);
        copyBaseTo(copy);
        return copy;
    }

    @Override
    public boolean containsPoint(double x, double y) {
        Point local = inverseTransformPoint(x, y);
        double dx = local.x;
        double dy = local.y;
        return dx * dx + dy * dy <= radius * radius;
    }

    @Override
    protected Bounds getBounds() {
        double r = Math.abs(radius * scaleFactor);
        return new Bounds(centerX - r, centerY - r, centerX + r, centerY + r);
    }

    @Override
    public String toString() {
        return "{r: " + getRadius() + ", centerX: " + getCenterX() + ", centerY: " + getCenterY() + "}";
    }
}
