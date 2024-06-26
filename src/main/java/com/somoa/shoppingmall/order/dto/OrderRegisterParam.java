package com.somoa.shoppingmall.order.dto;

import com.somoa.shoppingmall.order.entity.Order;
import lombok.Data;

import java.util.List;

@Data
public class OrderRegisterParam {

    private int deliveryFee;
    private String recipientName;
    private String recipientContact;
    private String deliveryAddress;
    private List<OrderProductRegisterParam> orderProducts;
    private AffiliateParam affiliateParam;

    public Order toEntity() {
        return Order.builder()
                .deliveryFee(deliveryFee)
                .recipientName(recipientName)
                .recipientContact(recipientContact)
                .deliveryAddress(deliveryAddress)
                .build();
    }
}
