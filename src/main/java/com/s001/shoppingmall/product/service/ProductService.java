package com.s001.shoppingmall.product.service;

import com.s001.shoppingmall.product.dto.ProductDetailResponse;
import com.s001.shoppingmall.product.dto.ProductRegisterParam;
import com.s001.shoppingmall.product.dto.ProductSearchCondition;
import com.s001.shoppingmall.product.entity.Product;
import com.s001.shoppingmall.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    public Integer save(ProductRegisterParam param) {
        validateDuplicateBarcode(param.getBarcode());
        Product product = productRepository.save(Product.builder()
                .name(param.getName())
                .price(param.getPrice())
                .imageUrl(param.getImageUrl())
                .thumbnailImageUrl(param.getThumbnailImageUrl())
                .reviewCount(param.getReviewCount())
                .rating(param.getRating())
                .barcode(param.getBarcode())
                .build());
        return product.getId();
    }

    public Page<Product> search(Pageable pageable, ProductSearchCondition searchCondition) {
        String keyword = searchCondition.getKeyword();
        if (Objects.nonNull(keyword) && !keyword.isBlank()) {
            return productRepository.findAllByNameContaining(pageable, keyword);
        } else {
            return productRepository.findAll(pageable);
        }
    }

    public ProductDetailResponse findOne(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("invalid product id."));
        return ProductDetailResponse.of(product);
    }

    @Transactional
    public void delete(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("invalid product id."));
        productRepository.delete(product);
    }

    private void validateDuplicateBarcode(String barcode) {
        productRepository.findByBarcode(barcode).ifPresent(action -> {
            throw new IllegalArgumentException("duplicate barcode.");
        });
    }
}
