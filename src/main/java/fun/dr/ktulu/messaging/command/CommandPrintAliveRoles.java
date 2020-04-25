package fun.dr.ktulu.messaging.command;

import fun.dr.ktulu.game.Faction;
import fun.dr.ktulu.game.Role;
import fun.dr.ktulu.messaging.command.exception.ExecutionException;
import fun.dr.ktulu.messaging.command.exception.ValidationException;
import net.dv8tion.jda.api.entities.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CommandPrintAliveRoles extends Command {
    private String text;

    public CommandPrintAliveRoles(Message message) {
        super(message);
    }

    @Override
    protected void validate() throws ValidationException {
        validateIsManituChannel();
        validateIsOngoingStage();
    }

    @Override
    protected void execute() {
        HashMap<Role, Boolean> rolesStatus = game.getRolesStatus();
        List<List<String>> textColumns = new ArrayList<>();
        List<Faction> factions = game.getRoles().stream()
                .map(Role::getFaction)
                .distinct()
                .collect(Collectors.toList());
        Map<Faction, List<Role>> binding = new HashMap<>();
        factions.forEach(faction -> binding.put(faction, game.getRoles().stream()
                .filter(role -> role.getFaction() == faction)
                .collect(Collectors.toList())));
        int numRows = binding.keySet().stream()
                .mapToInt(faction -> binding.get(faction).size())
                .max()
                .orElse(0) + 1;
        for (int i = 0; i < factions.size(); i++) {
            textColumns.add(new ArrayList<>());
            List<String> column = textColumns.get(i);
            String factionName = factions.get(i).getName();
            int maxCellSize = binding.get(factions.get(i)).stream()
                    .mapToInt(role -> role.getName().length())
                    .max()
                    .orElse(0) + 2;
            if (factionName.length() + 2 > maxCellSize)
                maxCellSize = factionName.length() + 2;
            column.add("**" + factionName + "**" + " ".repeat(maxCellSize - factionName.length()));
            for (int j = 0; j < numRows - 1; j++) {
                String cell;
                if (j >= binding.get(factions.get(i)).size())
                    cell = " ".repeat(maxCellSize);
                else {
                    Role role = binding.get(factions.get(i)).get(j);
                    cell = role.getName();
                    int rawLength = cell.length();
                    if (!rolesStatus.get(role))
                        cell = "~~" + cell + "~~";
                    cell = cell + " ".repeat(maxCellSize - rawLength);
                }
                column.add(cell);
            }
        }

        text = "";
        for (int j = 0; j < numRows; j++) {
            for (int i = 0; i < factions.size(); i++) {
                text += textColumns.get(i).get(j);
            }
            text += "\n";
        }
    }

    @Override
    protected void sendSuccessMessage() {
        game.getGameChannel().sendMessage("Oto pozostali przy życiu\n" + text).queue();

    }
}
