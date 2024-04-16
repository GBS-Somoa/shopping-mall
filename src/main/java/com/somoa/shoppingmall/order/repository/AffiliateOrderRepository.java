package com.somoa.shoppingmall.order.repository;

import com.somoa.shoppingmall.order.entity.AffiliateOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AffiliateOrderRepository extends JpaRepository<AffiliateOrder, Integer> {

	boolean existsByOrderId(Integer orderId);
}
