package verkkokauppa.api.utility.exceptions.custom_exceptions;

public class SupplierLockedException extends RuntimeException {

    public SupplierLockedException(String message) {
        super(message);
    }
}
