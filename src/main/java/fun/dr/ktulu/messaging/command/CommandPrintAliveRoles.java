package fun.dr.ktulu.messaging.command;

import fun.dr.ktulu.messaging.command.exception.ValidationException;
import net.dv8tion.jda.api.entities.Message;

public class CommandPrintAliveRoles extends Command {
    public CommandPrintAliveRoles(Message message) {
        super(message);
    }

    @Override
    protected void validate() throws ValidationException {
        validateIsManituChannel();
        validateIsOngoingStage();
    }

    @Override
    protected void execute() {
        MESSENGER.sendPlayersStatusMessage();
    }
}
