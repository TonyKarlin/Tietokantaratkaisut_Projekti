package verkkokauppa.api.dtos;

public record SupplierRequest(
        Integer id,
        String name,
        String contactName,
        String phone,
        String email
        ) {

}
