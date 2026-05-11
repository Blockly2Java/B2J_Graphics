public class Ellipse extends Shape {
    private double radiusX;
    private double radiusY;

    public Ellipse(double x, double y, double rX, double rY) {
        super(x, y);
        System.out.println(">>> Ellipse(" + x + ", " + y + ", " + rX + ", " + rY + ")");
        radiusX = rX;
        radiusY = rY;
        System.out.println("<<< Ellipse(" + x + ", " + y + ", " + rX + ", " + rY + ")");
    }
}
