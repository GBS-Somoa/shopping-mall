package com.s001.shoppingmall.order.repository;

import com.s001.shoppingmall.order.entity.Order;
import com.s001.shoppingmall.order.entity.OrderProduct;
import com.s001.shoppingmall.product.entity.Product;
import com.s001.shoppingmall.product.repository.ProductRepository;
import jakarta.persistence.EntityManager;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    OrderProductRepository orderProductRepository;

    @Autowired
    private ProductRepository productRepository;

    private final Integer[] productIds = new Integer[2];
    private Order order;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    public void setUp() {
        dataInit();
    }

    @Test
    void findByIdWithOrderProducts() {
        Optional<Order> foundOptional = orderRepository.findByIdWithOrderProducts(order.getId());

        assertThat(foundOptional).isPresent();
        Order found = foundOptional.get();

        assertThat(Hibernate.isInitialized(found.getOrderProducts())).isTrue();
        List<OrderProduct> foundOrderProducts = found.getOrderProducts();

        assertThat(foundOrderProducts.size()).isEqualTo(2);
        for (OrderProduct op : foundOrderProducts) {
            assertThat(Hibernate.isInitialized(op.getProduct())).isTrue();
        }

        assertThat(foundOrderProducts.stream().map(o -> o.getProduct().getId())).contains(productIds);
    }

    void dataInit() {
        Product[] products = new Product[2];

        products[0] = productRepository.save(Product.builder()
                .name("다우니")
                .price(8_000)
                .barcode("4902430341066")
                .reviewCount(0)
                .thumbnailImageUrl("thumbnail1.jpg")
                .imageUrl("image2.jpg")
                .rating(4.8)
                .build());

        products[1] = productRepository.save(Product.builder()
                .name("퍼실")
                .price(2_000)
                .barcode("4902412152515")
                .reviewCount(0)
                .thumbnailImageUrl("thumbnail1.jpg")
                .imageUrl("image2.jpg")
                .rating(4.2)
                .build());

        productIds[0] = products[0].getId();
        productIds[1] = products[1].getId();

        order = Order.builder()
                .deliveryAddress("address1")
                .deliveryFee(2_500)
                .paymentAmount(14_500)
                .recipientName("양유경")
                .recipientContact("010-1234-5678")
                .build();
        orderRepository.save(order);

        for (int i = 0; i < 2; ++i) {
            orderProductRepository.save(OrderProduct.builder()
                    .order(order)
                    .product(products[i])
                    .count(i + 1)
                    .build());
        }

        entityManager.flush();
        entityManager.clear();
    }
}
