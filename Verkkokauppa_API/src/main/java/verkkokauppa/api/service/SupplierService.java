package verkkokauppa.api.service;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import verkkokauppa.api.dtos.supplierDTOs.SupplierRequest;
import verkkokauppa.api.entity.Product;
import verkkokauppa.api.entity.Supplier;
import verkkokauppa.api.repository.ProductRepository;
import verkkokauppa.api.repository.SupplierRepository;
import verkkokauppa.api.utility.exceptions.custom_exceptions.InvalidArgumentException;
import verkkokauppa.api.utility.exceptions.custom_exceptions.ProductNotFoundException;
import verkkokauppa.api.utility.exceptions.custom_exceptions.SupplierLockedException;
import verkkokauppa.api.utility.exceptions.custom_exceptions.SupplierNotFoundException;

@Service
public class SupplierService {

    private static final int LOW_STOCK_THRESHOLD = 10;

    private final SupplierRepository supplierRepository;
    private final ProductRepository productRepository;
    private final ConcurrentMap<Integer, ReentrantLock> supplierStockUpdateLocks = new ConcurrentHashMap<>();

    public SupplierService(SupplierRepository supplierRepository, ProductRepository productRepository) {
        this.supplierRepository = supplierRepository;
        this.productRepository = productRepository;
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

    public Set<Product> getSupplierProducts(Integer supplierId) {
        if (supplierId == null) {
            throw new InvalidArgumentException("Supplier ID cannot be null");
        }
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new SupplierNotFoundException("Supplier not found with id: " + supplierId));

        return supplier.getProducts();
    }

    public Supplier addProductToSupplier(Integer supplierId, Integer productId) {
        if (supplierId == null || productId == null) {
            throw new InvalidArgumentException("Supplier ID and Product ID cannot be null");
        }

        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new SupplierNotFoundException("Supplier not found with id: " + supplierId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + productId));

        supplier.addProduct(product);
        return supplierRepository.save(supplier);
    }

    public Supplier removeProductFromSupplier(Integer supplierId, Integer productId) {
        if (supplierId == null || productId == null) {
            throw new InvalidArgumentException("Supplier ID and Product ID cannot be null");
        }

        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new SupplierNotFoundException("Supplier not found with id: " + supplierId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + productId));

        supplier.removeProduct(product);
        return supplierRepository.save(supplier);
    }

    @Transactional
    public int increaseProductsStockBySupplier(Integer supplierId, Integer amount) {
        if (supplierId == null) {
            throw new InvalidArgumentException("Supplier ID cannot be null");
        }
        if (amount == null || amount <= 0) {
            throw new InvalidArgumentException("Amount must be a positive integer");
        }
        if (!supplierRepository.existsById(supplierId)) {
            throw new SupplierNotFoundException("Supplier not found with id: " + supplierId);
        }

        boolean hasLowStockProducts = productRepository
                .countLowStockProductsBySupplierId(supplierId, LOW_STOCK_THRESHOLD) > 0;

        if (!hasLowStockProducts) {
            return productRepository.increaseStockBySupplierId(supplierId, amount);
        }

        ReentrantLock lock = supplierStockUpdateLocks
                .computeIfAbsent(supplierId, key -> new ReentrantLock());

        if (!lock.tryLock()) {
            throw new SupplierLockedException(
                    "Supplier " + supplierId
                    + " has low-stock products (< " + LOW_STOCK_THRESHOLD
                    + "). Only one stock update is allowed at a time.");
        }

        try {
            return productRepository.increaseStockBySupplierId(supplierId, amount);
        } finally {
            lock.unlock();
        }
    }
}
