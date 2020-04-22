package fun.dr.ktulu.messaging.command.exception;

public class ValidationException extends CommandException {
    public ValidationException(String botMessage) {
        super(botMessage);
    }
}
