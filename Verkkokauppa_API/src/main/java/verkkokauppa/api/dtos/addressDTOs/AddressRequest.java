package verkkokauppa.api.dtos.addressDTOs;

public record AddressRequest(Integer customerId, String street, String postalCode, String city, String country) {
}
