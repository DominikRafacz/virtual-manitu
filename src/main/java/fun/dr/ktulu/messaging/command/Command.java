package fun.dr.ktulu.messaging.command;

import fun.dr.ktulu.game.Game;
import fun.dr.ktulu.messaging.MessageManager;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Command {
    protected final Message message;
    protected final MessageChannel channel;
    protected final Game game;
    protected static final Logger LOGGER = LoggerFactory.getLogger(Command.class);

    protected Command(@NotNull Message message) {
        this.message = message;
        this.channel = message.getChannel();
        this.game = Game.getInstance();
    }

    @Contract("_ -> new")
    public static @NotNull Command matchCommand(Message message) throws UnknownCommandException {
        String commandText = MessageManager.extractCommandText(message);
        switch (commandText) {
            case "ping": return new CommandPing(message);
            case "nowa_gra": return new CommandNewGame(message);
            default: throw new UnknownCommandException(commandText);
        }
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
}
