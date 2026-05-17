/**
 * Konvertiert die flexiblen Farbwerte, die von der Grafik-API akzeptiert werden, in RGB-Ganzzahlen.
 */
public class ColorConverter {
    private ColorConverter() {
    }

    public static Integer convertToInt(Object color, boolean acceptNull) {
        if (color == null) {
            if (acceptNull) {
                return null;
            }
            throw new IllegalArgumentException("Color must not be null");
        }
        if (color instanceof Integer) {
            return (Integer) color;
        }
        if (color instanceof String) {
            Color parsed = Color.parse((String) color);
            return parsed == null ? 0xffffff : parsed.toInt();
        }
        if (color instanceof Color) {
            return ((Color) color).toInt();
        }
        throw new IllegalArgumentException("Unsupported color type: " + color.getClass().getName());
    }
}
