package fun.dr.ktulu.game.discord;

import fun.dr.ktulu.bot.messaging.BotMessenger;
import fun.dr.ktulu.game.Player;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.jetbrains.annotations.NotNull;

public class CommunicablePlayer extends Player implements CommunicableUser {
    private final String privateCommunicationChannelID;
    private final BotMessenger botMessenger;

    public CommunicablePlayer(@NotNull String playerID,
                              @NotNull String privateCommunicationChannelID,
                              @NotNull BotMessenger botMessenger) {
        super(playerID);
        this.privateCommunicationChannelID = privateCommunicationChannelID;
        this.botMessenger = botMessenger;
    }

    public void sendMessage(@NotNull String message) {
        getCommunicationChannel()
                .sendMessage(message)
                .queue();
    }

    @Override
    public MessageChannel getCommunicationChannel() {
        return botMessenger.getUserCommunicationChannel(this);
    }

    @Override
    public String getCommunicationChannelID() {
        return privateCommunicationChannelID;
    }

    @Override
    public String getCurrentName() {
        return botMessenger.getUserCurrentName(this);
    }
}
