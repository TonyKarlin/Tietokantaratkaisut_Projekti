package verkkokauppa.api.dtos.oiDTOs;

public record OrderItemRequest(
        Integer orderId,
        Integer productId,
        OIQuantityUpdateRequest quantity
) {
}

