package com.s001.shoppingmall.product.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProductTest {

    @Test
    void productBuilderTest() {
        Product product = Product.builder()
                .name("다우니")
                .price(30_000)
                .barcode("4902430341066")
                .reviewCount(0)
                .thumbnailImageUrl("thumbnail.jpg")
                .imageUrl("image.jpg")
                .rating(4.6)
                .build();

        assertThat(product.getName()).isEqualTo("다우니");
        assertThat(product.getPrice()).isEqualTo(30_000);
        assertThat(product.getBarcode()).isEqualTo("4902430341066");
        assertThat(product.getReviewCount()).isEqualTo(0);
        assertThat(product.getThumbnailImageUrl()).isEqualTo("thumbnail.jpg");
        assertThat(product.getImageUrl()).isEqualTo("image.jpg");
        assertThat(product.getRating()).isEqualTo(4.6);
    }
}
