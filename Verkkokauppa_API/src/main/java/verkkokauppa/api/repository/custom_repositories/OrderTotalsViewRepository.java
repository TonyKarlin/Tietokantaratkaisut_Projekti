package verkkokauppa.api.repository.custom_repositories;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import verkkokauppa.api.dtos.orderDTOs.OrderTotalsDTO;

import java.time.LocalDate;
import java.util.List;


// AI generated repository for the view v_order_totals
@Repository
public class OrderTotalsViewRepository {

    private final JdbcTemplate jdbcTemplate;

    public OrderTotalsViewRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final RowMapper<OrderTotalsDTO> MAPPER = (rs, rowNum) -> new OrderTotalsDTO(
            rs.getInt("order_id"),
            rs.getInt("customer_id"),
            rs.getObject("order_date", LocalDate.class),
            rs.getObject("delivery_date", LocalDate.class),
            (Integer) rs.getObject("shipping_address_id"),
            rs.getString("status"),
            rs.getInt("item_lines"),
            rs.getInt("total_quantity"),
            rs.getBigDecimal("total_amount")
    );

    public OrderTotalsDTO findByOrderId(int orderId) {
        List<OrderTotalsDTO> results = jdbcTemplate.query(
                "SELECT * FROM v_order_totals WHERE order_id = ?",
                MAPPER,
                orderId
        );
        return results.isEmpty() ? null : results.getFirst();
    }

    public List<OrderTotalsDTO> findByCustomerId(int customerId) {
        return jdbcTemplate.query(
                "SELECT * FROM v_order_totals WHERE customer_id = ? ORDER BY order_date DESC",
                MAPPER,
                customerId
        );
    }
}
