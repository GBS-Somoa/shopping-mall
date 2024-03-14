package com.s001.shoppingmall.product.entity;

import com.s001.shoppingmall.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Integer id;

    @Column(name = "product_name", nullable = false)
    private String name;

    @Column(name = "product_price", nullable = false)
    private int price;

    @Column(name = "product_thumbnail_image_url")
    private String thumbnailImageUrl;

    @Column(name = "product_image_url")
    private String imageUrl;

    @Column(name = "product_rating", nullable = false)
    private double rating;

    @Column(name = "product_comment_count", nullable = false)
    private int reviewCount;

    @Column(name = "product_barcode", unique = true)
    private String barcode;

    @Builder
    public Product(String name, int price, String thumbnailImageUrl, String imageUrl, double rating, int reviewCount, String barcode) {
        this.name = name;
        this.price = price;
        this.thumbnailImageUrl = thumbnailImageUrl;
        this.imageUrl = imageUrl;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.barcode = barcode;
    }
}
