package fun.dr.ktulu.game;

import lombok.Getter;

public enum Faction {
    MIASTO("Miasto"),
    BANDYCI("Bandyci"),
    INDIANIE("Indianie"),
    UFOLE("Ufole");

    @Getter
    private final String name;

    Faction(String name) {
        this.name = name;
    }
}
