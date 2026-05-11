public class Circle extends Shape {
    private double radius;
    public Circle(double x, double y, double r) {
        super(x, y);
        System.out.println(">>> Circle(" + x + ", " + y + ", " + r + ")");
        radius = r;
        System.out.println("<<< Circle(" + x + ", " + y + ", " + r + ")");
    }
}
