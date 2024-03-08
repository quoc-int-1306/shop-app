package com.project.shopapp.Repository;

import com.project.shopapp.Models.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDetaiRepository extends JpaRepository<OrderDetail, Long> {
    List<OrderDetail> findByOrderId(Long orderId);
}
