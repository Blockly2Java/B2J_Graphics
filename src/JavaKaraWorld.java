/**
 * Grid-based world used by the Kara examples.
 */
public class JavaKaraWorld extends FilledShape {
    public static final int NORTH = 0;
    public static final int WEST = 1;
    public static final int SOUTH = 2;
    public static final int EAST = 3;

    private final int sizeX;
    private final int sizeY;
    private final boolean[][] leaves;
    private final boolean[][] trees;
    private final boolean[][] mushrooms;

    private double left = 10;
    private double top = 10;
    private double cellWidth = 28;

    public JavaKaraWorld(int sizeX, int sizeY) {
        super(0, 0);
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.leaves = new boolean[sizeX][sizeY];
        this.trees = new boolean[sizeX][sizeY];
        this.mushrooms = new boolean[sizeX][sizeY];
        this.fillColor = new Color(180, 230, 180, 1.0);
        this.borderColor = new Color(170, 170, 170, 1.0);
        this.borderWidth = 2;
        this.centerX = left + (sizeX * cellWidth) / 2.0;
        this.centerY = top + (sizeY * cellWidth) / 2.0;
        registerWithWorld();
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public void clearAll() {
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                leaves[x][y] = false;
                trees[x][y] = false;
                mushrooms[x][y] = false;
            }
        }
        if (world != null) {
            B2J_JavaFX_Renderer.updateShape(this);
        }
    }

    public void setLeaf(int x, int y) {
        setOrRemoveLeaf(x, y);
    }

    public void setTree(int x, int y) {
        setOrRemoveTree(x, y);
    }

    public void setMushroom(int x, int y) {
        setOrRemoveMushroom(x, y);
    }

    public void setOrRemoveLeaf(int x, int y) {
        if (isOutside(x, y)) {
            return;
        }
        leaves[x][y] = !leaves[x][y];
    }

    public void setOrRemoveTree(int x, int y) {
        if (isOutside(x, y)) {
            return;
        }
        trees[x][y] = !trees[x][y];
    }

    public void setOrRemoveMushroom(int x, int y) {
        if (isOutside(x, y)) {
            return;
        }
        mushrooms[x][y] = !mushrooms[x][y];
    }

    public boolean isEmpty(int x, int y) {
        if (isOutside(x, y)) {
            return true;
        }
        return !leaves[x][y] && !trees[x][y] && !mushrooms[x][y];
    }

    public void init(String s) {
        clearAll();
        if (s == null) {
            return;
        }
        String[] lines = s.split("\n");
        for (int y = 0; y < lines.length && y < sizeY; y++) {
            String line = lines[y];
            for (int x = 0; x < line.length() && x < sizeX; x++) {
                char c = Character.toLowerCase(line.charAt(x));
                switch (c) {
                    case 'l':
                        leaves[x][y] = true;
                        break;
                    case 't':
                        trees[x][y] = true;
                        break;
                    case 'm':
                        mushrooms[x][y] = true;
                        break;
                    default:
                        break;
                }
            }
        }
        if (world != null) {
            B2J_JavaFX_Renderer.updateShape(this);
        }
    }

    public boolean isTree(int direction, DirectionDelta delta, int x, int y) {
        Position pos = positionInDirection(direction, delta, x, y);
        return !isOutside(pos.x, pos.y) && trees[pos.x][pos.y];
    }

    public boolean isLeaf(int direction, DirectionDelta delta, int x, int y) {
        Position pos = positionInDirection(direction, delta, x, y);
        return !isOutside(pos.x, pos.y) && leaves[pos.x][pos.y];
    }

    public boolean isMushroom(int direction, DirectionDelta delta, int x, int y) {
        Position pos = positionInDirection(direction, delta, x, y);
        return !isOutside(pos.x, pos.y) && mushrooms[pos.x][pos.y];
    }

    public void moveMushroom(int fromX, int fromY, int toX, int toY) {
        if (isOutside(fromX, fromY) || isOutside(toX, toY)) {
            return;
        }
        mushrooms[fromX][fromY] = false;
        mushrooms[toX][toY] = true;
    }

    Position positionInDirection(int direction, DirectionDelta delta, int x, int y) {
        int dirIndex = normalizeDirection(direction + delta.delta);
        int dx = 0;
        int dy = 0;
        switch (dirIndex) {
            case NORTH:
                dy = -1;
                break;
            case WEST:
                dx = -1;
                break;
            case SOUTH:
                dy = 1;
                break;
            case EAST:
                dx = 1;
                break;
            default:
                break;
        }
        int nx = (x + dx + sizeX) % sizeX;
        int ny = (y + dy + sizeY) % sizeY;
        return new Position(nx, ny);
    }

    boolean isOutside(int x, int y) {
        return x < 0 || y < 0 || x >= sizeX || y >= sizeY;
    }

    private int normalizeDirection(int dir) {
        int d = dir % 4;
        if (d < 0) {
            d += 4;
        }
        return d;
    }

    public enum DirectionDelta {
        NONE(0),
        LEFT(1),
        RIGHT(-1),
        FRONT(0);

        final int delta;

        DirectionDelta(int delta) {
            this.delta = delta;
        }
    }

    @Override
    public JavaKaraWorld copy() {
        JavaKaraWorld copy = new JavaKaraWorld(sizeX, sizeY);
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                copy.leaves[x][y] = this.leaves[x][y];
                copy.trees[x][y] = this.trees[x][y];
                copy.mushrooms[x][y] = this.mushrooms[x][y];
            }
        }
        copyBaseTo(copy);
        return copy;
    }
}
