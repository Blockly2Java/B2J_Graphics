public class Sector extends FilledShape {
    private double radius;
    private double startAngleDeg;
    private double endAngleDeg;
    private boolean drawRadii = true;

    public Sector() {
        this(200, 200, 200, 0, 180);
    }

    public Sector(double mx, double my, double radius, double startAngle, double endAngle) {
        super(mx, my);
        this.radius = radius;
        this.startAngleDeg = startAngle;
        this.endAngleDeg = endAngle;
        registerWithWorld();
    }

    public void setRadius(double radius) {
        if (radius < 0) {
            return;
        }
        this.radius = radius / scaleFactor;
    }

    public double getRadiusX() {
        return radius * scaleFactor;
    }

    public void setStartAngle(double startAngle) {
        this.startAngleDeg = startAngle;
    }

    public double getStartAngleX() {
        return startAngleDeg;
    }

    public void setEndAngle(double endAngle) {
        this.endAngleDeg = endAngle;
    }

    public double getEndAngleX() {
        return endAngleDeg;
    }

    public void drawRadii(boolean drawRadii) {
        this.drawRadii = drawRadii;
    }

    @Override
    public boolean containsPoint(double x, double y) {
        Point local = inverseTransformPoint(x, y);
        double dx = local.x;
        double dy = local.y;
        double r2 = dx * dx + dy * dy;
        if (r2 > radius * radius) {
            return false;
        }
        double angle = Math.toDegrees(Math.atan2(-dy, dx));
        return angleWithin(angle, startAngleDeg, endAngleDeg);
    }

    @Override
    protected Bounds getBounds() {
        double r = Math.abs(radius * scaleFactor);
        return new Bounds(centerX - r, centerY - r, centerX + r, centerY + r);
    }

    @Override
    public Sector copy() {
        Sector copy = new Sector(centerX, centerY, radius, startAngleDeg, endAngleDeg);
        copy.drawRadii = this.drawRadii;
        copyBaseTo(copy);
        return copy;
    }

    @Override
    public String toString() {
        return "{startAngle: " + startAngleDeg + ", endAngle: " + endAngleDeg + ", radius: "
            + getRadiusX() + ", centerX: " + getCenterX() + ", centerY: " + getCenterY() + "}";
    }

    private boolean angleWithin(double angle, double start, double end) {
        double normAngle = normalizeAngle(angle);
        double normStart = normalizeAngle(start);
        double normEnd = normalizeAngle(end);
        if (Math.abs(normStart - normEnd) < 1e-9) {
            return true;
        }
        if (normStart <= normEnd) {
            return normAngle >= normStart && normAngle <= normEnd;
        }
        return normAngle >= normStart || normAngle <= normEnd;
    }
}
