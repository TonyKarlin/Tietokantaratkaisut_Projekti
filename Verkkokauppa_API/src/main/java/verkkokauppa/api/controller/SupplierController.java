package verkkokauppa.api.controller;

import java.util.Set;
import java.util.stream.Collectors;

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
import verkkokauppa.api.dtos.SupplierDTO;
import verkkokauppa.api.dtos.SupplierRequest;
import verkkokauppa.api.entity.Supplier;
import verkkokauppa.api.service.SupplierService;
import verkkokauppa.api.utility.LoggerUtil;
import verkkokauppa.api.utility.assemblers.SupplierModelAssembler;

@RestController
@RequestMapping("/suppliers")
public class SupplierController {

    private final SupplierService supplierService;
    private final SupplierModelAssembler assembler;

    public SupplierController(SupplierService supplierService, SupplierModelAssembler assembler) {
        this.supplierService = supplierService;
        this.assembler = assembler;
    }

    @GetMapping
    public ResponseEntity<Page<SupplierDTO>> getAllSuppliers(@PageableDefault(size = 50) Pageable pageable) {
        Page<SupplierDTO> page = supplierService.getSuppliersPage(pageable)
                .map(SupplierDTO::new);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<SupplierDTO>> getSupplierById(@PathVariable Integer id) {
        LoggerUtil.logInfo("---FETCHING SUPPLIER WITH ID: " + id + "---");

        return supplierService.getSupplierById(id)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<SupplierDTO> postSupplier(@RequestBody SupplierRequest request) {
        LoggerUtil.logInfo("---ADDING NEW SUPPLIER: " + request.name() + "---");
        Supplier savedSupplier = supplierService.postSupplier(request);
        LoggerUtil.logInfo("---SUPPLIER ADDED SUCCESSFULLY WITH ID: " + savedSupplier.getId() + "---");

        EntityModel<SupplierDTO> supplierModel = assembler.toModel(savedSupplier);
        return ResponseEntity.created(supplierModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(supplierModel.getContent());
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupplierDTO> updateSupplier(@PathVariable Integer id,
            @RequestBody SupplierRequest request) {
        LoggerUtil.logInfo("---UPDATING SUPPLIER WITH ID: " + id + "---");
        Supplier updatedSupplier = supplierService.updateSupplier(id, request);
        LoggerUtil.logInfo("---SUPPLIER WITH ID: " + id + " UPDATED SUCCESSFULLY---");

        EntityModel<SupplierDTO> supplierModel = assembler.toModel(updatedSupplier);
        return ResponseEntity.ok()
                .location(supplierModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(supplierModel.getContent());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplierById(@PathVariable Integer id) {
        LoggerUtil.logInfo("---DELETING SUPPLIER WITH ID: " + id + "---");
        supplierService.deleteSupplier(id);
        LoggerUtil.logInfo("---SUPPLIER WITH ID: " + id + " DELETED SUCCESSFULLY---");
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/products")
    public ResponseEntity<Set<ProductDTO>> getSupplierProducts(@PathVariable Integer id) {
        LoggerUtil.logInfo("---FETCHING PRODUCTS FOR SUPPLIER ID: " + id + "---");
        Set<ProductDTO> products = supplierService.getSupplierProducts(id)
                .stream()
                .map(ProductDTO::new)
                .collect(Collectors.toSet());
        return ResponseEntity.ok(products);
    }

    @PostMapping("/{supplierId}/products/{productId}")
    public ResponseEntity<SupplierDTO> addProductToSupplier(
            @PathVariable Integer supplierId,
            @PathVariable Integer productId) {
        LoggerUtil.logInfo("---ADDING PRODUCT " + productId + " TO SUPPLIER " + supplierId + "---");
        Supplier updatedSupplier = supplierService.addProductToSupplier(supplierId, productId);
        return ResponseEntity.ok(new SupplierDTO(updatedSupplier));
    }

    @DeleteMapping("/{supplierId}/products/{productId}")
    public ResponseEntity<SupplierDTO> removeProductFromSupplier(
            @PathVariable Integer supplierId,
            @PathVariable Integer productId) {
        LoggerUtil.logInfo("---REMOVING PRODUCT " + productId + " FROM SUPPLIER " + supplierId + "---");
        Supplier updatedSupplier = supplierService.removeProductFromSupplier(supplierId, productId);
        return ResponseEntity.ok(new SupplierDTO(updatedSupplier));
    }
}
