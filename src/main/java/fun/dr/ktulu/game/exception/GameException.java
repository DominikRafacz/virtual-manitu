package fun.dr.ktulu.game.exception;

import lombok.Getter;

public class GameException extends Exception{
    @Getter
    private final String message;

    public GameException(String message) {
        super();
        this.message = message;
    }
}
