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
}
