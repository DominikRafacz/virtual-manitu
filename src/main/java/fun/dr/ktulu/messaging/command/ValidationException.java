package fun.dr.ktulu.messaging.command;

import net.dv8tion.jda.api.entities.MessageChannel;

public class ValidationException extends Exception {
    private String botMessage;

    public ValidationException() {
        super();
    }

    public ValidationException(String botMessage) {
        super();
        this.botMessage = botMessage;
    }

    public void sendBotMessage(MessageChannel channel) {
        channel.sendMessage(botMessage).queue();
    }
}
