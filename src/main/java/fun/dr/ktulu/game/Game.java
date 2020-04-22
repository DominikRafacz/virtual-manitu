package fun.dr.ktulu.game;

import fun.dr.ktulu.bot.AppManager;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;
import java.util.SplittableRandom;


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

    public enum GameStage {
        NOT_INITIATED,
        SETUP,
        ONGOING
    }
}
