package verkkokauppa.api.repository.custom_repositories;

import java.util.List;

public interface OrdersRepositoryCustom {
    int bulkUpdateStatus(List<Integer> orderIds, String currentStatus, String newStatus);
    int bulkUpdateStatusByCustomerId(Integer customerId, String currentStatus, String newStatus);

}
