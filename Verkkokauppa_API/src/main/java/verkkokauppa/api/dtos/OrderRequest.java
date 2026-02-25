package verkkokauppa.api.dtos;

import java.time.LocalDateTime;

public record OrderRequest(
        Integer customerId,
        Integer shippingAddressId,
        LocalDateTime orderDate,
        LocalDateTime deliveryDate,
        String status
) {
}
