package fun.dr.ktulu.game.event;

import fun.dr.ktulu.game.Ktulowiec;
import fun.dr.ktulu.game.Player;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public abstract class VotingEvent extends SpecialEvent {
    @Getter
    private final List<VotingOption> options;
    @Getter
    private final Map<Player, VotingOption> votes;

    protected VotingEvent(@NotNull List<VotingOption> options, @NotNull List<Player> voters) {
        this.options = options;
        votes = new HashMap<>();
        voters.forEach(voter -> votes.put(voter, null));
    }

    public Set<Player> getVoters() {
        return votes.keySet();
    }

    public void vote(Player voter, VotingOption votingOption) {
        votes.put(voter, votingOption);
    }

    public boolean everyoneVoted() {
        return votes.keySet().stream().allMatch(voter -> votes.get(voter) != null);
    }

    public String getResultsFormatted() {
        Map<VotingOption, Integer> summary = getSummary();
        return options.stream()
                .map(option -> "Na opcję " + option.toString() + " oddano " +
                        summary.get(option) + " głosów: \n" +
                        votes.keySet().stream()
                                .filter(voter -> votes.get(voter) == option)
                                .map(Ktulowiec::getCurrentName)
                                .collect(Collectors.joining(", ")))
                .collect(Collectors.joining("\n")) +
                "\n\n Zwycięża opcja/opcje: " + getResults().stream()
                .map(VotingOption::toString)
                .collect(Collectors.joining(", ")) + "! Gratulacje! :DDD";
    }

    public Map<VotingOption, Integer> getSummary() {
        Map<VotingOption, Integer> summary = new HashMap<>();
        options.forEach(option -> summary.put(option, (int) votes.keySet().stream()
                .filter(voter -> votes.get(voter) == option).count()));
        return summary;
    }

    public List<VotingOption> getResults() {
        Map<VotingOption, Integer> summary = getSummary();
        int numWinner = summary.keySet().stream()
                .mapToInt(summary::get)
                .max()
                .orElse(-1);

        return summary.keySet().stream()
                .filter(option -> summary.get(option) == numWinner)
                .collect(Collectors.toList());
    }
}
