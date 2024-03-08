package com.project.shopapp.Dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.Models.Product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ProductImageDTO {
    @JsonProperty("product_id")
    @Min(value = 1, message = "Product's ID must be > 0")
    private Long productId;

    @JsonProperty( "image_url")
    @Size(min = 5, max = 200,message = "Image's name")
    private String imageUrl;
}
