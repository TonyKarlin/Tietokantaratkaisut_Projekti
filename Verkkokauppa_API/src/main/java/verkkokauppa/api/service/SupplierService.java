package verkkokauppa.api.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import verkkokauppa.api.dtos.SupplierRequest;
import verkkokauppa.api.entity.Supplier;
import verkkokauppa.api.repository.SupplierRepository;
import verkkokauppa.api.utility.exceptions.custom_exceptions.InvalidArgumentException;
import verkkokauppa.api.utility.exceptions.custom_exceptions.SupplierNotFoundException;

@Service
public class SupplierService {

    private final SupplierRepository supplierRepository;

    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    public Page<Supplier> getSuppliersPage(Pageable pageable) {
        return supplierRepository.findAll(pageable);
    }

    public Optional<Supplier> getSupplierById(Integer id) {
        if (id == null) {
            throw new InvalidArgumentException("ID cannot be null");
        }
        return supplierRepository.findById(id);
    }

    public Supplier postSupplier(SupplierRequest request) {
        if (request == null) {
            throw new InvalidArgumentException("Supplier cannot be null");
        }
        if (!isValidSupplierRequest(request)) {
            throw new InvalidArgumentException("Invalid supplier data: "
                    + "Name field must be non-empty");
        }

        Supplier newSupplier = new Supplier(
                request.name(),
                request.contactName(),
                request.phone(),
                request.email()
        );

        return supplierRepository.save(newSupplier);
    }

    public Supplier updateSupplier(Integer id, SupplierRequest updateRequest) {
        if (id == null) {
            throw new InvalidArgumentException("ID cannot be null");
        }
        if (updateRequest == null) {
            throw new InvalidArgumentException("Update request cannot be null");
        }
        if (!isValidSupplierRequest(updateRequest)) {
            throw new InvalidArgumentException("Invalid update request");
        }

        Supplier existingSupplier = supplierRepository.findById(id)
                .orElseThrow(() -> new SupplierNotFoundException("Supplier not found with id: " + id));

        existingSupplier.setName(updateRequest.name());
        existingSupplier.setContactName(updateRequest.contactName());
        existingSupplier.setPhone(updateRequest.phone());
        existingSupplier.setEmail(updateRequest.email());

        return supplierRepository.save(existingSupplier);
    }

    public void deleteSupplier(Integer id) {
        if (id == null) {
            throw new InvalidArgumentException("ID cannot be null");
        }
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new SupplierNotFoundException("Supplier not found with id: " + id));

        supplierRepository.delete(supplier);
    }

    private boolean isValidSupplierRequest(SupplierRequest request) {
        return request.name() != null && !request.name().trim().isEmpty();
    }
}
