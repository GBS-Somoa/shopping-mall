package com.s001.shoppingmall.order.service;

import com.s001.shoppingmall.order.dto.*;
import com.s001.shoppingmall.order.entity.Order;
import com.s001.shoppingmall.order.entity.OrderProduct;
import com.s001.shoppingmall.order.repository.OrderProductRepository;
import com.s001.shoppingmall.order.repository.OrderRepository;
import com.s001.shoppingmall.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final ProductRepository productRepository;

    @Transactional
    public Integer save(OrderRegisterParam param) {
        Order order = orderRepository.save(param.toEntity());

        Map<Integer, Integer> countMap = getCountMap(param.getOrderProducts());
        List<OrderProduct> orderProducts = productRepository.findAllById(countMap.keySet()).stream()
                .map(product -> OrderProduct.builder()
                        .product(product)
                        .order(order)
                        .count(countMap.get(product.getId()))
                        .build()
                ).toList();
        orderProductRepository.saveAll(orderProducts);

        return order.getId();
    }

    @Transactional
    public void update(Integer id, OrderUpdateParam param) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("invalid order id."));

        if (param.getPaymentStatus() != null) {
            order.setPaymentStatus(param.getPaymentStatus());
        }
        if (param.getDeliveryStatus() != null) {
            order.setDeliveryStatus(param.getDeliveryStatus());
        }
    }

    public Page<OrderResponse> findAll(Pageable pageable) {
        return orderRepository.findAll(pageable).map(OrderResponse::of);
    }

    public OrderDetailResponse findOne(Integer id) {
        Order order = orderRepository.findByIdWithOrderProducts(id)
                .orElseThrow(() -> new IllegalArgumentException("invalid order id."));
        return OrderDetailResponse.of(order);
    }

    public Map<Integer, Integer> getCountMap(List<OrderProductRegisterParam> params) {
        return params.stream()
                .collect(Collectors.toMap(
                        OrderProductRegisterParam::getProductId,
                        OrderProductRegisterParam::getCount
                ));
    }
}
