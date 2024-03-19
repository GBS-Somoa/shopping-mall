package com.s001.shoppingmall.product.service;

import com.s001.shoppingmall.product.dto.ProductDetailResponse;
import com.s001.shoppingmall.product.dto.ProductRegisterParam;
import com.s001.shoppingmall.product.dto.ProductResponse;
import com.s001.shoppingmall.product.dto.ProductSearchCondition;
import com.s001.shoppingmall.product.entity.Product;
import com.s001.shoppingmall.product.exception.DuplicateBarcodeException;
import com.s001.shoppingmall.product.exception.ProductNotFoundException;
import com.s001.shoppingmall.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
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

    public Page<ProductResponse> search(Pageable pageable, ProductSearchCondition searchCondition) {
        String keyword = searchCondition.getKeyword();
        Page<Product> page;
        if (Objects.nonNull(keyword) && !keyword.isBlank()) {
            page = productRepository.findAllByNameContaining(pageable, keyword);
        } else {
            page = productRepository.findAll(pageable);
        }
        return new PageImpl<>(page.getContent()
            .stream()
            .map(ProductResponse::of)
            .toList(), page.getPageable(), page.getTotalElements());
    }

    public ProductDetailResponse findOne(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);
        return ProductDetailResponse.of(product);
    }

    @Transactional
    public void delete(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);
        productRepository.delete(product);
    }

    private void validateDuplicateBarcode(String barcode) {
        productRepository.findByBarcode(barcode).ifPresent(action -> {
            throw new DuplicateBarcodeException();
        });
    }
}
