package fun.dr.ktulu.messaging.command;

import fun.dr.ktulu.game.discord.CommunicablePlayer;
import fun.dr.ktulu.game.exception.GameException;
import fun.dr.ktulu.messaging.command.exception.ValidationException;
import net.dv8tion.jda.api.entities.Message;

public class CommandWantToPlay extends Command {
    public CommandWantToPlay(Message message) {
        super(message);
    }

    @Override
    protected void validate() throws ValidationException {
        validateIsPrivateChannel();
    }

    @Override
    protected void execute() throws GameException {
        CommunicablePlayer player = new CommunicablePlayer(issuer.getID(), channel.getId(), MESSENGER);
        GAME.addPlayer(player);
        MESSENGER.getPlayers().add(player);
        MESSENGER.sendToIssuer("Jasne! Zrozumiałem. Dodano do listy graczy!", this);
        MESSENGER.sendToManitu(MESSENGER.getUserCurrentName(issuer) + " dołączył do gry!");
    }
}
