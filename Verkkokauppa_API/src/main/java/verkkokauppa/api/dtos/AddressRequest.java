package verkkokauppa.api.dtos;

public record AddressRequest(Integer customerId, String street, String postalCode, String city, String country) {
}
