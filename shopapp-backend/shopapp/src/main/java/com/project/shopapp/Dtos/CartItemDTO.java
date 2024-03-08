package com.project.shopapp.Dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CartItemDTO {

    @JsonProperty("product_id")
    private Long productId;

    @JsonProperty("quantity")
    private Integer quantity;
}
