package fun.dr.ktulu.messaging.command;

import fun.dr.ktulu.game.discord.CommunicablePlayer;
import fun.dr.ktulu.game.exception.GameException;
import fun.dr.ktulu.messaging.command.exception.ValidationException;
import net.dv8tion.jda.api.entities.Message;

public class CommandKill extends Command {
    private CommunicablePlayer playerToKill;

    public CommandKill(Message message) {
        super(message);
    }

    @Override
    protected void validate() throws ValidationException {
        validateIsManituChannel();
        validateIsOngoingStage();
        if (args.size() == 0)
            throw new ValidationException("No, jakiegoś gracza to by się przydało podać, c'nie?");
        if (args.size() > 1)
            throw new ValidationException("Powoli! Zabijamy pojedynczo!");
        playerToKill = MESSENGER.findPlayerByNameIfExists(args.get(0));
        if (playerToKill == null)
            throw new ValidationException("Error 404. Nie ma takiego usera o takim nicku na serwerze!");
    }

    @Override
    protected void execute() throws GameException {
        GAME.killPlayer(playerToKill);
        MESSENGER.sendToGameChannel("Ginie " + MESSENGER.getMentionOnGameChannel(playerToKill) +"!");
        MESSENGER.sendToManitu("Zabiłeś/łaś/łoś " + args.get(0) + ".");
    }
}
