package fun.dr.ktulu.game.discord;

import net.dv8tion.jda.api.entities.MessageChannel;
import org.jetbrains.annotations.NotNull;

public interface CommunicableUser {
    void sendMessage(@NotNull String message);

    MessageChannel getCommunicationChannel();

    String getCommunicationChannelID();

    String getID();
}
