package com.project.shopapp.Service;

import com.project.shopapp.Dtos.OrderDTO;
import com.project.shopapp.Dtos.OrderDetailDTO;
import com.project.shopapp.Exceptions.DataNotFound;
import com.project.shopapp.Exceptions.DateException;
import com.project.shopapp.Mapper.ObjectMapper;
import com.project.shopapp.Models.Order;
import com.project.shopapp.Models.OrderDetail;
import com.project.shopapp.Models.Product;
import com.project.shopapp.Repository.OrderDetaiRepository;
import com.project.shopapp.Repository.OrderRepository;
import com.project.shopapp.Repository.ProductRepository;
import com.project.shopapp.Responses.OrderDetailResponse;
import com.project.shopapp.Service.impl.IOrderDetailService;
import com.project.shopapp.Service.impl.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DateTimeException;
import java.util.List;

@Service
public class OrderDetailService implements IOrderDetailService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderDetaiRepository orderDetailRepository;



    @Override
    @Transactional
    public OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO) throws DataNotFound {
        Order order = orderRepository.findById(orderDetailDTO.getOrderId())
                .orElseThrow(() -> new DataNotFound("Cannot find Order with id " + orderDetailDTO.getOrderId()));
        Product product = productRepository.findById(orderDetailDTO.getOrderId())
                .orElseThrow(() -> new DataNotFound("Cannot find Product with id " + orderDetailDTO.getProductId()));
        OrderDetail orderDetail = OrderDetail.builder()
                .order(order)
                .product(product)
                .price(orderDetailDTO.getPrice())
                .numberOfProducts(orderDetailDTO.getNumberOfProducts())
                .totalMoney(orderDetailDTO.getTotalMoney())
                .color(orderDetailDTO.getColor())
                .build();
        return orderDetailRepository.save(orderDetail);
    }

    @Override
    public OrderDetail getOderDetail(Long id) throws DataNotFound {
        return orderDetailRepository.findById(id)
                .orElseThrow(() -> new DataNotFound("Cannot find OrderDetail with id "+id));
    }

    @Override
    @Transactional
    public OrderDetail updateOrderDetail(Long id, OrderDetailDTO newOrderDetail) throws DataNotFound {
        OrderDetail existingOrderDetail = orderDetailRepository.findById(id)
                .orElseThrow(() -> new DataNotFound("Cannot find OrderDetail by id" + id));
        Order existingOrder = orderRepository.findById(newOrderDetail.getOrderId())
                .orElseThrow(() -> new DataNotFound("Cannot find Order by orderId "+ newOrderDetail.getOrderId()));
        Product existingProduct = productRepository.findById(newOrderDetail.getProductId())
                .orElseThrow(() -> new DataNotFound("Cannot find Product by Product Id "+ newOrderDetail.getProductId()));
        existingOrderDetail.setPrice(newOrderDetail.getPrice());
        existingOrderDetail.setNumberOfProducts(newOrderDetail.getNumberOfProducts());
        existingOrderDetail.setTotalMoney(newOrderDetail.getTotalMoney());
        existingOrderDetail.setColor(newOrderDetail.getColor());
        existingOrderDetail.setProduct(existingProduct);
        existingOrderDetail.setOrder(existingOrder);
        return orderDetailRepository.save(existingOrderDetail);
    }

    @Override
    @Transactional
    public void deleteOrderDetail(Long id) {
        orderDetailRepository.deleteById(id);
    }

    @Override
    public List<OrderDetailResponse> findByOrderId(Long id) {
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(id);
        return orderDetails.stream()
                .map(orderDetail -> ObjectMapper.fromOrderToOrderDetailResponse(orderDetail)).toList();
    }
}
