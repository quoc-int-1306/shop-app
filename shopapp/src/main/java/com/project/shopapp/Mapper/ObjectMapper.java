package com.project.shopapp.Mapper;

import com.project.shopapp.Dtos.OrderDetailDTO;
import com.project.shopapp.Models.*;
import com.project.shopapp.Responses.*;

import java.util.List;

public class ObjectMapper {
    public static ProductResponse fromProductToProductResponseMapper(Product product) {
        ProductResponse productResponse = ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .thumbnail(product.getThumbnail())
                .description(product.getDescription())
                .categoryId(product.getCategory().getId())
                .build();
        productResponse.setCreatedAt(product.getCreatedAt());
        productResponse.setUpdatedAt(product.getUpdatedAt());
        return productResponse;
    }

    public static ProductResponse fromProductToProductDetailResponseMapper(Product product, List<ProductImageResponse> images) {
        ProductResponse productResponse = ProductResponse.builder()
                .name(product.getName())
                .price(product.getPrice())
                .thumbnail(product.getThumbnail())
                .categoryId(product.getCategory().getId())
                .description(product.getDescription())
                .product_images(images)
                .build();
        productResponse.setCreatedAt(product.getCreatedAt());
        productResponse.setUpdatedAt(product.getUpdatedAt());
        return productResponse;
    }

    public static OrderDetailResponse fromOrderToOrderDetailResponse(OrderDetail orderDetail) {
        return  OrderDetailResponse.builder()
                .id(orderDetail.getId())
                .productId(orderDetail.getProduct().getId())
                .orderId(orderDetail.getOrder().getId())
                .numberOfProducts(orderDetail.getNumberOfProducts())
                .totalMoney(orderDetail.getTotalMoney())
                .price(orderDetail.getPrice())
                .color(orderDetail.getColor())
                .build();
    }

    public static UserResponse fromUserToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .dateOfBirth(user.getDateOfBirth())
                .active(user.isActive())
                .phoneNumber(user.getPhoneNumber())
                .facebookAccountId(user.getFacebookAccountId())
                .googleAccountId(user.getGoogleAccountId())
                .role(user.getRole())
                .build();
    }

    public static OrderResponse fromOrderToOrderResponse(Order order) {
        OrderResponse orderResponse =  OrderResponse
                .builder()
                .id(order.getId())
                .userId(order.getId())
                .fullName(order.getFullName())
                .phoneNumber(order.getPhoneNumber())
                .address(order.getAddress())
                .note(order.getNote())
                .orderDate(order.getOrderDate())
                .status(order.getStatus())
                .totalMoney(order.getTotalMoney())
                .shippingMethod(order.getShippingMethod())
                .shippingAddress(order.getShippingAddress())
                .shippingDate(order.getShippingDate())
                .paymentMethod(order.getPaymentMethod())
                .orderDetails(order.getOrderDetails())
                .build();
        return orderResponse;
    }
}
