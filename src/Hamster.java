/**
 * Repräsentiert den Hamster-Agenten, der in den Hamster-Welt-Beispielen verwendet wird.
 */
public class Hamster {
    public static final int NORD = 0;
    public static final int OST = 1;
    public static final int SUED = 2;
    public static final int WEST = 3;

    private final JavaHamsterWorld world;
    private int row;
    private int column;
    private int direction;
    private int grainCount;

    public Hamster(JavaHamsterWorld world, int reihe, int spalte, int blickrichtung, int anzahlkoerner) {
        if (world == null) {
            throw new RuntimeException("Der Parameter javaHamsterWorld darf nicht null sein.");
        }
        this.world = world;
        this.row = reihe;
        this.column = spalte;
        this.direction = normalizeDirection(blickrichtung);
        this.grainCount = Math.max(0, anzahlkoerner);
    }

    public void vor() {
        Position next = nextCell();
        if (world.isOutside(next.x, next.y)) {
            throw new RuntimeException("Die neue Position (" + next.x + ", " + next.y + ") ist außerhalb der Welt. Der Hamster kann daher nicht weitergehen.");
        }
        if (world.isWall(next.x, next.y)) {
            throw new RuntimeException("An der neuen Position (" + next.x + ", " + next.y + ") befindet sich eine Mauer. Der Hamster kann daher nicht weitergehen.");
        }
        this.column = next.x;
        this.row = next.y;
    }

    public JavaHamsterWorld getWorld() {
        return world;
    }

    public int getBlickrichtung() {
        return direction;
    }

    public int getReihe() {
        return row;
    }

    public int getSpalte() {
        return column;
    }

    public int getAnzahlKoerner() {
        return grainCount;
    }

    public void linksUm() {
        direction = normalizeDirection(direction - 1);
    }

    public void gib() {
        if (grainCount <= 0) {
            throw new RuntimeException("Der Hamster hat kein Korn mehr, das er ablegen könnte.");
        }
        grainCount--;
        world.setGrain(column, row, world.getGrainCount(column, row) + 1);
    }

    public void nimm() {
        if (world.getGrainCount(column, row) <= 0) {
            throw new RuntimeException("In der Zelle, in der sich der Hamster befindet, liegt kein Korn. Der Hamster kann daher keines nehmen.");
        }
        grainCount++;
        world.setGrain(column, row, world.getGrainCount(column, row) - 1);
    }

    public boolean vornFrei() {
        Position next = nextCell();
        return !world.isOutside(next.x, next.y) && !world.isWall(next.x, next.y);
    }

    public boolean maulLeer() {
        return grainCount == 0;
    }

    public boolean kornDa() {
        return world.getGrainCount(column, row) > 0;
    }

    public void schreib(String text) {
        System.out.println(text);
    }

    private Position nextCell() {
        int dx = 0;
        int dy = 0;
        switch (direction) {
            case NORD:
                dy = -1;
                break;
            case OST:
                dx = 1;
                break;
            case SUED:
                dy = 1;
                break;
            case WEST:
                dx = -1;
                break;
            default:
                break;
        }
        return new Position(column + dx, row + dy);
    }

    private int normalizeDirection(int dir) {
        int d = dir % 4;
        if (d < 0) {
            d += 4;
        }
        return d;
    }
}
