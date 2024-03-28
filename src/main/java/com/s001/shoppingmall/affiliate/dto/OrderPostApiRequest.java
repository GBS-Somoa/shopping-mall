package com.s001.shoppingmall.affiliate.dto;

import com.s001.shoppingmall.order.entity.Order;
import com.s001.shoppingmall.order.entity.OrderProduct;
import com.s001.shoppingmall.product.entity.Product;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderPostApiRequest {

    private Integer groupId;
    private Integer userId;
    private String supplyId;
    private String orderStatus;
    private String productName;
    private String orderStore;
    private String orderStoreId;
    private String productImg;
    private int orderCount;
    private String orderAmount;

    public static OrderPostApiRequest of(Integer groupId, Integer userId, String supplyId, Order order) {
        OrderProduct orderProduct = order.getOrderProducts().get(0);
        Product product = orderProduct.getProduct();
        int orderCount = orderProduct.getCount();

        return OrderPostApiRequest.builder()
                .groupId(groupId)
                .userId(userId)
                .supplyId(supplyId)
                .orderStatus("배송중")
                .productName(product.getName())
                .productImg(product.getThumbnailImageUrl())
                .orderStore("SSAG")
                .orderStoreId(String.valueOf(order.getId()))
                .orderCount(orderCount)
                .orderAmount("1L")
                .build();
    }
}
