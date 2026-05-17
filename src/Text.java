/**
 * Draws a text label at a world position.
 */
public class Text extends FilledShape {
    private double x;
    private double y;
    private double fontSize;
    private String text;
    private String fontFamily;
    private Alignment alignment = Alignment.left;
    private boolean bold;
    private boolean italic;
    private double width;
    private double height;

    public Text() {
        this(0, 0, 24, "Text", null);
    }

    public Text(double x, double y, double fontSize, String text) {
        this(x, y, fontSize, text, null);
    }

    public Text(double x, double y, double fontSize, String text, String fontFamily) {
        super(x, y);
        this.x = x;
        this.y = y;
        this.fontSize = fontSize == 0 ? 10 : fontSize;
        this.text = text == null ? "" : text;
        this.fontFamily = fontFamily;
        recalcBounds();
        registerWithWorld();
    }

    public Text(double x, double y, double fontSize, double text) {
        this(x, y, fontSize, String.valueOf(text), null);
    }

    public Text(double x, double y, double fontSize, double text, String fontFamily) {
        this(x, y, fontSize, String.valueOf(text), fontFamily);
    }

    public void setFontsize(double fontsize) {
        this.fontSize = fontsize == 0 ? 10 : fontsize;
        recalcBounds();
    }

    public double getFontsize() {
        return fontSize;
    }

    public void setText(String text) {
        this.text = text == null ? "" : text;
        recalcBounds();
    }

    public void setText(double text) {
        setText(String.valueOf(text));
    }

    public String getText() {
        return text;
    }

    public void setAlignment(Alignment alignment) {
        if (alignment != null) {
            this.alignment = alignment;
            recalcBounds();
        }
    }

    public void setStyle(boolean bold, boolean italic) {
        this.bold = bold;
        this.italic = italic;
        recalcBounds();
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    @Override
    public Text moveTo(double x, double y) {
        this.x = x;
        this.y = y;
        recalcBounds();
        return this;
    }

    @Override
    public Text copy() {
        Text copy = new Text(x, y, fontSize, text, fontFamily);
        copy.alignment = this.alignment;
        copy.bold = this.bold;
        copy.italic = this.italic;
        copyBaseTo(copy);
        return copy;
    }

    @Override
    protected Bounds getBounds() {
        double w = width * scaleFactor;
        double h = height * scaleFactor;
        return new Bounds(centerX - w / 2.0, centerY - h / 2.0, centerX + w / 2.0, centerY + h / 2.0);
    }

    @Override
    public String toString() {
        return "{text: " + text + ", centerX: " + getCenterX() + ", centerY: " + getCenterY() + "}";
    }

    private void recalcBounds() {
        double factor = 0.6;
        width = (text == null ? 0 : text.length()) * fontSize * factor;
        height = fontSize;
        if (width < 1) {
            width = 1;
        }
        if (height < 1) {
            height = 1;
        }

        double centerXOffset;
        switch (alignment) {
            case center:
                centerXOffset = 0;
                break;
            case right:
                centerXOffset = -width / 2.0;
                break;
            default:
                centerXOffset = width / 2.0;
                break;
        }

        double centerYOffset = height / 2.0;
        if (alignment == Alignment.top) {
            centerYOffset = height / 2.0;
        } else if (alignment == Alignment.bottom) {
            centerYOffset = -height / 2.0;
        }

        centerX = x + centerXOffset;
        centerY = y + centerYOffset;
    }
}
