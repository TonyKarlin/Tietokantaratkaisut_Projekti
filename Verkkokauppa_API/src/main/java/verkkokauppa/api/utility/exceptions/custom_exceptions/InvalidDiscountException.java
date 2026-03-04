package verkkokauppa.api.utility.exceptions.custom_exceptions;

public class InvalidDiscountException extends RuntimeException {
    public InvalidDiscountException(String message) {
        super(message);
    }
}
