package verkkokauppa.api.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import verkkokauppa.api.dtos.AddressRequest;
import verkkokauppa.api.entity.Customer;
import verkkokauppa.api.entity.CustomerAddress;
import verkkokauppa.api.repository.CustomerAddressesRepository;
import verkkokauppa.api.utility.exceptions.custom_exceptions.AddressNotFoundException;
import verkkokauppa.api.utility.exceptions.custom_exceptions.InvalidArgumentException;

@Service
public class CustomerAddressesService {
    private static final String NOT_FOUND_MESSAGE = "Address not found with id: ";
    private final CustomerAddressesRepository repository;
    private final CustomersService customersService;

    public CustomerAddressesService(CustomerAddressesRepository repository, CustomersService customersService) {
        this.repository = repository;
        this.customersService = customersService;
    }

    public Page<CustomerAddress> getPage(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public CustomerAddress getByIdOrThrow(Integer id) {
        if (id == null) {
            throw new InvalidArgumentException("Address ID cannot be null.");
        }
        return repository.findById(id)
                .orElseThrow(() -> new AddressNotFoundException(NOT_FOUND_MESSAGE + id));
    }

    public CustomerAddress postRequest(AddressRequest address) {
        if (address == null) {
            throw new AddressNotFoundException("Address is null.");
        }
        if (!isValidAddress(address)) {
            throw new InvalidArgumentException("Address has invalid fields.");
        }
        Customer customer = customersService.getByIdOrThrow(address.customerId());
        CustomerAddress customerAddress = new CustomerAddress();
        customerAddress.setCustomer(customer);
        customerAddress.setStreetAddress(address.street());
        customerAddress.setPostalCode(address.postalCode());
        customerAddress.setCity(address.city());
        customerAddress.setCountry(address.country());

        customer.setAddress(customerAddress);

        return repository.save(customerAddress);
    }

    public CustomerAddress putRequest(Integer addressId, AddressRequest address) {
        if (address == null) {
            throw new AddressNotFoundException("Address is null");
        }
        if (!isValidAddress(address)) {
            throw new InvalidArgumentException("Address has invalid fields.");
        }
        CustomerAddress existingAddress = getByIdOrThrow(addressId);

        existingAddress.setStreetAddress(address.street());
        existingAddress.setPostalCode(address.postalCode());
        existingAddress.setCity(address.city());
        existingAddress.setCountry(address.country());

        return repository.save(existingAddress);
    }

    public void delete(Integer id) {
        if (id == null) {
            throw new AddressNotFoundException(NOT_FOUND_MESSAGE + null);
        }
        CustomerAddress address = getByIdOrThrow(id);
        Customer customer = address.getCustomer();
        if (customer != null) {
            customer.setAddress(null);
            address.setCustomer(null);
        }
        repository.delete(address);
    }


    private boolean isValidAddress(AddressRequest address) {
        return (address.street() != null && !address.street().isBlank()) ||
                (address.postalCode() != null && !address.postalCode().isBlank()) ||
                (address.city() != null && !address.city().isBlank()) ||
                (address.country() != null && !address.country().isBlank());
    }
}
