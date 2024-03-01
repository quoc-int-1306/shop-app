package com.project.shopapp.Service.impl;

import com.project.shopapp.Dtos.OrderDTO;
import com.project.shopapp.Exceptions.DataNotFound;
import com.project.shopapp.Exceptions.DateException;
import com.project.shopapp.Models.Order;
import com.project.shopapp.Responses.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IOrderService {
    Order createOrder(OrderDTO orderDTO) throws DataNotFound, DateException;

    Order getOrder(Long id);

    Order updateOrder(Long id, OrderDTO orderDTO) throws DataNotFound;

    void deleteOrder(Long id) throws DataNotFound;

    List<Order> findByUserId(Long userId) throws DateException;

    Page<Order> getOrderByKeyword(String keyword, Pageable pageable);
}
