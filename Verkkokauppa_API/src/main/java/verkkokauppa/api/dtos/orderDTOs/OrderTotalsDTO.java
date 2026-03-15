package verkkokauppa.api.dtos.orderDTOs;

import java.math.BigDecimal;
import java.time.LocalDate;

public record OrderTotalsDTO(
        Integer orderId,
        Integer customerId,
        LocalDate orderDate,
        LocalDate deliveryDate,
        Integer shippingAddressId,
        String status,
        Integer itemLines,
        Integer totalQuantity,
        BigDecimal totalAmount
) {}
