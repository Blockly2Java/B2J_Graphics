public class FilledShapeDefaults {
    private FilledShapeDefaults() {
    }

    public static void initDefaultValues() {
        FilledShape.setDefaultFillColor(0x8080ff);
        FilledShape.setDefaultBorder(10, 0x000000, 1.0);
        Shape.setDefaultVisibility(true);
    }

    public static void setDefaultVisibility(boolean visible) {
        Shape.setDefaultVisibility(visible);
    }

    public static void setDefaultBorder(double width, Object color, Double alpha) {
        if (color instanceof String) {
            FilledShape.setDefaultBorder(width, (String) color);
            if (alpha != null) {
                FilledShape.setDefaultBorder(width, Color.parse((String) color).toInt(), alpha);
            }
            return;
        }
        if (color instanceof Integer) {
            double a = alpha == null ? 1.0 : alpha;
            FilledShape.setDefaultBorder(width, (Integer) color, a);
            return;
        }
        if (color instanceof Color) {
            Color c = (Color) color;
            double a = alpha == null ? c.alpha : alpha;
            FilledShape.setDefaultBorder(width, c.toInt(), a);
        }
    }

    public static void setDefaultFillColor(Object color, Double alpha) {
        if (color instanceof String) {
            FilledShape.setDefaultFillColor((String) color);
            if (alpha != null) {
                Color parsed = Color.parse((String) color);
                if (parsed != null) {
                    FilledShape.setDefaultFillColor(parsed.toInt(), alpha);
                }
            }
            return;
        }
        if (color instanceof Integer) {
            if (alpha != null) {
                FilledShape.setDefaultFillColor((Integer) color, alpha);
            } else {
                FilledShape.setDefaultFillColor((Integer) color);
            }
            return;
        }
        if (color instanceof Color) {
            Color c = (Color) color;
            double a = alpha == null ? c.alpha : alpha;
            FilledShape.setDefaultFillColor(c.toInt(), a);
        }
    }
}
