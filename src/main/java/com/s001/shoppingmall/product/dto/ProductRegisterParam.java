package com.s001.shoppingmall.product.dto;

import lombok.Data;

@Data
public class ProductRegisterParam {

    private String name;
    private int price;
    private String thumbnailImageUrl;
    private String imageUrl;
    private double rating;
    private int reviewCount;
    private String barcode;
}
