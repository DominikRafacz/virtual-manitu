package fun.dr.ktulu.messaging.command.exception;

public class ExecutionException extends CommandException {
    public ExecutionException(String botMessage) {
        super(botMessage);
    }
}
