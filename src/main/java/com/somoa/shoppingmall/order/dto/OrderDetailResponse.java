package com.somoa.shoppingmall.order.dto;

import com.somoa.shoppingmall.order.entity.DeliveryStatus;
import com.somoa.shoppingmall.order.entity.Order;
import com.somoa.shoppingmall.order.entity.PaymentStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class OrderDetailResponse {

    private Integer id;
    private LocalDateTime orderDate;
    private int paymentAmount;
    private int deliveryFee;
    private String recipientName;
    private String recipientContact;
    private String deliveryAddress;
    private PaymentStatus paymentStatus;
    private DeliveryStatus deliveryStatus;
    private List<OrderProductDetailResponse> orderProducts;

    public static OrderDetailResponse of(Order order) {
        List<OrderProductDetailResponse> orderProductDetailResponses = order.getOrderProducts().stream()
                .map(OrderProductDetailResponse::of)
                .toList();

        return OrderDetailResponse.builder()
                .id(order.getId())
                .orderDate(order.getOrderDate())
                .paymentAmount(order.getPaymentAmount())
                .deliveryFee(order.getDeliveryFee())
                .recipientName(order.getRecipientName())
                .recipientContact(order.getRecipientContact())
                .deliveryAddress(order.getDeliveryAddress())
                .paymentStatus(order.getPaymentStatus())
                .deliveryStatus(order.getDeliveryStatus())
                .orderProducts(orderProductDetailResponses)
                .build();
    }
}
