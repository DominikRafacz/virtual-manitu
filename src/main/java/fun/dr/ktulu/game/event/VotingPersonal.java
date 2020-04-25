package fun.dr.ktulu.game.event;

import fun.dr.ktulu.game.Ktulowiec;
import fun.dr.ktulu.game.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class VotingPersonal extends VotingEvent{
    protected final List<Player> candidates;

    protected VotingPersonal(@NotNull List<Player> candidates, @NotNull List<Player> voters) {
        super(candidates.stream()
                .map(Ktulowiec::getTempName)
                .map(VotingOptionPlayer::new)
                .collect(Collectors.toList()), voters);
        this.candidates = candidates;
    }
}
