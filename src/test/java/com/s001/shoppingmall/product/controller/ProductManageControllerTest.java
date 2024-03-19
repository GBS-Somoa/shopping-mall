package com.s001.shoppingmall.product.controller;

import com.s001.shoppingmall.product.dto.ProductDetailResponse;
import com.s001.shoppingmall.product.dto.ProductRegisterParam;
import com.s001.shoppingmall.product.dto.ProductResponse;
import com.s001.shoppingmall.product.dto.ProductSearchCondition;
import com.s001.shoppingmall.product.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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
    @DisplayName("[GET][ERROR] 상품 상세 조회 / 유효하지 않는 상품 번호")
    void detailTest_Fail() throws Exception {
        // test code
    }

    @Test
    @DisplayName("[GET] 상품 등록 페이지")
    void registerFormTest() throws Exception {
        // given

        // when
        ResultActions resultActions = mvc.perform(get("/products/manage/regist"));

        // then
        resultActions
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(handler().methodName("registerForm"))
                .andExpect(model().attribute("productRegisterForm", new ProductRegisterParam()))
                .andExpect(view().name("product/manage/regist"));
    }

    @Test
    @DisplayName("[POST] 상품 등록")
    void registTest() throws Exception {
        // given
        final Integer productId = 1;

        // mock
        when(productService.save(any(ProductRegisterParam.class)))
                .thenReturn(productId);

        // when
        ResultActions resultActions = mvc.perform(post("/products/manage"));

        // then
        resultActions
                .andExpect(status().is3xxRedirection())
                .andDo(print())
                .andExpect(handler().methodName("regist"))
                .andExpect(redirectedUrl("/products/manage/" + productId));

        verify(productService, times(1)).save(any(ProductRegisterParam.class));
    }

    @Test
    @DisplayName("[POST][ERROR] 상품 등록 / 검증 실패")
    void registTest_Fail() throws Exception {

    }

    @Test
    @DisplayName("[GET] 상품 목록 조회")
    void listTest() throws Exception {
        // given
        Page<ProductResponse> result = new PageImpl<>(new ArrayList<>());

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

    @Test
    @DisplayName("[DELETE][ERROR] 상품 목록 조회 / 유효하지 않는 상품 번호")
    void deleteTest_Fail() throws Exception {

    }
}