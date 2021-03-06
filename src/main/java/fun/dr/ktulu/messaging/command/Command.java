package fun.dr.ktulu.messaging.command;

import fun.dr.ktulu.game.Game;
import fun.dr.ktulu.game.Player;
import fun.dr.ktulu.messaging.command.exception.ExecutionException;
import fun.dr.ktulu.messaging.command.exception.ValidationException;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public abstract class Command {
    protected final Message message;
    protected final MessageChannel channel;
    protected final Game game;
    protected static final Logger LOGGER = LoggerFactory.getLogger(Command.class);
    protected List<String> args;

    protected Command(@NotNull Message message) {
        this.message = message;
        this.channel = message.getChannel();
        this.game = Game.getInstance();
    }

    public void issue() throws ValidationException, ExecutionException {
        validate();
        LOGGER.debug("Command {} validated.", this.getClass().toString());
        execute();
        LOGGER.info("Command {} executed.", this.getClass().toString());
        sendSuccessMessage();
    }

    protected abstract void validate() throws ValidationException;

    protected abstract void execute() throws ExecutionException;

    protected abstract void sendSuccessMessage();

    protected void sendResponseMessage(String messageText) {
        message.getChannel().sendMessage(messageText).queue();
    }

    protected void validateIsIssuerManitu() throws ValidationException {
        if (game.getManitu() == null || !message.getAuthor().getId().equals(game.getManitu().getUserID()))
            throw new ValidationException("Nie możesz tego wywołać! Musisz być Manitu!");
    }

    protected void validateIsSetupStage() throws ValidationException {
        if (game.getGameStage() != Game.GameStage.SETUP)
            throw new ValidationException("Żeby użyć tej komendy, gra powinna być w fazie przygotowania. " +
                    "A nie jest. No, inaczej bym tego nie pisał.");
    }

    protected void validateIsOngoingStage() throws ValidationException {
        if (game.getGameStage() != Game.GameStage.ONGOING)
            throw new ValidationException("Nie zabijamy poza grą. Szanujmy się.");
    }

    protected void validateIsManituChannel() throws ValidationException {
        if (!message.getChannel().getId().equals(game.getManituChannelID()))
            throw new ValidationException("Ekhm. To nie miejsce na to. Polecam iść na kanał Manitu: " +
                    game.getManituChannel().getAsMention());
    }

    protected void validateIsPrivateChannel() throws ValidationException {
        if (message.getChannel().getType() != ChannelType.PRIVATE)
            throw new ValidationException("Ta komenda powinna być użyta w prywatnej wiadomosci. Ostentacyjnie ją zignoruję");
    }

    protected void validateNoVotingOn() throws ValidationException {
        if (game.isVotingOn())
            throw new ValidationException("Już trwa jedno głosowanko!");
    }

    protected void validateVotingOn() throws ValidationException {
        if (!game.isVotingOn())
            throw new ValidationException("Bez głosowania, nie ma oddawania głosu. Bez oddania głosu... nie ma oddania głosu.");
    }

    protected Player findPlayerByNameIfExists(String name) {
        Optional<Player> potentialPlayer = game.getPlayers().stream()
                .filter(player -> player.getTempName().equals(name))
                .findFirst();
        if (potentialPlayer.isEmpty()) return null;
        return potentialPlayer.get();
    }

    protected void validateVoting() throws ValidationException {
        validateIsIssuerManitu();
        validateIsManituChannel();
        validateIsOngoingStage();
        validateNoVotingOn();
    }
}
