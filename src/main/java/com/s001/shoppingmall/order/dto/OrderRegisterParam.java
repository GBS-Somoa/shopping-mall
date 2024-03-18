package com.s001.shoppingmall.order.dto;

import com.s001.shoppingmall.order.entity.Order;
import lombok.Data;

import java.util.List;

@Data
public class OrderRegisterParam {

    private Integer paymentAmount;
    private Integer deliveryFee;
    private String recipientName;
    private String recipientContact;
    private String deliveryAddress;
    private List<OrderProductRegisterParam> orderProducts;

    public Order toEntity() {
        return Order.builder()
                .paymentAmount(paymentAmount)
                .deliveryFee(deliveryFee)
                .recipientName(recipientName)
                .recipientContact(recipientContact)
                .deliveryAddress(deliveryAddress)
                .build();
    }
}
