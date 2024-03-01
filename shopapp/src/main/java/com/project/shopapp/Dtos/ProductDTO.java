package com.project.shopapp.Dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@Setter
@Getter
public class ProductDTO {
    @NotEmpty(message = "Product's name cannot be empty")
    @Size(min = 3, max = 200, message = "Title must be between 3 and 200 characters")
    private String name;

    @Min(value = 0, message = "Price must be greater than or equal to 0")
    @Max(value = 10000000, message = "Price must be less than or equal to 10,000,000")
    private Float price;

    @Min(value = 0, message = "Discount must be greater than or equal to 0")
    @Max(value = 100, message = "Discount must be less than or equal to 100")
    private String thumbnail;

    private String description;

    @NotNull(message = "Category ID is required")
    @JsonProperty("category_id")
    private Long categoryId;

//    @NotNull(message = "color ID is required")
//    @JsonProperty("color_id")
//    private Long colorId;

//    private List<MultipartFile> files;

    public ProductDTO() {
        this.thumbnail = "";
    }
}
