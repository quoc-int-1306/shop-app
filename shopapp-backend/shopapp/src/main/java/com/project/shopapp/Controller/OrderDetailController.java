package com.project.shopapp.Controller;

import com.project.shopapp.Dtos.OrderDetailDTO;
import com.project.shopapp.Exceptions.DataNotFound;
import com.project.shopapp.Mapper.ObjectMapper;
import com.project.shopapp.Models.OrderDetail;
import com.project.shopapp.Responses.OrderDetailResponse;
import com.project.shopapp.Service.impl.IOrderDetailService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("${api.prefix}/order_details")
public class OrderDetailController {

    @Autowired
    private IOrderDetailService orderDetailService;

    @PostMapping
    public ResponseEntity<?> createOrderDetail(
            @Valid @RequestBody OrderDetailDTO orderDetailDTO,
            BindingResult result
            ) {
        try {
            OrderDetail newOrderDetail = orderDetailService.createOrderDetail(orderDetailDTO);
            return ResponseEntity.ok().body(ObjectMapper.fromOrderToOrderDetailResponse(newOrderDetail));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetail(
            @Valid @PathVariable("id") Long id
    ) {
        try {
            OrderDetail orderDetail = orderDetailService.getOderDetail(id);
            return ResponseEntity.ok(ObjectMapper.fromOrderToOrderDetailResponse(orderDetail));
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getOrderDetails(
            @Valid @PathVariable("orderId") Long orderId
    ) {
        try {
            List<OrderDetailResponse> orderDetails = orderDetailService.findByOrderId(orderId);
            return ResponseEntity.ok(orderDetails);
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateOderDetail(
            @Valid @PathVariable("id") Long id,
            @RequestBody OrderDetailDTO newOrderDetailData
    ) {
        try {
            OrderDetail orderDetail = orderDetailService.updateOrderDetail(id, newOrderDetailData);
            return ResponseEntity.ok(ObjectMapper.fromOrderToOrderDetailResponse(orderDetail));
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderDetail(
            @Valid @PathVariable("id") Long id
    ) {
        try {
            orderDetailService.deleteOrderDetail(id);
            return ResponseEntity.ok().body("Delete orderdetail successfully with id" + id);
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
