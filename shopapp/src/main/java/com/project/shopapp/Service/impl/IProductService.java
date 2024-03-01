package com.project.shopapp.Service.impl;

import com.project.shopapp.Dtos.ProductDTO;
import com.project.shopapp.Dtos.ProductImageDTO;
import com.project.shopapp.Exceptions.DataNotFound;
import com.project.shopapp.Exceptions.InvalidParamExvception;
import com.project.shopapp.Models.Product;
import com.project.shopapp.Models.ProductImage;
import com.project.shopapp.Responses.ProductImageResponse;
import com.project.shopapp.Responses.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IProductService {
    public Product createProduct(ProductDTO productDTO) throws DataNotFound;

    Product getProductById(Long id) throws DataNotFound;

    ProductResponse getProductDetailById(Long id) throws DataNotFound;

    Page<ProductResponse> getAllProducts(PageRequest pageRequest, Long categoryId, String keyword);

    Product updateProduct(Long id, ProductDTO productDTO) throws DataNotFound;

    void deleteProduct(Long id) throws DataNotFound;

    boolean existsByName(String name);

    public ProductImage createProductImage(
            Long productId,
            ProductImageDTO productImageDTO) throws DataNotFound, InvalidParamExvception;

    public List<Product> findProductsByIds(List<Long> productIds);
}
