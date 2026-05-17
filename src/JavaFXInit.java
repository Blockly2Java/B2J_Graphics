import java.awt.GraphicsEnvironment;

/**
 * Holds all JavaFX imports and initialization.
 * This class is only loaded when JavaFX is actually needed.
 * Headless environments will never load this class, avoiding NoClassDefFoundError.
 * Uses reflection to avoid direct compile-time dependency on JavaFX classes.
 */
class JavaFXInit {

    static final boolean FX_AVAILABLE = !GraphicsEnvironment.isHeadless();

    static {
        if (FX_AVAILABLE) {
            try {
                // Use reflection to load and call Platform.startup()
                // This prevents NoClassDefFoundError if JavaFX is missing at runtime
                Class<?> platformClass = Class.forName("javafx.application.Platform");
                java.lang.reflect.Method startupMethod = platformClass.getMethod("startup", java.util.function.Consumer.class);
                startupMethod.invoke(null, (java.util.function.Consumer<?>) args -> {
                    // No-op — only here to initialize the JavaFX Toolkit
                });
            } catch (Throwable e) {
                // JavaFX initialization failed or is unavailable — that's fine for headless/CI environments
            }
        }
    }

    /**
     * Lazily loads the JavaFX renderer classes.
     * This ensures they are only loaded when JavaFX is actually in use.
     */
    static void loadRendererClasses() {
        if (FX_AVAILABLE) {
            try {
                Class.forName("B2J_JavaFX_Renderer");
                Class.forName("ActorManager");
                Class.forName("MouseManager");
            } catch (ClassNotFoundException e) {
                // Should not happen if FX_AVAILABLE is true, but handle gracefully
            }
        }
    }
}