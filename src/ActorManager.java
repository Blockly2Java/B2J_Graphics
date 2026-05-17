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

public class ActorManager {
    private final EnumMap<ActorType, List<Actor>> actors = new EnumMap<>(ActorType.class);
    private final Set<String> keysDown = new HashSet<>();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    private ScheduledFuture<?> actTask;
    private int timerFrequencyHz = 30;

    public ActorManager(Scene scene) {
        for (ActorType type : ActorType.values()) {
            actors.put(type, new ArrayList<>());
        }
        if (scene != null) {
            registerKeyboardListeners(scene);
        }
        setTimerFrequency(timerFrequencyHz);
    }

    private void registerKeyboardListeners(Scene scene) {
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

    public void registerActor(Actor actor, ActorType type) {
        if (actor == null || type == null) {
            return;
        }
        actors.get(type).add(actor);
    }

    public void unregisterActor(Actor actor) {
        if (actor == null) {
            return;
        }
        for (ActorType type : ActorType.values()) {
            actors.get(type).remove(actor);
        }
    }

    public boolean hasActors() {
        for (ActorType type : ActorType.values()) {
            if (!actors.get(type).isEmpty()) {
                return true;
            }
        }
        return false;
    }

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
            Platform.runLater(() -> callActMethods(dt));
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
            }
        }
        List<Actor> actTimeActors = new ArrayList<>(actors.get(ActorType.ACT_WITH_TIME));
        for (Actor actor : actTimeActors) {
            if (actor.isActing()) {
                actor.act(deltaTimeMs);
            }
        }
    }

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

    public void shutdown() {
        if (actTask != null) {
            actTask.cancel(false);
        }
        scheduler.shutdownNow();
    }
}
