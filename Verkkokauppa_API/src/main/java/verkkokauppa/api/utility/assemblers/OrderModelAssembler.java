package verkkokauppa.api.utility.assemblers;

import org.jspecify.annotations.NullMarked;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import verkkokauppa.api.controller.OrdersController;
import verkkokauppa.api.dtos.OrderDTO;
import verkkokauppa.api.entity.Order;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@NullMarked
@Component
public class OrderModelAssembler implements RepresentationModelAssembler<Order, EntityModel<OrderDTO>> {
    @Override
    public EntityModel<OrderDTO> toModel(Order order) {
        OrderDTO dto = new ModelMapper().map(order, OrderDTO.class);
        return EntityModel.of(
                dto,
                linkTo(methodOn(OrdersController.class).getOrderById(order.getId())).withSelfRel(),
                linkTo(methodOn(OrdersController.class).getAllOrders(Pageable.unpaged())).withRel("orders")
        );
    }
}
