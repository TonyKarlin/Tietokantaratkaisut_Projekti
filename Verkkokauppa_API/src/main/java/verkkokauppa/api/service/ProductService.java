package verkkokauppa.api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import verkkokauppa.api.dtos.productDTOs.ProductRequest;
import verkkokauppa.api.dtos.productDTOs.ProductSearchRequest;
import verkkokauppa.api.entity.Product;
import verkkokauppa.api.entity.ProductCategory;
import verkkokauppa.api.entity.Supplier;
import verkkokauppa.api.repository.ProductCategoryRepository;
import verkkokauppa.api.repository.ProductRepository;
import verkkokauppa.api.repository.SupplierRepository;
import verkkokauppa.api.utility.exceptions.custom_exceptions.InvalidArgumentException;
import verkkokauppa.api.utility.exceptions.custom_exceptions.ProductCategoryNotFoundException;
import verkkokauppa.api.utility.exceptions.custom_exceptions.ProductNotFoundException;
import verkkokauppa.api.utility.exceptions.custom_exceptions.SupplierNotFoundException;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final SupplierRepository supplierRepository;
    private final EntityManager entityManager;

    public ProductService(ProductRepository productRepository, ProductCategoryRepository productCategoryRepository,
            SupplierRepository supplierRepository, EntityManager entityManager) {
        this.productRepository = productRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.supplierRepository = supplierRepository;
        this.entityManager = entityManager;
    }

    public Page<Product> getProductsPage(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Optional<Product> getProductById(Integer id) {
        if (id == null) {
            throw new InvalidArgumentException("ID cannot be null");
        }
        return productRepository.findById(id);
    }

    public Product postProducts(ProductRequest product) {
        if (product == null) {
            throw new InvalidArgumentException("Product cannot be null");
        }
        if (!isValidProductRequest(product)) {
            throw new InvalidArgumentException("Invalid product data: "
                    + "All fields must be non-empty");
        }

        // Haetaan kategoria ID:n perusteella
        ProductCategory category = productCategoryRepository.findById(product.categoryId())
                .orElseThrow(() -> new ProductCategoryNotFoundException(
                "Product category not found with id: " + product.categoryId()));

        Product newProduct = new Product(
                product.name(),
                product.description(),
                product.price(),
                product.stockQuantity(),
                product.supplierId()
        );

        // Asetetaan kategoria-objekti
        newProduct.setCategory(category);

        return productRepository.save(newProduct);
    }

    public Product updateProduct(Integer id, ProductRequest updateRequest) {
        if (id == null) {
            throw new InvalidArgumentException("ID cannot be null");
        }
        if (updateRequest == null) {
            throw new InvalidArgumentException("Update request cannot be null");
        }
        if (!isValidProductRequest(updateRequest)) {
            throw new InvalidArgumentException("Invalid update request:");
        }
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));

        // Haetaan uusi kategoria jos ID on muuttunut
        if (updateRequest.categoryId() != null
                && (existingProduct.getCategory() == null
                || !existingProduct.getCategory().getId().equals(updateRequest.categoryId()))) {
            ProductCategory category = productCategoryRepository.findById(updateRequest.categoryId())
                    .orElseThrow(() -> new ProductCategoryNotFoundException(
                    "Product category not found with id: " + updateRequest.categoryId()));
            existingProduct.setCategory(category);
        }

        existingProduct.setName(updateRequest.name());
        existingProduct.setDescription(updateRequest.description());
        existingProduct.setPrice(updateRequest.price());
        existingProduct.setStockQuantity(updateRequest.stockQuantity());
        existingProduct.setSupplierId(updateRequest.supplierId());

        return productRepository.save(existingProduct);
    }

    public void deleteProductById(Integer id) {
        if (id == null) {
            throw new InvalidArgumentException("ID cannot be null");
        }
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
        productRepository.delete(product);
    }

    private boolean isValidProductRequest(ProductRequest product) {
        return (product.name() != null && !product.name().isBlank())
                && (product.description() != null && !product.description().isBlank())
                && (product.price() != null)
                && (product.stockQuantity() != null)
                && (product.categoryId() != null);
        // supplierId voi olla null (valinnainen)
    }

    public Set<Supplier> getProductSuppliers(Integer productId) {
        if (productId == null) {
            throw new InvalidArgumentException("Product ID cannot be null");
        }
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + productId));

        return product.getSuppliers();
    }

    public Product addSupplierToProduct(Integer productId, Integer supplierId) {
        if (productId == null || supplierId == null) {
            throw new InvalidArgumentException("Product ID and Supplier ID cannot be null");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + productId));

        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new SupplierNotFoundException("Supplier not found with id: " + supplierId));

        product.addSupplier(supplier);
        return productRepository.save(product);
    }

    public Product removeSupplierFromProduct(Integer productId, Integer supplierId) {
        if (productId == null || supplierId == null) {
            throw new InvalidArgumentException("Product ID and Supplier ID cannot be null");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + productId));

        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new SupplierNotFoundException("Supplier not found with id: " + supplierId));

        product.removeSupplier(supplier);
        return productRepository.save(product);
    }

    public List<Product> searchProductsByCriteria(ProductSearchRequest request) {
        if (request == null) {
            throw new InvalidArgumentException("Search request cannot be null");
        }

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
        Root<Product> productRoot = criteriaQuery.from(Product.class);

        List<Predicate> predicates = new ArrayList<>();

        if (request.nameContains() != null && !request.nameContains().isBlank()) {
            predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(productRoot.get("name")),
                    "%" + request.nameContains().toLowerCase() + "%"
            ));
        }
        if (request.minPrice() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(productRoot.get("price"), request.minPrice()));
        }
        if (request.maxPrice() != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(productRoot.get("price"), request.maxPrice()));
        }
        if (request.minStock() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(productRoot.get("stockQuantity"), request.minStock()));
        }
        if (request.categoryId() != null) {
            predicates.add(criteriaBuilder.equal(productRoot.get("category").get("id"), request.categoryId()));
        }
        if (request.supplierId() != null) {
            predicates.add(criteriaBuilder.equal(productRoot.get("supplierId"), request.supplierId()));
        }

        criteriaQuery.select(productRoot)
                .where(predicates.toArray(Predicate[]::new))
                .orderBy(criteriaBuilder.asc(productRoot.get("id")));

        return entityManager.createQuery(criteriaQuery).getResultList();
    }
}
