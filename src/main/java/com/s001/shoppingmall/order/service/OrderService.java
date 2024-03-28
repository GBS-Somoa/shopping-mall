package com.s001.shoppingmall.order.service;

import com.s001.shoppingmall.affiliate.dto.OrderPostApiRequest;
import com.s001.shoppingmall.affiliate.dto.OrderStatusPatchApiRequest;
import com.s001.shoppingmall.affiliate.service.SomoaApiService;
import com.s001.shoppingmall.order.dto.*;
import com.s001.shoppingmall.order.entity.AffiliateOrder;
import com.s001.shoppingmall.order.entity.Order;
import com.s001.shoppingmall.order.entity.OrderProduct;
import com.s001.shoppingmall.order.repository.AffiliateOrderRepository;
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
    private final AffiliateOrderRepository affiliateOrderRepository;

    // 소모아 서비스 API 서비스
    private final SomoaApiService somoaApiService;

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

        order.calculatePaymentAmount();
        orderProductRepository.saveAll(orderProducts);

        if (param.getAffiliateParam() != null) {
            AffiliateOrder affiliateOrder = AffiliateOrder.builder()
                    .order(order)
                    .build();
            affiliateOrderRepository.save(affiliateOrder);

            // 서비스 앱 측에 주문 정보 보내기
            AffiliateParam affiliateParam = param.getAffiliateParam();
            Integer groupId = affiliateParam.getGroupId();
            Integer userId = affiliateParam.getUserId();
            String supplyId = affiliateParam.getSupplyId();

            OrderPostApiRequest apiRequest = OrderPostApiRequest.of(groupId, userId, supplyId, order);

            log.info("서비스 앱 주문 생성 api 호출 - apiRequest={}", apiRequest);
            boolean succeed = somoaApiService.callOrderSaveApi(apiRequest);
            log.info("서비스 앱 주문 생성 api 호출 - succeed={}", succeed);
            if (!succeed) {
                throw new RuntimeException("소모아 앱 API 호출 실패!");
            }
        }
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

            boolean isAffiliateOrder = affiliateOrderRepository.existsByOrderId(id);
            if (isAffiliateOrder) {
                // 서비스 측에 주문 상태 변경 이벤트 알림 보내기
                OrderStatusPatchApiRequest apiRequest = OrderStatusPatchApiRequest.of(param.getDeliveryStatus());
                log.info("서비스 앱 주문 상태 변경 api 호출 - apiRequest={}", apiRequest);
                boolean succeed = somoaApiService.callOrderStatusChangeApi(id, apiRequest);
                log.info("서비스 앱 주문 상태 변경 api 호출 - succeed={}", succeed);
                if (!succeed) {
                    throw new RuntimeException("소모아 앱 API 호출 실패!");
                }
            }
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

    private Map<Integer, Integer> getCountMap(List<OrderProductRegisterParam> params) {
        return params.stream()
                .collect(Collectors.toMap(
                        OrderProductRegisterParam::getProductId,
                        OrderProductRegisterParam::getCount
                ));
    }
}
