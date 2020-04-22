package fun.dr.ktulu.messaging.command;

import fun.dr.ktulu.messaging.MessageManager;
import fun.dr.ktulu.messaging.command.exception.ExecutionException;
import fun.dr.ktulu.messaging.command.exception.ValidationException;
import net.dv8tion.jda.api.entities.Message;

public class CommandRemoveRoles extends Command {
    public CommandRemoveRoles(Message message) {
        super(message);
    }

    @Override
    protected void validate() throws ValidationException {
        validateIsIssuerManitu();
        validateIsSetupStage();
        validateIsManituChannel();
        args = MessageManager.extractArgs(message);
        if (args.size() == 0) throw new ValidationException("Nic nie usuwać? Okay, to było łatwe.");
        if (!args.stream().allMatch(arg -> arg.length() > 0))
            throw new ValidationException("Proszę nie robić za dużo spacji. Zmuszałoby mnie to do traktowania " +
                    "pustego napisu jako nazwy roli. A tego byśmy nie chcieli. Więc nie usunąłem nikogo, sorki :/ ");
    }

    @Override
    protected void execute() throws ExecutionException {
        game.removeRoles(args);
    }

    @Override
    protected void sendSuccessMessage() {
        sendResponseMessage("Usunąłem następujące role: " + String.join(", ", args));
    }
}
