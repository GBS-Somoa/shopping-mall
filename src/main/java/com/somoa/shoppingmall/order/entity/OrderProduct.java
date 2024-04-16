package com.somoa.shoppingmall.order.entity;

import com.somoa.shoppingmall.common.entity.BaseEntity;
import com.somoa.shoppingmall.product.entity.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "order_product")
public class OrderProduct extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_product_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "order_product_amount", nullable = false)
    private int count;

    @Builder
    public OrderProduct(Order order, Product product, int count) {
        setOrder(order);
        this.product = product;
        this.count = count;
    }

    private void setOrder(Order order) {
        this.order = order;
        order.getOrderProducts().add(this);
    }

    public int getTotalPrice() {
        return product.getPrice() * count;
    }
}
