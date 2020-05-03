package fun.dr.ktulu.messaging.command;

import fun.dr.ktulu.game.Role;
import fun.dr.ktulu.game.exception.GameException;
import fun.dr.ktulu.messaging.CommandMatcher;
import fun.dr.ktulu.messaging.command.exception.ExecutionException;
import fun.dr.ktulu.messaging.command.exception.ValidationException;
import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class CommandAddDefaultRoles extends Command {
    private Set<Role> rolesToAdd;

    public CommandAddDefaultRoles(@NotNull Message message) { super(message); }

    @Override
    protected void validate() throws ValidationException {
        validateIsIssuerManitu();
        validateIsManituChannel();
        if (args.size() == 0) throw new ValidationException("...że ile, proszę?");
        if (args.size() > 1) throw new ValidationException("Czekaj, czekaj, jedną liczbę naraz, dobra?");
        Integer numRoles = Integer.getInteger(args.get(0).trim());
        rolesToAdd = Role.getDefaultRoles(numRoles);
    }

    @Override
    protected void execute() throws GameException {
        GAME.addRoles(rolesToAdd);
        MESSENGER.sendToManitu("Dodałem " + rolesToAdd.size() + "domyślnych ról.");
    }
}
