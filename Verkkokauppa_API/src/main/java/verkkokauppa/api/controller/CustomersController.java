package verkkokauppa.api.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import verkkokauppa.api.dtos.CustomerDTO;
import verkkokauppa.api.dtos.CustomerRequest;
import verkkokauppa.api.entity.Customers;
import verkkokauppa.api.service.CustomersService;
import verkkokauppa.api.utility.CustomerModelAssembler;
import verkkokauppa.api.utility.LoggerUtil;

@RestController
@RequestMapping("/customers")
public class CustomersController {

    private final CustomersService customersService;
    private final CustomerModelAssembler assembler;

    public CustomersController(CustomersService customersService, CustomerModelAssembler assembler) {
        this.customersService = customersService;
        this.assembler = assembler;
    }
    // metodit palauttavat RESTFUL-linkkejä vastauksessa HATEOAS:n (EntityModel) avulla
    // yleensä linkki itseensä ja linkkejä liittyviin resursseihin

    @GetMapping
    public ResponseEntity<Page<CustomerDTO>> getAllCustomers(@PageableDefault(size = 50) Pageable pageable) {
        Page<CustomerDTO> page = customersService.getCustomersPage(pageable)
                .map(CustomerDTO::new);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Customers>> getCustomerById(@PathVariable Integer id) {
        LoggerUtil.logInfo("---FETCHING CUSTOMER WITH ID: " + id + "---");
        return customersService.getCustomerById(id)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CustomerDTO> postCustomer(@RequestBody CustomerRequest request) {
        LoggerUtil.logInfo("---ADDING NEW CUSTOMER: " + request.firstName() + " " + request.lastName() + "---");
        Customers savedCustomer = customersService.postCustomer(request);
        LoggerUtil.logInfo("---CUSTOMER ADDED SUCCESSFULLY WITH ID: " + savedCustomer.getId() + "---");

        EntityModel<Customers> customerModel = assembler.toModel(savedCustomer);
        CustomerDTO customerDTO = new CustomerDTO(savedCustomer);
        return ResponseEntity.created(customerModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(customerDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable Integer id,
                                                      @RequestBody CustomerRequest request) {
        LoggerUtil.logInfo("---UPDATING CUSTOMER WITH ID: " + id + "---");
        Customers updatedCustomer = customersService.updateCustomer(id, request);
        LoggerUtil.logInfo("---CUSTOMER WITH ID: " + id + " UPDATED SUCCESSFULLY---");

        EntityModel<Customers> customerModel = assembler.toModel(updatedCustomer);
        CustomerDTO customerDTO = new CustomerDTO(updatedCustomer);
        return ResponseEntity.ok()
                .location(customerModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(customerDTO);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomerById(@PathVariable Integer id) {
        LoggerUtil.logInfo("---DELETING CUSTOMER WITH ID: " + id + "---");
        customersService.deleteCustomerById(id);
        LoggerUtil.logInfo("---CUSTOMER WITH ID: " + id + " DELETED SUCCESSFULLY---");
        return ResponseEntity.noContent().build();
    }
}
