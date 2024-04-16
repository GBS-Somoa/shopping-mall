package com.somoa.shoppingmall.order.dto;

import com.somoa.shoppingmall.order.entity.DeliveryStatus;
import com.somoa.shoppingmall.order.entity.PaymentStatus;
import lombok.Data;

@Data
public class OrderUpdateParam {

    PaymentStatus paymentStatus;
    DeliveryStatus deliveryStatus;
}
