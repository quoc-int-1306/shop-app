package com.project.shopapp.Service;

import com.project.shopapp.Dtos.CartItemDTO;
import com.project.shopapp.Dtos.OrderDTO;
import com.project.shopapp.Exceptions.DataNotFound;
import com.project.shopapp.Exceptions.DateException;
import com.project.shopapp.Models.*;
import com.project.shopapp.Repository.OrderDetaiRepository;
import com.project.shopapp.Repository.OrderRepository;
import com.project.shopapp.Repository.ProductRepository;
import com.project.shopapp.Repository.UserRepository;
import com.project.shopapp.Responses.OrderResponse;
import com.project.shopapp.Service.impl.IOrderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderService implements IOrderService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderDetaiRepository orderDetaiRepository;
    @Autowired
    private  ModelMapper modelMapper;

    @Override
    @Transactional
    public Order createOrder(OrderDTO orderDTO) throws DateException, DataNotFound {
        // Tim userId co ton tai khong
        User user = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new DataNotFound("Cannot find user with id: "+ orderDTO.getUserId()));
        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));
        Order order = new Order();
        modelMapper.map(orderDTO, order);
        order.setUser(user);
        order.setOrderDate(new Date());
        order.setStatus(OrderStatus.PENDING);
        // Kiem tra shipping date phai > thoi diem hien tai
        LocalDate shippingDate = orderDTO.getShippingDate() == null ? LocalDate.now() :  orderDTO.getShippingDate();
        if(shippingDate == null || shippingDate.isBefore(LocalDate.now())) {
            throw new DateException("Date must be at least today!");
        }
        order.setShippingDate(shippingDate);
        order.setActive(true);
        order.setTotalMoney(orderDTO.getTotalMoney());
        orderRepository.save(order);
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (CartItemDTO cartItemDTO: orderDTO.getCartItems()) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);

            Long productId = cartItemDTO.getProductId();
            int quantity = cartItemDTO.getQuantity();

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new DataNotFound("Product not found with id: "+ productId));

            orderDetail.setProduct(product);
            orderDetail.setNumberOfProducts(quantity);
            orderDetail.setPrice(product.getPrice());
            orderDetail.setTotalMoney(product.getPrice() * quantity);
            orderDetails.add(orderDetail);
        }
        orderDetaiRepository.saveAll(orderDetails);
        return order;
    }

    @Override
    public Order getOrder(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Order updateOrder(Long id, OrderDTO orderDTO) throws DataNotFound {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new DataNotFound("Cannot find order with id: "+ id));
        User existingUser = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new DataNotFound("Cannot find order with id: "+ orderDTO.getUserId()));
        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));
        modelMapper.map(orderDTO, order);
        order.setUser(existingUser);
        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public void deleteOrder(Long id) throws DataNotFound {
        Order order = orderRepository.findById(id)
                .orElse(null);
        if(order != null) {
            order.setActive(false);
            orderRepository.save(order);
        }else {
            throw new DataNotFound("Cannot find order by id" + id);
        }
    }

    @Override
    public List<Order> findByUserId(Long userId) throws DateException {
        List<Order> orders = orderRepository.findByUserId(userId);
        if(orders.size() == 0) {
            throw new DateException("Not found orders by user id");
        }else {
            return orders;
        }
    }

    @Override
    public Page<Order> getOrderByKeyword(String keyword, Pageable pageable) {
        return orderRepository.findByKeyword(keyword, pageable);
    }
}
