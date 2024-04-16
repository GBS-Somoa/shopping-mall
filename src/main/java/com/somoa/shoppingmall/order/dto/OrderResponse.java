package com.somoa.shoppingmall.order.dto;


import com.somoa.shoppingmall.order.entity.DeliveryStatus;
import com.somoa.shoppingmall.order.entity.Order;
import com.somoa.shoppingmall.order.entity.PaymentStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderResponse {

    private Integer id;
    private String recipientName;
    private PaymentStatus paymentStatus;
    private DeliveryStatus deliveryStatus;

    public static OrderResponse of(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .recipientName(order.getRecipientName())
                .paymentStatus(order.getPaymentStatus())
                .deliveryStatus(order.getDeliveryStatus())
                .build();
    }
}
