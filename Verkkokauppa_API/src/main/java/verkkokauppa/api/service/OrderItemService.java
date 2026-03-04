package verkkokauppa.api.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import verkkokauppa.api.entity.OrderItem;
import verkkokauppa.api.entity.OrderItemId;
import verkkokauppa.api.repository.OrderItemsRepository;
import verkkokauppa.api.utility.exceptions.custom_exceptions.InvalidDiscountException;
import verkkokauppa.api.utility.exceptions.custom_exceptions.OrderItemNotFoundException;
import verkkokauppa.api.utility.exceptions.custom_exceptions.OrderNotFoundException;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class OrderItemService {
    private static final String NOT_FOUND_MESSAGE = "Order item not found with id: ";
    private final OrderItemsRepository repository;

    public OrderItemService(OrderItemsRepository repository) {
        this.repository = repository;
    }

    public Page<OrderItem> getOrderItemsPage(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public OrderItem getByIdOrThrow(Integer orderId, Integer productId) {
        OrderItemId id = new OrderItemId(orderId, productId);
        return repository.findById(id)
                .orElseThrow(() ->
                        new OrderItemNotFoundException(NOT_FOUND_MESSAGE + orderId + "/" + productId));
    }

    @Transactional
    public int applyDiscountToOrderItems(Integer orderId, BigDecimal discount) {
        if (orderId == null) throw new OrderNotFoundException("Order id is null.");
        if (discount.compareTo(BigDecimal.ZERO) >= 0 && discount.compareTo(BigDecimal.valueOf(100)) <= 0) {
            BigDecimal multiplier = BigDecimal.ONE.subtract(
                    discount.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
            return repository.applyDiscountToOrder(orderId, multiplier);
        }
        throw new InvalidDiscountException("Discount must be between 0 and 100, got: " + discount);
    }

    @Transactional
    public int removeDiscountFromOrderItems(Integer orderId) {
        if (orderId == null) throw new OrderNotFoundException("Order id is null.");
        return repository.removeDiscountFromOrder(orderId);
    }
}
