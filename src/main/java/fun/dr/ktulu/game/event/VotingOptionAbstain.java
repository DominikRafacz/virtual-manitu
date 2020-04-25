package fun.dr.ktulu.game.event;

import lombok.Getter;

public class VotingOptionAbstain extends VotingOption {
    private final String option;
    @Getter
    private static final VotingOptionAbstain OPTION = new VotingOptionAbstain("WSTRZYMUJĘ_SIĘ");

    private VotingOptionAbstain(String option) {
        this.option = option;
    }

    @Override
    public String toString() {
        return option;
    }
}
