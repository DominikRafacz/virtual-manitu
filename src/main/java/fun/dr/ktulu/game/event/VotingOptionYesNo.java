package fun.dr.ktulu.game.event;

import lombok.Getter;

public class VotingOptionYesNo extends VotingOption {
    private final String option;
    @Getter
    private static final VotingOptionYesNo YES_OPTION = new VotingOptionYesNo("TAK");
    @Getter
    private static final VotingOptionYesNo NO_OPTION = new VotingOptionYesNo("NIE");

    private VotingOptionYesNo(String option) {
        this.option = option;
    }

    @Override
    public String toString() {
        return option;
    }
}
