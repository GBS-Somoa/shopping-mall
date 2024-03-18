package com.s001.shoppingmall.order.repository;

import com.s001.shoppingmall.order.entity.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Integer> {
}
