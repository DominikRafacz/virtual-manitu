package fun.dr.ktulu.bot;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public class BotStarter {
    public static void main(String[] args) {
        BotManager.getInstance().start();
    }
}
