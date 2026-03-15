package verkkokauppa.api.utility.assemblers;

import org.jspecify.annotations.NullMarked;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import verkkokauppa.api.controller.CustomerAddressesController;
import verkkokauppa.api.dtos.addressDTOs.AddressDTO;
import verkkokauppa.api.entity.CustomerAddress;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@NullMarked
@Component
public class AddressModelAssembler implements RepresentationModelAssembler<CustomerAddress, EntityModel<AddressDTO>> {
    @Override
    public EntityModel<AddressDTO> toModel(CustomerAddress address) {
        AddressDTO dto = new ModelMapper().map(address, AddressDTO.class);
        return EntityModel.of(
                dto,
                linkTo(methodOn(CustomerAddressesController.class).getCustomerAddressById(address.getId())).withSelfRel(),
                linkTo(methodOn(CustomerAddressesController.class).getAllCustomerAddresses(Pageable.unpaged())).withRel("addresses")
        );
    }
}
