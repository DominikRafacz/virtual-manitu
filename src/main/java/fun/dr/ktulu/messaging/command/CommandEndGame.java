package fun.dr.ktulu.messaging.command;

import fun.dr.ktulu.messaging.command.exception.ValidationException;
import net.dv8tion.jda.api.entities.Message;

public class CommandEndGame extends Command {
    public CommandEndGame(Message message) {
        super(message);
    }

    @Override
    protected void validate() throws ValidationException {
        validateIsIssuerManitu();
        validateIsManituChannel();
    }

    @Override
    protected void execute() {
        GAME.reset();
        MESSENGER.sendToManitu("No to paaa!");
        MESSENGER.sendToGameChannel("Dzięki za grę. Do następnego!");
    }
}
