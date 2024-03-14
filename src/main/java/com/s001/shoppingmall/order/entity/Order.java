package com.s001.shoppingmall.order.entity;

import com.s001.shoppingmall.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Integer id;

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @Column(name = "order_payment_amount", nullable = false)
    private int paymentAmount;

    @Column(name = "order_payment_status", nullable = false)
    private PaymentStatus paymentStatus;

    @Column(name = "order_delivery_fee", nullable = false)
    private int deliveryFee;

    @Column(name = "order_delivery_status", nullable = false)
    private DeliveryStatus deliveryStatus;

    @Column(name = "order_recipient_name", nullable = false)
    private String recipientName;

    @Column(name = "order_recipient_contact", nullable = false)
    private String recipientContact;

    @Column(name = "order_delivery_address", nullable = false)
    private String deliveryAddress;

    @OneToMany(mappedBy = "order")
    private List<OrderProduct> orderProducts = new ArrayList<>();

    @Builder
    public Order(int paymentAmount, PaymentStatus paymentStatus, int deliveryFee, DeliveryStatus deliveryStatus, String recipientName, String recipientContact, String deliveryAddress) {
        this.orderDate = LocalDateTime.now();
        this.paymentAmount = paymentAmount;
        this.paymentStatus = paymentStatus;
        this.deliveryFee = deliveryFee;
        this.deliveryStatus = deliveryStatus;
        this.recipientName = recipientName;
        this.recipientContact = recipientContact;
        this.deliveryAddress = deliveryAddress;
    }
}
