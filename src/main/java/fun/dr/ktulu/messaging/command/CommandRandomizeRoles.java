package fun.dr.ktulu.messaging.command;

import fun.dr.ktulu.game.Player;
import fun.dr.ktulu.messaging.command.exception.ValidationException;
import net.dv8tion.jda.api.entities.Message;

import java.util.stream.Collectors;

public class CommandRandomizeRoles extends Command {
    public CommandRandomizeRoles(Message message) {
        super(message);
    }

    @Override
    protected void validate() throws ValidationException {
        validateIsIssuerManitu();
        validateIsManituChannel();
        validateIsSetupStage();
        int numPlayers = game.getPlayers().size();
        if (numPlayers == 0)
            throw new ValidationException("0 graczy to odrobinkę za mało do dobrej gry :/");
        int numRoles = game.getRoles().size();
        if (numRoles == 0)
            throw new ValidationException("A może jakieś karty do gry by się przydały? Z pustego i Salomon nie potasuje.");
        if (numPlayers != numRoles)
            throw new ValidationException("Graczy jest: " + numPlayers + ", a ról: " + numRoles + ". Chyba widzisz, w czym problem.");
    }

    @Override
    protected void execute() {
        game.assignRoles();
    }

    @Override
    protected void sendSuccessMessage() {
        for (Player player : game.getPlayers()) {
            player.getCommunicationChannel().sendMessage("Twoja rola to: " + player.getRole().getName()).queue();
        }
        sendResponseMessage("Oto role i ich gracze:\n" +
                game.getPlayers()
                        .stream()
                        .map(player -> player.getTempName() + " -- " + player.getRole().getName() + "\n")
                        .collect(Collectors.joining()));
    }
}
