package verkkokauppa.api.controller;

import java.util.List;
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

import verkkokauppa.api.dtos.SupplierAddressDTO;
import verkkokauppa.api.dtos.SupplierAddressRequest;
import verkkokauppa.api.entity.SupplierAddress;
import verkkokauppa.api.service.SupplierAddressService;
import verkkokauppa.api.utility.LoggerUtil;
import verkkokauppa.api.utility.assemblers.SupplierAddressModelAssembler;

@RestController
@RequestMapping("/supplieraddresses")
public class SupplierAddressController {

    private final SupplierAddressService service;
    private final SupplierAddressModelAssembler assembler;

    public SupplierAddressController(SupplierAddressService service,
            SupplierAddressModelAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @GetMapping
    public ResponseEntity<Page<SupplierAddressDTO>> getAllSupplierAddresses(
            @PageableDefault(size = 50) Pageable pageable) {
        Page<SupplierAddressDTO> page = service.getAddressesPage(pageable)
                .map(SupplierAddressDTO::new);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<SupplierAddressDTO>> getSupplierAddressById(@PathVariable Integer id) {
        LoggerUtil.logInfo("---FETCHING SUPPLIER ADDRESS WITH ID: " + id + "---");

        return service.getAddressById(id)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/supplier/{supplierId}")
    public ResponseEntity<List<SupplierAddressDTO>> getAddressesBySupplier(@PathVariable Integer supplierId) {
        LoggerUtil.logInfo("---FETCHING ADDRESSES FOR SUPPLIER ID: " + supplierId + "---");

        List<SupplierAddressDTO> addresses = service.getAddressesBySupplierId(supplierId)
                .stream()
                .map(SupplierAddressDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(addresses);
    }

    @PostMapping
    public ResponseEntity<SupplierAddressDTO> postSupplierAddress(@RequestBody SupplierAddressRequest request) {
        LoggerUtil.logInfo("---ADDING NEW SUPPLIER ADDRESS---");
        SupplierAddress savedAddress = service.postAddress(request);
        LoggerUtil.logInfo("---SUPPLIER ADDRESS ADDED SUCCESSFULLY WITH ID: " + savedAddress.getId() + "---");

        EntityModel<SupplierAddressDTO> addressModel = assembler.toModel(savedAddress);
        return ResponseEntity.created(addressModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(addressModel.getContent());
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupplierAddressDTO> updateSupplierAddress(
            @PathVariable Integer id,
            @RequestBody SupplierAddressRequest request) {
        LoggerUtil.logInfo("---UPDATING SUPPLIER ADDRESS WITH ID: " + id + "---");
        SupplierAddress updatedAddress = service.updateAddress(id, request);
        LoggerUtil.logInfo("---SUPPLIER ADDRESS WITH ID: " + id + " UPDATED SUCCESSFULLY---");

        EntityModel<SupplierAddressDTO> addressModel = assembler.toModel(updatedAddress);
        return ResponseEntity.ok()
                .location(addressModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(addressModel.getContent());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplierAddressById(@PathVariable Integer id) {
        LoggerUtil.logInfo("---DELETING SUPPLIER ADDRESS WITH ID: " + id + "---");
        service.deleteAddress(id);
        LoggerUtil.logInfo("---SUPPLIER ADDRESS WITH ID: " + id + " DELETED SUCCESSFULLY---");
        return ResponseEntity.noContent().build();
    }
}
