/**
 * Definiert die Callback-Methoden, die ein Actor zur Animation und für Tastatureingaben implementieren kann.
 */
public interface IActor {
    boolean isActing();

    void act();

    void act(double deltaTimeMs);

    void onKeyTyped(String key);

    void onKeyUp(String key);

    void onKeyDown(String key);
}
