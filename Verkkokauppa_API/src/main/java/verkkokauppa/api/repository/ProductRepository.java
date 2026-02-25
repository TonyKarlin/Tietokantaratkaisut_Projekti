package verkkokauppa.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import verkkokauppa.api.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Modifying
    @Query(value = "delete from orderitems where product_id in (select id from products where category_id = :categoryId)", nativeQuery = true)
    void deleteOrderItemsByCategoryId(@Param("categoryId") Integer categoryId);
}
