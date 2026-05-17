public abstract class Actor implements IActor {
    private boolean isActing = true;
    private boolean isDestroyed = false;
    private ActorManager actorManager;
    private boolean registrationDone;

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
    public boolean isActing() {
        return isActing;
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }

    public void stopActing() {
        isActing = false;
    }

    public void restartActing() {
        isActing = true;
    }

    public void destroy() {
        if (isDestroyed) {
            return;
        }
        isDestroyed = true;
        if (actorManager != null) {
            actorManager.unregisterActor(this);
        }
    }

    public boolean isKeyUp(String key) {
        if (actorManager == null) {
            return true;
        }
        return !actorManager.isKeyDown(key);
    }

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

    public static void setActFrequency(int frequencyInHz) {
        World world = World.getWorld();
        ActorManager manager = world.getActorManager();
        if (manager != null) {
            manager.setTimerFrequency(frequencyInHz);
        }
    }

    @Override
    public void act() {
    }

    @Override
    public void act(double deltaTimeMs) {
    }

    @Override
    public void onKeyTyped(String key) {
    }

    @Override
    public void onKeyUp(String key) {
    }

    @Override
    public void onKeyDown(String key) {
    }
}
