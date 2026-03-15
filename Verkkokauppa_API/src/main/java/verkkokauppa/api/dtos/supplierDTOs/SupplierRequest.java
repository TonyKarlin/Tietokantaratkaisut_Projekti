package verkkokauppa.api.dtos.supplierDTOs;

public record SupplierRequest(
        Integer id,
        String name,
        String contactName,
        String phone,
        String email
        ) {

}
