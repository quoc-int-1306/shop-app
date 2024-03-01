package com.project.shopapp.Controller;

import com.github.javafaker.Faker;
import com.project.shopapp.Dtos.ProductDTO;
import com.project.shopapp.Dtos.ProductImageDTO;
import com.project.shopapp.Exceptions.DataNotFound;
import com.project.shopapp.Mapper.ObjectMapper;
import com.project.shopapp.Models.Product;
import com.project.shopapp.Models.ProductImage;
import com.project.shopapp.Responses.ProductListResponse;
import com.project.shopapp.Responses.ProductResponse;
import com.project.shopapp.Service.impl.IProductService;
import com.project.shopapp.Utils.LocalizationUtils;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.prefix}/products")
public class ProductController {

    @Autowired
    private IProductService productService;
    @Autowired
    private LocalizationUtils localizationUtils;

    private Logger logger =  LoggerFactory.getLogger(ProductController.class);

    @GetMapping("")
    public ResponseEntity<ProductListResponse> getProducts(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0", name = "category_id") Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "0") int limit
    ) {
        logger.info("RestAPI get Products");
        logger.info(String.format("keyword = %s, category = %d, page = %d, limit = %d", keyword, categoryId, page, limit));
        PageRequest pageRequest = PageRequest.of(page - 1, limit, Sort.by("id").ascending());
        Page<ProductResponse> productPage = productService.getAllProducts(pageRequest, categoryId, keyword);
        List<ProductResponse> products = productPage.getContent();
        return ResponseEntity.ok(ProductListResponse.builder()
                .products(products)
                .totalPages(productPage.getTotalPages())
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable("id") long productId) throws DataNotFound {
        try {
            ProductResponse existingProduct = productService.getProductDetailById(productId);
            return ResponseEntity.ok(existingProduct);
        } catch (DataNotFound dataNotFound) {
            return ResponseEntity.badRequest().body(dataNotFound.getMessage());
        }
    }

    @PostMapping(value = "")
    @Transactional
    public ResponseEntity<?> insertProduct(
           @Valid @RequestBody ProductDTO productDTO,
            BindingResult result
    ) {
        try {
            if(result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            Product newProduct = productService.createProduct(productDTO);
            return ResponseEntity.ok(newProduct);
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "uploads/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Transactional
    public ResponseEntity<?> uploadImages(
            @PathVariable("id") Long productId,
        @ModelAttribute("files") List<MultipartFile> files
    ) throws DataNotFound {
       try {
           Product existingProduct = productService.getProductById(productId);
           files = files == null ? new ArrayList<MultipartFile>() : files;
           if(files.size() > ProductImage.MAXIMUM_IMAGES_PER_PRODUCT) {
                return ResponseEntity.badRequest().body("You can only upload maximum 5 images");
           }
           if(files.size() == ProductImage.MINIMUM_IMAGES_PER_PRODUCT) {
                return ResponseEntity.badRequest().body("You must choose at least 1 image");
           }
           List<ProductImage> productImages = new ArrayList<>();
           for (MultipartFile file: files) {
               if(file != null) {
                   if(file.getSize() == 0) {
                       if(file.getSize() == 0) {
                           continue;
                       }
                   }
                   if(file.getSize() > 10*1024*1024) {
                       return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("File is to large! Maximum size is 10MB");
                   }
                   String contentType = file.getContentType();
                   if(contentType == null || !contentType.startsWith("image/")) {
                       return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                               .body("File must be an image");
                   }
                   // Lưu file và cập nhật thumbnail trong DTO
                   String filename = storeFile(file);
                   // Lưu vào trong đối tượng product trong DB
                   // Lưu đối tượng vào product_images
                   ProductImage productImage = productService.createProductImage(existingProduct.getId(),
                           ProductImageDTO
                                   .builder()
                                   .imageUrl(filename)
                                   .build());
                   productImages.add(productImage);
               }
           }
           return ResponseEntity.ok().body(productImages);
       }catch (Exception e) {
           return ResponseEntity.badRequest().body(e.getMessage());
       }
    }

    @GetMapping("/images/{imageName}")
    public ResponseEntity<?> viewImage(@PathVariable(name = "imageName") String imageName) {
        try {
            Path imagePath = Paths.get("uploads/" + imageName);
            UrlResource resource = new UrlResource(imagePath.toUri());

            if(resource.exists()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(resource);
            }else {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(new UrlResource(Paths.get("uploads/notfound404.jpg").toUri()));
            }

        }catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    private String storeFile(MultipartFile file) throws IOException {
        if(!isImageFile(file) || file.getOriginalFilename() == null) {
            throw new IOException("Invalid image format");
        }

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        //Them UUID vao trước tên file để đảm bảo tên file là duy nhất
        String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;
        //Kiểm tra và tạo thư mục mà bạn muốn lưu file
        Path uploadDir = Paths.get("uploads");
        //Kiem tra va tao thu muc neu no khong ton tai
        if(!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        //Duong dan day du den file
        Path destination = Paths.get(uploadDir.toString(), uniqueFileName);
        //Sao chép file vào thư mục đích
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFileName;
    }

    private boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<String> deleteProducts(@PathVariable long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok("Delete product with ID " + id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/generateFakeProducts")
    public ResponseEntity<String> generateFakeProducts() {
        Faker faker = new Faker();
        for(int i = 0; i < 1000; i++) {
            String productName = faker.commerce().productName();
            if(productService.existsByName(productName)) {
                continue;
            }
            ProductDTO productDTO = ProductDTO.builder()
                    .name(productName)
                    .price((float)faker.number().numberBetween(0,90000000))
                    .description(faker.lorem().sentence())
                    .thumbnail("")
                    .categoryId((long) faker.number().numberBetween(1,3))
                    .build();
            try {
                productService.createProduct(productDTO);
            } catch (DataNotFound dataNotFound) {
                return ResponseEntity.badRequest().body(dataNotFound.getMessage());
            }
        }
        return ResponseEntity.ok("Fake products created successfully");
    }

    @GetMapping("/by-ids")
    public ResponseEntity<?> getProductsByIds(@RequestParam("ids") String ids) {
        try {
            List<Long> productIds = Arrays.stream(ids.split(","))
                    .map(Long:: parseLong)
                    .collect(Collectors.toList());
            List<Product> products = productService.findProductsByIds(productIds);
            return ResponseEntity.ok(products);
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
