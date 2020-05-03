package fun.dr.ktulu.game.discord;

import fun.dr.ktulu.bot.messaging.BotMessenger;
import fun.dr.ktulu.game.Manitu;
import lombok.Getter;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;

public class CommunicableManitu extends Manitu implements CommunicableUser {
    @Getter
    private final String communicationChannelID;
    private final BotMessenger botMessenger;

    public CommunicableManitu(@NotNull String manituID,
                              @NotNull String communicationChannelID,
                              @NotNull BotMessenger botMessenger) {
        super(manituID);
        this.communicationChannelID = communicationChannelID;
        this.botMessenger = botMessenger;
    }

    @Override
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
    public String getCurrentName() {
        return botMessenger.getUserCurrentName(this);
    }
}
