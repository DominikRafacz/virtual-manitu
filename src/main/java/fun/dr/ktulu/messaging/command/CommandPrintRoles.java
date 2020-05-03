package fun.dr.ktulu.messaging.command;

import fun.dr.ktulu.game.Role;
import fun.dr.ktulu.game.exception.GameException;
import net.dv8tion.jda.api.entities.Message;

import java.util.List;
import java.util.stream.Collectors;

public class CommandPrintRoles extends Command {
    private List<Role> roles;

    public CommandPrintRoles(Message message) {
        super(message);
    }

    @Override
    protected void validate() {
    }

    @Override
    protected void execute() throws GameException {
        roles = GAME.getRoles();
        MESSENGER.sendToIssuer( "W tej grze mamy następujące role: " +
                roles.stream()
                        .map(role -> role.getName() + " (" + role.getFaction().getName() + ")")
                        .collect(Collectors.joining(", ")),
                this);
    }
}
