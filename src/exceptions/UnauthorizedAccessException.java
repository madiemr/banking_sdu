package exceptions;

public class UnauthorizedAccessException extends BankingException {
    public UnauthorizedAccessException(String message) {
        super(message);
    }
}
