package com.s001.shoppingmall.product.service;

import com.s001.shoppingmall.product.dto.ProductDetailResponse;
import com.s001.shoppingmall.product.dto.ProductRegisterParam;
import com.s001.shoppingmall.product.dto.ProductSearchCondition;
import com.s001.shoppingmall.product.entity.Product;
import com.s001.shoppingmall.product.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(SpringExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private static final String PRODUCT_NAME = "세탁세제9";
    private static final int PRODUCT_PRICE = 10000;
    private static final String PRODUCT_BARCODE = "ABCD-1234";
    private static final double PRODUCT_RATING = 3.4;
    private static final int PRODUCT_REVIEW_COUNT = 5;
    private static final String PRODUCT_THUMBNAIL_IMAGE_URL = "thumbnailImageUrl";
    private static final String PRODUCT_IMAGE_URL = "imageUrl";

    @Test
    @DisplayName("상품 등록에 성공한다.")
    void saveTest() throws Exception {
        // given
        ProductRegisterParam param = new ProductRegisterParam();
        param.setName(PRODUCT_NAME);
        param.setPrice(PRODUCT_PRICE);
        param.setBarcode(PRODUCT_BARCODE);

        // mock
        Integer fakeId = 1;
        Product product = getDummyProduct();
        ReflectionTestUtils.setField(product, "id", fakeId);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        // when
        Integer savedId = productService.save(param);

        // then
        verify(productRepository, times(1)).save(any(Product.class));
        assertThat(savedId).isEqualTo(fakeId);
    }

    @Test
    @DisplayName("중복된 바코드를 가진 상품이 존재하면 상품 등록에 실패한다.")
    void saveTest_Fail() throws Exception {
        // given
        ProductRegisterParam param = new ProductRegisterParam();
        param.setBarcode(PRODUCT_BARCODE);

        // mock
        Product duplicateBarcodeProduct = Product.builder()
                .name("이미 등록된 제품")
                .barcode(PRODUCT_BARCODE)
                .build();
        when(productRepository.findByBarcode("ABCD-1234")).thenReturn(Optional.of(duplicateBarcodeProduct));

        // assert
        assertThatThrownBy(() -> productService.save(param))
                .isInstanceOf(IllegalArgumentException.class);

        // then
        verify(productRepository, times(1)).findByBarcode("ABCD-1234");
        verify(productRepository, times(0)).save(any(Product.class));
    }

    @Test
    @DisplayName("상품 검색 시 검색 조건 키워드가 있으면 findAllByNameContaining 메서드로 조회한다.")
    void findAllTest() throws Exception {
        // given
        final String keyword = "세탁세제";
        ProductSearchCondition condition = new ProductSearchCondition();
        condition.setKeyword(keyword);

        // mock
        when(productRepository.findAllByNameContaining(any(Pageable.class), any(String.class)))
            .thenReturn(new PageImpl<>(new ArrayList<>()));

        // when
        productService.search(PageRequest.of(0, 2), condition);

        // then
        verify(productRepository, times(1)).findAllByNameContaining(any(Pageable.class), eq(keyword));
        verify(productRepository, times(0)).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("상품 검색 시 검색 조건 키워드가 null이거나 공백으로만 이루어진 문자열이면 findAll 메서드로 조회한다.")
    void findAllTest2() throws Exception {
        // given
        ProductSearchCondition condition1 = new ProductSearchCondition();
        condition1.setKeyword(null);
        ProductSearchCondition condition2 = new ProductSearchCondition();
        condition2.setKeyword("");
        ProductSearchCondition condition3 = new ProductSearchCondition();
        condition2.setKeyword(" ");

        // mock
        when(productRepository.findAll(any(Pageable.class)))
            .thenReturn(new PageImpl<>(new ArrayList<>()));

        // when
        productService.search(PageRequest.of(0, 2), condition1);
        productService.search(PageRequest.of(0, 2), condition2);
        productService.search(PageRequest.of(0, 2), condition3);

        // then
        verify(productRepository, times(0)).findAllByNameContaining(any(Pageable.class), anyString());
        verify(productRepository, times(3)).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("상품 상세 조회에 성공한다.")
    void findOneTest() throws Exception {
        // given
        Integer productId = 1;

        // mock
        Product product = getDummyProduct();
        ReflectionTestUtils.setField(product, "id", productId);
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // when
        ProductDetailResponse result = productService.findOne(productId);

        // then
        verify(productRepository, times(1)).findById(productId);
        assertThat(result.getId()).isEqualTo(productId);
        assertThat(result.getName()).isEqualTo(PRODUCT_NAME);
        assertThat(result.getPrice()).isEqualTo(PRODUCT_PRICE);
        assertThat(result.getRating()).isEqualTo(PRODUCT_RATING);
        assertThat(result.getReviewCount()).isEqualTo(PRODUCT_REVIEW_COUNT);
        assertThat(result.getImageUrl()).isEqualTo(PRODUCT_IMAGE_URL);
    }

    @Test
    @DisplayName("상세 조회 시 잘못된 상품 번호를 제공하면 조회에 실패한다.")
    void findOneTest_Fail() throws Exception {
        // given
        Integer productId = 99;

        // mock
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // assert
        assertThatThrownBy(() -> productService.findOne(productId))
                .isInstanceOf(IllegalArgumentException.class);

        // then
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    @DisplayName("상품 삭제에 성공한다.")
    void deleteTest() throws Exception {
        // given
        Integer productId = 1;

        // mock
        Product product = getDummyProduct();
        ReflectionTestUtils.setField(product, "id", productId);
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // when
        productService.delete(productId);

        // then
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).delete(any());
    }

    @Test
    @DisplayName("상세 삭제 시 잘못된 상품 번호를 제공하면 조회에 실패한다.")
    void deleteTest_Fail() throws Exception {
        // given
        Integer productId = 99;

        // mock
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // assert
        assertThatThrownBy(() -> productService.findOne(productId))
                .isInstanceOf(IllegalArgumentException.class);

        // then
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(0)).delete(any());
    }

    private Product getDummyProduct() {
        return Product.builder()
                .name(PRODUCT_NAME)
                .price(PRODUCT_PRICE)
                .barcode(PRODUCT_BARCODE)
                .rating(PRODUCT_RATING)
                .reviewCount(PRODUCT_REVIEW_COUNT)
                .thumbnailImageUrl(PRODUCT_THUMBNAIL_IMAGE_URL)
                .imageUrl(PRODUCT_IMAGE_URL)
                .build();
    }
}
