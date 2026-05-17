/**
 * Base class for objects that react to time and input.
 *
 * <p>Subclass this when you want your object to animate itself or receive key events.</p>
 */
public abstract class Actor implements IActor {
    private boolean isActing = true;
    private boolean isDestroyed = false;
    private ActorManager actorManager;
    private boolean registrationDone;

    /**
     * Creates a new actor and registers any overridden callbacks with the current world.
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
     * Returns whether the actor is currently allowed to receive act callbacks.
     */
    public boolean isActing() {
        return isActing;
    }

    /**
     * Returns whether the actor has already been destroyed.
     */
    public boolean isDestroyed() {
        return isDestroyed;
    }

    /**
     * Pauses automatic act callbacks for this actor.
     */
    public void stopActing() {
        isActing = false;
    }

    /**
     * Re-enables automatic act callbacks for this actor.
     */
    public void restartActing() {
        isActing = true;
    }

    /**
     * Removes the actor from the world and stops it from receiving further callbacks.
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
     * Returns true when the given key is not currently pressed.
     */
    public boolean isKeyUp(String key) {
        if (actorManager == null) {
            return true;
        }
        return !actorManager.isKeyDown(key);
    }

    /**
     * Returns true when the given key is currently pressed.
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
     * Changes how often the world calls act methods, measured in frames per second.
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
     * Called once per frame for actors that override the frame-based animation hook.
     */
    public void act() {
    }

    @Override
    /**
     * Called once per frame for actors that need elapsed time in milliseconds.
     */
    public void act(double deltaTimeMs) {
    }

    @Override
    /**
     * Called when the user types a character key.
     */
    public void onKeyTyped(String key) {
    }

    @Override
    /**
     * Called when a key is released.
     */
    public void onKeyUp(String key) {
    }

    @Override
    /**
     * Called when a key is pressed.
     */
    public void onKeyDown(String key) {
    }
}
