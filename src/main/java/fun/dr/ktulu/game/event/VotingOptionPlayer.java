package fun.dr.ktulu.game.event;

public class VotingOptionPlayer extends VotingOption {
    private final String playerTempName;

    public VotingOptionPlayer(String playerTempName) {
        this.playerTempName = playerTempName;
    }

    @Override
    public String toString() {
        return playerTempName;
    }
}
