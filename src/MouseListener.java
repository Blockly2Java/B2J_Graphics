/**
 * Empfängt Mausereignisse in Weltkoordinaten.
 */
public interface MouseListener {
    /**
     * Wird aufgerufen, wenn eine Maustaste losgelassen wird.
     *
     * @param x Welt-X-Koordinate des Ereignisses
     * @param y Welt-Y-Koordinate des Ereignisses
     * @param button ganzzahliger Tasten-Code. Verwendet JavaFX MouseButton.ordinal():
     *               0 = NONE, 1 = PRIMARY (normalerweise links), 2 = MIDDLE, 3 = SECONDARY (normalerweise rechts),
     *               höhere Werte für zusätzliche Tasten.
     */
    void onMouseUp(double x, double y, int button);

    /**
     * Wird aufgerufen, wenn eine Maustaste gedrückt wird.
     *
     * @param x Welt-X-Koordinate des Ereignisses
     * @param y Welt-Y-Koordinate des Ereignisses
     * @param button ganzzahliger Tasten-Code. Verwendet JavaFX MouseButton.ordinal():
     *               0 = NONE, 1 = PRIMARY (normalerweise links), 2 = MIDDLE, 3 = SECONDARY (normalerweise rechts),
     *               höhere Werte für zusätzliche Tasten.
     */
    void onMouseDown(double x, double y, int button);

    /**
     * Wird aufgerufen, wenn sich die Maus über der Welt bewegt. Es werden keine Tasteninformationen übergeben.
     *
     * @param x Welt-X-Koordinate der Maus
     * @param y Welt-Y-Koordinate der Maus
     */
    void onMouseMove(double x, double y);

    /**
     * Wird aufgerufen, wenn die Maus erstmals den Bereich der Form betritt.
     *
     * @param x Welt-X-Koordinate der Maus
     * @param y Welt-Y-Koordinate der Maus
     */
    void onMouseEnter(double x, double y);

    /**
     * Wird aufgerufen, wenn die Maus den Bereich der Form verlässt.
     *
     * @param x Welt-X-Koordinate der Maus
     * @param y Welt-Y-Koordinate der Maus
     */
    void onMouseLeave(double x, double y);
}
