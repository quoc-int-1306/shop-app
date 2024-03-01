package com.project.shopapp.Repository;

import com.project.shopapp.Models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByName(String name);

    @Query(value = "update products set status_products = 0 where id = ?1", nativeQuery = true)
    void deleteProduct(Long id);

    Page<Product> findAll(Pageable pageable);

    @Query("SELECT p FROM Product p where " +
            "(:categoryId IS NULL OR :categoryId = 0 OR p.category.id =: categoryId)" +
            "AND (:keyword IS NULL OR :keyword = '' OR p.name LIKE %:keyword% OR p.description LIKE %:keyword%)")
    Page<Product> searchProducts(@Param("categoryId") Long categoryId,
                                 @Param("keyword") String keyword, Pageable pageable
                                 );

    @Query("select p from Product p where p.id in :productIds")
    List<Product> findProductsByIds(@Param("productIds") List<Long> productIds);
}
