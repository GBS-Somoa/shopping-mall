package com.somoa.shoppingmall.order.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "affiliate_order")
public class AffiliateOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "affiliate_order_id")
    private Integer id;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Builder
    public AffiliateOrder(Order order) {
        this.order = order;
        order.setAffiliateOrder(this);
    }
}
