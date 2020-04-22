package fun.dr.ktulu.messaging.command;

import fun.dr.ktulu.game.Game;
import fun.dr.ktulu.messaging.command.exception.ValidationException;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;

public class CommandNewGame extends Command {
    public CommandNewGame(Message message) {
        super(message);
    }

    @Override
    protected void validate() throws ValidationException {
        if (game.getGameStage() != Game.GameStage.NOT_INITIATED) throw new ValidationException("Halo, gra już jest zainicjowana!");
        if (message.getChannel().getType() != ChannelType.TEXT) throw new ValidationException("yhm... sugerowałbym użycie tego na prywatnym tekstowym kanale na serwerze...");
        if (message.getMentionedChannels().size() != 1) throw new ValidationException("Powinieneś/nienaś wspomnieć dokładnie jeden kanał w tej wiadomości, skoro piszesz do mnie prywatnie!");
    }

    @Override
    public void execute() {
        game.initiate(
                message.getGuild().getId(),
                message.getAuthor().getId(),
                message.getChannel().getId(),
                message.getMentionedChannels().get(0).getId()
        );
    }

    @Override
    protected void sendSuccessMessage() {
        game.getGameChannel()
                .sendMessage("Nowa gra! Tutaj będzie kanał gry, " + game.getManitu().getAsMember().getAsMention() + " będzie manitować!")
                .queue(s -> message.getChannel().sendMessage("Rozpoczynamy przygotowania do gry!").queue());
    }


}
