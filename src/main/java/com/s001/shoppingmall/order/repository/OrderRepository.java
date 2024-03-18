package com.s001.shoppingmall.order.repository;

import com.s001.shoppingmall.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.orderProducts op LEFT JOIN FETCH op.product p WHERE o.id = :id")
    Optional<Order> findByIdWithOrderProducts(@Param("id") Integer id);
}
