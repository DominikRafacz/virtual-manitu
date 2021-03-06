package fun.dr.ktulu.messaging.command;

import net.dv8tion.jda.api.entities.Message;

public class CommandPing extends Command {
    public CommandPing(Message message) {
        super(message);
    }

    @Override
    public void validate() {}

    @Override
    public void execute() {
        LOGGER.debug("Pinged to user {}", message.getAuthor().getName());
    }

    @Override
    protected void sendSuccessMessage() {
        sendResponseMessage("pong");
    }
}
