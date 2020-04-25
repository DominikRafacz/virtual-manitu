package fun.dr.ktulu.messaging.command;

import fun.dr.ktulu.game.Player;
import fun.dr.ktulu.messaging.MessageManager;
import fun.dr.ktulu.messaging.command.exception.ExecutionException;
import fun.dr.ktulu.messaging.command.exception.ValidationException;
import net.dv8tion.jda.api.entities.Message;

public class CommandKill extends Command {
    private Player playerToKill;

    public CommandKill(Message message) {
        super(message);
    }

    @Override
    protected void validate() throws ValidationException {
        validateIsIssuerManitu();
        validateIsManituChannel();
        validateIsOngoingStage();
        args = MessageManager.extractArgs(message);
        if (args.size() == 0)
            throw new ValidationException("No, jakiegoś gracza to by się przydało podać, c'nie?");
        if (args.size() > 1)
            throw new ValidationException("Powoli! Zabijamy pojedynczo!");
        playerToKill = findPlayerByNameIfExists(args.get(0));
        if (playerToKill == null)
            throw new ValidationException("Error 404. Nie ma takiego gracza na serwerze!");
    }

    @Override
    protected void execute() throws ExecutionException {
        game.killPlayer(playerToKill);
    }

    @Override
    protected void sendSuccessMessage() {
        game.getGameChannel()
                .sendMessage("Ginie " + playerToKill.getAsMember().getAsMention() + "!")
                .queue(message -> sendResponseMessage("Zabiłeś/łaś/łoś " + args.get(0) + "."));
    }
}
