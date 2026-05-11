public class Shape {
    private double centerX;
    private double centerY;
    private double radius;
    private String fillColor;

    public Shape(double x, double y) {
        System.out.println(">>> Shape(" + x + ", " + y + ")");
        centerX = x;
        centerY = y;
        System.out.println("<<< Shape(" + x + ", " + y + ")");
    }

    public void setFillColor(String color) {
        System.out.println(">>> setFillColor(" + color + ")");
        fillColor = color;
        System.out.println("<<< setFillColor(" + color + ")");
    }

    public void setFillColor(String color, double alpha) {
        System.out.println(">>> setFillColor(" + color + ", " + alpha + ")");
        fillColor = color;
        setAlpha(alpha);
        System.out.println("<<< setFillColor(" + color + ", " + alpha + ")");
    }

    public void setBorderColor(String color) {
        System.out.println(">>> setBorderColor(" + color + ")");
        if (color == null) {
            System.out.println("<<< setBorderColor(" + color + ")");
            return;
        }
        System.out.println("<<< setBorderColor(" + color + ")");
    }

    public void setBorderWidth(double width) {
        System.out.println(">>> setBorderWidth(" + width + ")");
        if (width < 0) {
            System.out.println("<<< setBorderWidth(" + width + ")");
            return;
        }
        System.out.println("<<< setBorderWidth(" + width + ")");
    }

    public void setAlpha(double alpha) {
        System.out.println(">>> setAlpha(" + alpha + ")");
        double normalizedAlpha = Math.max(0.0, Math.min(1.0, alpha));
        if (normalizedAlpha < 0.0) {
            System.out.println("<<< setAlpha(" + alpha + ")");
            return;
        }
        System.out.println("<<< setAlpha(" + alpha + ")");
    }

    public void setVisible(boolean visible) {
        System.out.println(">>> setVisible(" + visible + ")");
        if (!visible) {
            System.out.println("<<< setVisible(" + visible + ")");
            return;
        }
        System.out.println("<<< setVisible(" + visible + ")");
    }

    public void bringToFront() {
        System.out.println(">>> bringToFront()");
        getWorld();
        System.out.println("<<< bringToFront()");
    }

    public void sendToBack() {
        System.out.println(">>> sendToBack()");
        getWorld();
        System.out.println("<<< sendToBack()");
    }

    public double getCenterX() {
        System.out.println(">>> getCenterX()");
        double result = centerX;
        System.out.println("<<< getCenterX()");
        return result;
    }

    public double getCenterY() {
        System.out.println(">>> getCenterY()");
        double result = centerY;
        System.out.println("<<< getCenterY()");
        return result;
    }

    public void move(double dx, double dy) {
        System.out.println(">>> move(" + dx + ", " + dy + ")");
        centerX += dx;
        centerY += dy;
        System.out.println("<<< move(" + dx + ", " + dy + ")");
    }

    public void rotate(double angle) {
        System.out.println(">>> rotate(" + angle + ")");
        System.out.println("<<< rotate(" + angle + ")");
    }

    public void scale(double factor) {
        System.out.println(">>> scale(" + factor + ")");
        System.out.println("<<< scale(" + factor + ")");
    }

    public World getWorld() {
        System.out.println(">>> getWorld()");
        World result = new World(800,600);
        System.out.println("<<< getWorld()");
        return result;
    }

}
