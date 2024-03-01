package com.project.shopapp.Service.impl;

import com.project.shopapp.Dtos.OrderDetailDTO;
import com.project.shopapp.Exceptions.DataNotFound;
import com.project.shopapp.Models.OrderDetail;
import com.project.shopapp.Responses.OrderDetailResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IOrderDetailService {
    OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO) throws DataNotFound;

    OrderDetail getOderDetail(Long id) throws DataNotFound;

    OrderDetail updateOrderDetail(Long id, OrderDetailDTO newOrderDetail) throws DataNotFound;

    void deleteOrderDetail(Long id);

    List<OrderDetailResponse> findByOrderId(Long id);
}
