package fun.dr.ktulu.game;

public enum Faction {
    MIASTO("Miasto"),
    BANDYCI("Bandyci"),
    INDIANIE("Indianie"),
    UFOLE("Ufole");

    private final String name;

    Faction(String name) {
        this.name = name;
    }
}
