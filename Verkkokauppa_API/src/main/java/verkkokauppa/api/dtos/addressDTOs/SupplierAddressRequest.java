package verkkokauppa.api.dtos.addressDTOs;

public record SupplierAddressRequest(
        Integer id,
        Integer supplierId,
        String streetAddress,
        String postalCode,
        String city,
        String country
        ) {

}
