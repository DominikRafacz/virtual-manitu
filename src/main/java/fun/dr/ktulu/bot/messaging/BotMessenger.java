package fun.dr.ktulu.bot.messaging;

import fun.dr.ktulu.game.Faction;
import fun.dr.ktulu.game.Player;
import fun.dr.ktulu.game.discord.CommunicableUser;
import fun.dr.ktulu.game.discord.CommunicableManitu;
import fun.dr.ktulu.game.discord.CommunicablePlayer;
import fun.dr.ktulu.messaging.CommandMatcher;
import fun.dr.ktulu.messaging.command.Command;
import fun.dr.ktulu.messaging.command.exception.ExecutionException;
import fun.dr.ktulu.messaging.command.exception.UnknownCommandException;
import fun.dr.ktulu.messaging.command.exception.ValidationException;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.IMentionable;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

public class BotMessenger implements EventListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommandMatcher.class);
    @Setter
    private JDA jda;
    @Setter
    private String gameChannelID;
    @Setter
    @Getter
    private CommunicableManitu manitu;
    @Getter
    private Set<CommunicablePlayer> players;

    public BotMessenger() {
        this.players = new HashSet<>();
    }

    public void sendToManitu(@NotNull String message) {
        manitu.sendMessage(message);
    }

    public void sendToGameChannel(@NotNull String message) {
        Objects.requireNonNull(jda.getTextChannelById(gameChannelID)).sendMessage(message).queue();
    }

    public void sendToPlayer(@NotNull String message, @NotNull CommunicablePlayer player) {
        player.sendMessage(message);
    }

    public void sendToIssuer(@NotNull String message, @NotNull Command command) {
        command.getChannel().sendMessage(message).queue();
    }

    @Override
    public void onEvent(@Nonnull GenericEvent event) {
        if (event instanceof MessageReceivedEvent) {
            LOGGER.debug("Received message.");
            Message message = ((MessageReceivedEvent) event).getMessage();
            if (CommandMatcher.isCommand(message)) {
                LOGGER.debug("Detected command.");
                try {
                    CommandMatcher.matchCommand(message).issue();
                } catch (UnknownCommandException e) {
                    LOGGER.warn("Command not recognized!");
                    message.getChannel().sendMessage("Nie znam takiego polecenia!").queue();
                } catch (ExecutionException e) {
                    LOGGER.error("Execution exception raised!");
                    e.sendBotMessage(message.getChannel());
                } catch (ValidationException e) {
                    LOGGER.warn("Validation exception raised!");
                    e.sendBotMessage(message.getChannel());
                }
            }
        }
    }

    public String getMentionOnGameChannel(@NotNull CommunicableUser communicable) {
        return getOptionalMember(communicable).map(IMentionable::getAsMention).orElse("");
    }

    public String getUserCurrentName(@NotNull CommunicableUser communicable) {
        return getOptionalMember(communicable).map(Member::getNickname).orElse("");
    }

    public MessageChannel getUserCommunicationChannel(@NotNull CommunicableUser communicable) {
        return Objects.requireNonNull(jda.getTextChannelById(communicable.getCommunicationChannelID()));
    }

    private @NotNull Optional<Member> getOptionalMember(@NotNull CommunicableUser communicable) {
        return Objects.requireNonNull(jda
                .getTextChannelById(gameChannelID))
                .getMembers()
                .stream()
                .filter(member -> member.getUser().getId().equals(communicable.getID()))
                .findFirst();
    }

    public void sendPlayersStatusMessage() {
        sendToGameChannel("Aktualny status ról w grze:\n" +
                players.stream()
                .map(player -> player.getRole().getFaction())
                .distinct()
                .sorted(Comparator.comparingInt(Faction::getOrder))
                .map(faction -> "**" + faction.getName() + "**:\n" +
                        players.stream()
                                .filter(player -> player.getRole().getFaction() == faction)
                                .map(player -> player.isAlive() ?
                                        player.getRole().getName() :
                                        "~~" + player.getRole().getName() + "~~")
                                .collect(Collectors.joining(", ")))
                .collect(Collectors.joining("\n")));
    }

    public CommunicablePlayer findPlayerByNameIfExists(String name) {
        return players.stream()
                .filter(player -> player.getCurrentName().equals(name))
                .findFirst()
                .orElse(null);
    }
}
