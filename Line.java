public class Line extends Shape {
    private double x1;
    private double y1;
    private double x2;
    private double y2;

    public Line(double x1, double y1, double x2, double y2) {
        super((x1 + x2) / 2.0, (y1 + y2) / 2.0);
        System.out.println(">>> Line(" + x1 + ", " + y1 + ", " + x2 + ", " + y2 + ")");
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        System.out.println("<<< Line(" + x1 + ", " + y1 + ", " + x2 + ", " + y2 + ")");
    }

    @Override
    public void move(double dx, double dy) {
        System.out.println(">>> move(" + dx + ", " + dy + ")");
        super.move(dx, dy);
        x1 += dx;
        y1 += dy;
        x2 += dx;
        y2 += dy;
        System.out.println("<<< move(" + dx + ", " + dy + ")");
    }
}
