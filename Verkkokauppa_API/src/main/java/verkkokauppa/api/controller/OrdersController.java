package verkkokauppa.api.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import verkkokauppa.api.dtos.orderDTOs.BatchStatusByCustomerRequest;
import verkkokauppa.api.dtos.orderDTOs.BatchStatusRequest;
import verkkokauppa.api.dtos.orderDTOs.OrderDTO;
import verkkokauppa.api.dtos.orderDTOs.OrderRequest;
import verkkokauppa.api.entity.Order;
import verkkokauppa.api.service.OrdersService;
import verkkokauppa.api.utility.LoggerUtil;
import verkkokauppa.api.utility.assemblers.OrderModelAssembler;

import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrdersController {
    private final OrdersService ordersService;
    private final OrderModelAssembler assembler;

    public OrdersController(OrdersService ordersService, OrderModelAssembler assembler) {
        this.ordersService = ordersService;
        this.assembler = assembler;
    }

    @GetMapping
    public ResponseEntity<Page<OrderDTO>> getAllOrders(@PageableDefault(size = 50) Pageable pageable) {
        Page<OrderDTO> page = ordersService.getOrdersPage(pageable)
                .map(OrderDTO::new);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<OrderDTO>> getOrderById(@PathVariable Integer id) {
        return ordersService.getOrderById(id)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-customer/{id}")
    public ResponseEntity<Page<OrderDTO>> getOrdersByCustomer(
            @PathVariable Integer id,
            @PageableDefault(size = 50) Pageable pageable
    ) {
        Page<OrderDTO> page = ordersService.getOrdersByCustomer(id, pageable)
                .map(OrderDTO::new);
        return ResponseEntity.ok(page);
    }


    @PostMapping
    public ResponseEntity<OrderDTO> postOrder(@RequestBody OrderRequest request) {
        LoggerUtil.logInfo("---ADDING NEW ORDER: " + request.status() + "---");
        Order savedOrder = ordersService.postOrder(request);
        LoggerUtil.logInfo("---ORDER ADDED SUCCESSFULLY WITH STATUS: " + request.status() + "---");
        EntityModel<OrderDTO> dtoModel = assembler.toModel(savedOrder);
        return ResponseEntity.created(dtoModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(dtoModel.getContent());
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderDTO> updateOrder(@PathVariable Integer id,
                                                @RequestBody OrderRequest request) {
        LoggerUtil.logInfo("---UPDATING ORDER WITH ID: " + id + "---");
        Order updatedOrder = ordersService.putRequest(id, request);
        LoggerUtil.logInfo("---ORDER WITH ID: " + id + " UPDATED SUCCESSFULLY---");

        EntityModel<OrderDTO> dtoModel = assembler.toModel(updatedOrder);
        return ResponseEntity.ok()
                .location(dtoModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(dtoModel.getContent());
    }

    @PatchMapping("/batch/status")
    public ResponseEntity<Map<String, Object>> bulkUpdateStatus(
            @RequestBody BatchStatusRequest request) {
        int updated = ordersService.bulkTransitionStatus(
                request.getOrderIds(),
                request.getFromStatus(),
                request.getToStatus());
        return ResponseEntity.ok(Map.of("updatedCount", updated));
    }

    @PatchMapping("/batch/status/by-customer")
    public ResponseEntity<Map<String, Object>> bulkUpdateStatusByCustomer(
            @RequestBody BatchStatusByCustomerRequest request) {
        int updated = ordersService.bulkTransitionStatusByCustomer(
                request.getCustomerId(),
                request.getFromStatus(),
                request.getToStatus());
        return ResponseEntity.ok(Map.of("updatedCount", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderById(@PathVariable Integer id) {
        LoggerUtil.logInfo("---DELETING ORDER WITH ID: " + id + "---");
        ordersService.delete(id);
        LoggerUtil.logInfo("---ORDER WITH ID: " + id + " DELETED SUCCESSFULLY---");
        return ResponseEntity.noContent().build();
    }

}