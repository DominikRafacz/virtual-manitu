package fun.dr.ktulu.game;

import lombok.Getter;

public enum Faction {
    MIASTO("Miasto", 1),
    BANDYCI("Bandyci", 2),
    INDIANIE("Indianie", 3),
    UFOLE("Ufole", 4);

    @Getter
    private final String name;

    @Getter
    private final int order;

    Faction(String name, int order) {
        this.name = name;
        this.order = order;
    }
}
