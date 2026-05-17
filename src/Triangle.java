public class Triangle extends Polygon {
    public Triangle() {
        this(0, 0, 100, 0, 0, 100);
    }

    public Triangle(double x1, double y1, double x2, double y2, double x3, double y3) {
        super(true, x1, y1, x2, y2, x3, y3);
    }

    public Triangle setPoints(double x1, double y1, double x2, double y2, double x3, double y3) {
        setPoints(new double[] {x1, y1, x2, y2, x3, y3});
        return this;
    }

    @Override
    public Triangle copy() {
        java.util.List<Point> points = getTransformedPoints();
        if (points.size() < 3) {
            Triangle copy = new Triangle();
            this.copyBaseTo(copy);
            return copy;
        }
        Triangle copy = new Triangle(
            points.get(0).x, points.get(0).y,
            points.get(1).x, points.get(1).y,
            points.get(2).x, points.get(2).y
        );
        this.copyBaseTo(copy);
        return copy;
    }
}
