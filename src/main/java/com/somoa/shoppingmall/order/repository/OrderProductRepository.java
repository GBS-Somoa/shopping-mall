package com.somoa.shoppingmall.order.repository;

import com.somoa.shoppingmall.order.entity.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Integer> {
}
