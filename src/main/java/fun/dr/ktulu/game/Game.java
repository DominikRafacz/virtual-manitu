package fun.dr.ktulu.game;

import fun.dr.ktulu.bot.AppManager;
import fun.dr.ktulu.messaging.command.exception.ExecutionException;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;


public class Game {
    private static final Logger LOGGER = LoggerFactory.getLogger(Game.class);
    private static Game INSTANCE;

    @Getter
    private Set<Player> players;

    @Getter
    private Manitu manitu;

    @Getter
    private Set<String> roles;

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
        players = new HashSet<>();
        roles = new HashSet<>();
        gameStage = GameStage.NOT_INITIATED;
    }

    public TextChannel getGameChannel() {
        return AppManager.getInstance().getJda().getTextChannelById(gameChannelID);
    }

    public TextChannel getManituChannel() {
        return AppManager.getInstance().getJda().getTextChannelById(manituChannelID);
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
        } else throw new ExecutionException("Sorrrry, ktoś już dodał te role: " +
                String.join(", ", duplicates));
    }

    public void removeRoles(@NotNull Collection<String> rolesToRemove) throws ExecutionException {
        List<String> notPresent = rolesToRemove.stream()
                .filter(role -> !roles.contains(role))
                .collect(Collectors.toList());
        if (notPresent.size() == 0) {
            roles.removeAll(rolesToRemove);
        } else throw new ExecutionException("Coś jest nie tak. Tych roli nie ma: " +
                String.join(", ", notPresent));
    }

    public enum GameStage {
        NOT_INITIATED,
        SETUP,
        ONGOING
    }
}
