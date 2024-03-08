package com.project.shopapp.Responses;

import com.project.shopapp.Models.Product;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ProductListResponse {
    List<ProductResponse> products;
    private int totalPages;
}
