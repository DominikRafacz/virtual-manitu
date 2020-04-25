package fun.dr.ktulu.messaging.command;

import fun.dr.ktulu.game.Role;
import fun.dr.ktulu.game.exception.RoleNotMatchedException;
import fun.dr.ktulu.messaging.MessageManager;
import fun.dr.ktulu.messaging.command.exception.ExecutionException;
import fun.dr.ktulu.messaging.command.exception.ValidationException;
import net.dv8tion.jda.api.entities.Message;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class CommandAddRoles extends Command {
    private Set<Role> rolesToAdd;
    public CommandAddRoles(Message message) {
        super(message);
    }

    @Override
    protected void validate() throws ValidationException {
        validateIsIssuerManitu();
        validateIsSetupStage();
        validateIsManituChannel();
        args = MessageManager.extractArgs(message);
        if (args.size() == 0) throw new ValidationException("... można powtórzyć? Nie usłyszałem, co mam dodać");
        if (!args.stream().allMatch(arg -> arg.length() > 0))
            throw new ValidationException("Proszę nie robić za dużo spacji. Zmuszałoby mnie to do traktowania " +
                    "pustego napisu jako nazwy roli. A tego byśmy nie chcieli. Więc nie dodałem nikogo, sorki :/ ");
        rolesToAdd = new HashSet<>();
        for (String arg : args) {
            try {
                rolesToAdd.add(Role.matchRole(arg));
            } catch (RoleNotMatchedException e) {
                throw new ValidationException("Nie ma takiego numeru! Znaczy... takiej roli w bazie (przynajmniej na razie)");
            }
        }
    }

    @Override
    protected void execute() throws ExecutionException {
        game.addRoles(rolesToAdd);
    }

    @Override
    protected void sendSuccessMessage() {
        sendResponseMessage("Dodałem następujące role: " +
                rolesToAdd.stream().map(Role::getName).collect(Collectors.joining(", ")));
    }
}
