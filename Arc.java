public class Arc extends FilledShape {
    private double innerRadius;
    private double outerRadius;
    private double startAngleDeg;
    private double endAngleDeg;

    public Arc() {
        this(200, 200, 100, 200, 0, 180);
    }

    public Arc(double mx, double my, double innerRadius, double outerRadius, double startAngle, double endAngle) {
        super(mx, my);
        this.innerRadius = innerRadius;
        this.outerRadius = outerRadius;
        this.startAngleDeg = startAngle;
        this.endAngleDeg = endAngle;
        registerWithWorld();
    }

    public void setInnerRadius(double innerRadius) {
        if (innerRadius < 0) {
            return;
        }
        this.innerRadius = innerRadius / scaleFactor;
    }

    public double getInnerRadiusX() {
        return innerRadius * scaleFactor;
    }

    public void setOuterRadius(double outerRadius) {
        if (outerRadius < 0) {
            return;
        }
        this.outerRadius = outerRadius / scaleFactor;
    }

    public double getOuterRadiusX() {
        return outerRadius * scaleFactor;
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

    @Override
    public boolean containsPoint(double x, double y) {
        Point local = inverseTransformPoint(x, y);
        double dx = local.x;
        double dy = local.y;
        double r2 = dx * dx + dy * dy;
        double inner = innerRadius * innerRadius;
        double outer = outerRadius * outerRadius;
        if (r2 < inner || r2 > outer) {
            return false;
        }
        double angle = Math.toDegrees(Math.atan2(-dy, dx));
        return angleWithin(angle, startAngleDeg, endAngleDeg);
    }

    @Override
    protected Bounds getBounds() {
        double r = Math.abs(outerRadius * scaleFactor);
        return new Bounds(centerX - r, centerY - r, centerX + r, centerY + r);
    }

    @Override
    public Arc copy() {
        Arc copy = new Arc(centerX, centerY, innerRadius, outerRadius, startAngleDeg, endAngleDeg);
        copyBaseTo(copy);
        return copy;
    }

    @Override
    public String toString() {
        return "{startAngle: " + startAngleDeg + ", endAngle: " + endAngleDeg + ", innerRadius: "
            + getInnerRadiusX() + ", outerRadius: " + getOuterRadiusX() + ", centerX: " + getCenterX()
            + ", centerY: " + getCenterY() + "}";
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
