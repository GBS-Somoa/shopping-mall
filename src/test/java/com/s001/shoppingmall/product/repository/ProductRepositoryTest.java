package com.s001.shoppingmall.product.repository;

import com.s001.shoppingmall.product.dto.ProductSearchCondition;
import com.s001.shoppingmall.product.entity.Product;
import org.assertj.core.api.Assert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void findAllByNameContaining() {
        // given
        String[] productNames = {"세탁세제1", "세탁세제2", "세탁세제3", "섬유유연제1", "섬유유연제2", "식기세척기세제1", "식기세척기세제2"};
        for (String productName: productNames) {
            productRepository.save(Product.builder()
                    .name(productName)
                    .price(0)
                    .rating(0)
                    .reviewCount(0)
                    .build());
        }

        final String keyword = "세탁세제";
        Pageable pageable;

        // when
        pageable = PageRequest.of(0, 2);
        Page<Product> result1 = productRepository.findAllByNameContaining(pageable, keyword);

        pageable = PageRequest.of(1, 2);
        Page<Product> result2 = productRepository.findAllByNameContaining(pageable, keyword);

        // then
        assertThat(result1.getNumber()).isEqualTo(0);
        assertThat(result1.getSize()).isEqualTo(2);
        assertThat(result1.getTotalPages()).isEqualTo(2);
        assertThat(result1.getNumberOfElements()).isEqualTo(2);
        assertThat(result1.isFirst()).isTrue();
        assertThat(result1.hasNext()).isTrue();
        assertThat(result1.isLast()).isFalse();
        assertThat(result1.getContent().stream().map(Product::getName).toList())
                .containsExactly("세탁세제1", "세탁세제2");

        assertThat(result2.getNumber()).isEqualTo(1);
        assertThat(result2.getSize()).isEqualTo(2);
        assertThat(result2.getTotalPages()).isEqualTo(2);
        assertThat(result2.getNumberOfElements()).isEqualTo(1);
        assertThat(result2.isFirst()).isFalse();
        assertThat(result2.hasNext()).isFalse();
        assertThat(result2.isLast()).isTrue();
        assertThat(result2.getContent().stream().map(Product::getName).toList())
                .containsExactly("세탁세제3");
    }
}