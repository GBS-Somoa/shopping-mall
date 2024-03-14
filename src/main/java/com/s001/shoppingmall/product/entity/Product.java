package com.s001.shoppingmall.product.entity;

import com.s001.shoppingmall.common.entity.BaseEntity;
import com.s001.shoppingmall.order.entity.OrderProduct;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

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
    private int rating;

    @Column(name = "product_comment_count", nullable = false)
    private int commentCount;

    @Column(name = "product_barcode", unique = true)
    private String barcode;

    @OneToMany(mappedBy = "product")
    private List<OrderProduct> orderProducts;

    @Builder
    public Product(String name, int price, String thumbnailImageUrl, String imageUrl, int rating, int commentCount, String barcode) {
        this.name = name;
        this.price = price;
        this.thumbnailImageUrl = thumbnailImageUrl;
        this.imageUrl = imageUrl;
        this.rating = rating;
        this.commentCount = commentCount;
        this.barcode = barcode;
    }
}
