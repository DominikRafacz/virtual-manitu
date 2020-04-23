package fun.dr.ktulu.bot.listeners;

import fun.dr.ktulu.messaging.MessageManager;
import fun.dr.ktulu.messaging.command.Command;
import fun.dr.ktulu.messaging.command.exception.ExecutionException;
import fun.dr.ktulu.messaging.command.exception.UnknownCommandException;
import fun.dr.ktulu.messaging.command.exception.ValidationException;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

public class MessageListener implements EventListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageManager.class);

    @Override
    public void onEvent(@Nonnull GenericEvent event) {
        if (event instanceof MessageReceivedEvent) {
            LOGGER.debug("Received message.");
            Message message = ((MessageReceivedEvent) event).getMessage();
            if (MessageManager.isCommand(message)) {
                LOGGER.debug("Detected command.");
                try {
                    MessageManager.matchCommand(message).issue();
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
}
