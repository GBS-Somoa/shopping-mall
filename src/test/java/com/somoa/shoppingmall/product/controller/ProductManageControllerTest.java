package com.somoa.shoppingmall.product.controller;

import com.somoa.shoppingmall.product.dto.ProductDetailResponse;
import com.somoa.shoppingmall.product.dto.ProductRegisterParam;
import com.somoa.shoppingmall.product.dto.ProductResponse;
import com.somoa.shoppingmall.product.dto.ProductSearchCondition;
import com.somoa.shoppingmall.product.exception.DuplicateBarcodeException;
import com.somoa.shoppingmall.product.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ProductManageController.class)
class ProductManageControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ProductService productService;

    @Test
    @DisplayName("[GET] 상품 상세 조회")
    void detailTest() throws Exception {
        // given
        final Integer productId = 1;
        ProductDetailResponse response = new ProductDetailResponse(productId, "세탁세제", 10_000, null, 4.5, 10, "ABCD-1234");

        // mock
        when(productService.findOne(productId))
                .thenReturn(response);

        // when
        ResultActions resultActions = mvc.perform(get("/products/manage/" + productId));

        // then
        resultActions
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(handler().methodName("detail"))
                .andExpect(model().attribute("product", response))
                .andExpect(view().name("product/manage/detail"));

        verify(productService, times(1)).findOne(productId);
    }

    @Test
    @DisplayName("[GET] 상품 등록 페이지")
    void registerFormTest() throws Exception {
        // given

        // when
        ResultActions resultActions = mvc.perform(get("/products/manage/register"));

        // then
        resultActions
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(handler().methodName("registerForm"))
                .andExpect(model().attribute("productRegisterForm", new ProductRegisterParam()))
                .andExpect(view().name("product/manage/register"));
    }

    @Test
    @DisplayName("[POST] 상품 등록")
    void registerTest() throws Exception {
        // given
        final Integer productId = 1;

        ProductRegisterParam param = new ProductRegisterParam();
        param.setName("세탁세제");
        param.setBarcode("ABCD-1234");
        param.setPrice(10_000);

        // mock
        when(productService.save(any(ProductRegisterParam.class)))
                .thenReturn(productId);

        // when
        ResultActions resultActions = mvc.perform(post("/products/manage")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("name", param.getName())
                .param("barcode", param.getBarcode())
                .param("price", String.valueOf(param.getPrice())));

        // then
        resultActions
                .andExpect(status().is3xxRedirection())
                .andDo(print())
                .andExpect(handler().methodName("register"))
                .andExpect(redirectedUrl("/products/manage/" + productId));

        verify(productService, times(1)).save(any(ProductRegisterParam.class));
    }

    @Test
    @DisplayName("[POST][ERROR] 상품 등록 / 상품명 공백")
    void registerTest_Fail_NameBlank() throws Exception {
        // given
        ProductRegisterParam param = new ProductRegisterParam();
        param.setName(" ");
        param.setBarcode("ABCD-1234");
        param.setPrice(10_000);

        // test
        testProductRegisterForm(param, "name", "NotBlank");
        verify(productService, times(0)).save(any(ProductRegisterParam.class));
    }

    @Test
    @DisplayName("[POST][ERROR] 상품 등록 / 바코드 공백")
    void registerTest_Fail_BarcodeBlank() throws Exception {
        // given
        ProductRegisterParam param = new ProductRegisterParam();
        param.setName("세탁세제");
        param.setBarcode("");
        param.setPrice(10_000);

        // test
        testProductRegisterForm(param, "barcode", "NotBlank");
        verify(productService, times(0)).save(any(ProductRegisterParam.class));
    }

    @Test
    @DisplayName("[POST][ERROR] 상품 등록 / 음수 가격")
    void registerTest_Fail_PriceNegative() throws Exception {
        // given
        ProductRegisterParam param = new ProductRegisterParam();
        param.setName("세탁세제");
        param.setBarcode("ABCD-1234");
        param.setPrice(-500);

        // test
        testProductRegisterForm(param, "price", "Min");
        verify(productService, times(0)).save(any(ProductRegisterParam.class));
    }

    @Test
    @DisplayName("[POST][ERROR] 상품 등록 / 이미 등록된 바코드")
    void registerTest_Fail_DuplicateBarcode() throws Exception {
        // given
        ProductRegisterParam param = new ProductRegisterParam();
        param.setName("세탁세제");
        param.setBarcode("ABCD-1234");
        param.setPrice(10000);

        // mock
        when(productService.save(any(ProductRegisterParam.class)))
                .thenThrow(new DuplicateBarcodeException());

        // test
        testProductRegisterForm(param, "barcode", "Duplicate");
        verify(productService, times(1)).save(any(ProductRegisterParam.class));
    }

    @Test
    @DisplayName("[GET] 상품 목록 조회")
    void listTest() throws Exception {
        // given
        Page<ProductResponse> result = new PageImpl<>(new ArrayList<>(), PageRequest.of(0, 2), 10);

        // mock
        when(productService.search(any(Pageable.class), any(ProductSearchCondition.class)))
                .thenReturn(result);

        // when
        ResultActions resultActions = mvc.perform(get("/products/manage"));

        // then
        resultActions
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(handler().methodName("list"))
                .andExpect(model().attribute("products", result))
                .andExpect(view().name("product/manage/list"));

        verify(productService, times(1)).search(any(Pageable.class), any(ProductSearchCondition.class));
    }

    @Test
    @DisplayName("[DELETE] 상품 목록 조회")
    void deleteTest() throws Exception {
        // given
        final Integer productId = 1;

        // when
        ResultActions resultActions = mvc.perform(delete("/products/manage/" + productId));

        // then
        resultActions
                .andExpect(status().is3xxRedirection())
                .andDo(print())
                .andExpect(handler().methodName("delete"))
                .andExpect(redirectedUrl("/products/manage"));

        verify(productService, times(1)).delete(productId);
    }

    private void testProductRegisterForm(ProductRegisterParam param, String fieldName, String error) throws Exception {
        // when
        ResultActions resultActions = mvc.perform(post("/products/manage")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("name", param.getName())
                .param("barcode", param.getBarcode())
                .param("price", String.valueOf(param.getPrice())));

        // then
        resultActions
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(handler().methodName("register"))
                .andExpect(view().name("product/manage/register"))
                .andExpect(model().errorCount(1))
                .andExpect(model().attributeHasFieldErrors("productRegisterForm", fieldName))
                .andExpect(model().attributeHasFieldErrorCode("productRegisterForm", fieldName, error));
    }
}