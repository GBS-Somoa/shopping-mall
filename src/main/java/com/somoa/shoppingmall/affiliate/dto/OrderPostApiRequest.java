package com.somoa.shoppingmall.affiliate.dto;

import com.somoa.shoppingmall.order.entity.Order;
import com.somoa.shoppingmall.order.entity.OrderProduct;
import com.somoa.shoppingmall.product.entity.Product;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderPostApiRequest {

    private Integer groupId;
    private String userId;
    private String supplyId;
    private String orderStatus;
    private String orderStore;
    private String orderStoreId;
    private int orderCount;
    private String productName;
    private String productImg;
    private String productBarcode;

    public static OrderPostApiRequest of(Integer groupId, String userId, String supplyId, Order order) {
        OrderProduct orderProduct = order.getOrderProducts().get(0);
        Product product = orderProduct.getProduct();
        int orderCount = orderProduct.getCount();

        return OrderPostApiRequest.builder()
                .groupId(groupId)
                .userId(userId)
                .supplyId(supplyId)
                .orderStatus("주문 완료")
                .orderStore("SSAG")
                .orderStoreId(String.valueOf(order.getId()))
                .orderCount(orderCount)
                .productName(product.getName())
                .productImg(product.getImageUrl())
                .productBarcode(product.getBarcode())
                .build();
    }
}
