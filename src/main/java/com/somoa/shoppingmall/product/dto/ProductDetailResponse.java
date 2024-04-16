package com.somoa.shoppingmall.product.dto;

import com.somoa.shoppingmall.product.entity.Product;
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
    private String barcode;

    public static ProductDetailResponse of(Product product) {
        return ProductDetailResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .imageUrl(product.getImageUrl())
                .rating(product.getRating())
                .reviewCount(product.getReviewCount())
                .barcode(product.getBarcode())
                .build();
    }
}
