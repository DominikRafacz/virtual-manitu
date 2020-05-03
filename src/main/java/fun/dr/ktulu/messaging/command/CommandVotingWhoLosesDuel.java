package fun.dr.ktulu.messaging.command;

import fun.dr.ktulu.game.Player;
import fun.dr.ktulu.game.discord.CommunicablePlayer;
import fun.dr.ktulu.game.event.VotingEvent;
import fun.dr.ktulu.game.exception.GameException;
import fun.dr.ktulu.messaging.CommandMatcher;
import fun.dr.ktulu.messaging.command.exception.ExecutionException;
import fun.dr.ktulu.messaging.command.exception.ValidationException;
import net.dv8tion.jda.api.entities.Message;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CommandVotingWhoLosesDuel extends Command {

    private List<CommunicablePlayer> duelers;

    public CommandVotingWhoLosesDuel(Message message) {
        super(message);
    }

    @Override
    protected void validate() throws ValidationException {
        validateVoting();
        if (args.size() != 2)
            throw new ValidationException("Kandydatów powinno być dokładnie dwóch");
        duelers = args.stream().map(MESSENGER::findPlayerByNameIfExists).collect(Collectors.toList());
        if (duelers.stream().anyMatch(Objects::isNull))
            throw new ValidationException("Nie mogę znaleźć co najmniej jednego spośród tych userów w bazie...");
        if (duelers.stream().distinct().count() != 2)
            throw new ValidationException("Ej, nie można podać jednego człowieczka dwa razy, lol.");
    }

    @Override
    protected void execute() throws GameException {
        GAME.startVotingWhoLosesDuel(duelers.stream().map(player -> (Player) player).collect(Collectors.toList()));
        MESSENGER.sendToGameChannel("Rozpoczynamy głosowanie nad tym, kto zginie w pojedynku. Możliwe opcje:\n" +
                ((VotingEvent) GAME.getSpecialEvent()).getOptions().stream()
                        .map(Object::toString).collect(Collectors.joining("\n")) +
                "\nMożna zagłosować poprzez komendę 'm!głosuję-na X', gdzie X to jedna z możliwych opcji");
        MESSENGER.sendToManitu("Kości zostały rzucone. Niech lud wybierze!");
    }
}
