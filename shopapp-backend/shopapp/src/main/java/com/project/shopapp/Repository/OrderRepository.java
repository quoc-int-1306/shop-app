package com.project.shopapp.Repository;

import com.project.shopapp.Models.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long id);

    @Query("SELECT o FROM Order o WHERE o.active = true and (:keyword IS NULL OR :keyword = '' OR " +
            "o.fullName LIKE %:keyword% " +
            "OR o.address LIKE %:keyword% " +
            "OR o.note LIKE %:keyword% " +
            "OR o.email LIKE %:keyword%)")
    Page<Order> findByKeyword(String keyword, Pageable pageable);
}
