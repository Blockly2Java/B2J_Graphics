import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Registriert Actoren und verteilt deren `act`- und Tastatur-Callbacks.
 * Uses reflection to avoid direct JavaFX imports that would fail in headless environments.
 */
class ActorManager {

    private static final boolean FX_AVAILABLE = !GraphicsEnvironment.isHeadless();
    private final EnumMap<ActorType, List<Actor>> actors = new EnumMap<>(ActorType.class);
    private final Set<String> keysDown = new HashSet<>();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    private ScheduledFuture<?> actTask;
    private int timerFrequencyHz = 30;
    private Object scene;

    /**
     * Erzeugt einen Manager für eine JavaFX-Scene und startet den Act-Timer.
     * Accepts Object to avoid direct JavaFX import in callers.
     */
    public ActorManager(Object sceneObj) {
        for (ActorType type : ActorType.values()) {
            actors.put(type, new ArrayList<>());
        }
        if (FX_AVAILABLE && sceneObj != null) {
            this.scene = sceneObj;
            try {
                registerKeyboardListeners(sceneObj);
            } catch (Exception e) {
                // Keyboard registration failed — continue without it
            }
        }
        if (FX_AVAILABLE) {
            setTimerFrequency(timerFrequencyHz);
        }
    }

    private void registerKeyboardListeners(Object sceneObj) throws Exception {
        if (!FX_AVAILABLE || sceneObj == null) {
            return;
        }
        // Use reflection to add keyboard event handlers to the Scene
        Class<?> sceneClass = sceneObj.getClass();
        java.lang.reflect.Method addEventHandlerMethod = sceneClass.getMethod("addEventHandler", java.lang.Object.class, java.lang.Object.class);
        
        // KEY_PRESSED
        Object keyEventType = getEventType("KEY_PRESSED");
        addEventHandlerMethod.invoke(sceneObj, keyEventType, new java.util.function.Consumer<Object>() {
            @Override
            public void accept(Object event) {
                try {
                    String key = normalizeKey(
                        (String) event.getClass().getMethod("getText").invoke(event),
                        (String) event.getClass().getMethod("getCode").invoke(event)
                    );
                    keysDown.add(key);
                    onKeyDown(key);
                } catch (Exception ex) {
                    // ignore
                }
            }
        });
        
        // KEY_RELEASED
        Object keyReleasedType = getEventType("KEY_RELEASED");
        addEventHandlerMethod.invoke(sceneObj, keyReleasedType, new java.util.function.Consumer<Object>() {
            @Override
            public void accept(Object event) {
                try {
                    String key = normalizeKey(
                        (String) event.getClass().getMethod("getText").invoke(event),
                        (String) event.getClass().getMethod("getCode").invoke(event)
                    );
                    keysDown.remove(key);
                    onKeyUp(key);
                } catch (Exception ex) {
                    // ignore
                }
            }
        });
        
        // KEY_TYPED
        Object keyTypedType = getEventType("KEY_TYPED");
        addEventHandlerMethod.invoke(sceneObj, keyTypedType, new java.util.function.Consumer<Object>() {
            @Override
            public void accept(Object event) {
                try {
                    String key = normalizeKey(
                        (String) event.getClass().getMethod("getCharacter").invoke(event),
                        (String) event.getClass().getMethod("getCharacter").invoke(event)
                    );
                    onKeyTyped(key);
                } catch (Exception ex) {
                    // ignore
                }
            }
        });
    }

    private Object getEventType(String typeName) throws Exception {
        Class<?> keyEventTypeClass = Class.forName("javafx.scene.input.KeyEvent");
        java.lang.reflect.Field field = keyEventTypeClass.getField(typeName);
        return field.get(null);
    }

    private String normalizeKey(String primary, String fallback) {
        if (primary != null && !primary.isEmpty()) {
            return primary;
        }
        return fallback == null ? "" : fallback;
    }

    /**
     * Fügt einen Actor einer der Callback-Gruppen hinzu, die von der Welt verwaltet werden.
     */
    public void registerActor(Actor actor, ActorType type) {
        if (actor == null || type == null) {
            return;
        }
        actors.get(type).add(actor);
    }

    /**
     * Entfernt einen Actor aus allen Callback-Gruppen.
     */
    public void unregisterActor(Actor actor) {
        if (actor == null) {
            return;
        }
        for (ActorType type : ActorType.values()) {
            actors.get(type).remove(actor);
        }
    }

    /**
     * Gibt true zurück, wenn mindestens ein Actor noch registriert ist.
     */
    public boolean hasActors() {
        for (ActorType type : ActorType.values()) {
            if (!actors.get(type).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Legt fest, wie oft Act-Callbacks ausgeführt werden.
     */
    public final void setTimerFrequency(int frequencyInHz) {
        if (frequencyInHz <= 0) {
            return;
        }
        timerFrequencyHz = frequencyInHz;
        if (actTask != null) {
            actTask.cancel(false);
        }
        final double dt = 1000.0 / timerFrequencyHz;
        actTask = scheduler.scheduleAtFixedRate(() -> {
            if (FX_AVAILABLE) {
                try {
                    Class<?> platformClass = Class.forName("javafx.application.Platform");
                    java.lang.reflect.Method runLaterMethod = platformClass.getMethod("runLater", java.lang.Runnable.class);
                    runLaterMethod.invoke(null, (Object) new Runnable() {
                        @Override
                        public void run() {
                            callActMethods(dt);
                        }
                    });
                } catch (Exception e) {
                    callActMethods(dt);
                }
            } else {
                callActMethods(dt);
            }
        }, 0, Math.max(1, 1000 / timerFrequencyHz), TimeUnit.MILLISECONDS);
    }

    private void callActMethods(double deltaTimeMs) {
        if (!hasActors()) {
            return;
        }
        List<Actor> actActors = new ArrayList<>(actors.get(ActorType.ACT));
        for (Actor actor : actActors) {
            if (actor.isActing()) {
                actor.act();
                refreshShape(actor);
            }
        }
        List<Actor> actTimeActors = new ArrayList<>(actors.get(ActorType.ACT_WITH_TIME));
        for (Actor actor : actTimeActors) {
            if (actor.isActing()) {
                actor.act(deltaTimeMs);
                refreshShape(actor);
            }
        }
    }

    private void refreshShape(Actor actor) {
        if (actor instanceof Shape shape && !shape.isDestroyed()) {
            B2J_JavaFX_Renderer.updateShape(shape);
        }
    }

    /**
     * Gibt true zurück, wenn die angegebene Taste aktuell gehalten wird.
     */
    public boolean isKeyDown(String key) {
        return keysDown.contains(key);
    }

    private void onKeyDown(String key) {
        List<Actor> list = new ArrayList<>(actors.get(ActorType.KEY_DOWN));
        for (Actor actor : list) {
            actor.onKeyDown(key);
        }
    }

    private void onKeyUp(String key) {
        List<Actor> list = new ArrayList<>(actors.get(ActorType.KEY_UP));
        for (Actor actor : list) {
            actor.onKeyUp(key);
        }
    }

    private void onKeyTyped(String key) {
        List<Actor> list = new ArrayList<>(actors.get(ActorType.KEY_TYPED));
        for (Actor actor : list) {
            actor.onKeyTyped(key);
        }
    }

    /**
     * Stoppt den Timer und beendet den Scheduler-Thread.
     */
    public void shutdown() {
        if (actTask != null) {
            actTask.cancel(false);
        }
        scheduler.shutdownNow();
    }
}