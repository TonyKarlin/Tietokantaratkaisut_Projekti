package verkkokauppa.api.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import verkkokauppa.api.dtos.ProductDTO;
import verkkokauppa.api.dtos.ProductRequest;
import verkkokauppa.api.entity.Product;
import verkkokauppa.api.service.ProductService;
import verkkokauppa.api.utility.LoggerUtil;
import verkkokauppa.api.utility.ProductModelAssembler;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final ProductModelAssembler assembler;

    public ProductController(ProductService productService, ProductModelAssembler assembler) {
        this.productService = productService;
        this.assembler = assembler;
    }

    @GetMapping
    public ResponseEntity<Page<ProductDTO>> getAllProducts(@PageableDefault(size = 50) Pageable pageable) {
        Page<ProductDTO> page = productService.getProductsPage(pageable)
                .map(ProductDTO::new);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Product>> getProductById(@PathVariable Integer id) {
        LoggerUtil.logInfo("---FETCHING PRODUCT WITH ID: " + id + "---");

        return productService.getProductById(id)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProductDTO> postProduct(@RequestBody ProductRequest request) {
        LoggerUtil.logInfo("---ADDING NEW PRODUCT: " + request.name() + "---");
        Product savedProduct = productService.postProducts(request);
        LoggerUtil.logInfo("---PRODUCT ADDED SUCCESSFULLY WITH ID: " + savedProduct.getId() + "---");

        EntityModel<Product> productModel = assembler.toModel(savedProduct);
        ProductDTO productDTO = new ProductDTO(savedProduct);
        return ResponseEntity.created(productModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(productDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Integer id,
            @RequestBody ProductRequest request) {
        LoggerUtil.logInfo("---UPDATING PRODUCT WITH ID: " + id + "---");
        Product updatedProduct = productService.updateProduct(id, request);
        LoggerUtil.logInfo("---PRODUCT WITH ID: " + id + " UPDATED SUCCESSFULLY---");

        EntityModel<Product> productModel = assembler.toModel(updatedProduct);
        ProductDTO productDTO = new ProductDTO(updatedProduct);
        return ResponseEntity.ok()
                .location(productModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(productDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductById(@PathVariable Integer id) {
        LoggerUtil.logInfo("---DELETING PRODUCT WITH ID: " + id + "---");
        productService.deleteCustomerById(id);
        LoggerUtil.logInfo("---PRODUCT WITH ID: " + id + " DELETED SUCCESSFULLY---");
        return ResponseEntity.noContent().build();
    }
}
