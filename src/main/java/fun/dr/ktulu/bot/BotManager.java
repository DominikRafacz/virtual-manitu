package fun.dr.ktulu.bot;


import net.dv8tion.jda.api.JDABuilder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public class BotManager {

    private static BotManager INSTANCE = null;
    private final static String CONFIG_FILE = "src/main/resources/config.json";
    private String BOT_TOKEN;

    private BotManager() {
        loadConfig();
    }

    public static BotManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BotManager();
        }
        return INSTANCE;
    }

    public void start() {

    }

    private void loadConfig() {
        try {
            JSONObject json = (JSONObject) new JSONParser().parse(new FileReader(CONFIG_FILE));
            BOT_TOKEN = (String) json.get("BOT_TOKEN");

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}
