package com.s001.shoppingmall.product.dto;

import com.s001.shoppingmall.product.entity.Product;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ProductDetailResponse {

    private Integer id;
    private String name;
    private int price;
    private String imageUrl;
    private double rating;
    private int reviewCount;

    public static ProductDetailResponse of(Product product) {
        return ProductDetailResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .imageUrl(product.getImageUrl())
                .rating(product.getRating())
                .reviewCount(product.getReviewCount())
                .build();
    }
}
