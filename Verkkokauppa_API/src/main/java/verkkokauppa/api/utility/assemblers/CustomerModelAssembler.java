package verkkokauppa.api.utility.assemblers;

import org.jspecify.annotations.NullMarked;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import verkkokauppa.api.controller.CustomersController;
import verkkokauppa.api.dtos.customerDTOs.CustomerDTO;
import verkkokauppa.api.entity.Customer;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@NullMarked
@Component
public class CustomerModelAssembler implements RepresentationModelAssembler<Customer, EntityModel<CustomerDTO>> {

    @Override
    public EntityModel<CustomerDTO> toModel(Customer customer) {
        CustomerDTO dto = new ModelMapper().map(customer, CustomerDTO.class);
        return EntityModel.of(
                dto,
                linkTo(methodOn(CustomersController.class).getCustomerById(customer.getId())).withSelfRel(),
                linkTo(methodOn(CustomersController.class).getAllCustomers(Pageable.unpaged())).withRel("customers")
        );
    }
}