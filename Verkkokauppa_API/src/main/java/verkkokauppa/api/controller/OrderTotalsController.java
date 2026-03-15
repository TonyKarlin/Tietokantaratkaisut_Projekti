package verkkokauppa.api.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import verkkokauppa.api.dtos.orderDTOs.OrderTotalsDTO;
import verkkokauppa.api.service.OrderTotalsService;
import verkkokauppa.api.utility.assemblers.OTModelAssembler;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/order-totals")
public class OrderTotalsController {
    private final OrderTotalsService service;
    private final OTModelAssembler assembler;

    public OrderTotalsController(OrderTotalsService service, OTModelAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<EntityModel<OrderTotalsDTO>> getByOrderId(@PathVariable Integer orderId) {
        return Optional.ofNullable(service.getByOrderId(orderId))
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-customer/{customerId}")
    public ResponseEntity<CollectionModel<EntityModel<OrderTotalsDTO>>> getByCustomerId(@PathVariable Integer customerId) {
        List<OrderTotalsDTO> dtos = service.getByCustomerId(customerId);
        return ResponseEntity.ok(assembler.toCollectionModel(dtos));
    }


}
