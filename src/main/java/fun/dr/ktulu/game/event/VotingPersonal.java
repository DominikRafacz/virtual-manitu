package fun.dr.ktulu.game.event;

import fun.dr.ktulu.game.Ktulowiec;
import fun.dr.ktulu.game.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class VotingPersonal extends VotingEvent{

    protected VotingPersonal(@NotNull List<Player> candidates, @NotNull List<Player> voters) {
        this(candidates, voters, false);
    }

    protected VotingPersonal(@NotNull List<Player> candidates, @NotNull List<Player> voters, boolean allowAbstain) {
        super(getVotingOptions(candidates, allowAbstain), voters);
    }

    private static List<VotingOption> getVotingOptions(@NotNull List<Player> candidates, boolean allowAbstain) {
        List<VotingOption> votingOptions = candidates.stream()
                .map(Ktulowiec::getTempName)
                .map(VotingOptionPlayer::new)
                .collect(Collectors.toList());
        if (allowAbstain) {
            votingOptions.add(VotingOptionAbstain.getOPTION());
        }
        return votingOptions;
    }
}
