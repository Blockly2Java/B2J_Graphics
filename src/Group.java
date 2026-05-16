import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Group<T extends Shape> extends Shape {
    private final List<Shape> children = new ArrayList<>();

    public Group() {
        super(0, 0);
        registerWithWorld();
    }

    public Group(Shape... shapes) {
        this();
        if (shapes != null) {
            for (Shape shape : shapes) {
                addInternal(shape);
            }
        }
    }

    public void add(Shape shape) {
        addInternal(shape);
    }

    public void add(Shape... shapes) {
        if (shapes == null) {
            return;
        }
        for (Shape shape : shapes) {
            addInternal(shape);
        }
    }

    public void remove(Shape shape) {
        if (shape == null) {
            return;
        }
        if (children.remove(shape)) {
            shape.belongsToGroup = null;
            if (world != null) {
                world.registerShape(shape);
            }
            updateCenterFromChildren();
        }
    }

    public void remove(int index) {
        if (index < 0 || index >= children.size()) {
            return;
        }
        remove(children.get(index));
    }

    @SuppressWarnings("unchecked")
    public T get(int index) {
        if (index < 0 || index >= children.size()) {
            return null;
        }
        return (T) children.get(index);
    }

    public int indexOf(Shape shape) {
        return children.indexOf(shape);
    }

    public int size() {
        return children.size();
    }

    public void empty() {
        List<Shape> copy = new ArrayList<>(children);
        for (Shape child : copy) {
            remove(child);
        }
    }

    public void destroyAllChildren() {
        List<Shape> copy = new ArrayList<>(children);
        for (Shape child : copy) {
            child.destroy();
        }
        children.clear();
    }

    @Override
    public Group<T> copy() {
        Group<T> copy = new Group<>();
        copyBaseTo(copy);
        for (Shape child : children) {
            copy.add(child.copy());
        }
        return copy;
    }

    @Override
    public Shape move(double dx, double dy) {
        super.move(dx, dy);
        for (Shape shape : children) {
            shape.move(dx, dy);
        }
        return this;
    }

    @Override
    public boolean containsPoint(double x, double y) {
        for (Shape shape : children) {
            if (shape.containsPoint(x, y)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean collidesWith(Shape other) {
        if (other == null) {
            return false;
        }
        for (Shape shape : children) {
            if (shape.collidesWith(other)) {
                return true;
            }
        }
        return false;
    }

    public List<Shape> getCollidingShapes(Shape other) {
        if (other == null) {
            return Collections.emptyList();
        }
        List<Shape> result = new ArrayList<>();
        for (Shape shape : children) {
            if (shape.collidesWith(other)) {
                result.add(shape);
            }
        }
        return result;
    }

    public List<Shape> getChildren() {
        return Collections.unmodifiableList(children);
    }

    @Override
    protected Bounds getBounds() {
        if (children.isEmpty()) {
            return new Bounds(centerX, centerY, centerX, centerY);
        }
        Bounds result = null;
        for (Shape shape : children) {
            Bounds b = shape.getBounds();
            if (result == null) {
                result = new Bounds(b.minX, b.minY, b.maxX, b.maxY);
            } else {
                result.minX = Math.min(result.minX, b.minX);
                result.minY = Math.min(result.minY, b.minY);
                result.maxX = Math.max(result.maxX, b.maxX);
                result.maxY = Math.max(result.maxY, b.maxY);
            }
        }
        return result == null ? new Bounds(centerX, centerY, centerX, centerY) : result;
    }

    void bringChildToFront(Shape child) {
        int index = children.indexOf(child);
        if (index < 0 || index == children.size() - 1) {
            return;
        }
        children.remove(index);
        children.add(child);
    }

    void sendChildToBack(Shape child) {
        int index = children.indexOf(child);
        if (index <= 0) {
            return;
        }
        children.remove(index);
        children.add(0, child);
    }

    private boolean containsRecursively(Shape shape) {
        for (Shape child : children) {
            if (child == shape) {
                return true;
            }
            if (child instanceof Group && ((Group<?>) child).containsRecursively(shape)) {
                return true;
            }
        }
        return false;
    }

    private void addInternal(Shape shape) {
        if (shape == null) {
            return;
        }
        if (shape == this || containsRecursively(shape)) {
            return;
        }
        if (shape.belongsToGroup != null) {
            shape.belongsToGroup.remove(shape);
        } else if (world != null) {
            world.unregisterFromDefaultList(shape);
        }
        children.add(shape);
        shape.belongsToGroup = this;
        updateCenterFromChildren();
    }

    private void updateCenterFromChildren() {
        if (children.isEmpty()) {
            return;
        }
        double sumX = 0.0;
        double sumY = 0.0;
        for (Shape child : children) {
            sumX += child.getCenterX();
            sumY += child.getCenterY();
        }
        centerX = sumX / children.size();
        centerY = sumY / children.size();
    }

    @Override
    public String toString() {
        return "Group" + Arrays.toString(children.toArray());
    }
}
