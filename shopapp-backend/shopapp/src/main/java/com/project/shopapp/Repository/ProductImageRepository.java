package com.project.shopapp.Repository;

import com.project.shopapp.Models.Product;
import com.project.shopapp.Models.ProductImage;
import com.project.shopapp.Responses.ProductImageResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    @Query("SELECT new com.project.shopapp.Responses.ProductImageResponse(p.product.id, p.imageUrl) " +
            "from ProductImage p where p.product.id = ?1")
    List<ProductImageResponse> findByProductId(Long productId);
}
