package verkkokauppa.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import verkkokauppa.api.entity.CustomerAddresses;

@Repository
public interface CustomerAddressesRepository extends JpaRepository<CustomerAddresses, Integer> {
}
