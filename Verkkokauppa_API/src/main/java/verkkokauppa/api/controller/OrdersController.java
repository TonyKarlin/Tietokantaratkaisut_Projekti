package verkkokauppa.api.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import verkkokauppa.api.dtos.OrderDTO;
import verkkokauppa.api.dtos.OrderRequest;
import verkkokauppa.api.entity.Order;
import verkkokauppa.api.service.OrdersService;
import verkkokauppa.api.utility.LoggerUtil;
import verkkokauppa.api.utility.assemblers.OrderModelAssembler;

@RestController
@RequestMapping("/orders")
public class OrdersController {
    private final OrdersService service;
    private final OrderModelAssembler assembler;

    public OrdersController(OrdersService service, OrderModelAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @GetMapping
    public ResponseEntity<Page<OrderDTO>> getAllOrders(@PageableDefault(size = 50) Pageable pageable) {
        Page<OrderDTO> page = service.getOrdersPage(pageable)
                .map(OrderDTO::new);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<OrderDTO>> getOrderById(@PathVariable Integer id) {
        return service.getOrderById(id)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<OrderDTO> postOrder(@RequestBody OrderRequest request) {
        LoggerUtil.logInfo("---ADDING NEW ORDER: " + request.status() + "---");
        Order savedOrder = service.postOrder(request);
        LoggerUtil.logInfo("---ORDER ADDED SUCCESSFULLY WITH STATUS: " + request.status() + "---");
        EntityModel<OrderDTO> dtoModel = assembler.toModel(savedOrder);
        return ResponseEntity.created(dtoModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(dtoModel.getContent());
    }
}
