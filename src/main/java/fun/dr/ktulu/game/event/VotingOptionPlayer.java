package fun.dr.ktulu.game.event;

public class VotingOptionPlayer extends VotingOption {
    private final String playerCurrentName;

    public VotingOptionPlayer(String playerCurrentName) {
        this.playerCurrentName = playerCurrentName;
    }

    @Override
    public String toString() {
        return playerCurrentName;
    }
}
