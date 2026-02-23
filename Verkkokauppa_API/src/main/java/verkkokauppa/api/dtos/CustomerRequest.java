package verkkokauppa.api.dtos;

public record CustomerRequest(
        String firstName,
        String lastName,
        String email,
        String phoneNumber
) {
}
