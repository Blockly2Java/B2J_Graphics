/**
 * Describes the basic world information available to graphics objects and actors.
 */
public interface IWorld {
    double getWidth();

    double getHeight();

    double getLeft();

    double getTop();

    void setCursor(String cursor);

    boolean hasActors();
}
