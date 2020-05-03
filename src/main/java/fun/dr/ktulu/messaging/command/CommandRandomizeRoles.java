package fun.dr.ktulu.messaging.command;

import fun.dr.ktulu.game.Ktulowiec;
import fun.dr.ktulu.game.discord.CommunicablePlayer;
import fun.dr.ktulu.game.exception.GameException;
import fun.dr.ktulu.messaging.command.exception.ValidationException;
import net.dv8tion.jda.api.entities.Message;

import java.util.Comparator;
import java.util.stream.Collectors;

public class CommandRandomizeRoles extends Command {
    public CommandRandomizeRoles(Message message) {
        super(message);
    }

    @Override
    protected void validate() throws ValidationException {
        validateIsIssuerManitu();
        validateIsManituChannel();
    }

    @Override
    protected void execute() throws GameException {
        GAME.assignRoles();
        for (CommunicablePlayer player : MESSENGER.getPlayers()) {
            player.sendMessage("Twoja rola to:" +
                    player.getRole().getRoleAndFactionName());
        }
        MESSENGER.sendToManitu("Oto gracze i ich role:" +
                MESSENGER.getPlayers().stream()
                .sorted(Comparator.comparing(Ktulowiec::getCurrentName))
                .map(player -> player.getRole().getRoleAndFactionName())
                .collect(Collectors.joining(",/n"))
        );
        MESSENGER.sendPlayersStatusMessage();
    }
}
