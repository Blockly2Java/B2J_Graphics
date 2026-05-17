/**
 * Repräsentiert den Kara-Agenten, der sich durch die Kara-Welt bewegt.
 */
public class Kara {
    private final JavaKaraWorld world;
    private int x;
    private int y;
    private int direction;

    public Kara(JavaKaraWorld world, int x, int y, int direction) {
        if (world == null) {
            throw new RuntimeException("Der Parameter javaKaraWorld darf nicht null sein.");
        }
        this.world = world;
        this.x = wrap(x, world.getSizeX());
        this.y = wrap(y, world.getSizeY());
        this.direction = normalizeDirection(direction);
    }

    public Position getPosition() {
        return new Position(x, y);
    }

    public void move() {
        Position front = world.positionInDirection(direction, JavaKaraWorld.DirectionDelta.FRONT, x, y);
        if (world.isTree(0, JavaKaraWorld.DirectionDelta.NONE, front.x, front.y)) {
            throw new RuntimeException("An der neuen Position (" + front.x + ", " + front.y + ") befindet sich ein Baumstumpf. Kara kann nicht dorthin gehen.");
        }
        if (world.isMushroom(0, JavaKaraWorld.DirectionDelta.NONE, front.x, front.y)) {
            Position next = world.positionInDirection(direction, JavaKaraWorld.DirectionDelta.FRONT, front.x, front.y);
            if (world.isTree(0, JavaKaraWorld.DirectionDelta.NONE, next.x, next.y)) {
                throw new RuntimeException("An der neuen Position (" + front.x + ", " + front.y + ") befindet sich ein Pilz, dahinter ein Baum. Kara kann den Pilz daher nicht schieben.");
            }
            if (world.isMushroom(0, JavaKaraWorld.DirectionDelta.NONE, next.x, next.y)) {
                throw new RuntimeException("An der neuen Position (" + front.x + ", " + front.y + ") befindet sich ein Pilz, dahinter noch ein Pilz. Kara kann den Pilz daher nicht schieben.");
            }
            world.moveMushroom(front.x, front.y, next.x, next.y);
        }
        x = front.x;
        y = front.y;
    }

    public JavaKaraWorld getWorld() {
        return world;
    }

    public int getDirection() {
        return direction;
    }

    public boolean onLeaf() {
        return world.isLeaf(direction, JavaKaraWorld.DirectionDelta.NONE, x, y);
    }

    public boolean treeFront() {
        return world.isTree(direction, JavaKaraWorld.DirectionDelta.FRONT, x, y);
    }

    public boolean treeLeft() {
        return world.isTree(direction, JavaKaraWorld.DirectionDelta.LEFT, x, y);
    }

    public boolean treeRight() {
        return world.isTree(direction, JavaKaraWorld.DirectionDelta.RIGHT, x, y);
    }

    public boolean mushroomFront() {
        return world.isMushroom(direction, JavaKaraWorld.DirectionDelta.FRONT, x, y);
    }

    public void turnLeft() {
        direction = normalizeDirection(direction + 1);
    }

    public void turnRight() {
        direction = normalizeDirection(direction - 1);
    }

    public void putLeaf() {
        if (world.isLeaf(direction, JavaKaraWorld.DirectionDelta.NONE, x, y)) {
            throw new RuntimeException("Unter Kara liegt schon ein Kleeblatt, es kann an dieser Position nicht noch eines abgelegt werden.");
        }
        world.setLeaf(x, y);
    }

    public void removeLeaf() {
        if (!world.isLeaf(direction, JavaKaraWorld.DirectionDelta.NONE, x, y)) {
            throw new RuntimeException("Unter Kara liegt kein Kleeblatt, daher kann Kara keines aufheben (Methode removeLeaf).");
        }
        world.setLeaf(x, y);
    }

    private int normalizeDirection(int dir) {
        int d = dir % 4;
        if (d < 0) {
            d += 4;
        }
        return d;
    }

    private int wrap(int value, int max) {
        if (max <= 0) {
            return 0;
        }
        int m = value % max;
        if (m < 0) {
            m += max;
        }
        return m;
    }
}
