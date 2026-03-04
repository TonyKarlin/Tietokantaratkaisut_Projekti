package verkkokauppa.api.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import verkkokauppa.api.entity.OrderItem;
import verkkokauppa.api.repository.OrderItemsRepository;
import verkkokauppa.api.utility.exceptions.custom_exceptions.OrderItemNotFoundException;

@Service
public class OrderItemService {
    private static final String NOT_FOUND_MESSAGE = "Order not found with id: ";
    private final OrderItemsRepository repository;

    public OrderItemService(OrderItemsRepository repository) {
        this.repository = repository;
    }

    public Page<OrderItem> getOrderItemsPage(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public OrderItem getByIdOrThrow(Integer id) {
        if (id == null) {
            throw new OrderItemNotFoundException(NOT_FOUND_MESSAGE + null);
        }
        return repository.findById(id)
                .orElseThrow(() ->
                        new OrderItemNotFoundException(NOT_FOUND_MESSAGE + id));
    }
}
