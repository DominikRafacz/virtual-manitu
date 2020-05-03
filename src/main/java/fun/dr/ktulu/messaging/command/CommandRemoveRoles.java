package fun.dr.ktulu.messaging.command;

import fun.dr.ktulu.game.Role;
import fun.dr.ktulu.game.exception.GameException;
import fun.dr.ktulu.game.exception.RoleNotMatchedException;
import fun.dr.ktulu.messaging.CommandMatcher;
import fun.dr.ktulu.messaging.command.exception.ExecutionException;
import fun.dr.ktulu.messaging.command.exception.ValidationException;
import net.dv8tion.jda.api.entities.Message;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class CommandRemoveRoles extends Command {
    private Set<Role> rolesToRemove;

    public CommandRemoveRoles(Message message) {
        super(message);
    }

    @Override
    protected void validate() throws ValidationException {
        validateIsIssuerManitu();
        validateIsManituChannel();
        if (args.size() == 0) throw new ValidationException("Nic nie usuwać? Okay, to było łatwe.");
        if (!args.stream().allMatch(arg -> arg.length() > 0))
            throw new ValidationException("Proszę nie robić za dużo spacji. Zmuszałoby mnie to do traktowania " +
                    "pustego napisu jako nazwy roli. A tego byśmy nie chcieli. Więc nie usunąłem nikogo, sorki :/ ");
        rolesToRemove = new HashSet<>();
        for (String arg : args) {
            try {
                rolesToRemove.add(Role.matchRole(arg));
            } catch (RoleNotMatchedException e) {
                throw new ValidationException("Nie ma takiego numeru! Znaczy... takiej roli w bazie (przynajmniej na razie)");
            }
        }
    }

    @Override
    protected void execute() throws GameException {
        GAME.removeRoles(rolesToRemove);
        MESSENGER.sendToManitu("Usunąłem następujące role: " +
                rolesToRemove.stream().map(Role::getName).collect(Collectors.joining(", ")));
    }
}
