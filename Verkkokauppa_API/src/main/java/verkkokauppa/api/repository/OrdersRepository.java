package verkkokauppa.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import verkkokauppa.api.entity.Order;

import java.util.Optional;

public interface OrdersRepository extends JpaRepository<Order, Integer> {
    @Query("""
            select o
            from Order o
            left join fetch o.customer
            left join fetch o.address
            where o.id = :id
            """)
    Optional<Order> findByIdWithCustomerAndAddress(@Param("id") Integer id);
    Page<Order> findAllOrdersByCustomerId(Integer customerId, Pageable pageable);
}
