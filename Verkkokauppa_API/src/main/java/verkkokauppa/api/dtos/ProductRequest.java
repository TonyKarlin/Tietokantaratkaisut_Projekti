package verkkokauppa.api.dtos;

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


