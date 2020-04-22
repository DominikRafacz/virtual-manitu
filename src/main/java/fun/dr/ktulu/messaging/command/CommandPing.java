package fun.dr.ktulu.messaging.command;

import net.dv8tion.jda.api.entities.Message;

public class CommandPing extends Command {
    public CommandPing(Message message) {
        super(message);
    }

    @Override
    public void execute() {
        channel.sendMessage("pong").queue();
    }
}
