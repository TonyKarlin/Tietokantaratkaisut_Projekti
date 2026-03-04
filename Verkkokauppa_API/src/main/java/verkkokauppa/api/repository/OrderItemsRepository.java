package verkkokauppa.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import verkkokauppa.api.entity.OrderItem;

public interface OrderItemsRepository extends JpaRepository<OrderItem, Integer> {
}
