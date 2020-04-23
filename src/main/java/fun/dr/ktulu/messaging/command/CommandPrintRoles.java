package fun.dr.ktulu.messaging.command;

import fun.dr.ktulu.game.Game;
import fun.dr.ktulu.messaging.command.exception.ValidationException;
import net.dv8tion.jda.api.entities.Message;

import java.util.List;

public class CommandPrintRoles extends Command {
    private List<String> roles;

    public CommandPrintRoles(Message message) {
        super(message);
    }

    @Override
    protected void validate() throws ValidationException {
        if (game.getGameStage() == Game.GameStage.NOT_INITIATED)
            throw new ValidationException("Gra jeszcze nie zainicjowana, powoli!");
    }

    @Override
    protected void execute() {
        roles = game.getRoles();
    }

    @Override
    protected void sendSuccessMessage() {
        sendResponseMessage("W tej grze mamy następujące role: " + String.join(", ", roles));
    }
}
