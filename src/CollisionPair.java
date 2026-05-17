/**
 * Enthält zwei Formen, die als kollidierend erkannt wurden.
 */
public class CollisionPair<U extends Shape, V extends Shape> {
    public U shapeA;
    public V shapeB;

    public CollisionPair(U shapeA, V shapeB) {
        this.shapeA = shapeA;
        this.shapeB = shapeB;
    }
}
