package verkkokauppa.api.utility;

import org.jspecify.annotations.NullMarked;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import verkkokauppa.api.controller.CustomersController;
import verkkokauppa.api.entity.Customers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@NullMarked
@Component
public class CustomerModelAssembler implements RepresentationModelAssembler<Customers, EntityModel<Customers>> {

    @Override
    public EntityModel<Customers> toModel(Customers customer) {
        return EntityModel.of(
                customer,
                linkTo(methodOn(CustomersController.class).getCustomerById(customer.getId())).withSelfRel(),
                linkTo(methodOn(CustomersController.class).getAllCustomers(Pageable.unpaged())).withRel("customers")
        );
    }
}