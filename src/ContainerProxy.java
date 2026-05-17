/**
 * Creates the exception used when code keeps using a graphic object after it was destroyed.
 */
public class ContainerProxy {
    public static final ContainerProxy instance = new ContainerProxy();

    private ContainerProxy() {
    }

    public RuntimeException methodOfDestroyedGraphicObjectCalled() {
        return new RuntimeException("Eine Methode eines zerstörten grafischen Objekts wurde aufgerufen.");
    }
}
