package com.project.shopapp.Responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.Models.BaseEntity;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ProductResponse extends BaseEntity {

    private Long id;

    private String name;

    private Float price;

    private String thumbnail;

    private List<ProductImageResponse> product_images;

    private String description;
    @JsonProperty("category_id")
    private Long categoryId;
}
