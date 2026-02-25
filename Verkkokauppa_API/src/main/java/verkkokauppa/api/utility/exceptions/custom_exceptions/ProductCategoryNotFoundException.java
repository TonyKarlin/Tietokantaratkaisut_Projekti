package verkkokauppa.api.utility.exceptions.custom_exceptions;

public class ProductCategoryNotFoundException extends RuntimeException {

    public ProductCategoryNotFoundException(String message) {
        super(message);
    }
}
