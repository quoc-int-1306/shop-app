package com.project.shopapp.Responses;

import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ProductDetailResponse extends BaseResponse{
    private Long id;

    private String name;

    private List<ProductImageResponse> product_images;

    private String thumbnail;

    private String description;

    private Double price;

    private int category_id;
}
