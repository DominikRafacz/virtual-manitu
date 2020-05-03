package fun.dr.ktulu.game;

import fun.dr.ktulu.game.event.*;
import fun.dr.ktulu.game.exception.GameException;
import fun.dr.ktulu.messaging.command.exception.ValidationException;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class Game {
    private static final Logger LOGGER = LoggerFactory.getLogger(Game.class);

    @Getter
    private final List<Player> players;

    @Getter
    private Manitu manitu;

    private final List<Role> roles;

    @Getter
    private GameStage stage;

    @Getter
    private SpecialEvent specialEvent;

    public Game() {
        LOGGER.info("Game instantiated.");
        players = new ArrayList<>();
        roles = new ArrayList<>();
        stage = GameStage.NOT_INITIATED;
    }

    public void initiate(@NotNull Manitu manitu) throws GameException {
        if (stage != GameStage.NOT_INITIATED) throw new GameException("Halo, gra już jest zainicjowana!");
        stage = GameStage.SETUP;
        this.manitu = manitu;
        LOGGER.info("Game initiated! Manitu set up!");
    }

    public void addRoles(@NotNull Collection<Role> rolesToAdd) throws GameException {
       validateIsSetupStage();
        List<Role> duplicates = roles.stream()
                .filter(rolesToAdd::contains)
                .collect(Collectors.toList());
        if (duplicates.size() == 0) {
            roles.addAll(rolesToAdd);
            LOGGER.info("Added some roles.");
        } else throw new GameException("Sorrrry, ktoś już dodał te role: " +
                duplicates.stream()
                        .map(Role::getName)
                        .collect(Collectors.joining(", ")));
    }

    public void removeRoles(@NotNull Collection<Role> rolesToRemove) throws GameException {
        validateIsSetupStage();
        List<Role> notPresent = rolesToRemove.stream()
                .filter(role -> !roles.contains(role))
                .collect(Collectors.toList());
        if (notPresent.size() == 0) {
            roles.removeAll(rolesToRemove);
            LOGGER.info("Removed some roles.");
        } else throw new GameException("Coś jest nie tak. Tych roli nie ma: " +
                notPresent.stream()
                        .map(Role::getName)
                        .collect(Collectors.joining(", ")));
    }

    public void addPlayer(Player player) throws GameException {
        validateIsSetupStage();
        if (players.stream().anyMatch(p -> p.getID().equals(player.getID())))
            throw new GameException("Halko, już grasz! Nie musisz się już zgłaszać!");
        if (manitu.getID().equals(player.getID()))
            throw new GameException("Nope. Już grasz. Jako Manitu. Doceń to!");
        players.add(player);
        LOGGER.info("Added player.");
    }

    public void assignRoles() throws GameException {
        validateIsSetupStage();
        int numPlayers = players.size();
        if (numPlayers == 0)
            throw new GameException("0 graczy to odrobinkę za mało do dobrej gry :/");
        int numRoles = roles.size();
        if (numRoles == 0)
            throw new GameException("A może jakieś karty do gry by się przydały? Z pustego i Salomon nie potasuje.");
        if (numPlayers != numRoles)
            throw new GameException("Graczy jest: " + numPlayers + ", a ról: " + numRoles + ". Chyba widzisz, w czym problem.");
        Random random = new Random();
        List<Integer> sequence = IntStream.range(0, players.size()).boxed().collect(Collectors.toList());
        for (Player player : players) {
            player.setRole(roles.get(sequence.remove(random.nextInt(sequence.size()))));
        }
        stage = GameStage.ONGOING;
        LOGGER.info("Randomized and assigned roles.");
    }

    public void reset() {
        stage = GameStage.NOT_INITIATED;
        this.players.clear();
        this.roles.clear();
        this.manitu = null;
        this.specialEvent = null;
        LOGGER.info("Restored NOT_INITIATED game state.");
    }

    public void killPlayer(@NotNull Player playerToKill) throws GameException {
        if (!playerToKill.isAlive())
            throw new GameException("Ten gracz jest już martwy. I na zewnątrz, i w środeczku");
        playerToKill.setAlive(false);
        LOGGER.info("Killed.");
    }

    public boolean isVotingOn() {
        return specialEvent instanceof VotingEvent;
    }

    public void startVotingWhoToSearch(@NotNull List<Player> searchCandidates) throws GameException {
        List<Player> candidates = searchCandidates.stream()
                .filter(Player::isAlive)
                .collect(Collectors.toList());
        if (candidates.size() < 3)
            throw new GameException("Co najmniej jeden z przeszukiwanych jest martwy. Nie ma grabieży zwłok. Ponownej.");
        List<Player> voters = players.stream()
                .filter(Player::isAlive)
                .collect(Collectors.toList());
        specialEvent = new VotingWhoToSearch(candidates, voters);
        LOGGER.info("Began voting on who to search.");
    }

    public void startVotingIfToHang() {
        specialEvent = new VotingIfToHang(
                players.stream()
                        .filter(Player::isAlive)
                        .collect(Collectors.toList()));
        LOGGER.info("Began voting on if to hang");
    }

    public void vote(Player player, VotingOption votingOption) throws GameException {
        if (((VotingEvent) specialEvent).getVotes().containsKey(player))
            throw new GameException("Nie jesteś uprawniony do głosowania!");
        if (((VotingEvent) specialEvent).getVotes().get(player) != null)
            throw new GameException("Już oddałeś/łaś/łoś głos!");
        ((VotingEvent) specialEvent).vote(player, votingOption);
        LOGGER.info("Voted.");
    }

    public void endVoting() {
        specialEvent = null;
        LOGGER.info("Ended voting.");
    }

    public void startVotingWhoToHang(@NotNull List<Player> hangCandidates) throws GameException {
        List<Player> aliveCandidates = hangCandidates.stream()
                .filter(Player::isAlive)
                .collect(Collectors.toList());
        if (aliveCandidates.size() < 2)
            throw new GameException("Na pewno chcesz wieszać martwych... ?");
        List<Player> aliveVoters = players.stream()
                .filter(Player::isAlive)
                .collect(Collectors.toList());
        specialEvent = new VotingWhoToHang(aliveCandidates, aliveVoters);
        LOGGER.info("Began voting on who to hang.");
    }

    public void startVotingWhoLosesDuel(@NotNull List<Player> duelers) throws GameException {
        List<Player> aliveDuelers = duelers.stream()
                .filter(Player::isAlive)
                .collect(Collectors.toList());
        if (aliveDuelers.size() < 2)
            throw new GameException("Martwi głosu nie mają. Więc też nie mogą wyzwać nikogo. W szczególności na pojedynek");
        List<Player> aliveVoters = players.stream()
                .filter(Player::isAlive)
                .collect(Collectors.toList());
        specialEvent = new VotingWhoLosesDuel(aliveDuelers, aliveVoters);
        LOGGER.info("Began voting on who loses the duel.");
    }

    public List<Role> getRoles() throws GameException {
        if (stage == Game.GameStage.NOT_INITIATED)
            throw new GameException("Gra jeszcze nie zainicjowana, powoli!");
        return roles;
    }

    public HashMap<Role, Boolean> getRolesStatus() {
        HashMap<Role, Boolean> status = new HashMap<>();
        players.forEach(player -> status.put(player.getRole(), player.isAlive()));
        return status;
    }

    public enum GameStage {
        NOT_INITIATED,
        SETUP,
        ONGOING
    }

    private void validateIsSetupStage() throws GameException {
        if (stage != Game.GameStage.SETUP)
            throw new GameException("Żeby to zrobić, gra powinna być w fazie przygotowania. " +
                    "A nie jest. No, inaczej bym tego nie pisał.");
    }
}
