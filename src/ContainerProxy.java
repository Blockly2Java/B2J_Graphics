/**
 * Erzeugt die Ausnahme, die geworfen wird, wenn weiterhin auf ein grafisches Objekt
 * zugegriffen wird, nachdem es zerstört wurde.
 */
public class ContainerProxy {
    public static final ContainerProxy instance = new ContainerProxy();

    private ContainerProxy() {
    }

    public RuntimeException methodOfDestroyedGraphicObjectCalled() {
        return new RuntimeException("Eine Methode eines zerstörten grafischen Objekts wurde aufgerufen.");
    }
}
