package verkkokauppa.api.dtos;

public record SupplierAddressRequest(
        Integer id,
        Integer supplierId,
        String streetAddress,
        String postalCode,
        String city,
        String country
        ) {

}
