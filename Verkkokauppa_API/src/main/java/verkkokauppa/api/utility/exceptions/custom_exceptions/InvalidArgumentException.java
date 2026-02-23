package verkkokauppa.api.utility.exceptions.custom_exceptions;

public class InvalidArgumentException extends RuntimeException {
    public InvalidArgumentException(String message) {
        super(message);
    }
}
