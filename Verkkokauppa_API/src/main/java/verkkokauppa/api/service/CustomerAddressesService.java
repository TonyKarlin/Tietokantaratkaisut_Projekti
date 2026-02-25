package verkkokauppa.api.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import verkkokauppa.api.entity.CustomerAddress;
import verkkokauppa.api.repository.CustomerAddressesRepository;
import verkkokauppa.api.utility.exceptions.custom_exceptions.InvalidArgumentException;

import java.util.Optional;

@Service
public class CustomerAddressesService {
    private final CustomerAddressesRepository repository;

    public CustomerAddressesService(CustomerAddressesRepository repository) {
        this.repository = repository;
    }

    public Page<CustomerAddress> getAddressesPage(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Optional<CustomerAddress> getAddressById(Integer id) {
        if (id == null) {
            throw new InvalidArgumentException("ID cannot be null");
        }
        return repository.findById(id);
    }
}
