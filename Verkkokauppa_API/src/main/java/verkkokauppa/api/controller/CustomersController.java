package verkkokauppa.api.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import verkkokauppa.api.dtos.customerDTOs.CustomerDTO;
import verkkokauppa.api.dtos.customerDTOs.CustomerRequest;
import verkkokauppa.api.entity.Customer;
import verkkokauppa.api.service.CustomersService;
import verkkokauppa.api.utility.assemblers.CustomerModelAssembler;
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
        Page<CustomerDTO> customerPage = customersService.getPage(pageable)
                .map(CustomerDTO::new);
        return ResponseEntity.ok(customerPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<CustomerDTO>> getCustomerById(@PathVariable Integer id) {

        LoggerUtil.logInfo("---FETCHING CUSTOMER WITH ID: " + id + "---");
        Customer customer =
                customersService.getByIdOrThrow(id);

        return ResponseEntity.ok(
                assembler.toModel(customer)
        );
    }

    @PostMapping
    public ResponseEntity<CustomerDTO> postCustomer(@RequestBody CustomerRequest request) {
        if (request == null) {
            return ResponseEntity.noContent().build();
        }
        LoggerUtil.logInfo("---ADDING NEW CUSTOMER: " + request.firstName() + " " + request.lastName() + "---");
        Customer savedCustomer = customersService.postRequest(request);
        LoggerUtil.logInfo("---CUSTOMER ADDED SUCCESSFULLY WITH ID: " + savedCustomer.getId() + "---");

        EntityModel<CustomerDTO> dtoModel = assembler.toModel(savedCustomer);
        return ResponseEntity.created(dtoModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(dtoModel.getContent());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable Integer id,
                                                      @RequestBody CustomerRequest request) {
        LoggerUtil.logInfo("---UPDATING CUSTOMER WITH ID: " + id + "---");
        Customer updatedCustomer = customersService.putRequest(id, request);
        LoggerUtil.logInfo("---CUSTOMER WITH ID: " + id + " UPDATED SUCCESSFULLY---");

        EntityModel<CustomerDTO> dtoModel = assembler.toModel(updatedCustomer);
        return ResponseEntity.ok()
                .location(dtoModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(dtoModel.getContent());

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomerById(@PathVariable Integer id) {
        LoggerUtil.logInfo("---DELETING CUSTOMER WITH ID: " + id + "---");
        customersService.delete(id);
        LoggerUtil.logInfo("---CUSTOMER WITH ID: " + id + " DELETED SUCCESSFULLY---");
        return ResponseEntity.noContent().build();
    }
}
