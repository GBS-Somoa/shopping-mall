package com.s001.shoppingmall.order.dto;

import com.s001.shoppingmall.order.entity.DeliveryStatus;
import com.s001.shoppingmall.order.entity.PaymentStatus;
import lombok.Data;

@Data
public class OrderUpdateParam {

    PaymentStatus paymentStatus;
    DeliveryStatus deliveryStatus;
}
