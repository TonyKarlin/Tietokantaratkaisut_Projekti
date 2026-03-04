package verkkokauppa.api.repository.custom_repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import verkkokauppa.api.entity.Order;

import java.util.List;

@Repository
public class OrdersRepositoryCustomImpl implements OrdersRepositoryCustom{
    private static final String STATUS = "status";

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public int bulkUpdateStatus(List<Integer> orderIds,
                                String currentStatus,
                                String newStatus) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaUpdate<Order> update = cb.createCriteriaUpdate(Order.class);
        Root<Order> root = update.from(Order.class);

        update.set(root.get(STATUS), newStatus);

        update.where(
                cb.and(
                        root.get("id").in(orderIds),
                        cb.equal(root.get(STATUS), currentStatus)
                )
        );

        return entityManager.createQuery(update).executeUpdate();
    }

    @Transactional
    public int bulkUpdateStatusByCustomerId(Integer customerId,
                                            String currentStatus,
                                            String newStatus) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaUpdate<Order> update = cb.createCriteriaUpdate(Order.class);
        Root<Order> root = update.from(Order.class);

        update.set(root.get(STATUS), newStatus);

        update.where(
                cb.and(
                        cb.equal(root.get("customer").get("id"), customerId),
                        cb.equal(root.get(STATUS), currentStatus)
                )
        );

        return entityManager.createQuery(update).executeUpdate();
    }
}
