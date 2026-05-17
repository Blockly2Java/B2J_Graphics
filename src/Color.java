import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

/**
 * Represents an RGB color with an optional alpha value.
 *
 * <p>The class accepts named colors, hex strings, and integer RGB values, and
 * provides helpers for converting between the supported formats.</p>
 */
public class Color {
	public int red;
	public int green;
	public int blue;
	public double alpha = 1.0;

	public static final Color black = new Color(0, 0, 0);
	public static final Color white = new Color(255, 255, 255);
	public static final Color redColor = new Color(255, 0, 0);
	public static final Color greenColor = new Color(0, 255, 0);
	public static final Color blueColor = new Color(0, 0, 255);
	public static final Color yellow = new Color(255, 255, 0);
	public static final Color cyan = new Color(0, 255, 255);
	public static final Color magenta = new Color(255, 0, 255);
	public static final Color gray = new Color(128, 128, 128);
	public static final Color orange = new Color(255, 165, 0);
	public static final Color pink = new Color(255, 105, 180);
	public static final Color purple = new Color(128, 0, 128);
	public static final Color brown = new Color(139, 69, 19);

	public static final Color BLACK = black;
	public static final Color WHITE = white;
	public static final Color RED = redColor;
	public static final Color GREEN = greenColor;
	public static final Color BLUE = blueColor;
	public static final Color YELLOW = yellow;
	public static final Color CYAN = cyan;
	public static final Color MAGENTA = magenta;
	public static final Color GRAY = gray;
	public static final Color ORANGE = orange;
	public static final Color PINK = pink;
	public static final Color PURPLE = purple;
	public static final Color BROWN = brown;

	private static final Random RANDOM = new Random();
	private static final Map<String, Color> NAMED_COLORS;

	static {
		Map<String, Color> map = new HashMap<>();
		map.put("black", black);
		map.put("white", white);
		map.put("red", redColor);
		map.put("green", greenColor);
		map.put("blue", blueColor);
		map.put("yellow", yellow);
		map.put("cyan", cyan);
		map.put("magenta", magenta);
		map.put("gray", gray);
		map.put("grey", gray);
		map.put("orange", orange);
		map.put("pink", pink);
		map.put("purple", purple);
		map.put("brown", brown);
		NAMED_COLORS = Collections.unmodifiableMap(map);
	}

	public Color() {
		this(0, 0, 0, 1.0);
	}

	public Color(int red, int green, int blue) {
		this(red, green, blue, 1.0);
	}

	public Color(int red, int green, int blue, double alpha) {
		this.red = clampColorValue(red);
		this.green = clampColorValue(green);
		this.blue = clampColorValue(blue);
		this.alpha = clampAlpha(alpha);
	}

	public int getRed() {
		return red;
	}

	public int getGreen() {
		return green;
	}

	public int getBlue() {
		return blue;
	}

	public double getAlpha() {
		return alpha;
	}

	public int toInt() {
		return (red << 16) + (green << 8) + blue;
	}

	@Override
	public String toString() {
		return toHexRGB(red, green, blue);
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof Color)) {
			return false;
		}
		Color o = (Color) other;
		return red == o.red && green == o.green && blue == o.blue;
	}

	@Override
	public int hashCode() {
		return (red << 16) ^ (green << 8) ^ blue;
	}

	public static int randomColor() {
		return RANDOM.nextInt(0x1000000);
	}

	public static int randomColor(int minimumBrightness) {
		return randomColor(minimumBrightness, 255);
	}

	public static int randomColor(int minimumBrightness, int maximumBrightness) {
		int min = Math.max(0, minimumBrightness);
		int max = Math.max(0, maximumBrightness);

		double brightness = (RANDOM.nextDouble() * (max - min) + min) * Math.sqrt(3.0);

		double r = RANDOM.nextDouble();
		double g = RANDOM.nextDouble();
		double b = RANDOM.nextDouble();

		double d = Math.sqrt(r * r + g * g + b * b);
		if (d < 1e-10) {
			d = 1e-10;
		}

		int rr = (int) Math.floor(r / d * brightness);
		int gg = (int) Math.floor(g / d * brightness);
		int bb = (int) Math.floor(b / d * brightness);

		rr = Math.min(rr, 255);
		gg = Math.min(gg, 255);
		bb = Math.min(bb, 255);

		return (rr << 16) + (gg << 8) + bb;
	}

	public static int fromRGB(int red, int green, int blue) {
		int r = clampColorValue(red);
		int g = clampColorValue(green);
		int b = clampColorValue(blue);
		return (r << 16) + (g << 8) + b;
	}

	public static String fromRGBA(int red, int green, int blue, double alpha) {
		int r = clampColorValue(red);
		int g = clampColorValue(green);
		int b = clampColorValue(blue);
		int a = (int) Math.round(clampAlpha(alpha) * 255.0);
		return toHexRGBA(r, g, b, a);
	}

	public static String fromHSLA(double hue, double saturation, double luminance, double alpha) {
		RGB rgb = hslToRgb(hue, saturation, luminance);
		int a = (int) Math.round(clampAlpha(alpha) * 255.0);
		return toHexRGBA(rgb.r, rgb.g, rgb.b, a);
	}

	public static int fromHSL(double hue, double saturation, double luminance) {
		RGB rgb = hslToRgb(hue, saturation, luminance);
		return (rgb.r << 16) + (rgb.g << 8) + rgb.b;
	}

	static Color parse(String color) {
		if (color == null) {
			return null;
		}
		String c = color.trim().toLowerCase(Locale.ROOT);
		if (c.isEmpty()) {
			return null;
		}
		if (c.startsWith("#")) {
			return parseHex(c.substring(1));
		}
		if (c.startsWith("0x")) {
			return parseHex(c.substring(2));
		}
		Color named = NAMED_COLORS.get(c);
		if (named != null) {
			return new Color(named.red, named.green, named.blue, named.alpha);
		}
		return null;
	}

	static Color fromInt(int rgb) {
		int r = (rgb >> 16) & 0xff;
		int g = (rgb >> 8) & 0xff;
		int b = rgb & 0xff;
		return new Color(r, g, b, 1.0);
	}

	private static Color parseHex(String hex) {
		String h = hex.replace("_", "");
		if (h.length() == 3 || h.length() == 4) {
			int r = Integer.parseInt(h.substring(0, 1) + h.substring(0, 1), 16);
			int g = Integer.parseInt(h.substring(1, 2) + h.substring(1, 2), 16);
			int b = Integer.parseInt(h.substring(2, 3) + h.substring(2, 3), 16);
			if (h.length() == 4) {
				int a = Integer.parseInt(h.substring(3, 4) + h.substring(3, 4), 16);
				return new Color(r, g, b, a / 255.0);
			}
			return new Color(r, g, b, 1.0);
		}
		if (h.length() == 6 || h.length() == 8) {
			int r = Integer.parseInt(h.substring(0, 2), 16);
			int g = Integer.parseInt(h.substring(2, 4), 16);
			int b = Integer.parseInt(h.substring(4, 6), 16);
			if (h.length() == 8) {
				int a = Integer.parseInt(h.substring(6, 8), 16);
				return new Color(r, g, b, a / 255.0);
			}
			return new Color(r, g, b, 1.0);
		}
		return null;
	}

	private static int clampColorValue(int value) {
		return Math.max(0, Math.min(255, value));
	}

	private static double clampAlpha(double value) {
		return Math.max(0.0, Math.min(1.0, value));
	}

	private static String toHexRGB(int r, int g, int b) {
		return String.format("#%02x%02x%02x", r, g, b);
	}

	private static String toHexRGBA(int r, int g, int b, int a) {
		return String.format("#%02x%02x%02x%02x", r, g, b, a);
	}

	private static RGB hslToRgb(double hue, double saturation, double luminance) {
		double h = Math.max(0.0, Math.min(360.0, hue));
		double s = Math.max(0.0, Math.min(100.0, saturation)) / 100.0;
		double l = Math.max(0.0, Math.min(100.0, luminance)) / 100.0;

		double c = (1.0 - Math.abs(2.0 * l - 1.0)) * s;
		double x = c * (1.0 - Math.abs((h / 60.0) % 2.0 - 1.0));
		double m = l - c / 2.0;

		double r1 = 0.0;
		double g1 = 0.0;
		double b1 = 0.0;

		if (0 <= h && h < 60) {
			r1 = c;
			g1 = x;
		} else if (60 <= h && h < 120) {
			r1 = x;
			g1 = c;
		} else if (120 <= h && h < 180) {
			g1 = c;
			b1 = x;
		} else if (180 <= h && h < 240) {
			g1 = x;
			b1 = c;
		} else if (240 <= h && h < 300) {
			r1 = x;
			b1 = c;
		} else if (300 <= h && h <= 360) {
			r1 = c;
			b1 = x;
		}

		int r = (int) Math.round((r1 + m) * 255.0);
		int g = (int) Math.round((g1 + m) * 255.0);
		int b = (int) Math.round((b1 + m) * 255.0);

		return new RGB(r, g, b);
	}

	private static final class RGB {
		final int r;
		final int g;
		final int b;

		private RGB(int r, int g, int b) {
			this.r = r;
			this.g = g;
			this.b = b;
		}
	}
}
