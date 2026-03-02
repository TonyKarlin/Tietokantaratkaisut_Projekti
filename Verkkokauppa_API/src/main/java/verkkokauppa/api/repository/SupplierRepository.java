package verkkokauppa.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import verkkokauppa.api.entity.Supplier;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Integer> {

    List<Supplier> findByNameContainingIgnoreCase(String name);

    Supplier findByEmail(String email);
}
