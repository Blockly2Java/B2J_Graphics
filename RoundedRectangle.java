public class RoundedRectangle extends Shape {
    private double radius;
    private double width;
    private double height;

    public RoundedRectangle(double x, double y, double width, double height, double r) {
        super(x, y);
        System.out.println(">>> RoundedRectangle(" + x + ", " + y + ", " + width + ", " + height + ", " + r + ")");
        radius = r;
        this.width = width;
        this.height = height;
        System.out.println("<<< RoundedRectangle(" + x + ", " + y + ", " + width + ", " + height + ", " + r + ")");
    }
}
