package verkkokauppa.api.dtos.productDTOs;

import java.math.BigDecimal;

public record ProductSearchRequest(
        String nameContains,
        BigDecimal minPrice,
        BigDecimal maxPrice,
        Integer minStock,
        Integer categoryId,
        Integer supplierId
        ) {

}
