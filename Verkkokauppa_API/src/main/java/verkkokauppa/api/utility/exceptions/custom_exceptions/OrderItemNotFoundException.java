package verkkokauppa.api.utility.exceptions.custom_exceptions;

public class OrderItemNotFoundException extends RuntimeException {
    public OrderItemNotFoundException(String message) {
        super(message);
    }
}
