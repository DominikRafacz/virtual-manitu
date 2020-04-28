package fun.dr.ktulu.messaging;

import fun.dr.ktulu.messaging.command.*;
import fun.dr.ktulu.messaging.command.exception.UnknownCommandException;
import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MessageManager {

    private static final String COMMAND_PREFIX = "m!";
    private static final String ARGS_SEPARATOR = " */ *";

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
        String content = message.getContentRaw();
        int spaceIndex = content.indexOf(" ");
        if (spaceIndex == -1)
            return new ArrayList<>(0);
        return new ArrayList<>(
                Arrays.asList(
                        content.substring(spaceIndex + 1)
                                .split(ARGS_SEPARATOR)));
    }

    public static @NotNull Command matchCommand(Message message) throws UnknownCommandException {
        String commandText = MessageManager.extractCommandText(message);
        switch (commandText) {
            case "ping":
                return new CommandPing(message);
            case "nowa-gra":
                return new CommandNewGame(message);
            case "dodaj-role":
                return new CommandAddRoles(message);
            case "dodaj-domyślne-role":
                return new CommandAddDefaultRoles(message);
            case "wypisz-role":
                return new CommandPrintRoles(message);
            case "usuń-role":
                return new CommandRemoveRoles(message);
            case "chcę-grać":
                return new CommandWantToPlay(message);
            case "rozlosuj-role":
                return new CommandRandomizeRoles(message);
            case "wypisz-kto-żyje":
            case "ż":
                return new CommandPrintAliveRoles(message);
            case "ginie":
            case "g":
                return new CommandKill(message);
            case "koniec-gry":
                return new CommandEndGame(message);
            case "głosujemy-kogo-przeszukać":
            case "kto-p":
                return new CommandVotingWhoToSearch(message);
            case "głosujemy-czy-wieszać":
            case "czy-w":
                return new CommandVotingIfToHang(message);
            case "głosujemy-kogo-wieszać":
            case "kto-w":
                return new CommandVotingWhoToHang(message);
            case "głosujemy-kto-przegrywa-pojedynek":
            case "kto-d":
                return new CommandVotingWhoLosesDuel(message);
            case "głosuję-na":
            case "v":
                return new CommandVote(message);
            default:
                throw new UnknownCommandException(commandText);
        }
    }
}
