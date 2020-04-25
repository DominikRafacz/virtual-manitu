package fun.dr.ktulu.game.event;

import fun.dr.ktulu.game.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VotingIfToHang extends VotingEvent {

    public VotingIfToHang(@NotNull List<Player> voters) {
        super(new ArrayList<>(Arrays.asList(
                VotingOptionYesNo.getNO_OPTION(),
                VotingOptionYesNo.getYES_OPTION()
        )), voters);

    }
}
