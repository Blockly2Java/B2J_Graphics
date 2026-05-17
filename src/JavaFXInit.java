import java.awt.GraphicsEnvironment;

import javafx.application.Platform;

/**
 * Holds all JavaFX imports and initialization.
 * This class is only loaded when JavaFX is actually needed.
 * Headless environments will never load this class, avoiding NoClassDefFoundError.
 */
class JavaFXInit {

    static final boolean FX_AVAILABLE = !GraphicsEnvironment.isHeadless();

    static {
        if (FX_AVAILABLE) {
            try {
                Platform.startup(() -> {
                    // No-op — only here to initialize the JavaFX Toolkit
                });
            } catch (IllegalStateException e) {
                // Toolkit already initialized (e.g., via Application.launch() in B2J_Graphics_Main)
            } catch (Exception e) {
                // JavaFX initialization failed on this headless system
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