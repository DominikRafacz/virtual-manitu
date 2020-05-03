package fun.dr.ktulu.messaging.command;

import fun.dr.ktulu.bot.AppManager;
import fun.dr.ktulu.bot.messaging.BotMessenger;
import fun.dr.ktulu.game.Game;
import fun.dr.ktulu.game.Manitu;
import fun.dr.ktulu.game.Player;
import fun.dr.ktulu.game.discord.CommunicableUser;
import fun.dr.ktulu.game.discord.CommunicablePlayer;
import fun.dr.ktulu.game.discord.CommunicableTempUser;
import fun.dr.ktulu.game.exception.GameException;
import fun.dr.ktulu.messaging.CommandMatcher;
import fun.dr.ktulu.messaging.command.exception.ExecutionException;
import fun.dr.ktulu.messaging.command.exception.ValidationException;
import lombok.Getter;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public abstract class Command {
    protected static final Logger LOGGER = LoggerFactory.getLogger(Command.class);
    protected static final Game GAME = AppManager.getInstance().getGame();
    protected static final BotMessenger MESSENGER = AppManager.getInstance().getMessenger();

    protected final Message message;
    protected final CommunicableUser issuer;
    @Getter
    protected final MessageChannel channel;
    protected final List<String> args;

    protected Command(@NotNull Message message) {
        this.message = message;
        this.channel = message.getChannel();
        this.issuer = matchIssuer();
        this.args = extractArgs();
    }

    private CommunicableUser matchIssuer() {
        String issuerID = message.getAuthor().getId();
        if (MESSENGER.getManitu() != null && MESSENGER.getManitu().getID().equals(issuerID))
            return MESSENGER.getManitu();
        Optional<CommunicablePlayer> optPlayer = MESSENGER.getPlayers().stream()
                .filter(player -> player.getID().equals(issuerID))
                .findFirst();
        if (optPlayer.isPresent())
            return optPlayer.get();
        else return new CommunicableTempUser(message.getAuthor().getId(), message.getChannel());
    }

    @Contract(" -> new")
    private @NotNull List<String> extractArgs() {
        String content = message.getContentRaw();
        int spaceIndex = content.indexOf(" ");
        if (spaceIndex == -1)
            return new ArrayList<>(0);
        return new ArrayList<>(
                Arrays.asList(
                        content.substring(spaceIndex + 1)
                                .split(CommandMatcher.ARGS_SEPARATOR)));
    }

    public void issue() throws ValidationException, ExecutionException {
        validate();
        LOGGER.debug("Command {} validated.", this.getClass().toString());
        try {
            execute();
        } catch (GameException e) {
            throw new ExecutionException(e.getMessage());
        }
        LOGGER.info("Command {} executed.", this.getClass().toString());
    }

    protected abstract void validate() throws ValidationException;

    protected abstract void execute() throws ExecutionException, GameException;

    protected void validateIsIssuerManitu() throws ValidationException {
        if (issuer instanceof Manitu)
            throw new ValidationException("Nie możesz tego wywołać! Musisz być Manitu!");
    }

    protected void validateIsOngoingStage() throws ValidationException {
        if (GAME.getStage() != Game.GameStage.ONGOING)
            throw new ValidationException("Nie zabijamy poza grą. Szanujmy się.");
    }

    protected void validateIsManituChannel() throws ValidationException {
        if (!message.getChannel().getId().equals(MESSENGER.getManitu().getCommunicationChannel().getId()))
            throw new ValidationException("Ekhm. To nie miejsce na to. Polecam iść na kanał Manitu.");
    }

    protected void validateIsPrivateChannel() throws ValidationException {
        if (message.getChannel().getType() != ChannelType.PRIVATE)
            throw new ValidationException("Ta komenda powinna być użyta w prywatnej wiadomosci. Ostentacyjnie ją zignoruję");
    }

    protected void validateNoVotingOn() throws ValidationException {
        if (GAME.isVotingOn())
            throw new ValidationException("Już trwa jedno głosowanko!");
    }

    protected void validateVotingOn() throws ValidationException {
        if (!GAME.isVotingOn())
            throw new ValidationException("Bez głosowania, nie ma oddawania głosu. Bez oddania głosu... nie ma oddania głosu.");
    }

    protected void validateIsPlayer() throws ValidationException {
        if (!(issuer instanceof Player))
            throw new ValidationException("Nie jesteś graczem, nie możesz głosować ://");
    }

    protected void validateVoting() throws ValidationException {
        validateIsIssuerManitu();
        validateIsManituChannel();
        validateIsOngoingStage();
        validateNoVotingOn();
    }

}
