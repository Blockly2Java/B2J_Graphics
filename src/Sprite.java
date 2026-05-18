import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
import java.util.List;

/**
 * Zeigt ein Bild aus der Sprite-Bibliothek an und kann Frame-Animationen abspielen.
 */
class Sprite extends Shape {

    private static final boolean FX_AVAILABLE = !GraphicsEnvironment.isHeadless();
    private static final String DEFAULT_SPRITE_BASE = "/sprites";

    private SpriteLibrary spriteLibrary;
    private int imageIndex;
    private ScaleMode scaleMode = ScaleMode.nearest_neighbour;
    // Use Object to avoid direct JavaFX import
    private Object image;
    private double baseWidth = 50;
    private double baseHeight = 50;

    private int[] animationIndices;
    private RepeatType repeatType = RepeatType.once;
    private boolean animationPaused;

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

    public void setImage(SpriteLibrary spriteLibrary, int imageIndex) {
        this.spriteLibrary = spriteLibrary;
        this.imageIndex = imageIndex;
        loadImage();
        if (FX_AVAILABLE && world != null) {
            JavaFXBridge.updateSpriteShape(this);
        }
    }

    public void setImageIndex(int imageIndex) {
        this.imageIndex = imageIndex;
        loadImage();
        if (FX_AVAILABLE && world != null) {
            JavaFXBridge.updateSpriteShape(this);
        }
    }

    public void playAnimation(int[] imageIndexArray, RepeatType repeatType, int imagesPerSecond) {
        if (imageIndexArray == null || imageIndexArray.length == 0) {
            return;
        }
        this.animationIndices = imageIndexArray.clone();
        this.repeatType = repeatType == null ? RepeatType.once : repeatType;
        JavaFXBridge.startSpriteAnimation(this, imagesPerSecond, buildSequenceFromArray(imageIndexArray), this.repeatType);
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
        JavaFXBridge.stopSpriteAnimation(this);
        animationPaused = false;
        animationIndices = null;
    }

    public void pauseAnimation() {
        JavaFXBridge.pauseSpriteAnimation(this);
        animationPaused = true;
    }

    public void resumeAnimation() {
        JavaFXBridge.resumeSpriteAnimation(this);
        animationPaused = false;
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
        if (FX_AVAILABLE && world != null) {
            JavaFXBridge.updateSpriteShape(this);
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
        return JavaFXBridge.getSpritePixelColor(this, x, y);
    }

    public double getPixelAlpha(int x, int y) {
        if (image == null) {
            return 0.0;
        }
        return JavaFXBridge.getSpritePixelAlpha(this, x, y);
    }

    public Object getImage() {
        return image;
    }

    @Override
    protected Bounds getBounds() {
        double w = getWidth();
        double h = getHeight();
        return new Bounds(centerX - w / 2.0, centerY - h / 2.0, centerX + w / 2.0, centerY + h / 2.0);
    }

    private void loadImage() {
        if (!FX_AVAILABLE) {
            image = null;
            baseWidth = Math.max(baseWidth, 50);
            baseHeight = Math.max(baseHeight, 50);
            return;
        }
        Object loaded = JavaFXBridge.loadSpriteImage(spriteLibrary, imageIndex);
        if (loaded != null) {
            image = loaded;
            baseWidth = JavaFXBridge.getImageWidth(image);
            baseHeight = JavaFXBridge.getImageHeight(image);
        } else {
            image = null;
            baseWidth = Math.max(baseWidth, 50);
            baseHeight = Math.max(baseHeight, 50);
        }
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
}