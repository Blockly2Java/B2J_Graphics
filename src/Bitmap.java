import java.util.Arrays;

public class Bitmap extends Shape {
    private final int resolutionX;
    private final int resolutionY;
    private final double left;
    private final double top;
    private final double displayWidth;
    private final double displayHeight;

    private final int[] colors;
    private final double[] alphas;

    public Bitmap(int resolutionX, int resolutionY, double left, double top, double displayWidth, double displayHeight) {
        super(left + displayWidth / 2.0, top + displayHeight / 2.0);
        this.resolutionX = resolutionX;
        this.resolutionY = resolutionY;
        this.left = left;
        this.top = top;
        this.displayWidth = displayWidth;
        this.displayHeight = displayHeight;
        this.colors = new int[resolutionX * resolutionY];
        this.alphas = new double[resolutionX * resolutionY];
        Arrays.fill(this.alphas, 1.0);
        registerWithWorld();
    }

    public int getResolutionX() {
        return resolutionX;
    }

    public int getResolutionY() {
        return resolutionY;
    }

    public void setColor(int x, int y, int color) {
        setColorInternal(x, y, color, 1.0);
    }

    public void setColor(int x, int y, int color, double alpha) {
        setColorInternal(x, y, color, alpha);
    }

    public void setColor(int x, int y, String color) {
        Color parsed = Color.parse(color);
        if (parsed == null) {
            return;
        }
        setColorInternal(x, y, parsed.toInt(), parsed.alpha);
    }

    public void setColor(int x, int y, String color, double alpha) {
        Color parsed = Color.parse(color);
        if (parsed == null) {
            return;
        }
        setColorInternal(x, y, parsed.toInt(), alpha);
    }

    public Color getColor(int x, int y) {
        int idx = index(x, y);
        int color = colors[idx];
        Color c = Color.fromInt(color);
        c.alpha = alphas[idx];
        return c;
    }

    public int getColorAsInt(int x, int y) {
        return colors[index(x, y)];
    }

    public double getAlpha(int x, int y) {
        return alphas[index(x, y)];
    }

    public boolean isColor(int x, int y, String color) {
        Color parsed = Color.parse(color);
        if (parsed == null) {
            return false;
        }
        return colors[index(x, y)] == parsed.toInt();
    }

    public boolean isColor(int x, int y, int color) {
        return colors[index(x, y)] == (color & 0x00ffffff);
    }

    public Position screenCoordinatesToBitmapCoordinates(double x, double y) {
        int bx = (int) Math.floor((x - left) / displayWidth * resolutionX);
        int by = (int) Math.floor((y - top) / displayHeight * resolutionY);
        return new Position(bx, by);
    }

    public void fillAll(int color, double alpha) {
        int c = color & 0x00ffffff;
        for (int i = 0; i < colors.length; i++) {
            colors[i] = c;
            alphas[i] = clampAlpha(alpha);
        }
    }

    public void fillAll(String color, double alpha) {
        Color parsed = Color.parse(color);
        if (parsed == null) {
            return;
        }
        fillAll(parsed.toInt(), alpha);
    }

    public void downloadAsPngFile(String filename) {
    }

    @Override
    public Bitmap copy() {
        Bitmap copy = new Bitmap(resolutionX, resolutionY, left, top, displayWidth, displayHeight);
        System.arraycopy(colors, 0, copy.colors, 0, colors.length);
        System.arraycopy(alphas, 0, copy.alphas, 0, alphas.length);
        copyBaseTo(copy);
        return copy;
    }

    @Override
    protected Bounds getBounds() {
        double w = displayWidth * scaleFactor;
        double h = displayHeight * scaleFactor;
        return new Bounds(centerX - w / 2.0, centerY - h / 2.0, centerX + w / 2.0, centerY + h / 2.0);
    }

    @Override
    public String toString() {
        return "{width: " + displayWidth + ", height: " + displayHeight + ", centerX: " + getCenterX()
            + ", centerY: " + getCenterY() + "}";
    }

    private void setColorInternal(int x, int y, int color, double alpha) {
        int idx = index(x, y);
        colors[idx] = color & 0x00ffffff;
        alphas[idx] = clampAlpha(alpha);
    }

    private int index(int x, int y) {
        if (x < 0 || x >= resolutionX || y < 0 || y >= resolutionY) {
            throw new IllegalArgumentException("Bitmap coordinates out of bounds: (" + x + ", " + y + ")");
        }
        return x + y * resolutionX;
    }
}
