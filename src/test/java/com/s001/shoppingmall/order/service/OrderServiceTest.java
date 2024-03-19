package com.s001.shoppingmall.order.service;

import com.s001.shoppingmall.order.dto.*;
import com.s001.shoppingmall.order.entity.DeliveryStatus;
import com.s001.shoppingmall.order.entity.Order;
import com.s001.shoppingmall.order.entity.PaymentStatus;
import com.s001.shoppingmall.order.repository.OrderProductRepository;
import com.s001.shoppingmall.order.repository.OrderRepository;
import com.s001.shoppingmall.product.entity.Product;
import com.s001.shoppingmall.product.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderProductRepository orderProductRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderService orderService;

    private static final int ORDER_PAYMENT_AMOUNT = 14_500;
    private static final int ORDER_DELIVERY_FEE = 2_500;
    private static final String ORDER_RECIPIENT_NAME = "양유경";
    private static final String ORDER_RECIPIENT_CONTACT = "010-1234-5678";
    private static final String ORDER_DELIVERY_ADDRESS = "address1";


    @Test
    @DisplayName("주문 등록에 성공한다.")
    void saveTest() {
        OrderRegisterParam orderRegisterParam = new OrderRegisterParam();
        orderRegisterParam.setPaymentAmount(ORDER_PAYMENT_AMOUNT);
        orderRegisterParam.setDeliveryFee(ORDER_DELIVERY_FEE);
        orderRegisterParam.setRecipientName(ORDER_RECIPIENT_NAME);
        orderRegisterParam.setRecipientContact(ORDER_RECIPIENT_CONTACT);
        orderRegisterParam.setDeliveryAddress(ORDER_DELIVERY_ADDRESS);
        orderRegisterParam.setOrderProducts(getDummyOrderProducts());

        Integer orderId = 1;
        Order savedOrder = getDummyOrder(orderId);

        Product product1 = getDummyProduct(1);
        Product product2 = getDummyProduct(2);

        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);
        when(productRepository.findAllById(any(Collection.class))).thenReturn(Arrays.asList(product1, product2));

        Integer savedId = orderService.save(orderRegisterParam);

        verify(orderRepository, times(1)).save(any(Order.class));
        verify(orderProductRepository, times(1)).saveAll(anyList());
        assertThat(savedId).isEqualTo(orderId);
        assertThat(savedOrder.getOrderProducts().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("주문 수정에 성공한다.")
    void updateTest() {
        Integer orderId = 1;
        OrderUpdateParam updateParam = new OrderUpdateParam();
        updateParam.setPaymentStatus(PaymentStatus.REFUNDED);

        Order existingOrder = getDummyOrder(orderId);
        ReflectionTestUtils.setField(existingOrder, "paymentStatus", PaymentStatus.COMPLETED);
        ReflectionTestUtils.setField(existingOrder, "deliveryStatus", DeliveryStatus.ORDER_COMPLETED);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));

        orderService.update(orderId, updateParam);

        assertThat(existingOrder.getPaymentStatus()).isEqualTo(PaymentStatus.REFUNDED);
        assertThat(existingOrder.getDeliveryStatus()).isEqualTo(DeliveryStatus.ORDER_COMPLETED);
        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    @DisplayName("유효하지 않은 주문 ID로 주문 수정에 실패한다.")
    void updateTest_Fail() {
        Integer orderId = 99;
        OrderUpdateParam updateParam = new OrderUpdateParam();

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.update(orderId, updateParam), "invalid order id.");
        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    @DisplayName("주문 목록 조회에 성공한다.")
    void findAllTest() {
        Order order1 = getDummyOrder(1);
        Order order2 = getDummyOrder(2);
        Pageable pageable = PageRequest.of(0, 2);
        Page<Order> ordersPage = new PageImpl<>(Arrays.asList(order1, order2), pageable, 2);

        when(orderRepository.findAll(any(PageRequest.class))).thenReturn(ordersPage);
        orderRepository.findAll(pageable);

        verify(orderRepository, times(1)).findAll(PageRequest.of(0, 2));
        assertThat(ordersPage.getContent().size()).isEqualTo(ordersPage.getSize());
    }

    @Test
    @DisplayName("주문 상세 조회에 성공한다.")
    void findOneTest() {
        Integer orderId = 1;
        Order order = getDummyOrder(orderId);
        when(orderRepository.findByIdWithOrderProducts(orderId)).thenReturn(Optional.of(order));

        OrderDetailResponse response = orderService.findOne(orderId);

        verify(orderRepository, times(1)).findByIdWithOrderProducts(orderId);
        assertThat(response.getId()).isEqualTo(orderId);
    }

    @Test
    @DisplayName("유효하지 않은 주문 ID로 주문 상세 조회에 실패한다.")
    void findOneTest_Fail() {
        Integer orderId = 99;
        when(orderRepository.findByIdWithOrderProducts(orderId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.findOne(orderId), "invalid order id.");
        verify(orderRepository, times(1)).findByIdWithOrderProducts(orderId);
    }

    private Order getDummyOrder(int id) {
        Order order = Order.builder()
                .deliveryAddress(ORDER_DELIVERY_ADDRESS)
                .deliveryFee(ORDER_DELIVERY_FEE)
                .paymentAmount(ORDER_PAYMENT_AMOUNT)
                .recipientName(ORDER_RECIPIENT_NAME)
                .recipientContact(ORDER_RECIPIENT_CONTACT)
                .build();

        ReflectionTestUtils.setField(order, "id", id);
        return order;
    }

    private Product getDummyProduct(int id) {
        Product product = Product.builder().build();
        ReflectionTestUtils.setField(product, "id", id);
        return product;
    }

    private List<OrderProductRegisterParam> getDummyOrderProducts() {
        OrderProductRegisterParam productParam1 = new OrderProductRegisterParam();
        productParam1.setProductId(1);
        productParam1.setCount(2);

        OrderProductRegisterParam productParam2 = new OrderProductRegisterParam();
        productParam2.setProductId(2);
        productParam2.setCount(1);

        return Arrays.asList(productParam1, productParam2);
    }
}
