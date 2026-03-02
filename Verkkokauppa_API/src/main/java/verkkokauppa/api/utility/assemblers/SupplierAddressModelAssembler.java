package verkkokauppa.api.utility.assemblers;

import org.jspecify.annotations.NullMarked;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Component;

import verkkokauppa.api.controller.SupplierAddressController;
import verkkokauppa.api.dtos.SupplierAddressDTO;
import verkkokauppa.api.entity.SupplierAddress;

@NullMarked
@Component
public class SupplierAddressModelAssembler implements RepresentationModelAssembler<SupplierAddress, EntityModel<SupplierAddressDTO>> {

    @Override
    public EntityModel<SupplierAddressDTO> toModel(SupplierAddress address) {
        SupplierAddressDTO dto = new ModelMapper().map(address, SupplierAddressDTO.class);
        return EntityModel.of(
                dto,
                linkTo(methodOn(SupplierAddressController.class).getSupplierAddressById(address.getId())).withSelfRel(),
                linkTo(methodOn(SupplierAddressController.class).getAllSupplierAddresses(Pageable.unpaged())).withRel("supplieraddresses")
        );
    }
}
