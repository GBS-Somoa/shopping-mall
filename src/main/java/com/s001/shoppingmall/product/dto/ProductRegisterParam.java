package com.s001.shoppingmall.product.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProductRegisterParam {

    @NotBlank
    private String name;

    @Min(value = 0)
    @Max(value = 10_000_000)
    private int price;

    private String thumbnailImageUrl;
    private String imageUrl;

    private double rating;
    private int reviewCount;

    @NotBlank
    private String barcode;
}
