package verkkokauppa.api.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import verkkokauppa.api.dtos.OrderRequest;
import verkkokauppa.api.entity.Customer;
import verkkokauppa.api.entity.CustomerAddress;
import verkkokauppa.api.entity.Order;
import verkkokauppa.api.repository.OrdersRepository;
import verkkokauppa.api.utility.exceptions.custom_exceptions.InvalidArgumentException;

import java.util.Optional;

@Service
public class OrdersService {
    private final OrdersRepository repository;
    private final CustomersService customersService;
    private final CustomerAddressesService addressesService;

    public OrdersService(OrdersRepository repository, CustomersService customersService, CustomerAddressesService addressesService) {
        this.repository = repository;
        this.customersService = customersService;
        this.addressesService = addressesService;
    }

    public Page<Order> getOrdersPage(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Optional<Order> getOrderById(Integer id) {
        if (id == null) {
            throw new InvalidArgumentException("ID cannot be null");
        }
        return repository.findByIdWithCustomerAndAddress(id);
    }

    public Page<Order> getOrdersByCustomer(Integer customerId, Pageable pageable) {
        if (customerId == null) {
            throw new InvalidArgumentException("Customer ID cannot be null");
        }
        fetchCustomer(customerId);
        return repository.findAllOrdersByCustomerId(customerId, pageable);

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
        return repository.save(newOrder);
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
