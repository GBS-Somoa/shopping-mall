package com.somoa.shoppingmall.product.dto;

import com.somoa.shoppingmall.product.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ProductResponse {

    private Integer id;
    private String name;
    private int price;
    private String thumbnailImageUrl;
    private double rating;
    private int reviewCount;

    public static ProductResponse of(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .thumbnailImageUrl(product.getThumbnailImageUrl())
                .rating(product.getRating())
                .reviewCount(product.getReviewCount())
                .build();
    }
}
