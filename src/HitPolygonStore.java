import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HitPolygonStore {
    private static final Map<String, List<Shape.Point>> polygonStore = new HashMap<>();

    private HitPolygonStore() {
    }

    public static List<Shape.Point> getPolygonForSprite(String name, int index, Sprite sprite) {
        String key = name + "#" + index;
        List<Shape.Point> polygon = polygonStore.get(key);
        if (polygon == null) {
            polygon = createBoundingPolygon(sprite);
            polygonStore.put(key, polygon);
        }
        return polygon;
    }

    private static List<Shape.Point> createBoundingPolygon(Sprite sprite) {
        Shape.Bounds bounds = sprite.getBounds();
        List<Shape.Point> polygon = new ArrayList<>();
        polygon.add(new Shape.Point(bounds.minX, bounds.minY));
        polygon.add(new Shape.Point(bounds.maxX, bounds.minY));
        polygon.add(new Shape.Point(bounds.maxX, bounds.maxY));
        polygon.add(new Shape.Point(bounds.minX, bounds.maxY));
        return polygon;
    }
}
