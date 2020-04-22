package fun.dr.ktulu.messaging;

import net.dv8tion.jda.api.entities.Message;

public class MessageManager {

    private static final String COMMAND_PREFIX = "m!";

    public static boolean isCommand(Message message) {
        return message
                .getContentRaw()
                .startsWith("m!");

    }

    public static String extractCommandText(Message message) {
        return message
                .getContentRaw()
                .split(" ")[0]
                .replaceFirst(COMMAND_PREFIX, "");
    }
}
