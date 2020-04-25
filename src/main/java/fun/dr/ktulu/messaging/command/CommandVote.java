package fun.dr.ktulu.messaging.command;

import fun.dr.ktulu.game.Ktulowiec;
import fun.dr.ktulu.game.Player;
import fun.dr.ktulu.game.event.VotingEvent;
import fun.dr.ktulu.game.event.VotingOption;
import fun.dr.ktulu.messaging.MessageManager;
import fun.dr.ktulu.messaging.command.exception.ExecutionException;
import fun.dr.ktulu.messaging.command.exception.ValidationException;
import net.dv8tion.jda.api.entities.Message;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class CommandVote extends Command {
    private Player voter;
    private VotingOption votingOption;

    public CommandVote(Message message) {
        super(message);
    }

    @Override
    protected void validate() throws ValidationException {
        validateVotingOn();
        Set<Player> voters = ((VotingEvent) game.getSpecialEvent()).getVoters();
        Optional<Player> optVoter = voters.stream()
                .filter(player -> player.getUserID().equals(message.getAuthor().getId()))
                .findFirst();
        if (optVoter.isEmpty())
            throw new ValidationException("Nie jesteś uprawniony do głosowania!");
        voter = optVoter.get();
        if (((VotingEvent) game.getSpecialEvent()).getVotes().get(voter) != null)
            throw new ValidationException("Już oddałeś/łaś/łoś głos!");
        args = MessageManager.extractArgs(message);
        if (args.size() != 1)
            throw new ValidationException("Powinnieneś wybrać dokładnie jedną opcję, ni mniej, ni więcej.");
        Optional<VotingOption> potentialOption = ((VotingEvent) game.getSpecialEvent()).getOptions().stream()
                .filter(option -> option.toString().equals(args.get(0)))
                .findFirst();
        if (potentialOption.isEmpty())
            throw new ValidationException("To nie jest opcja wchodząca w grę. Powinieneś/nenaś/nenoś wybrać jedno z:\n" +
                    ((VotingEvent) game.getSpecialEvent()).getOptions().stream()
                            .map(VotingOption::toString)
                            .collect(Collectors.joining("\n")));
        votingOption = potentialOption.get();
    }

    @Override
    protected void execute() {
        game.vote(voter, votingOption);
    }

    @Override
    protected void sendSuccessMessage() {
        VotingEvent voting = ((VotingEvent) game.getSpecialEvent());
        message.getChannel().sendMessage("taaajest!")
                .queue(message -> game.getManituChannel().sendMessage("Kolejny zagłosował!")
                        .queue(messageB -> {
                            if (voting.everyoneVoted())
                                game.getGameChannel().sendMessage("Mamy wyniki głosowania!\n" +
                                        voting.getResultsFormatted()).queue();
                        }));

    }
}
