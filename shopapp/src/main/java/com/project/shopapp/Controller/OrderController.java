package com.project.shopapp.Controller;

import com.project.shopapp.Dtos.OrderDTO;
import com.project.shopapp.Exceptions.DataNotFound;
import com.project.shopapp.Mapper.ObjectMapper;
import com.project.shopapp.Models.Order;
import com.project.shopapp.Responses.OrderListResponse;
import com.project.shopapp.Responses.OrderResponse;
import com.project.shopapp.Service.impl.IOrderService;
import com.project.shopapp.Utils.LocalizationUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/orders")
public class OrderController {

    @Autowired
    private IOrderService orderService;
    @Autowired
    private LocalizationUtils localizationUtils;

    @PostMapping
    public ResponseEntity<?> createOrder(
            @RequestBody @Valid OrderDTO orderDTO,
            BindingResult result) {
        System.out.println(orderDTO.toString());
            try {
                if(result.hasErrors()) {
                    List<String> errorMessages = result.getFieldErrors()
                            .stream()
                            .map(FieldError::getDefaultMessage)
                            .toList();
                    return ResponseEntity.badRequest().body(errorMessages);
                }
                Order orderResponse = orderService.createOrder(orderDTO);
                    return ResponseEntity.ok(orderResponse);
            }catch (Exception e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
    }

    @GetMapping("/users/{user_id}")
    public ResponseEntity<?> getOrders(
            @Valid @PathVariable("user_id") Long userId
    ) {
        try {
            List<Order> orders = orderService.findByUserId(userId);
            return ResponseEntity.ok(orders);
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrder(
            @Valid @PathVariable("id") Long id
    ) {
        try {
            Order existingOrder = orderService.getOrder(id);
            OrderResponse orderResponse = ObjectMapper.fromOrderToOrderResponse(existingOrder);
            return ResponseEntity.ok(orderResponse);
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(
            @Valid @PathVariable("id") Long id,
            @Valid @RequestBody OrderDTO orderDTO
    ) {
        try {
            Order order = orderService.updateOrder(id, orderDTO);
            OrderResponse orderResponse = ObjectMapper.fromOrderToOrderResponse(order);
            return ResponseEntity.ok(orderResponse);
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(
            @Valid @PathVariable Long id
    ) throws DataNotFound {
        try {
            orderService.deleteOrder(id);
            return ResponseEntity.ok("Xóa thành công order id " + id);
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/get-orders-by-keyword")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<OrderListResponse> getOrderByKeyword(@RequestParam(defaultValue = "") String keyword,
                                                               @RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "10") int limit
                                                         ) {
        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("id").descending());

        Page<OrderResponse> orderPage = orderService.getOrderByKeyword(keyword, pageRequest)
                                                    .map(OrderResponse:: fromOrderToOrderResponse);

        int totalPages = orderPage.getTotalPages();
        List<OrderResponse> orderResponses = orderPage.getContent();
        return ResponseEntity.ok(OrderListResponse
                .builder()
                .orderResponses(orderResponses)
                .totalPages(totalPages)
                .build());
    }
}
