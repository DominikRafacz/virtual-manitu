package fun.dr.ktulu.messaging.command;

import fun.dr.ktulu.messaging.MessageManager;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public abstract class Command {
    protected final Message message;
    protected final MessageChannel channel;

    protected Command(@NotNull Message message) {
        this.message = message;
        this.channel = message.getChannel();
    }

    @Contract("_ -> new")
    public static @NotNull Command matchCommand(Message message) throws UnknownCommandException {
        String commandText = MessageManager.extractCommandText(message);
        switch (commandText) {
            case "ping": return new CommandPing(message);
            default: throw new UnknownCommandException(commandText);
        }
    }

    public abstract void execute();
}
