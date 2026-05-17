/**
 * Grid-based world used by the Hamster examples.
 */
public class JavaHamsterWorld extends FilledShape {
    private final int sizeX;
    private final int sizeY;
    private final int[][] grainCounts;
    private final boolean[][] walls;

    private double left = 10;
    private double top = 10;
    private double cellWidth = 40;

    public JavaHamsterWorld(int width, int height) {
        super(0, 0);
        this.sizeX = width;
        this.sizeY = height;
        this.grainCounts = new int[width][height];
        this.walls = new boolean[width][height];
        this.fillColor = new Color(144, 238, 144, 1.0);
        this.borderColor = new Color(139, 69, 19, 1.0);
        this.borderWidth = 2;
        this.centerX = left + (sizeX * cellWidth) / 2.0;
        this.centerY = top + (sizeY * cellWidth) / 2.0;
        registerWithWorld();
    }

    public int getBreite() {
        return sizeX;
    }

    public int getHoehe() {
        return sizeY;
    }

    public void loescheAlles() {
        clearAll();
    }

    public void setzeMauer(int x, int y) {
        setOrRemoveWall(x, y);
    }

    public void setzeGetreide(int x, int y, int anzahl) {
        setGrain(x, y, anzahl);
    }

    public void init(String worldAsString) {
        clearAll();
        if (worldAsString == null) {
            return;
        }
        String[] lines = worldAsString.split("\n");
        for (int y = 0; y < lines.length && y < sizeY; y++) {
            String line = lines[y];
            for (int x = 0; x < line.length() && x < sizeX; x++) {
                char c = Character.toLowerCase(line.charAt(x));
                if (c == 'm') {
                    walls[x][y] = true;
                } else if (c >= '1' && c <= '9') {
                    grainCounts[x][y] = c - '0';
                } else if (c >= 'a' && c <= 'f') {
                    grainCounts[x][y] = 10 + (c - 'a');
                }
            }
        }
        if (world != null) {
            B2J_JavaFX_Renderer.updateShape(this);
        }
    }

    public Shape scale(double factor) {
        return super.scale(factor, left, top);
    }

    void setGrain(int x, int y, int count) {
        if (isOutside(x, y)) {
            return;
        }
        grainCounts[x][y] = Math.max(0, count);
    }

    int getGrainCount(int x, int y) {
        if (isOutside(x, y)) {
            return 0;
        }
        return grainCounts[x][y];
    }

    void setOrRemoveWall(int x, int y) {
        if (isOutside(x, y)) {
            return;
        }
        walls[x][y] = !walls[x][y];
    }

    boolean isWall(int x, int y) {
        return !isOutside(x, y) && walls[x][y];
    }

    boolean isOutside(int x, int y) {
        return x < 0 || y < 0 || x >= sizeX || y >= sizeY;
    }

    void clearAll() {
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                grainCounts[x][y] = 0;
                walls[x][y] = false;
            }
        }
        if (world != null) {
            B2J_JavaFX_Renderer.updateShape(this);
        }
    }

    @Override
    public JavaHamsterWorld copy() {
        JavaHamsterWorld copy = new JavaHamsterWorld(sizeX, sizeY);
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                copy.grainCounts[x][y] = this.grainCounts[x][y];
                copy.walls[x][y] = this.walls[x][y];
            }
        }
        copyBaseTo(copy);
        return copy;
    }
}
