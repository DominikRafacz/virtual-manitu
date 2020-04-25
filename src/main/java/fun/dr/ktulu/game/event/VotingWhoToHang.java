package fun.dr.ktulu.game.event;

import fun.dr.ktulu.game.Player;

import java.util.List;

public class VotingWhoToHang extends VotingPersonal {
    public VotingWhoToHang(List<Player> hangCandidates, List<Player> voters) {
        super(hangCandidates, voters);
    }
}
