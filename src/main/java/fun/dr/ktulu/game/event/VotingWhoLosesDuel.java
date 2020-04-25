package fun.dr.ktulu.game.event;

import fun.dr.ktulu.game.Player;

import java.util.List;

public class VotingWhoLosesDuel extends VotingPersonal {
    public VotingWhoLosesDuel(List<Player> duelers, List<Player> voters) {
        super(duelers, voters, true);
    }
}
