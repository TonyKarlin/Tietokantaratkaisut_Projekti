package verkkokauppa.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import verkkokauppa.api.entity.SupplierAddress;

@Repository
public interface SupplierAddressRepository extends JpaRepository<SupplierAddress, Integer> {

    List<SupplierAddress> findBySupplierId(Integer supplierId);

    List<SupplierAddress> findByCityContainingIgnoreCase(String city);
}
