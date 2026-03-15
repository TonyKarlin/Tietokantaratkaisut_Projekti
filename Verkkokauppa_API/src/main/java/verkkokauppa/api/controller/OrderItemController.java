package verkkokauppa.api.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import verkkokauppa.api.dtos.oiDTOs.OIQuantityUpdateRequest;
import verkkokauppa.api.dtos.oiDTOs.OrderItemDTO;
import verkkokauppa.api.dtos.oiDTOs.OrderItemRequest;
import verkkokauppa.api.entity.OrderItem;
import verkkokauppa.api.service.OrderItemService;
import verkkokauppa.api.utility.LoggerUtil;
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

    @GetMapping("/{orderId}/product/{productId}")
    public ResponseEntity<EntityModel<OrderItemDTO>> getOrderItemById(
            @PathVariable Integer orderId,
            @PathVariable Integer productId) {
        OrderItem orderItem = orderItemService.getByIdOrThrow(orderId, productId);
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

    @PostMapping
    public ResponseEntity<OrderItemDTO> postOrderItem(@RequestBody OrderItemRequest request) {
        if (request == null) {
            return ResponseEntity.noContent().build();
        }
        LoggerUtil.logInfo("---ADDING NEW ORDER ITEM: " + request.orderId() + "/" + request.productId() + "---");
        OrderItem saved = orderItemService.postRequest(request);
        LoggerUtil.logInfo("---ORDER ITEM ADDED SUCCESSFULLY WITH ID: "
                + saved.getId().getOrderId() + "/" + saved.getId().getProductId() + "---");

        EntityModel<OrderItemDTO> dtoModel = assembler.toModel(saved);
        return ResponseEntity.created(dtoModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(dtoModel.getContent());
    }

    @PutMapping("/{orderId}/product/{productId}")
    public ResponseEntity<OrderItemDTO> updateOrderItem(@PathVariable Integer orderId,
                                                        @PathVariable Integer productId,
                                                        @RequestBody OIQuantityUpdateRequest request) {
        LoggerUtil.logInfo("---UPDATING ORDER ITEM WITH ID: " + orderId + "/" + productId + "---");
        OrderItem updated = orderItemService.putRequest(orderId, productId, request);
        LoggerUtil.logInfo("---ORDER ITEM WITH ID: " + orderId + "/" + productId + " UPDATED SUCCESSFULLY---");

        EntityModel<OrderItemDTO> dtoModel = assembler.toModel(updated);
        return ResponseEntity.ok()
                .location(dtoModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(dtoModel.getContent());
    }
}
