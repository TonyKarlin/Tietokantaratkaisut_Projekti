package verkkokauppa.api.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import verkkokauppa.api.dtos.AddressDTO;
import verkkokauppa.api.service.CustomerAddressesService;
import verkkokauppa.api.utility.AddressModelAssembler;

@RestController
@RequestMapping("/addresses")
public class CustomerAddressesController {
    private final CustomerAddressesService service;
    private final AddressModelAssembler assembler;

    public CustomerAddressesController(
            CustomerAddressesService service,
            AddressModelAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @GetMapping
    public ResponseEntity<Page<AddressDTO>> getAllCustomerAddresses(@PageableDefault(size = 50)Pageable
                                                                    pageable) {
    return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<AddressDTO>> getCustomerAddressById(@PathVariable Integer id) {
        return service.getAddressById(id)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
