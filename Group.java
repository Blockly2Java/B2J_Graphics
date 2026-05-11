import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Group extends Shape {
    private List<Shape> children;

    public Group() {
        super(0, 0);
        System.out.println(">>> Group()");
        children = new ArrayList<>();
        System.out.println("<<< Group()");
    }

    public Group(Shape... shapes) {
        this();
        String shapesLog = shapes == null ? "null" : Arrays.toString(shapes);
        System.out.println(">>> Group(" + shapesLog + ")");
        if (shapes == null) {
            System.out.println("<<< Group(" + shapesLog + ")");
            return;
        }
        for (Shape shape : shapes) {
            add(shape);
        }
        System.out.println("<<< Group(" + shapesLog + ")");
    }

    public void add(Shape shape) {
        System.out.println(">>> add(" + shape + ")");
        if (shape == null) {
            System.out.println("<<< add(" + shape + ")");
            return;
        }
        children.add(shape);
        updateCenterFromChildren();
        System.out.println("<<< add(" + shape + ")");
    }

    @Override
    public void move(double dx, double dy) {
        System.out.println(">>> move(" + dx + ", " + dy + ")");
        super.move(dx, dy);
        for (Shape shape : children) {
            shape.move(dx, dy);
        }
        System.out.println("<<< move(" + dx + ", " + dy + ")");
    }

    private void updateCenterFromChildren() {
        System.out.println(">>> updateCenterFromChildren()");
        if (children.isEmpty()) {
            System.out.println("<<< updateCenterFromChildren()");
            return;
        }

        double sumX = 0;
        double sumY = 0;
        for (Shape child : children) {
            sumX += child.getCenterX();
            sumY += child.getCenterY();
        }

        double targetX = sumX / children.size();
        double targetY = sumY / children.size();
        super.move(targetX - getCenterX(), targetY - getCenterY());
        System.out.println("<<< updateCenterFromChildren()");
    }
}
