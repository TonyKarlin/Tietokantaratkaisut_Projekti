package verkkokauppa.api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import verkkokauppa.api.dtos.SupplierAddressRequest;
import verkkokauppa.api.entity.Supplier;
import verkkokauppa.api.entity.SupplierAddress;
import verkkokauppa.api.repository.SupplierAddressRepository;
import verkkokauppa.api.repository.SupplierRepository;
import verkkokauppa.api.utility.exceptions.custom_exceptions.InvalidArgumentException;
import verkkokauppa.api.utility.exceptions.custom_exceptions.SupplierAddressNotFoundException;
import verkkokauppa.api.utility.exceptions.custom_exceptions.SupplierNotFoundException;

@Service
public class SupplierAddressService {

    private final SupplierAddressRepository addressRepository;
    private final SupplierRepository supplierRepository;

    public SupplierAddressService(SupplierAddressRepository addressRepository,
            SupplierRepository supplierRepository) {
        this.addressRepository = addressRepository;
        this.supplierRepository = supplierRepository;
    }

    public Page<SupplierAddress> getAddressesPage(Pageable pageable) {
        return addressRepository.findAll(pageable);
    }

    public Optional<SupplierAddress> getAddressById(Integer id) {
        if (id == null) {
            throw new InvalidArgumentException("Address ID cannot be null");
        }
        return addressRepository.findById(id);
    }

    public List<SupplierAddress> getAddressesBySupplierId(Integer supplierId) {
        if (supplierId == null) {
            throw new InvalidArgumentException("Supplier ID cannot be null");
        }
        return addressRepository.findBySupplierId(supplierId);
    }

    public SupplierAddress postAddress(SupplierAddressRequest request) {
        if (request == null) {
            throw new InvalidArgumentException("Address cannot be null");
        }
        if (!isValidAddressRequest(request)) {
            throw new InvalidArgumentException("Invalid address data: "
                    + "Supplier ID, street address, and city are required");
        }

        Supplier supplier = supplierRepository.findById(request.supplierId())
                .orElseThrow(() -> new SupplierNotFoundException(
                "Supplier not found with id: " + request.supplierId()));

        SupplierAddress newAddress = new SupplierAddress();
        newAddress.setSupplier(supplier);
        newAddress.setStreetAddress(request.streetAddress());
        newAddress.setPostalCode(request.postalCode());
        newAddress.setCity(request.city());
        newAddress.setCountry(request.country());

        supplier.getAddresses().add(newAddress);

        return addressRepository.save(newAddress);
    }

    public SupplierAddress updateAddress(Integer id, SupplierAddressRequest updateRequest) {
        if (id == null) {
            throw new InvalidArgumentException("ID cannot be null");
        }
        if (updateRequest == null) {
            throw new InvalidArgumentException("Update request cannot be null");
        }
        if (!isValidAddressRequest(updateRequest)) {
            throw new InvalidArgumentException("Invalid update request");
        }

        SupplierAddress existingAddress = addressRepository.findById(id)
                .orElseThrow(() -> new SupplierAddressNotFoundException(
                "Supplier address not found with id: " + id));

        Supplier supplier = supplierRepository.findById(updateRequest.supplierId())
                .orElseThrow(() -> new SupplierNotFoundException(
                "Supplier not found with id: " + updateRequest.supplierId()));

        existingAddress.setSupplier(supplier);
        existingAddress.setStreetAddress(updateRequest.streetAddress());
        existingAddress.setPostalCode(updateRequest.postalCode());
        existingAddress.setCity(updateRequest.city());
        existingAddress.setCountry(updateRequest.country());

        return addressRepository.save(existingAddress);
    }

    public void deleteAddress(Integer id) {
        if (id == null) {
            throw new InvalidArgumentException("ID cannot be null");
        }
        SupplierAddress address = addressRepository.findById(id)
                .orElseThrow(() -> new SupplierAddressNotFoundException(
                "Supplier address not found with id: " + id));

        addressRepository.delete(address);
    }

    private boolean isValidAddressRequest(SupplierAddressRequest request) {
        return request.supplierId() != null
                && request.streetAddress() != null && !request.streetAddress().trim().isEmpty()
                && request.city() != null && !request.city().trim().isEmpty();
    }
}
