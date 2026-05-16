public enum SpriteLibrary {
    Background("Background"),
    Bird("Bird"),
    Boardgames("Boardgames"),
    Boulders("Boulders"),
    Breakin("Breakin"),
    Characters("Characters"),
    Characters_1("Characters_1"),
    Explosion_1("Explosion_1"),
    Frogger("Frogger"),
    Hamster("Hamster"),
    Kara("Kara"),
    Minesweeper("Minesweeper"),
    Particles("Particles"),
    pixelmon("pixelmon"),
    Plattforms("Plattforms"),
    PMan("PMan"),
    robot("robot"),
    Ship_1("Ship_1"),
    Sleepy_Blocks("Sleepy_Blocks"),
    Sneaker("Sneaker"),
    Soko("Soko"),
    Space_Shooter_1("Space_Shooter_1"),
    Space_Shooter_2("Space_Shooter_2"),
    standard_textures("standard_textures"),
    TowerDefense("TowerDefense"),
    Werkzeug("Werkzeug"),
    Unknown("Unknown");

    private final String name;

    SpriteLibrary(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static SpriteLibrary fromName(String name) {
        if (name == null) {
            return null;
        }
        for (SpriteLibrary value : values()) {
            if (value.name.equals(name) || value.name().equals(name)) {
                return value;
            }
        }
        return Unknown;
    }
}
