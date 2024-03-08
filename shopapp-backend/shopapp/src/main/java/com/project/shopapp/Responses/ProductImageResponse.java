package com.project.shopapp.Responses;

import lombok.*;

@Data
@NoArgsConstructor
@Setter
@Getter
@Builder
public class ProductImageResponse {
    private Long id;

    private String image_url;

    public ProductImageResponse(Long id, String image_url) {
        this.id = id;
        this.image_url = image_url;
    }
}
