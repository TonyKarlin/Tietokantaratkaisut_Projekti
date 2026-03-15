package verkkokauppa.api.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import verkkokauppa.api.dtos.oiDTOs.OIQuantityUpdateRequest;
import verkkokauppa.api.dtos.oiDTOs.OrderItemRequest;
import verkkokauppa.api.entity.Order;
import verkkokauppa.api.entity.OrderItem;
import verkkokauppa.api.entity.OrderItemId;
import verkkokauppa.api.entity.Product;
import verkkokauppa.api.repository.OrderItemsRepository;
import verkkokauppa.api.repository.OrdersRepository;
import verkkokauppa.api.repository.ProductRepository;
import verkkokauppa.api.utility.exceptions.custom_exceptions.InvalidArgumentException;
import verkkokauppa.api.utility.exceptions.custom_exceptions.InvalidDiscountException;
import verkkokauppa.api.utility.exceptions.custom_exceptions.OrderItemNotFoundException;
import verkkokauppa.api.utility.exceptions.custom_exceptions.OrderNotFoundException;
import verkkokauppa.api.utility.exceptions.custom_exceptions.ProductNotFoundException;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class OrderItemService {
    private static final String NOT_FOUND_MESSAGE = "Order item not found with id: ";
    private final OrderItemsRepository repository;
    private final OrdersRepository ordersRepository;
    private final ProductRepository productRepository;

    public OrderItemService(OrderItemsRepository repository,
                            OrdersRepository ordersRepository,
                            ProductRepository productRepository) {
        this.repository = repository;
        this.ordersRepository = ordersRepository;
        this.productRepository = productRepository;
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

    public OrderItem postRequest(OrderItemRequest request) {
        if (request == null) {
            throw new InvalidArgumentException("Order item cannot be null");
        }
        if (!isValidOrderItemRequest(request)) {
            throw new InvalidArgumentException("Invalid order item data: required fields missing");
        }

        Order order = ordersRepository.findById(request.orderId())
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + request.orderId()));

        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + request.productId()));

        OrderItemId id = new OrderItemId(request.orderId(), request.productId());
        if (repository.existsById(id)) {
            throw new InvalidArgumentException("Order item already exists with id: "
                    + request.orderId() + "/" + request.productId());
        }

        OrderItem newItem = new OrderItem();
        newItem.setId(id);
        newItem.setOrder(order);
        newItem.setProduct(product);
        newItem.setQuantity(request.quantity().amount());

        BigDecimal price = product.getPrice();
        newItem.setUnitPrice(price);
        newItem.setDiscountedPrice(null);

        return repository.save(newItem);
    }

    public OrderItem putRequest(Integer orderId, Integer productId, OIQuantityUpdateRequest request) {
        if (orderId == null || productId == null) {
            throw new InvalidArgumentException("Order ID and Product ID cannot be null");
        }
        if (request == null) {
            throw new InvalidArgumentException("Update request cannot be null");
        }
        if (!isValidQuantity(request.amount())) {
            throw new InvalidArgumentException("Invalid update request: required fields missing");
        }

        OrderItem existing = getByIdOrThrow(orderId, productId);

        Order order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + orderId));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + productId));

        existing.setOrder(order);
        existing.setProduct(product);
        existing.setQuantity(request.amount());

        BigDecimal price = product.getPrice();
        existing.setUnitPrice(price);

        return repository.save(existing);
    }

    private boolean isValidOrderItemRequest(OrderItemRequest request) {
        return request.orderId() != null
                && request.productId() != null
                && isValidQuantity(request.quantity().amount());
    }

    private boolean isValidQuantity(Integer quantity) {
        return quantity != null && quantity > 0;
    }
}
