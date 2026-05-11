public class World {
    private double height;
    private double width;

    public World(double height, double width) {
        System.out.println(">>> World(" + height + ", " + width + ")");
        this.height = height;
        this.width = width;
        System.out.println("<<< World(" + height + ", " + width + ")");
    }

    public double getWidth() {
        System.out.println(">>> getWidth()");
        double result = width;
        System.out.println("<<< getWidth()");
        return result;
    }

    public double getHeight() {
        System.out.println(">>> getHeight()");
        double result = height;
        System.out.println("<<< getHeight()");
        return result;
    }
}
