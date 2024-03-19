package com.s001.shoppingmall.common.configuration;

import com.s001.shoppingmall.order.dto.OrderProductRegisterParam;
import com.s001.shoppingmall.order.dto.OrderRegisterParam;
import com.s001.shoppingmall.order.service.OrderService;
import com.s001.shoppingmall.product.dto.ProductRegisterParam;
import com.s001.shoppingmall.product.service.ProductService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Profile("local")
@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final ProductService productService;
        private final OrderService orderService;

        public void dbInit() {
            int productCount = 500;

            for (int i = 0; i < productCount; ++i) {
                ProductRegisterParam param = new ProductRegisterParam();
                param.setName("상품 " + i);
                param.setBarcode("ABCD-" + i);
                param.setPrice(10000);
                param.setRating(i % 5);
                param.setReviewCount(i);
                productService.save(param);
            }
            int orderCount = 500;
            for (int i = 0; i < orderCount; ++i) {
                OrderRegisterParam param = new OrderRegisterParam();
                param.setRecipientName("양유경");
                param.setDeliveryAddress("싸피");
                param.setDeliveryFee(3000);
                param.setRecipientContact("010-1234-5678");
                OrderProductRegisterParam productParam = new OrderProductRegisterParam();
                productParam.setProductId(1);
                productParam.setCount(2);
                ArrayList<OrderProductRegisterParam> list = new ArrayList<>();
                list.add(productParam);
                param.setOrderProducts(list);
                orderService.save(param);
            }
        }
    }
}