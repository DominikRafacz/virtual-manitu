package fun.dr.ktulu.messaging.command;

import fun.dr.ktulu.game.event.VotingEvent;
import fun.dr.ktulu.messaging.command.exception.ValidationException;
import net.dv8tion.jda.api.entities.Message;

import java.util.stream.Collectors;

public class CommandVotingIfToHang extends Command {
    public CommandVotingIfToHang(Message message) {
        super(message);
    }

    @Override
    protected void validate() throws ValidationException {
        validateVoting();
    }

    @Override
    protected void execute() {
        game.startVotingIfToHang();
    }

    @Override
    protected void sendSuccessMessage() {
        game.getGameChannel()
                .sendMessage("Rozpoczynamy głosowanie nad tym, czy wieszamy. Możliwe opcje:\n" +
                        ((VotingEvent) game.getSpecialEvent()).getOptions().stream()
                                .map(Object::toString).collect(Collectors.joining("\n")) +
                        "\nMożna zagłosować poprzez komendę 'm!głosuję-na X', gdzie X to jedna z możliwych opcji")
                .queue(message -> sendResponseMessage("Kości zostały rzucone. Niech lud wybierze!"));

    }
}
