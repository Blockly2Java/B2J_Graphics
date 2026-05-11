public class Rectangle extends Shape{
    private double width;
    private double height;

    public Rectangle(double x, double y, double w, double h) {
        super(x, y);
        System.out.println(">>> Rectangle(" + x + ", " + y + ", " + w + ", " + h + ")");
        width = w;
        height = h;
        System.out.println("<<< Rectangle(" + x + ", " + y + ", " + w + ", " + h + ")");
    }
}
