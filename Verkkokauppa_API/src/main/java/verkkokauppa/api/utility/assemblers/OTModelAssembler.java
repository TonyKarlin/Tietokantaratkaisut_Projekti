package verkkokauppa.api.utility.assemblers;

import org.jspecify.annotations.NullMarked;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import verkkokauppa.api.controller.OrderTotalsController;
import verkkokauppa.api.dtos.orderDTOs.OrderTotalsDTO;

import java.util.List;
import java.util.Objects;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@NullMarked
@Component
public class OTModelAssembler implements RepresentationModelAssembler<OrderTotalsDTO, EntityModel<OrderTotalsDTO>> {

    @Override
    public EntityModel<OrderTotalsDTO> toModel(OrderTotalsDTO dto) {
        Integer orderId = dto.orderId();
        Integer customerId = dto.customerId();

        EntityModel<OrderTotalsDTO> model = EntityModel.of(dto);

        if (orderId != null) {
            model.add(linkTo(methodOn(OrderTotalsController.class).getByOrderId(orderId)).withSelfRel());
        }

        if (customerId != null) {
            model.add(linkTo(methodOn(OrderTotalsController.class).getByCustomerId(customerId)).withRel("byCustomer"));
        }
        return model;
    }

    public CollectionModel<EntityModel<OrderTotalsDTO>> toCollectionModel(List<OrderTotalsDTO> dtos) {
        List<EntityModel<OrderTotalsDTO>> items = dtos.stream().map(this::toModel).toList();

        Integer customerId = dtos.stream()
                .map(OrderTotalsDTO::customerId)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);

        if (customerId != null) {
            return CollectionModel.of(items,
                    linkTo(methodOn(OrderTotalsController.class).getByCustomerId(customerId)).withSelfRel());
        }

        return CollectionModel.of(items);
    }
}
