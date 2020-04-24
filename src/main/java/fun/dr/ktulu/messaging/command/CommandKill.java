package fun.dr.ktulu.messaging.command;

import fun.dr.ktulu.bot.AppManager;
import fun.dr.ktulu.messaging.MessageManager;
import fun.dr.ktulu.messaging.command.exception.ExecutionException;
import fun.dr.ktulu.messaging.command.exception.ValidationException;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

import java.util.Objects;
import java.util.Optional;

public class CommandKill extends Command {
    private String userId;
    private String name;

    public CommandKill(Message message) {
        super(message);
    }

    @Override
    protected void validate() throws ValidationException {
        validateIsIssuerManitu();
        validateIsManituChannel();
        validateIsOngoingStage();
        name = MessageManager.extractAllButCommandText(message);
        if (name.length() == 0)
            throw new ValidationException("No, jakiegoś gracza to by się przydało podać, c'nie?");
        Optional<Member> memberToKill = game.getGameChannel()
                .getMembers()
                .stream()
                .filter(member -> name.equals(member.getNickname()) || name.equals(member.getUser().getName()))
                .findFirst();
        if (memberToKill.isEmpty())
            throw new ValidationException("Nie ma takiego gracza na kanale!");
        userId = memberToKill.get().getUser().getId();
    }

    @Override
    protected void execute() throws ExecutionException {
        game.killPlayer(userId);

    }

    @Override
    protected void sendSuccessMessage() {
        User user = Objects.requireNonNull(AppManager.getInstance().getJda().getUserById(userId));
        game.getGameChannel()
                .sendMessage("Ginie " + user.getAsMention() + "!")
                .queue(message -> sendResponseMessage("Zabiłeś/łaś/łoś " + name + "."));
    }
}
