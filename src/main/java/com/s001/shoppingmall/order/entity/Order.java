package com.s001.shoppingmall.order.entity;

import com.s001.shoppingmall.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

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
    @Enumerated(EnumType.STRING)
    @Setter
    private PaymentStatus paymentStatus;

    @Column(name = "order_delivery_fee", nullable = false)
    private int deliveryFee;

    @Column(name = "order_delivery_status", nullable = false)
    @Enumerated(EnumType.STRING)
    @Setter
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
    public Order(int paymentAmount, int deliveryFee, String recipientName, String recipientContact, String deliveryAddress) {
        this.orderDate = LocalDateTime.now();
        this.paymentAmount = paymentAmount;
        this.deliveryFee = deliveryFee;
        this.recipientName = recipientName;
        this.recipientContact = recipientContact;
        this.deliveryAddress = deliveryAddress;
        this.paymentStatus = PaymentStatus.COMPLETED;
        this.deliveryStatus = DeliveryStatus.ORDER_COMPLETED;
    }

    public void calculatePaymentAmount() {
        int totalPrice = orderProducts.stream()
                .mapToInt(OrderProduct::getTotalPrice)
                .sum();
        this.paymentAmount = totalPrice + deliveryFee;
    }
}
