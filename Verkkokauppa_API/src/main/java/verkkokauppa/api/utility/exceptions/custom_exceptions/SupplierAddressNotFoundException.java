package verkkokauppa.api.utility.exceptions.custom_exceptions;

public class SupplierAddressNotFoundException extends RuntimeException {

    public SupplierAddressNotFoundException(String message) {
        super(message);
    }
}
