package fun.dr.ktulu.messaging.command;

import fun.dr.ktulu.game.Player;
import fun.dr.ktulu.game.event.VotingEvent;
import fun.dr.ktulu.messaging.MessageManager;
import fun.dr.ktulu.messaging.command.exception.ExecutionException;
import fun.dr.ktulu.messaging.command.exception.ValidationException;
import net.dv8tion.jda.api.entities.Message;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CommandVotingWhoToHang extends Command {
    private List<Player> hangCandidates;

    public CommandVotingWhoToHang(Message message) {
        super(message);
    }

    @Override
    protected void validate() throws ValidationException {
        validateVoting();
        args = MessageManager.extractArgs(message);
        if (args.size() != 2)
            throw new ValidationException("Propozycje powinny być dokładnie dwie. Więcej ani mniej nie obsługuję");
        hangCandidates = args.stream().map(this::findPlayerByNameIfExists).collect(Collectors.toList());
        if (hangCandidates.stream().anyMatch(Objects::isNull))
            throw new ValidationException("Nie mogę znaleźć co najmniej jednego spośród tych userów w bazie...");
        if (hangCandidates.stream().distinct().count() != 2)
            throw new ValidationException("Ej, nie można podać jednego człowieczka dwa razy, lol.");
    }

    @Override
    protected void execute() throws ExecutionException {
        game.startVotingWhoToHang(hangCandidates);
    }

    @Override
    protected void sendSuccessMessage() {
        game.getGameChannel()
                .sendMessage("Rozpoczynamy głosowanie nad tym, kogo wieszamy (wieszanko!). Możliwe opcje:\n" +
                        ((VotingEvent) game.getSpecialEvent()).getOptions().stream()
                                .map(Object::toString).collect(Collectors.joining("\n")) +
                        "\nMożna zagłosować poprzez komendę 'm!głosuję-na X', gdzie X to jedna z możliwych opcji")
                .queue(message -> sendResponseMessage("Kości zostały rzucone. Niech lud wybierze!"));
    }
}
