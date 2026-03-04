package verkkokauppa.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import verkkokauppa.api.entity.OrderItem;
import verkkokauppa.api.entity.OrderItemId;

import java.math.BigDecimal;

public interface OrderItemsRepository extends JpaRepository<OrderItem, OrderItemId> {

    @Modifying
    @Query("UPDATE OrderItem oi SET oi.discountedPrice = oi.unitPrice * :multiplier WHERE oi.order.id = :orderId")
    int applyDiscountToOrder(@Param("orderId") Integer id,
                             @Param("multiplier") BigDecimal multiplier);

    @Modifying
    @Query("UPDATE OrderItem oi SET oi.discountedPrice = null WHERE oi.order.id = :orderId")
    int removeDiscountFromOrder(@Param("orderId") Integer orderId);
}
