package fun.dr.ktulu.messaging;

import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MessageManager {

    private static final String COMMAND_PREFIX = "m!";

    public static boolean isCommand(@NotNull Message message) {
        return message
                .getContentRaw()
                .startsWith("m!");

    }

    public static @NotNull String extractCommandText(@NotNull Message message) {
        return message
                .getContentRaw()
                .split(" ")[0]
                .replaceFirst(COMMAND_PREFIX, "");
    }

    public static @NotNull List<String> extractArgs(@NotNull Message message) {
        List<String> args = new ArrayList<>(
                Arrays.asList(message
                        .getContentRaw()
                        .split(" ")));
        args.remove(0);
        return args;
    }
}
