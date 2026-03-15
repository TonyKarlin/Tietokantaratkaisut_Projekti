package verkkokauppa.api.dtos.productDTOs;

import java.math.BigDecimal;

public record ProductRequest (
        Integer id,
        String name,
        String description,
        BigDecimal price,
        Integer stockQuantity,
        Integer categoryId,
        Integer supplierId
) {
}


