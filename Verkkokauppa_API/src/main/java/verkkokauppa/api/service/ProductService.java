package verkkokauppa.api.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import verkkokauppa.api.dtos.ProductRequest;
import verkkokauppa.api.entity.Product;
import verkkokauppa.api.repository.ProductRepository;
import verkkokauppa.api.utility.exceptions.custom_exceptions.InvalidArgumentException;
import verkkokauppa.api.utility.exceptions.custom_exceptions.ProductNotFoundException;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
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
        Product newProduct = new Product(
                product.name(),
                product.description(),
                product.price(),
                product.stockQuantity(),
                product.categoryId(),
                product.supplierId()
        );

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

        existingProduct.setName(updateRequest.name());
        existingProduct.setDescription(updateRequest.description());
        existingProduct.setPrice(updateRequest.price());
        existingProduct.setStockQuantity(updateRequest.stockQuantity());
        existingProduct.setCategoryId(updateRequest.categoryId());
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
                && (product.categoryId() != null)
                && (product.supplierId() != null);
    }
}
