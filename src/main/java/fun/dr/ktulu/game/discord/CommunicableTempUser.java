package fun.dr.ktulu.game.discord;

import net.dv8tion.jda.api.entities.MessageChannel;
import org.jetbrains.annotations.NotNull;

public class CommunicableTempUser implements CommunicableUser {
    private final MessageChannel communicationChannel;
    private final String ID;

    public CommunicableTempUser(String ID, MessageChannel channel) {
        this.communicationChannel = channel;
        this.ID = ID;
    }

    @Override
    public void sendMessage(@NotNull String message) {
        communicationChannel.sendMessage(message).queue();
    }

    @Override
    public MessageChannel getCommunicationChannel() {
        return communicationChannel;
    }

    @Override
    public String getCommunicationChannelID() {
        return communicationChannel.getId();
    }

    @Override
    public String getID() {
        return ID;
    }
}
