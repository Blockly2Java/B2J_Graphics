/**
 * Base class for shapes that can be filled and outlined.
 */
public abstract class FilledShape extends Shape {
    protected static int defaultFillColor = 0x8080ff;
    protected static double defaultFillAlpha = 1.0;
    protected static Integer defaultBorderColor = null;
    protected static double defaultBorderAlpha = 1.0;
    protected static double defaultBorderWidth = 5.0;

    protected FilledShape() {
        this(0, 0);
    }

    protected FilledShape(double x, double y) {
        super(x, y);
        applyDefaults();
    }

    private void applyDefaults() {
        fillAlpha = clampAlpha(defaultFillAlpha);
        fillColor = Color.fromInt(defaultFillColor);
        fillColor.alpha = fillAlpha;

        borderWidth = defaultBorderWidth;
        borderAlpha = clampAlpha(defaultBorderAlpha);
        if (defaultBorderColor != null) {
            borderColor = Color.fromInt(defaultBorderColor);
            borderColor.alpha = borderAlpha;
        } else {
            borderColor = null;
        }
    }

    @Override
    public FilledShape setFillColor(Color color) {
        super.setFillColor(color);
        return this;
    }

    @Override
    public FilledShape setFillColor(Color color, double alpha) {
        super.setFillColor(color, alpha);
        return this;
    }

    @Override
    public FilledShape setFillColor(int color) {
        super.setFillColor(color);
        return this;
    }

    @Override
    public FilledShape setFillColor(int color, double alpha) {
        super.setFillColor(color, alpha);
        return this;
    }

    @Override
    public FilledShape setFillColor(String color) {
        super.setFillColor(color);
        return this;
    }

    @Override
    public FilledShape setFillColor(String color, double alpha) {
        super.setFillColor(color, alpha);
        return this;
    }

    @Override
    public FilledShape setBorderColor(Color color) {
        super.setBorderColor(color);
        return this;
    }

    @Override
    public FilledShape setBorderColor(Color color, double alpha) {
        super.setBorderColor(color, alpha);
        return this;
    }

    @Override
    public FilledShape setBorderColor(int color) {
        super.setBorderColor(color);
        return this;
    }

    @Override
    public FilledShape setBorderColor(int color, double alpha) {
        super.setBorderColor(color, alpha);
        return this;
    }

    @Override
    public FilledShape setBorderColor(String color) {
        super.setBorderColor(color);
        return this;
    }

    @Override
    public FilledShape setBorderColor(String color, double alpha) {
        super.setBorderColor(color, alpha);
        return this;
    }

    @Override
    public FilledShape setBorderWidth(double width) {
        super.setBorderWidth(width);
        return this;
    }

    @Override
    public FilledShape setAlpha(double alpha) {
        super.setAlpha(alpha);
        return this;
    }

    public static void setDefaultBorder(double width, String color) {
        defaultBorderWidth = width;
        if (color == null) {
            defaultBorderColor = null;
            return;
        }
        Color parsed = Color.parse(color);
        if (parsed != null) {
            defaultBorderColor = parsed.toInt();
            defaultBorderAlpha = parsed.alpha;
        }
    }

    public static void setDefaultBorder(double width, int color, double alpha) {
        defaultBorderWidth = width;
        defaultBorderColor = color & 0x00ffffff;
        defaultBorderAlpha = clampAlpha(alpha);
    }

    public static void setDefaultFillColor(String color) {
        Color parsed = Color.parse(color);
        if (parsed != null) {
            defaultFillColor = parsed.toInt();
            defaultFillAlpha = parsed.alpha;
        }
    }

    public static void setDefaultFillColor(int color, double alpha) {
        defaultFillColor = color & 0x00ffffff;
        defaultFillAlpha = clampAlpha(alpha);
    }

    public static void setDefaultFillColor(int color) {
        defaultFillColor = color & 0x00ffffff;
        defaultFillAlpha = 1.0;
    }
}
