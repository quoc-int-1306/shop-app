package com.project.shopapp.Responses;

import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class OrderListResponse {
    private List<OrderResponse> orderResponses;
    private int totalPages;
}
