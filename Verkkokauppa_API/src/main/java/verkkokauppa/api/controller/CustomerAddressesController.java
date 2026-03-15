package verkkokauppa.api.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import verkkokauppa.api.dtos.addressDTOs.AddressDTO;
import verkkokauppa.api.dtos.addressDTOs.AddressRequest;
import verkkokauppa.api.entity.CustomerAddress;
import verkkokauppa.api.service.CustomerAddressesService;
import verkkokauppa.api.utility.assemblers.AddressModelAssembler;

@RestController
@RequestMapping("/customer-addresses")
public class CustomerAddressesController {
    private final CustomerAddressesService addressService;
    private final AddressModelAssembler assembler;

    public CustomerAddressesController(
            CustomerAddressesService addressService,
            AddressModelAssembler assembler) {
        this.addressService = addressService;
        this.assembler = assembler;
    }

    @GetMapping
    public ResponseEntity<Page<AddressDTO>> getAllCustomerAddresses(@PageableDefault(size = 50)Pageable pageable) {
        Page<AddressDTO> addressPage = addressService.getPage(pageable)
                .map(AddressDTO::new);
    return ResponseEntity.ok(addressPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<AddressDTO>> getCustomerAddressById(@PathVariable Integer id) {
        CustomerAddress address = addressService.getByIdOrThrow(id);
        return ResponseEntity.ok(
                assembler.toModel(address));
    }

    @PostMapping
    public ResponseEntity<AddressDTO> postCustomerAddress(@RequestBody AddressRequest request) {
        if (request == null) {
            return ResponseEntity.noContent().build();
        }
        CustomerAddress address = addressService.postRequest(request);

        EntityModel<AddressDTO> dtoModel = assembler.toModel(address);
        return ResponseEntity.created(dtoModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(dtoModel.getContent());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddressDTO> updateCustomerAddress(@PathVariable Integer id,
                                                         @RequestBody AddressRequest request) {
        CustomerAddress address = addressService.putRequest(id, request);
        EntityModel<AddressDTO> dtoModel = assembler.toModel(address);
        return ResponseEntity.ok()
                .location(dtoModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(dtoModel.getContent());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomerAddress(@PathVariable Integer id) {
        addressService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
