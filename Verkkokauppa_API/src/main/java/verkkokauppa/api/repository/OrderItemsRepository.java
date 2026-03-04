package verkkokauppa.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import verkkokauppa.api.entity.OrderItem;

import java.math.BigDecimal;

public interface OrderItemsRepository extends JpaRepository<OrderItem, Integer> {

    @Modifying
    @Query("UPDATE OrderItem oi SET oi.discountedPrice = oi.unitPrice * :multiplier WHERE oi.order.id = :orderId")
    int applyDiscountToOrder(@Param("orderId") Integer id,
                             @Param("multiplier") BigDecimal multiplier);
}
