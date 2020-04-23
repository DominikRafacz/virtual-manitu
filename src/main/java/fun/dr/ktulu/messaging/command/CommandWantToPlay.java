package fun.dr.ktulu.messaging.command;

import fun.dr.ktulu.game.Ktulowiec;
import fun.dr.ktulu.messaging.command.exception.ValidationException;
import net.dv8tion.jda.api.entities.Message;

public class CommandWantToPlay extends Command {
    private String userID;
    public CommandWantToPlay(Message message) {
        super(message);
    }

    @Override
    protected void validate() throws ValidationException {
        validateIsPrivateChannel();
        validateIsSetupStage();
        userID = message.getAuthor().getId();
        if (userID.equals(game.getManitu().getUserID()))
            throw new ValidationException("Nope. Już grasz. Jako Manitu. Doceń to!");
        if (game.getPlayers().stream().map(Ktulowiec::getUserID).anyMatch(id -> id.equals(userID)))
            throw new ValidationException("Wiem. Już Cię dodawałem. Nie musisz powtarzać ;} ");
    }

    @Override
    protected void execute() {
        game.addPlayer(userID, message.getChannel().getId());
    }

    @Override
    protected void sendSuccessMessage() {
        sendResponseMessage("Jasne! Zrozumiałem. Dodano do listy graczy!");
    }
}
