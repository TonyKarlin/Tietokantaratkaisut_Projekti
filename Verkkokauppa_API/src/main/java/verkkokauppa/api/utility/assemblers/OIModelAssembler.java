package verkkokauppa.api.utility.assemblers;

import org.jspecify.annotations.NullMarked;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import verkkokauppa.api.controller.OrderItemController;
import verkkokauppa.api.dtos.OrderItemDTO;
import verkkokauppa.api.entity.OrderItem;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@NullMarked
@Component
public class OIModelAssembler implements RepresentationModelAssembler<OrderItem, EntityModel<OrderItemDTO>> {

    @Override
    public EntityModel<OrderItemDTO> toModel(OrderItem orderItem) {
        OrderItemDTO dto = new ModelMapper().map(orderItem, OrderItemDTO.class);
        return EntityModel.of(
                dto,
                linkTo(methodOn(OrderItemController.class).getOrderItemById(orderItem.getId())).withSelfRel(),
                linkTo(methodOn(OrderItemController.class).getAllOrderItems(Pageable.unpaged())).withRel("customers")
        );
    }
}
