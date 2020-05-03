package fun.dr.ktulu.game;

import lombok.Getter;
import lombok.Setter;

public abstract class Player extends Ktulowiec {
    @Getter
    @Setter
    protected Role role;

    @Getter
    @Setter
    protected boolean alive;

    public Player(String playerID) {
        super(playerID);
        this.alive = true;
    }
}
