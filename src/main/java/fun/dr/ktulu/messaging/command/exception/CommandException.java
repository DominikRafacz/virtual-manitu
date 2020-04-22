package fun.dr.ktulu.messaging.command.exception;

import net.dv8tion.jda.api.entities.MessageChannel;

public class CommandException extends Exception {
    private String botMessage;

    public CommandException() {
        super();
    }

    public CommandException(String botMessage) {
        super();
        this.botMessage = botMessage;
    }

    public void sendBotMessage(MessageChannel channel) {
        channel.sendMessage(botMessage).queue();
    }
}
