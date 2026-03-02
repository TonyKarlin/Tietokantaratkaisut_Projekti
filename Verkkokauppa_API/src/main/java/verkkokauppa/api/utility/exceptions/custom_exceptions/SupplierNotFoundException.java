package verkkokauppa.api.utility.exceptions.custom_exceptions;

public class SupplierNotFoundException extends RuntimeException {
    public SupplierNotFoundException(String message) {
        super(message);
    }
}
