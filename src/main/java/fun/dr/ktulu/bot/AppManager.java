package fun.dr.ktulu.bot;


import fun.dr.ktulu.bot.listeners.MessageListener;
import fun.dr.ktulu.bot.listeners.ReadyListener;
import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class AppManager {

    private static AppManager INSTANCE = null;
    private static final String CONFIG_FILE = "src/main/resources/config.json";
    private static final Logger LOGGER = LoggerFactory.getLogger(AppManager.class);
    private String BOT_TOKEN;

    @Getter
    private JDA jda;

    private AppManager() {
        loadConfig();
        LOGGER.info("Instantiated successfully.");
    }

    public static AppManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AppManager();
        }
        return INSTANCE;
    }

    public void start() {
        try {
            jda = JDABuilder.create(BOT_TOKEN,
                    List.of(
                            GatewayIntent.GUILD_MESSAGES,
                            GatewayIntent.GUILD_MEMBERS,
                            GatewayIntent.DIRECT_MESSAGES
                    ))
                    .enableIntents(GatewayIntent.GUILD_MEMBERS)
                    .addEventListeners(
                            new ReadyListener(),
                            new MessageListener())
                    .disableCache(List.of(
                            CacheFlag.ACTIVITY,
                            CacheFlag.VOICE_STATE,
                            CacheFlag.EMOTE,
                            CacheFlag.CLIENT_STATUS,
                            CacheFlag.MEMBER_OVERRIDES
                    ))
                    .build();
            LOGGER.info("JDA built.");
            jda.awaitReady();
            LOGGER.info("JDA loaded.");
        } catch (LoginException | InterruptedException e) {
            LOGGER.error("Error during JDA building!");
            e.printStackTrace();
        }
    }

    private void loadConfig() {
        try {
            JSONObject json = (JSONObject) new JSONParser().parse(new FileReader(CONFIG_FILE));
            BOT_TOKEN = (String) json.get("BOT_TOKEN");
            LOGGER.info("Loaded config.");
        } catch (IOException | ParseException e) {
            LOGGER.error("Cannot load config.");
            e.printStackTrace();
        }
    }
}
