package fun.dr.ktulu.game;

import fun.dr.ktulu.bot.AppManager;
import fun.dr.ktulu.game.event.*;
import fun.dr.ktulu.messaging.command.exception.ExecutionException;
import lombok.Getter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class Game {
    private static final Logger LOGGER = LoggerFactory.getLogger(Game.class);
    private static Game INSTANCE;

    @Getter
    private final List<Player> players;

    @Getter
    private Manitu manitu;

    @Getter
    private final List<Role> roles;

    @Getter
    private GameStage gameStage;

    @Getter
    private SpecialEvent specialEvent;

    @Getter
    private String manituChannelID;

    @Getter
    private String gameChannelID;

    @Getter
    private String guildID;

    private Game() {
        LOGGER.info("Game instantiated.");
        players = new ArrayList<>();
        roles = new ArrayList<>();
        gameStage = GameStage.NOT_INITIATED;
    }

    public TextChannel getGameChannel() {
        return AppManager.getInstance().getJda().getTextChannelById(gameChannelID);
    }

    public TextChannel getManituChannel() {
        return AppManager.getInstance().getJda().getTextChannelById(manituChannelID);
    }

    public Guild getGuild() {
        return AppManager.getInstance().getJda().getGuildById(guildID);
    }

    public static Game getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Game();
        }
        return INSTANCE;
    }

    public void initiate(@NotNull String guildID,
                         @NotNull String manituID,
                         @NotNull String manituChannelID,
                         @NotNull String gameChannelID) {
        gameStage = GameStage.SETUP;
        this.guildID = guildID;
        this.manituChannelID = manituChannelID;
        this.gameChannelID = gameChannelID;
        this.manitu = new Manitu(manituID);
        LOGGER.info("Game initiated!");
    }

    public void addRoles(@NotNull Collection<Role> rolesToAdd) throws ExecutionException {
        List<Role> duplicates = roles.stream()
                .filter(rolesToAdd::contains)
                .collect(Collectors.toList());
        if (duplicates.size() == 0) {
            roles.addAll(rolesToAdd);
            LOGGER.info("Added some roles.");
        } else throw new ExecutionException("Sorrrry, ktoś już dodał te role: " +
                duplicates.stream()
                        .map(Role::getName)
                        .collect(Collectors.joining(", ")));
    }

    public void removeRoles(@NotNull Collection<Role> rolesToRemove) throws ExecutionException {
        List<Role> notPresent = rolesToRemove.stream()
                .filter(role -> !roles.contains(role))
                .collect(Collectors.toList());
        if (notPresent.size() == 0) {
            roles.removeAll(rolesToRemove);
            LOGGER.info("Removed some roles.");
        } else throw new ExecutionException("Coś jest nie tak. Tych roli nie ma: " +
                notPresent.stream()
                        .map(Role::getName)
                        .collect(Collectors.joining(", ")));
    }

    public void addPlayer(@NotNull String userID, @NotNull String communicationChannelID) {
        players.add(new Player(userID, communicationChannelID));
        LOGGER.info("Added player.");
    }

    public void assignRoles() {
        Random random = new Random();
        List<Integer> sequence = IntStream.range(0, players.size()).boxed().collect(Collectors.toList());
        for (Player player : players) {
            player.setRole(roles.get(sequence.remove(random.nextInt(sequence.size()))));
        }
        gameStage = GameStage.ONGOING;
        LOGGER.info("Randomized and assigned roles.");
    }

    public void reset() {
        gameStage = GameStage.NOT_INITIATED;
        this.players.clear();
        this.roles.clear();
        this.manitu = null;
        this.gameChannelID = null;
        this.manituChannelID = null;
        this.guildID = null;
        this.specialEvent = null;
        LOGGER.info("Restored NOT_INITIATED game state.");
    }

    public void killPlayer(@NotNull Player playerToKill) throws ExecutionException {
        if (!playerToKill.isAlive())
            throw new ExecutionException("Ten gracz jest już martwy. I na zewnątrz, i w środeczku");
        playerToKill.setAlive(false);
        LOGGER.info("Killed.");
    }

    public boolean isVotingOn() {
        return specialEvent instanceof VotingEvent;
    }

    public void startVotingWhoToSearch(@NotNull List<Player> searchCandidates) throws ExecutionException {
        List<Player> aliveCandidates = searchCandidates.stream()
                .filter(Player::isAlive)
                .collect(Collectors.toList());
        if (aliveCandidates.size() < 3)
            throw new ExecutionException("Co najmniej jeden z przeszukiwanych jest martwy. Nie ma grabieży zwłok. Ponownej.");
        List<Player> aliveVoters = players.stream()
                .filter(Player::isAlive)
                .collect(Collectors.toList());
        specialEvent = new VotingWhoToSearch(aliveCandidates, aliveVoters);
        LOGGER.info("Began voting on who to search.");
    }

    public void startVotingIfToHang() {
        specialEvent = new VotingIfToHang(
                players.stream()
                        .filter(Player::isAlive)
                        .collect(Collectors.toList()));
        LOGGER.info("Began voting on if to hang");
    }

    public void vote(Player voter, VotingOption votingOption) {
        ((VotingEvent) specialEvent).vote(voter, votingOption);
        LOGGER.info("Voted.");
    }

    public void endVoting() {
        specialEvent = null;
        LOGGER.info("Ended voting.");
    }

    public void startVotingWhoToHang(@NotNull List<Player> hangCandidates) throws ExecutionException {
        List<Player> aliveCandidates = hangCandidates.stream()
                .filter(Player::isAlive)
                .collect(Collectors.toList());
        if (aliveCandidates.size() < 2)
            throw new ExecutionException("Na pewno chcesz wieszać martwych... ?");
        List<Player> aliveVoters = players.stream()
                .filter(Player::isAlive)
                .collect(Collectors.toList());
        specialEvent = new VotingWhoToHang(aliveCandidates, aliveVoters);
        LOGGER.info("Began voting on who to hang.");
    }

    public void startVotingWhoLosesDuel(@NotNull List<Player> duelers) throws ExecutionException {
        List<Player> aliveDuelers = duelers.stream()
                .filter(Player::isAlive)
                .collect(Collectors.toList());
        if (aliveDuelers.size() < 2)
            throw new ExecutionException("Martwi głosu nie mają. Więc też nie mogą wyzwać nikogo. W szczególności na pojedynek");
        List<Player> aliveVoters = players.stream()
                .filter(Player::isAlive)
                .collect(Collectors.toList());
        specialEvent = new VotingWhoLosesDuel(aliveDuelers, aliveVoters);
        LOGGER.info("Began voting on who loses the duel.");
    }

    public enum GameStage {
        NOT_INITIATED,
        SETUP,
        ONGOING
    }
}
