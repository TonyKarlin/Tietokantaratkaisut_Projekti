package verkkokauppa.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import verkkokauppa.api.entity.Customers;

@Repository
public interface CustomersRepository extends JpaRepository<Customers, Integer> {
}
