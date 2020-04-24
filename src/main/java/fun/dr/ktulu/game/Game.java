package fun.dr.ktulu.game;

import fun.dr.ktulu.bot.AppManager;
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
    private final List<String> roles;

    @Getter
    private GameStage gameStage;

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

    public void initiate(String guildID,
                         String manituID,
                         String manituChannelID,
                         String gameChannelID) {
        gameStage = GameStage.SETUP;
        this.guildID = guildID;
        this.manituChannelID = manituChannelID;
        this.gameChannelID = gameChannelID;
        this.manitu = new Manitu(manituID);
        LOGGER.info("Game initiated!");
    }

    public void addRoles(@NotNull Collection<String> rolesToAdd) throws ExecutionException {
        List<String> duplicates = roles.stream()
                .filter(rolesToAdd::contains)
                .collect(Collectors.toList());
        if (duplicates.size() == 0) {
            roles.addAll(rolesToAdd);
            LOGGER.info("Added some roles.");
        } else throw new ExecutionException("Sorrrry, ktoś już dodał te role: " +
                String.join(", ", duplicates));
    }

    public void removeRoles(@NotNull Collection<String> rolesToRemove) throws ExecutionException {
        List<String> notPresent = rolesToRemove.stream()
                .filter(role -> !roles.contains(role))
                .collect(Collectors.toList());
        if (notPresent.size() == 0) {
            roles.removeAll(rolesToRemove);
            LOGGER.info("Removed some roles.");
        } else throw new ExecutionException("Coś jest nie tak. Tych roli nie ma: " +
                String.join(", ", notPresent));
    }

    public void addPlayer(String userID, String communicationChannelID) {
        players.add(new Player(userID, communicationChannelID));
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
        LOGGER.info("Restored NOT_INITIATED game state.");
    }

    public void killPlayer(String playerID) throws ExecutionException {
        Optional<Player> playerToKill = players.stream()
                .filter(player -> player.getUserID().equals(playerID))
                .findFirst();
        if (playerToKill.isEmpty())
            throw new ExecutionException("Gracz o takim ID nie gra, soreczka.");
        Player player = playerToKill.get();
        if (!player.isAlive())
            throw new ExecutionException("Ten gracz jest już martwy. I na zewnątrz, i w środeczku");
        player.setAlive(false);
    }

    public enum GameStage {
        NOT_INITIATED,
        SETUP,
        ONGOING
    }
}
