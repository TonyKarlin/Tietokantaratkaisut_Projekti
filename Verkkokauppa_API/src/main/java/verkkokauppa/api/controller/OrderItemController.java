package verkkokauppa.api.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import verkkokauppa.api.dtos.OrderItemDTO;
import verkkokauppa.api.entity.OrderItem;
import verkkokauppa.api.service.OrderItemService;
import verkkokauppa.api.utility.assemblers.OIModelAssembler;

import java.math.BigDecimal;


@RestController
@RequestMapping("/order-items")
public class OrderItemController {
    private final OrderItemService orderItemService;
    private final OIModelAssembler assembler;

    public OrderItemController(OrderItemService orderItemService, OIModelAssembler assembler) {
        this.orderItemService = orderItemService;
        this.assembler = assembler;
    }

    @GetMapping
    public ResponseEntity<Page<OrderItemDTO>> getAllOrderItems(@PageableDefault(size = 50) Pageable pageable) {
        Page<OrderItemDTO> page = orderItemService.getOrderItemsPage(pageable)
                .map(OrderItemDTO::new);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<OrderItemDTO>> getOrderItemById(@PathVariable Integer id) {
        OrderItem orderItem = orderItemService.getByIdOrThrow(id);
        return ResponseEntity.ok(
                assembler.toModel(orderItem)
        );
    }

    @PatchMapping("/{orderId}/discount")
    public ResponseEntity<Integer> applyDiscount(
            @PathVariable Integer orderId,
            @RequestParam BigDecimal discount) {
        int updatedCount = orderItemService.applyDiscountToOrderItems(orderId, discount);
        return ResponseEntity.ok(updatedCount);
    }

    @PatchMapping("/{orderId}/remove-discount")
    public ResponseEntity<Integer> removeDiscount(@PathVariable Integer orderId) {
        int updatedCount = orderItemService.removeDiscountFromOrderItems(orderId);
        return ResponseEntity.ok(updatedCount);
    }
}
