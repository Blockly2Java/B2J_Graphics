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

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;

/**
 * Registriert Actoren und verteilt deren `act`- und Tastatur-Callbacks.
 */
class ActorManager {

    private static final boolean FX_AVAILABLE = !GraphicsEnvironment.isHeadless();
    private final EnumMap<ActorType, List<Actor>> actors = new EnumMap<>(ActorType.class);
    private final Set<String> keysDown = new HashSet<>();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    private ScheduledFuture<?> actTask;
    private int timerFrequencyHz = 30;

    /**
     * Erzeugt einen Manager für eine JavaFX-Scene und startet den Act-Timer.
     */
    public ActorManager(Scene scene) {
        for (ActorType type : ActorType.values()) {
            actors.put(type, new ArrayList<>());
        }
        if (FX_AVAILABLE && scene != null) {
            registerKeyboardListeners(scene);
        }
        if (FX_AVAILABLE) {
            setTimerFrequency(timerFrequencyHz);
        }
    }

    private void registerKeyboardListeners(Scene scene) {
        if (!FX_AVAILABLE) {
            return;
        }
        scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            String key = normalizeKey(event.getText(), event.getCode().getName());
            keysDown.add(key);
            onKeyDown(key);
        });
        scene.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            String key = normalizeKey(event.getText(), event.getCode().getName());
            keysDown.remove(key);
            onKeyUp(key);
        });
        scene.addEventHandler(KeyEvent.KEY_TYPED, event -> {
            String key = normalizeKey(event.getCharacter(), event.getCharacter());
            onKeyTyped(key);
        });
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
        actTask = scheduler.scheduleAtFixedRate(() -> {
            double dt = 1000.0 / timerFrequencyHz;
            if (FX_AVAILABLE) {
                Platform.runLater(() -> callActMethods(dt));
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
