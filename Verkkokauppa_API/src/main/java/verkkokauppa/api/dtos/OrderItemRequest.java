package verkkokauppa.api.dtos;

import java.math.BigDecimal;

public record OrderItemRequest(
        Integer orderId,
        Integer productId,
        OIQuantityUpdateRequest quantity
) {
}

