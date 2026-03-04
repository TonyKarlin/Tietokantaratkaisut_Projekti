package verkkokauppa.api.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import verkkokauppa.api.dtos.OrderRequest;
import verkkokauppa.api.entity.Customer;
import verkkokauppa.api.entity.CustomerAddress;
import verkkokauppa.api.entity.Order;
import verkkokauppa.api.repository.OrdersRepository;
import verkkokauppa.api.utility.exceptions.custom_exceptions.InvalidArgumentException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class OrdersService {
    private final OrdersRepository orderRepository;
    private final CustomersService customersService;
    private final CustomerAddressesService addressesService;

    private static final String CANCELLED_TEXT = "CANCELLED";
    private static final Map<String, List<String>> VALID_TRANSITIONS = Map.of(
            "NEW",       List.of("PENDING", CANCELLED_TEXT),
            "PENDING",   List.of("SHIPPED", CANCELLED_TEXT),
            "SHIPPED",   List.of(CANCELLED_TEXT),
            CANCELLED_TEXT, List.of()
    );

    public OrdersService(OrdersRepository orderRepository, CustomersService customersService, CustomerAddressesService addressesService) {
        this.orderRepository = orderRepository;
        this.customersService = customersService;
        this.addressesService = addressesService;
    }

    public Page<Order> getOrdersPage(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    public Optional<Order> getOrderById(Integer id) {
        if (id == null) {
            throw new InvalidArgumentException("ID cannot be null");
        }
        return orderRepository.findByIdWithCustomerAndAddress(id);
    }

    public Page<Order> getOrdersByCustomer(Integer customerId, Pageable pageable) {
        if (customerId == null) {
            throw new InvalidArgumentException("Customer ID cannot be null");
        }
        fetchCustomer(customerId);
        return orderRepository.findAllOrdersByCustomerId(customerId, pageable);

    }

    public Order postOrder(OrderRequest order) {
        if (order == null) {
            throw new InvalidArgumentException("Order cannot be null");
        }
        if (!isValidOrderRequest(order)) {
            throw new InvalidArgumentException("Required fields missing.");
        }
        Customer customer = fetchCustomer(order.customerId());
        CustomerAddress address = fetchAddress(order.shippingAddressId());
        Order newOrder = new Order(
                customer,
                address,
                order.orderDate(),
                order.deliveryDate(),
                order.status()
        );
        return orderRepository.save(newOrder);
    }

    @Transactional
    public int bulkTransitionStatus(List<Integer> orderIds,
                                    String fromStatus,
                                    String toStatus) {
        List<String> allowed = VALID_TRANSITIONS.getOrDefault(fromStatus, List.of());
        if (!allowed.contains(toStatus)) {
            throw new IllegalArgumentException(
                    "Invalid transition: " + fromStatus + " → " + toStatus);
        }
        return orderRepository.bulkUpdateStatus(orderIds, fromStatus, toStatus);
    }

    @Transactional
    public int bulkTransitionStatusByCustomer(Integer customerId,
                                              String fromStatus,
                                              String toStatus) {
        List<String> allowed = VALID_TRANSITIONS.getOrDefault(fromStatus, List.of());
        if (!allowed.contains(toStatus)) {
            throw new IllegalArgumentException(
                    "Invalid transition: " + fromStatus + " → " + toStatus);
        }
        return orderRepository.bulkUpdateStatusByCustomerId(customerId, fromStatus, toStatus);
    }


    private boolean isValidOrderRequest(OrderRequest order) {
        return ((order.status() != null && !order.status().isBlank()) &&
                (order.orderDate() != null));
    }

    private Customer fetchCustomer(Integer id) {
        return customersService.getByIdOrThrow(id);
    }

    private CustomerAddress fetchAddress(Integer id) {
        return addressesService.getByIdOrThrow(id);
    }
}
