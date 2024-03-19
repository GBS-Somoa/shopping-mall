package com.s001.shoppingmall.order.dto;

import com.s001.shoppingmall.order.entity.OrderProduct;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderProductDetailResponse {

    private Integer id;
    private Integer productId;
    private String name;
    private int price;
    private String imageUrl;
    private int count;

    public static OrderProductDetailResponse of(OrderProduct orderProduct) {
        return OrderProductDetailResponse.builder()
                .id(orderProduct.getId())
                .productId(orderProduct.getProduct().getId())
                .name(orderProduct.getProduct().getName())
                .price(orderProduct.getProduct().getPrice())
                .imageUrl(orderProduct.getProduct().getThumbnailImageUrl())
                .count(orderProduct.getCount())
                .build();
    }
}
