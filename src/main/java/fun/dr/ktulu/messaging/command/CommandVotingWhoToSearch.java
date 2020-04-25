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

public class CommandVotingWhoToSearch extends Command {
    private List<Player> searchCandidates;

    public CommandVotingWhoToSearch(Message message) {
        super(message);
    }

    @Override
    protected void validate() throws ValidationException {
        validateIsIssuerManitu();
        validateIsManituChannel();
        validateIsOngoingStage();
        validateNoVotingOn();
        args = MessageManager.extractArgs(message);
        if (args.size() != 3)
            throw new ValidationException("Propozycji powinno być dokładnie trzy. Więcej ani mniej nie obsługuję");
        searchCandidates = args.stream().map(this::findPlayerByNameIfExists).collect(Collectors.toList());
        if (searchCandidates.stream().anyMatch(Objects::isNull))
            throw new ValidationException("Nie mogę znaleźć co najmniej jednego spośród tych userów w bazie...");
        if (searchCandidates.stream().distinct().count() != 3)
            throw new ValidationException("Ej, nie można podać jednego człowieczka dwa razy, lol.");
    }

    @Override
    protected void execute() throws ExecutionException {
        game.startVotingWhoToSearch(searchCandidates);
    }

    @Override
    protected void sendSuccessMessage() {
        game.getGameChannel()
                .sendMessage("Rozpoczynamy głosowanie nad tym, kogo NIE przeszukać. Możliwe opcje:\n" +
                        ((VotingEvent) game.getSpecialEvent()).getOptions().stream()
                                .map(Object::toString).collect(Collectors.joining("\n")) +
                        "\nMożna zagłosować poprzez komendę 'm!głosuję_na X', gdzie X to jedna z możliwych opcji")
                .queue(message -> sendResponseMessage("Kości zostały rzucone. Niech lud wybierze!"));
    }
}
