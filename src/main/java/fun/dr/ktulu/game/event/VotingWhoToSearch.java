package fun.dr.ktulu.game.event;

import fun.dr.ktulu.game.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class VotingWhoToSearch extends VotingPersonal {

    public VotingWhoToSearch(@NotNull List<Player> searchCandidates, @NotNull List<Player> voters) {
        super(searchCandidates, voters);
    }
}
