package verkkokauppa.api.dtos.orderDTOs;

import java.time.LocalDateTime;

public record OrderRequest(
        Integer customerId,
        Integer shippingAddressId,
        LocalDateTime orderDate,
        LocalDateTime deliveryDate,
        String status
) {
}
