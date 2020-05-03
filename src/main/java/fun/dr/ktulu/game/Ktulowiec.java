package fun.dr.ktulu.game;

import lombok.Getter;

public abstract class Ktulowiec {
    @Getter
    protected final String ID;

    public Ktulowiec(String ID) {
        this.ID = ID;
    }

    public abstract String getCurrentName();
}
