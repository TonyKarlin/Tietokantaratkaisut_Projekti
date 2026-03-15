package verkkokauppa.api.utility.assemblers;

import org.jspecify.annotations.NullMarked;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Component;

import verkkokauppa.api.controller.SupplierController;
import verkkokauppa.api.dtos.supplierDTOs.SupplierDTO;
import verkkokauppa.api.entity.Supplier;

@NullMarked
@Component
public class SupplierModelAssembler implements RepresentationModelAssembler<Supplier, EntityModel<SupplierDTO>> {

    @Override
    public EntityModel<SupplierDTO> toModel(Supplier supplier) {
        SupplierDTO dto = new ModelMapper().map(supplier, SupplierDTO.class);
        return EntityModel.of(
                dto,
                linkTo(methodOn(SupplierController.class).getSupplierById(supplier.getId())).withSelfRel(),
                linkTo(methodOn(SupplierController.class).getAllSuppliers(Pageable.unpaged())).withRel("suppliers")
        );
    }
}
