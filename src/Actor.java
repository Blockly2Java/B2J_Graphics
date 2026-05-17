/**
 * Basisklasse für Objekte, die auf Zeit und Eingaben reagieren.
 *
 * <p>Erben Sie von dieser Klasse, wenn Ihr Objekt sich selbst animieren oder Tastaturereignisse empfangen soll.</p>
 */
public abstract class Actor implements IActor {
    private boolean isActing = true;
    private boolean isDestroyed = false;
    private ActorManager actorManager;
    private boolean registrationDone;

    /**
     * Erzeugt einen neuen Actor und registriert ggf. überschriebenen Callback-Methoden
     * beim aktuellen World-Objekt.
     */
    protected Actor() {
        ensureRegistration();
    }

    final void ensureRegistration() {
        if (registrationDone) {
            return;
        }
        World world = World.getWorld();
        this.actorManager = world.getActorManager();
        if (actorManager == null) {
            return;
        }
        if (isOverridden("act")) {
            actorManager.registerActor(this, ActorType.ACT);
        }
        if (isOverridden("act", double.class)) {
            actorManager.registerActor(this, ActorType.ACT_WITH_TIME);
        }
        if (isOverridden("onKeyDown", String.class)) {
            actorManager.registerActor(this, ActorType.KEY_DOWN);
        }
        if (isOverridden("onKeyUp", String.class)) {
            actorManager.registerActor(this, ActorType.KEY_UP);
        }
        if (isOverridden("onKeyTyped", String.class)) {
            actorManager.registerActor(this, ActorType.KEY_TYPED);
        }
        registrationDone = true;
    }

    private boolean isOverridden(String methodName, Class<?>... paramTypes) {
        try {
            return getClass().getMethod(methodName, paramTypes).getDeclaringClass() != Actor.class;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    @Override
    /**
     * Gibt zurück, ob der Actor momentan automatische Act-Callbacks erhalten darf.
     */
    public boolean isActing() {
        return isActing;
    }

    /**
     * Gibt zurück, ob der Actor bereits zerstört wurde.
     */
    public boolean isDestroyed() {
        return isDestroyed;
    }

    /**
     * Pausiert automatische Act-Callbacks für diesen Actor.
     */
    public void stopActing() {
        isActing = false;
    }

    /**
     * Aktiviert automatische Act-Callbacks für diesen Actor wieder.
     */
    public void restartActing() {
        isActing = true;
    }

    /**
     * Entfernt den Actor aus der Welt und verhindert weitere Callback-Aufrufe.
     */
    public void destroy() {
        if (isDestroyed) {
            return;
        }
        isDestroyed = true;
        if (actorManager != null) {
            actorManager.unregisterActor(this);
        }
    }

    /**
     * Gibt true zurück, wenn die angegebene Taste momentan nicht gedrückt ist.
     */
    public boolean isKeyUp(String key) {
        if (actorManager == null) {
            return true;
        }
        return !actorManager.isKeyDown(key);
    }

    /**
     * Gibt true zurück, wenn die angegebene Taste momentan gedrückt ist.
     */
    public boolean isKeyDown(String key) {
        if (actorManager == null) {
            return false;
        }
        return actorManager.isKeyDown(key);
    }

    public boolean isGamepadButtonDown(int gamepadIndex, int buttonIndex) {
        return false;
    }

    public boolean isGamepadConnected(int gamepadIndex) {
        return false;
    }

    public double getGamepadAxisValue(int gamepadIndex, int axisIndex) {
        return 0.0;
    }

    /**
     * Ändert, wie oft die Welt Act-Methoden aufruft (in Hertz / Bilder pro Sekunde).
     */
    public static void setActFrequency(int frequencyInHz) {
        World world = World.getWorld();
        ActorManager manager = world.getActorManager();
        if (manager != null) {
            manager.setTimerFrequency(frequencyInHz);
        }
    }

    @Override
    /**
     * Wird einmal pro Frame aufgerufen für Actoren, die die frame-basierte
     * Animationsmethode überschreiben.
     */
    public void act() {
    }

    @Override
    /**
     * Wird einmal pro Frame aufgerufen und übergibt die verstrichene Zeit in Millisekunden.
     */
    public void act(double deltaTimeMs) {
    }

    @Override
    /**
     * Wird aufgerufen, wenn der Benutzer einen Zeichen-Tastendruck tippt.
     */
    public void onKeyTyped(String key) {
    }

    @Override
    /**
     * Wird aufgerufen, wenn eine Taste losgelassen wird.
     */
    public void onKeyUp(String key) {
    }

    @Override
    /**
     * Wird aufgerufen, wenn eine Taste gedrückt wird.
     */
    public void onKeyDown(String key) {
    }
}
