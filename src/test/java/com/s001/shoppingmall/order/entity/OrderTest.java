package com.s001.shoppingmall.order.entity;

import com.s001.shoppingmall.product.entity.Product;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class OrderTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void orderBuilderTest() {
        Order order = Order.builder()
                .deliveryAddress("address1")
                .deliveryFee(2500)
                .paymentAmount(62_500)
                .recipientName("양유경")
                .recipientContact("010-1234-5678")
                .build();

        assertThat(order.getDeliveryAddress()).isEqualTo("address1");
        assertThat(order.getDeliveryFee()).isEqualTo(2500);
        assertThat(order.getPaymentAmount()).isEqualTo(62_500);
        assertThat(order.getRecipientName()).isEqualTo("양유경");
        assertThat(order.getRecipientContact()).isEqualTo("010-1234-5678");
        assertThat(order.getPaymentStatus()).isEqualTo(PaymentStatus.COMPLETED);
        assertThat(order.getDeliveryStatus()).isEqualTo(DeliveryStatus.ORDER_COMPLETED);
    }

    @Test
    void orderLazyLoadingTest() {

        Product product = Product.builder()
                .name("다우니")
                .price(30_000)
                .barcode("4902430341066")
                .reviewCount(0)
                .thumbnailImageUrl("thumbnail.jpg")
                .imageUrl("image.jpg")
                .rating(4.6)
                .build();

        Order order = Order.builder()
                .deliveryAddress("address1")
                .deliveryFee(2500)
                .paymentAmount(62_500)
                .recipientName("양유경")
                .recipientContact("010-1234-5678")
                .build();

        OrderProduct orderProduct = OrderProduct.builder()
                .product(product)
                .order(order)
                .count(2)
                .build();

        Integer orderId = entityManager.persist(order).getId();
        entityManager.persist(product);
        entityManager.persist(orderProduct);
        entityManager.flush();

        entityManager.clear();

        Order foundOrder = entityManager.find(Order.class, orderId);
        assertThat(foundOrder).isNotEqualTo(null);

        // orderProduct 로딩 전
        assertThat(foundOrder.getRecipientName()).isEqualTo(order.getRecipientName());
        assertThat(Hibernate.isInitialized(foundOrder.getOrderProducts())).isEqualTo(false);

        // orderProduct 로딩 후
        List<OrderProduct> orderProducts = foundOrder.getOrderProducts();
        for (OrderProduct op : orderProducts) {
            assertThat(Hibernate.isInitialized(op.getProduct())).isEqualTo(false);
        }
    }
}
