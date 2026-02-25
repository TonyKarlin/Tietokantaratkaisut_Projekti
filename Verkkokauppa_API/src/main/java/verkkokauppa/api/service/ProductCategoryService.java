package verkkokauppa.api.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import verkkokauppa.api.dtos.ProductCategoryRequest;
import verkkokauppa.api.entity.ProductCategory;
import verkkokauppa.api.repository.ProductCategoryRepository;
import verkkokauppa.api.utility.LoggerUtil;
import verkkokauppa.api.utility.exceptions.custom_exceptions.InvalidArgumentException;
import verkkokauppa.api.utility.exceptions.custom_exceptions.ProductCategoryNotFoundException;

@Service
public class ProductCategoryService {

    private final ProductCategoryRepository productCategoryRepository;

    public ProductCategoryService(ProductCategoryRepository productCategoryRepository) {
        this.productCategoryRepository = productCategoryRepository;
    }

    public Page<ProductCategory> getProductCategoriesPage(Pageable pageable) {
        return productCategoryRepository.findAll(pageable);
    }

    public Optional<ProductCategory> getProductCategoryById(Integer id) {
        if (id == null) {
            throw new InvalidArgumentException("ID cannot be null");
        }
        return productCategoryRepository.findById(id);
    }

    // Kategorian kaikki tuotteet --LAzy loading
    public Optional<ProductCategory> getProductCategoryWithProducts(Integer id) {
        if (id == null) {
            throw new InvalidArgumentException("ID cannot be null");
        }
        Optional<ProductCategory> category = productCategoryRepository.findById(id);

        // Tuotteet ladataan vasta kun niitä käytetään (LAZY loading)
        // Tämä getProducts()-kutsu pakottaa tuotteiden lataamisen
        category.ifPresent(c -> {
            LoggerUtil.logInfo("Ladataan kategorian " + c.getName() + " tuotteet (LAZY loading)...");
            int productCount = c.getProducts().size();
            LoggerUtil.logInfo("Kategoriassa on " + productCount + " tuotetta");
        });

        return category;
    }

    public ProductCategory postProductCategory(ProductCategoryRequest productCategory) {
        if (productCategory == null) {
            throw new InvalidArgumentException("Product cannot be null");
        }
        if (!isValidProductCategoryRequest(productCategory)) {
            throw new InvalidArgumentException("Invalid product category data: "
                    + "All fields must be non-empty");
        }
        ProductCategory newProductCategory = new ProductCategory(
                productCategory.name(),
                productCategory.description()
        );

        return productCategoryRepository.save(newProductCategory);
    }

    public ProductCategory updateProductCategory(Integer id, ProductCategoryRequest updateRequest) {
        if (id == null) {
            throw new InvalidArgumentException("ID cannot be null");
        }
        if (updateRequest == null) {
            throw new InvalidArgumentException("Update request cannot be null");
        }
        if (!isValidProductCategoryRequest(updateRequest)) {
            throw new InvalidArgumentException("Invalid update request:");
        }
        ProductCategory existingProductCategory = productCategoryRepository.findById(id)
                .orElseThrow(() -> new ProductCategoryNotFoundException("Product category not found with id: " + id));

        existingProductCategory.setName(updateRequest.name());
        existingProductCategory.setDescription(updateRequest.description());

        return productCategoryRepository.save(existingProductCategory);
    }

    public void deleteProductCategoryById(Integer id) {
        if (id == null) {
            throw new InvalidArgumentException("ID cannot be null");
        }
        ProductCategory productCategory = productCategoryRepository.findById(id)
                .orElseThrow(() -> new ProductCategoryNotFoundException("Product category not found with id: " + id));
        productCategoryRepository.delete(productCategory);
    }

    private boolean isValidProductCategoryRequest(ProductCategoryRequest product) {
        return (product.name() != null && !product.name().isBlank())
                && (product.description() != null && !product.description().isBlank());

    }
}
