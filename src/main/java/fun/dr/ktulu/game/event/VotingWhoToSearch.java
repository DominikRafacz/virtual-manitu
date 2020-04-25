package fun.dr.ktulu.game.event;

import fun.dr.ktulu.game.Ktulowiec;
import fun.dr.ktulu.game.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class VotingWhoToSearch extends VotingEvent {
    private final List<Player> searchCandidates;

    public VotingWhoToSearch(@NotNull List<Player> searchCandidates, @NotNull List<Player> voters) {
        super(searchCandidates.stream()
                .map(Ktulowiec::getTempName)
                .map(VotingOptionPlayer::new)
                .collect(Collectors.toList()),
                voters);
        this.searchCandidates = searchCandidates;
    }
}
