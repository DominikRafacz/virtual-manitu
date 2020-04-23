package fun.dr.ktulu.game;

import fun.dr.ktulu.bot.AppManager;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.PrivateChannel;

public class Player extends Ktulowiec {
    private final String communicationChannelID;

    @Getter
    @Setter
    private String role;

    public Player(String playerID, String communicationChannelID) {
        super(playerID);
        this.communicationChannelID = communicationChannelID;
    }

    public PrivateChannel getCommunicationChannel() {
        return AppManager.getInstance().getJda().getPrivateChannelById(communicationChannelID);
    }
}
