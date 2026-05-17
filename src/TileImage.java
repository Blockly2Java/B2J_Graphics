/**
 * Describes the logical size of a repeating sprite tile.
 */
public class TileImage {
    public final double width;
    public final double height;
    public final double gapX;
    public final double gapY;

    public TileImage(double width, double height, double gapX, double gapY) {
        this.width = width;
        this.height = height;
        this.gapX = gapX;
        this.gapY = gapY;
    }
}
