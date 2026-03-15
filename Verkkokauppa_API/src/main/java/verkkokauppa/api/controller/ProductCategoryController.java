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

import verkkokauppa.api.dtos.categoryDTOs.ProductCategoryDTO;
import verkkokauppa.api.dtos.categoryDTOs.ProductCategoryRequest;
import verkkokauppa.api.entity.ProductCategory;
import verkkokauppa.api.service.ProductCategoryService;
import verkkokauppa.api.utility.LoggerUtil;
import verkkokauppa.api.utility.assemblers.ProductCategoryModelAssembler;

@RestController
@RequestMapping("/productcategories")
public class ProductCategoryController {

    private final ProductCategoryService productCategoryService;
    private final ProductCategoryModelAssembler assembler;

    public ProductCategoryController(ProductCategoryService productCategoryService, ProductCategoryModelAssembler assembler) {
        this.productCategoryService = productCategoryService;
        this.assembler = assembler;
    }

    @GetMapping
    public ResponseEntity<Page<ProductCategoryDTO>> getAllProductCategories(@PageableDefault(size = 50) Pageable pageable) {
        Page<ProductCategoryDTO> page = productCategoryService.getProductCategoriesPage(pageable)
                .map(ProductCategoryDTO::new);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ProductCategoryDTO>> getProductCategoryById(@PathVariable Integer id) {
        LoggerUtil.logInfo("---FETCHING PRODUCT CATEGORY WITH ID: " + id + "---");

        return productCategoryService.getProductCategoryById(id)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Haetaan tietyn kategorian kaikki tuotteet
    @GetMapping("/{id}/products")
    public ResponseEntity<ProductCategory> getProductCategoryWithProducts(@PathVariable Integer id) {
        LoggerUtil.logInfo("---FETCHING PRODUCT CATEGORY WITH PRODUCTS, ID: " + id + "---");

        return productCategoryService.getProductCategoryWithProducts(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProductCategoryDTO> postProductCategory(@RequestBody ProductCategoryRequest request) {
        LoggerUtil.logInfo("---ADDING NEW PRODUCT CATEGORY: " + request.name() + "---");
        ProductCategory savedProductCategory = productCategoryService.postProductCategory(request);
        LoggerUtil.logInfo("---PRODUCT CATEGORY ADDED SUCCESSFULLY WITH ID: " + savedProductCategory.getId() + "---");

        EntityModel<ProductCategoryDTO> productCategoryModel = assembler.toModel(savedProductCategory);
        return ResponseEntity.created(productCategoryModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(productCategoryModel.getContent());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductCategoryDTO> updateProductCategory(@PathVariable Integer id,
            @RequestBody ProductCategoryRequest request) {
        LoggerUtil.logInfo("---UPDATING PRODUCT CATEGORY WITH ID: " + id + "---");
        ProductCategory updatedProductCategory = productCategoryService.updateProductCategory(id, request);
        LoggerUtil.logInfo("---PRODUCT CATEGORY WITH ID: " + id + " UPDATED SUCCESSFULLY---");

        EntityModel<ProductCategoryDTO> productCategoryModel = assembler.toModel(updatedProductCategory);
        return ResponseEntity.ok()
                .location(productCategoryModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(productCategoryModel.getContent());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductCategoryById(@PathVariable Integer id) {
        LoggerUtil.logInfo("---DELETING PRODUCT CATEGORY WITH ID: " + id + "---");
        productCategoryService.deleteProductCategoryById(id);
        LoggerUtil.logInfo("---PRODUCT CATEGORY WITH ID: " + id + " DELETED SUCCESSFULLY---");
        return ResponseEntity.noContent().build();
    }
}
