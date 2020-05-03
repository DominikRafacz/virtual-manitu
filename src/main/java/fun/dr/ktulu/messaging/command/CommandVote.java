package fun.dr.ktulu.messaging.command;

import fun.dr.ktulu.game.Player;
import fun.dr.ktulu.game.event.VotingEvent;
import fun.dr.ktulu.game.event.VotingOption;
import fun.dr.ktulu.game.exception.GameException;
import fun.dr.ktulu.messaging.CommandMatcher;
import fun.dr.ktulu.messaging.command.exception.ValidationException;
import net.dv8tion.jda.api.entities.Message;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class CommandVote extends Command {
    private VotingOption votingOption;

    public CommandVote(Message message) {
        super(message);
    }

    @Override
    protected void validate() throws ValidationException {
        validateVotingOn();
        validateIsPlayer();
        if (args.size() != 1)
            throw new ValidationException("Powinieneś wybrać dokładnie jedną opcję, ni mniej, ni więcej.");
        Optional<VotingOption> potentialOption = ((VotingEvent) GAME.getSpecialEvent()).getOptions().stream()
                .filter(option -> option.toString().equals(args.get(0)))
                .findFirst();
        if (potentialOption.isEmpty())
            throw new ValidationException("To nie jest opcja wchodząca w grę. Powinieneś/nenaś/nenoś wybrać jedno z:\n" +
                    ((VotingEvent) GAME.getSpecialEvent()).getOptions().stream()
                            .map(VotingOption::toString)
                            .collect(Collectors.joining("\n")));
        votingOption = potentialOption.get();
    }

    @Override
    protected void execute() throws GameException {
        GAME.vote((Player) issuer, votingOption);
        MESSENGER.sendToIssuer("taaajest!", this);

        VotingEvent voting = ((VotingEvent) GAME.getSpecialEvent());
        if (voting.everyoneVoted())
            MESSENGER.sendToGameChannel("Mamy wyniki głosowania!\n\n" + voting.getResultsFormatted());

    }
}
