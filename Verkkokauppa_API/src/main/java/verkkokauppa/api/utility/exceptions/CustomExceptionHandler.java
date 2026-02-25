package verkkokauppa.api.utility.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import verkkokauppa.api.utility.exceptions.custom_exceptions.CustomerNotFoundException;
import verkkokauppa.api.utility.exceptions.custom_exceptions.InvalidArgumentException;
import verkkokauppa.api.utility.exceptions.custom_exceptions.OrderNotFoundException;
import verkkokauppa.api.utility.exceptions.custom_exceptions.ProductNotFoundException;

@ControllerAdvice
public class CustomExceptionHandler {
    // malli viesti, johon syötetään luokan nimi, joka aiheutti poikkeuksen
    // "%s" = paikka, johon luokan nimi asetetaan
    private static final String NOT_FOUND_MESSAGE = "The requested %s was not found.";
    private static final String EMPTY_INPUT_MESSAGE = "Invalid input: empty or null value provided.";

    private static String notFoundMessage(Class<?> classType) {
        return String.format(NOT_FOUND_MESSAGE, classType.getSimpleName());
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<String> handleCustomerNotFoundException(CustomerNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(notFoundMessage(CustomerNotFoundException.class) + "\n" + e.getMessage());
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<String> handleProductNotFoundException(ProductNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(notFoundMessage(ProductNotFoundException.class) + "\n" + e.getMessage());
    }

    @ExceptionHandler(InvalidArgumentException.class)
    public ResponseEntity<String> handleEmptyInputException(InvalidArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(EMPTY_INPUT_MESSAGE + "\n" + e.getMessage());
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<String> handleOrderNotFoundException(OrderNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(notFoundMessage(OrderNotFoundException.class) + "\n" + e.getMessage());
    }
}
