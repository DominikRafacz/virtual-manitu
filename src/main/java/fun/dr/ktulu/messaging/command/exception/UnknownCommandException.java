package fun.dr.ktulu.messaging.command.exception;

public class UnknownCommandException extends Throwable {
    private final String commandMessage;

    public UnknownCommandException(String commandMessage) {
        super();
        this.commandMessage = commandMessage;
    }

    @Override
    public String getMessage() {
        return "Command " + commandMessage + " unknown!";
    }
}
