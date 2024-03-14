package com.s001.shoppingmall.order.entity;

import com.s001.shoppingmall.product.entity.Product;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class OrderProductTest {

    @Autowired
    private TestEntityManager entityManager;

    private Product product;
    private Order order;

    @BeforeEach
    public void setUp() {
        dataInit();
    }

    @Test
    void OrderProductLazyLoadingTest() {
        OrderProduct orderProduct = OrderProduct.builder()
                .product(product)
                .order(order)
                .count(2)
                .build();

        entityManager.persist(product);
        entityManager.persist(order);
        entityManager.persist(orderProduct);
        entityManager.flush();

        entityManager.clear();

        OrderProduct found = entityManager.find(OrderProduct.class, orderProduct.getId());

        // product 와 order 로딩 전
        assertThat(Hibernate.isInitialized(found.getProduct())).isFalse();
        assertThat(Hibernate.isInitialized(found.getOrder())).isFalse();

        // product 와 order 로딩 후
        Product foundProduct = found.getProduct();
        assertThat(foundProduct.getName()).isEqualTo(product.getName());
        Order foundOrder = found.getOrder();
        assertThat(foundOrder.getRecipientName()).isEqualTo(order.getRecipientName());
    }

    @Test
    void orderProductBuilderTest() {
        OrderProduct orderProduct = OrderProduct.builder()
                .product(product)
                .order(order)
                .count(2)
                .build();

        assertThat(orderProduct.getProduct().getName()).isEqualTo(product.getName());
        assertThat(orderProduct.getOrder().getRecipientName()).isEqualTo(order.getRecipientName());
        assertThat(order.getOrderProducts().size()).isEqualTo(1);
    }

    void dataInit() {
        product = Product.builder()
                .name("다우니")
                .price(30_000)
                .barcode("4902430341066")
                .reviewCount(0)
                .thumbnailImageUrl("thumbnail.jpg")
                .imageUrl("image.jpg")
                .rating(4.6)
                .build();

        order = Order.builder()
                .deliveryAddress("address1")
                .deliveryFee(2500)
                .paymentAmount(62_500)
                .recipientName("양유경")
                .recipientContact("010-1234-5678")
                .build();
    }
}
