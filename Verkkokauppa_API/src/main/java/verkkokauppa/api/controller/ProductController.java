package verkkokauppa.api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import verkkokauppa.api.entity.Product;
import verkkokauppa.api.repository.ProductRepository;
import verkkokauppa.api.utility.LoggerUtil;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // GET 
    @GetMapping
    public List<Product> getAllProducts() {
        LoggerUtil.logInfo("---FETCHING ALL PRODUCTS---");
        return productRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Integer id) {
        LoggerUtil.logInfo("---FETCHING PRODUCT WITH ID: " + id + "---");

        return productRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST
    @PostMapping
    public Product addProduct(@RequestBody Product product) {
        LoggerUtil.logInfo("---ADDING NEW PRODUCT: " + product.getName() + "---");
        Product savedProduct = productRepository.save(product);
        LoggerUtil.logInfo("---PRODUCT ADDED SUCCESSFULLY WITH ID: " + savedProduct.getId() + "---");
        return savedProduct;
    }

}
