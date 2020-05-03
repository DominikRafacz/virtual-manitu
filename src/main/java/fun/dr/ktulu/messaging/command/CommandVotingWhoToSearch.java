package fun.dr.ktulu.messaging.command;

import fun.dr.ktulu.game.Player;
import fun.dr.ktulu.game.discord.CommunicablePlayer;
import fun.dr.ktulu.game.event.VotingEvent;
import fun.dr.ktulu.game.exception.GameException;
import fun.dr.ktulu.messaging.command.exception.ValidationException;
import net.dv8tion.jda.api.entities.Message;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CommandVotingWhoToSearch extends Command {
    private List<CommunicablePlayer> searchCandidates;

    public CommandVotingWhoToSearch(Message message) {
        super(message);
    }

    @Override
    protected void validate() throws ValidationException {
        validateVoting();
        if (args.size() != 3)
            throw new ValidationException("Propozycji powinno być dokładnie trzy. Więcej ani mniej nie obsługuję");
        searchCandidates = args.stream().map(MESSENGER::findPlayerByNameIfExists).collect(Collectors.toList());
        if (searchCandidates.stream().anyMatch(Objects::isNull))
            throw new ValidationException("Nie mogę znaleźć co najmniej jednego spośród tych userów w bazie...");
        if (searchCandidates.stream().distinct().count() != 3)
            throw new ValidationException("Ej, nie można podać jednego człowieczka dwa razy, lol.");
    }

    @Override
    protected void execute() throws GameException {
        GAME.startVotingWhoToSearch(searchCandidates.stream().map(player -> (Player) player).collect(Collectors.toList()));
        MESSENGER.sendToGameChannel("Rozpoczynamy głosowanie nad tym, kogo NIE przeszukać. Możliwe opcje:\n" +
                ((VotingEvent) GAME.getSpecialEvent()).getOptions().stream()
                        .map(Object::toString).collect(Collectors.joining("\n")) +
                "\nMożna zagłosować poprzez komendę 'm!głosuję-na X', gdzie X to jedna z możliwych opcji");
        MESSENGER.sendToManitu("Kości zostały rzucone. Niech lud wybierze!");
    }
}
