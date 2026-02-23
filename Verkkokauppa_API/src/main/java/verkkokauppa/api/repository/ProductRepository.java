package verkkokauppa.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import verkkokauppa.api.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
}
