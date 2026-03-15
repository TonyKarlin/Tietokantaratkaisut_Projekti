package verkkokauppa.api.dtos.customerDTOs;

public record CustomerRequest(
        String firstName,
        String lastName,
        String email,
        String phoneNumber
) {
}
