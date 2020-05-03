package fun.dr.ktulu.messaging.command;

import fun.dr.ktulu.game.discord.CommunicableManitu;
import fun.dr.ktulu.game.exception.GameException;
import fun.dr.ktulu.messaging.command.exception.ValidationException;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;

public class CommandNewGame extends Command {
    public CommandNewGame(Message message) {
        super(message);
    }

    @Override
    protected void validate() throws ValidationException {
        if (message.getChannel().getType() != ChannelType.TEXT) throw new ValidationException("yhm... sugerowałbym użycie tego na prywatnym tekstowym kanale na serwerze...");
        if (message.getMentionedChannels().size() != 1) throw new ValidationException("Powinieneś/nienaś wspomnieć dokładnie jeden kanał w tej wiadomości, skoro piszesz do mnie prywatnie!");
    }

    @Override
    public void execute() throws GameException {
        CommunicableManitu manitu = new CommunicableManitu(issuer.getID(), issuer.getCommunicationChannelID(), MESSENGER);
        GAME.initiate(manitu);
        MESSENGER.setManitu(manitu);
        MESSENGER.setGameChannelID(message.getMentionedChannels().get(0).getId());
        MESSENGER.sendToManitu("Zaczynamy przygotowania do gry! Wybierz role czy cośtam, ludzie niech się zgłaszają.");
        MESSENGER.sendToGameChannel("Nowa gra! Tutaj będzie kanał gry, " +
                MESSENGER.getMentionOnGameChannel(manitu));
    }
}
