package verkkokauppa.api.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import verkkokauppa.api.dtos.CustomerRequest;
import verkkokauppa.api.entity.Customer;
import verkkokauppa.api.repository.CustomersRepository;
import verkkokauppa.api.utility.exceptions.custom_exceptions.CustomerNotFoundException;
import verkkokauppa.api.utility.exceptions.custom_exceptions.InvalidArgumentException;

@Service
public class CustomersService {
    private static final String CUSTOMER_NOT_FOUND = "Customer not found with id: ";
    private final CustomersRepository customersRepository;

    public CustomersService(CustomersRepository customersRepository) {
        this.customersRepository = customersRepository;
    }

    public Page<Customer> getPage(Pageable pageable) {
        return customersRepository.findAll(pageable);
    }

    public Customer getByIdOrThrow(Integer id) {
        if (id == null) {
            throw new InvalidArgumentException("Customer ID cannot be null");
        }
        return customersRepository.findById(id)
                .orElseThrow(() ->
                        new CustomerNotFoundException(CUSTOMER_NOT_FOUND + id));
    }

    public Customer postRequest(CustomerRequest customer) {
        if (customer == null) {
            throw new InvalidArgumentException("Customer cannot be null");
        }
        if (!isValidCustomerRequest(customer)) {
            throw new InvalidArgumentException("Invalid customer data: "
                    + "Fields other than phone number must be non-empty");
        }
        Customer newCustomer = new Customer(
                customer.firstName(),
                customer.lastName(),
                customer.email(),
                customer.phoneNumber());

        return customersRepository.save(newCustomer);
    }

    public Customer putRequest(Integer id, CustomerRequest updateRequest) {
        if (id == null) {
            throw new InvalidArgumentException("ID cannot be null");
        }
        if (updateRequest == null) {
            throw new CustomerNotFoundException("Update request cannot be null");
        }
        if (!isValidCustomerRequest(updateRequest)) {
            throw new InvalidArgumentException("Invalid update request:"
                    + " Fields other than phone number must be non-empty");
        }
        Customer existingCustomer = customersRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(CUSTOMER_NOT_FOUND + id));

        existingCustomer.setFirstName(updateRequest.firstName());
        existingCustomer.setLastName(updateRequest.lastName());
        existingCustomer.setEmail(updateRequest.email());
        existingCustomer.setPhone(updateRequest.phoneNumber());

        return customersRepository.save(existingCustomer);
    }

    public void delete(Integer id) {
        if (id == null) {
            throw new InvalidArgumentException("ID cannot be null");
        }
        Customer customer = getByIdOrThrow(id);
        customersRepository.delete(customer);
    }

    private boolean isValidCustomerRequest(CustomerRequest updateRequest) {
        return (updateRequest.firstName() != null && !updateRequest.firstName().isBlank())
                && (updateRequest.lastName() != null && !updateRequest.lastName().isBlank())
                && (updateRequest.email() != null && !updateRequest.email().isBlank());
    }
}
