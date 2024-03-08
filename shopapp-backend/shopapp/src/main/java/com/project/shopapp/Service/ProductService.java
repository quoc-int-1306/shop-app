package com.project.shopapp.Service;

import com.project.shopapp.Dtos.ProductDTO;
import com.project.shopapp.Dtos.ProductImageDTO;
import com.project.shopapp.Exceptions.DataNotFound;
import com.project.shopapp.Exceptions.InvalidParamExvception;
import com.project.shopapp.Mapper.ObjectMapper;
import com.project.shopapp.Models.Category;
import com.project.shopapp.Models.Product;
import com.project.shopapp.Models.ProductImage;
import com.project.shopapp.Repository.CategoryRepository;
import com.project.shopapp.Repository.ProductImageRepository;
import com.project.shopapp.Repository.ProductRepository;
import com.project.shopapp.Responses.ProductImageResponse;
import com.project.shopapp.Responses.ProductResponse;
import com.project.shopapp.Service.impl.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService implements IProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductImageRepository productImageRepository;

    @Override
    @Transactional
    public Product createProduct(ProductDTO productDTO) throws DataNotFound {
        Category existingCategory = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new DataNotFound("Cannot find category with id "+ productDTO.getCategoryId()));

        Product newProduct = Product.builder()
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .thumbnail(productDTO.getThumbnail())
                .category(existingCategory)
                .build();
        return productRepository.save(newProduct);
    }

    @Override
    public Product getProductById(Long id) throws DataNotFound {
        return productRepository.findById(id).orElseThrow(() -> new DataNotFound("Cannot find product id" + id));
    }

    @Override
    public ProductResponse getProductDetailById(Long id) throws DataNotFound {
        List<ProductImageResponse> images = productImageRepository.findByProductId(id);
        List<ProductImageResponse> imagesRespone = new ArrayList<>();
        Product product = productRepository.findById(id).orElseThrow(() -> new DataNotFound("Cannot find product id" + id));
        return ObjectMapper.fromProductToProductDetailResponseMapper(product, images);
    }

    @Override
    public Page<ProductResponse> getAllProducts(PageRequest pageRequest, Long categoryId, String keyword) {
         return productRepository.searchProducts(categoryId, keyword, pageRequest)
                 .map(product -> ObjectMapper.fromProductToProductResponseMapper(product));
    }

    @Override
    @Transactional
    public Product updateProduct(
            Long id,
            ProductDTO productDTO)
            throws DataNotFound {
        Product existingProduct = productRepository.findById(id).orElseThrow(() -> new DataNotFound("Not found product with id" + id));
        if(existingProduct != null) {
            //copy cac thuoc tinh tu DTO sang Product
            //Co the su dung ModelMapper
            Category existingCategory = categoryRepository.findById(productDTO.getCategoryId())
                    .orElseThrow(() -> new DataNotFound("Cannot find category with id "+ productDTO.getCategoryId()));
            existingProduct.setName(productDTO.getName());
            existingProduct.setCategory(existingCategory);
            existingProduct.setPrice(productDTO.getPrice());
            existingProduct.setDescription(productDTO.getDescription());
            existingProduct.setThumbnail(productDTO.getThumbnail());
            return productRepository.save(existingProduct);
        }
        return null;
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) throws DataNotFound {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if(optionalProduct.isPresent()) {
            productRepository.deleteProduct(optionalProduct.get().getId());
        }else {
            throw new DataNotFound("Not found product with id" + id);
        }
    }

    @Override
    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }

    @Override
    @Transactional
    public ProductImage createProductImage(
            Long productId,
            ProductImageDTO productImageDTO) throws DataNotFound, InvalidParamExvception {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new DataNotFound("Cannot find category with id "+ productImageDTO.getProductId()));
        ProductImage newProductImage = ProductImage.builder()
                .product(existingProduct)
                .imageUrl(productImageDTO.getImageUrl())
                .build();
        // Khong cho insert qua 5 anh cho 1 san pham
        int size = productImageRepository.findByProductId(productId).size();
        if(size >= ProductImage.MAXIMUM_IMAGES_PER_PRODUCT) {
            throw new InvalidParamExvception("Number of images must be <= "
                    + ProductImage.MAXIMUM_IMAGES_PER_PRODUCT);
        }
        return productImageRepository.save(newProductImage);
    }

    @Override
    public List<Product> findProductsByIds(List<Long> productIds) {
        return productRepository.findProductsByIds(productIds);
    }
}
