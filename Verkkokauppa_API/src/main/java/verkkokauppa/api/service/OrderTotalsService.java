package verkkokauppa.api.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import verkkokauppa.api.dtos.orderDTOs.OrderTotalsDTO;
import verkkokauppa.api.repository.custom_repositories.OrderTotalsViewRepository;

import java.util.List;

@Service
public class OrderTotalsService {

    private final OrderTotalsViewRepository repository;

    public OrderTotalsService(OrderTotalsViewRepository repository) {
        this.repository = repository;
    }

    public OrderTotalsDTO getByOrderId(Integer orderId) {
        if (orderId == null) {
            return null;
        }
        return repository.findByOrderId(orderId);
    }

    public List<OrderTotalsDTO> getByCustomerId(Integer customerId) {
        if (customerId == null) {
            return List.of();
        }
        return repository.findByCustomerId(customerId);
    }
}
