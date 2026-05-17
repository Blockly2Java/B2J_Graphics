import java.util.ArrayList;
import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.util.Duration;

/**
 * Zeigt ein Bild aus der Sprite-Bibliothek an und kann Frame-Animationen abspielen.
 */
public class Sprite extends Shape {
    private static final String DEFAULT_SPRITE_BASE = "/sprites";

    private SpriteLibrary spriteLibrary;
    private int imageIndex;
    private ScaleMode scaleMode = ScaleMode.nearest_neighbour;
    private Image image;
    private double baseWidth = 50;
    private double baseHeight = 50;

    private Timeline animationTimeline;
    private RepeatType repeatType = RepeatType.once;
    private boolean animationPaused;
    private int[] animationIndices;

    public Sprite(double x, double y, SpriteLibrary spriteLibrary, int index, ScaleMode scaleMode) {
        super(x, y);
        this.spriteLibrary = spriteLibrary;
        this.imageIndex = index;
        if (scaleMode != null) {
            this.scaleMode = scaleMode;
        }
        loadImage();
        registerWithWorld();
    }

    public Sprite(double x, double y, SpriteLibrary spriteLibrary, int index) {
        this(x, y, spriteLibrary, index, ScaleMode.nearest_neighbour);
    }

    public Sprite(double x, double y, SpriteLibrary spriteLibrary) {
        this(x, y, spriteLibrary, 0, ScaleMode.nearest_neighbour);
    }

    public Sprite(Shape shape, ScaleMode scaleMode) {
        super(0, 0);
        if (shape != null) {
            Bounds bounds = shape.getBounds();
            this.centerX = (bounds.minX + bounds.maxX) / 2.0;
            this.centerY = (bounds.minY + bounds.maxY) / 2.0;
            this.baseWidth = bounds.maxX - bounds.minX;
            this.baseHeight = bounds.maxY - bounds.minY;
        }
        if (scaleMode != null) {
            this.scaleMode = scaleMode;
        }
        registerWithWorld();
    }

    public Sprite(Shape shape) {
        this(shape, ScaleMode.nearest_neighbour);
    }

    public void setImage(SpriteLibrary spriteLibrary, int imageIndex) {
        this.spriteLibrary = spriteLibrary;
        this.imageIndex = imageIndex;
        loadImage();
        if (world != null) {
            B2J_JavaFX_Renderer.updateShape(this);
        }
    }

    public void setImageIndex(int imageIndex) {
        this.imageIndex = imageIndex;
        loadImage();
        if (world != null) {
            B2J_JavaFX_Renderer.updateShape(this);
        }
    }

    public void playAnimation(int[] imageIndexArray, RepeatType repeatType, int imagesPerSecond) {
        if (imageIndexArray == null || imageIndexArray.length == 0) {
            return;
        }
        this.animationIndices = imageIndexArray.clone();
        this.repeatType = repeatType == null ? RepeatType.once : repeatType;
        startTimeline(imagesPerSecond, buildSequenceFromArray(imageIndexArray));
    }

    public void playAnimation(int fromIndex, int toIndex, RepeatType repeatType, int imagesPerSecond) {
        if (fromIndex == toIndex) {
            setImageIndex(fromIndex);
            return;
        }
        int start = Math.min(fromIndex, toIndex);
        int end = Math.max(fromIndex, toIndex);
        List<Integer> indices = new ArrayList<>();
        for (int i = start; i <= end; i++) {
            indices.add(i);
        }
        int[] array = indices.stream().mapToInt(Integer::intValue).toArray();
        playAnimation(array, repeatType, imagesPerSecond);
    }

    public void stopAnimation() {
        if (animationTimeline != null) {
            animationTimeline.stop();
        }
        animationPaused = false;
        animationIndices = null;
    }

    public void pauseAnimation() {
        if (animationTimeline != null) {
            animationTimeline.pause();
            animationPaused = true;
        }
    }

    public void resumeAnimation() {
        if (animationTimeline != null && animationPaused) {
            animationTimeline.play();
            animationPaused = false;
        }
    }

    public void setAsBackgroundImage() {
        sendToBack();
        setStatic(true);
    }

    @Override
    public Sprite copy() {
        Sprite copy = new Sprite(centerX, centerY, spriteLibrary, imageIndex, scaleMode);
        copy.baseWidth = baseWidth;
        copy.baseHeight = baseHeight;
        copyBaseTo(copy);
        return copy;
    }

    public double getWidth() {
        return baseWidth * scaleFactor;
    }

    public double getHeight() {
        return baseHeight * scaleFactor;
    }

    public int getImageIndex() {
        return imageIndex;
    }

    public void makeTiling(double width, double height) {
        this.baseWidth = width;
        this.baseHeight = height;
        if (world != null) {
            B2J_JavaFX_Renderer.updateShape(this);
        }
    }

    public void makeTiling(double width, double height, double gapX, double gapY) {
        makeTiling(width, height);
    }

    public TileImage getTileImage() {
        return new TileImage(baseWidth, baseHeight, 0, 0);
    }

    public int getPixelColor(int x, int y) {
        if (image == null) {
            return 0;
        }
        PixelReader reader = image.getPixelReader();
        if (reader == null) {
            return 0;
        }
        int ix = clampPixel(x, image.getWidth());
        int iy = clampPixel(y, image.getHeight());
        return reader.getArgb(ix, iy) & 0x00ffffff;
    }

    public double getPixelAlpha(int x, int y) {
        if (image == null) {
            return 0.0;
        }
        PixelReader reader = image.getPixelReader();
        if (reader == null) {
            return 0.0;
        }
        int ix = clampPixel(x, image.getWidth());
        int iy = clampPixel(y, image.getHeight());
        int argb = reader.getArgb(ix, iy);
        int alpha = (argb >> 24) & 0xff;
        return alpha / 255.0;
    }

    public Image getImage() {
        return image;
    }

    @Override
    protected Bounds getBounds() {
        double w = getWidth();
        double h = getHeight();
        return new Bounds(centerX - w / 2.0, centerY - h / 2.0, centerX + w / 2.0, centerY + h / 2.0);
    }

    private void loadImage() {
        Image loaded = null;
        if (spriteLibrary != null) {
            String resourcePath = String.format("%s/%s/%d.png", DEFAULT_SPRITE_BASE, spriteLibrary.getName(), imageIndex);
            loaded = loadImageFromResource(resourcePath);
        }
        if (loaded == null) {
            image = null;
            baseWidth = Math.max(baseWidth, 50);
            baseHeight = Math.max(baseHeight, 50);
            return;
        }
        image = loaded;
        baseWidth = image.getWidth();
        baseHeight = image.getHeight();
    }

    private Image loadImageFromResource(String path) {
        try {
            return new Image(Sprite.class.getResourceAsStream(path));
        } catch (Exception e) {
            return null;
        }
    }

    private void startTimeline(int imagesPerSecond, List<Integer> sequence) {
        stopAnimation();
        if (imagesPerSecond <= 0) {
            imagesPerSecond = 1;
        }
        Duration frameDuration = Duration.millis(1000.0 / imagesPerSecond);
        animationTimeline = new Timeline();
        for (int i = 0; i < sequence.size(); i++) {
            int index = sequence.get(i);
            animationTimeline.getKeyFrames().add(new KeyFrame(frameDuration.multiply(i), event -> setImageIndex(index)));
        }
        animationTimeline.setCycleCount(repeatType == RepeatType.loop || repeatType == RepeatType.backAndForth
            ? Timeline.INDEFINITE
            : 1);
        animationTimeline.play();
    }

    private List<Integer> buildSequenceFromArray(int[] array) {
        List<Integer> sequence = new ArrayList<>();
        for (int i : array) {
            sequence.add(i);
        }
        if (repeatType == RepeatType.backAndForth && array.length > 1) {
            for (int i = array.length - 2; i > 0; i--) {
                sequence.add(array[i]);
            }
        }
        return sequence;
    }

    private int clampPixel(int value, double max) {
        if (max <= 0) {
            return 0;
        }
        return Math.max(0, Math.min((int) max - 1, value));
    }
}
