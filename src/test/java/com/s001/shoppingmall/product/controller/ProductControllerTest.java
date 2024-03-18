package com.s001.shoppingmall.product.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;

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

import com.s001.shoppingmall.product.dto.ProductDetailResponse;
import com.s001.shoppingmall.product.dto.ProductResponse;
import com.s001.shoppingmall.product.dto.ProductSearchCondition;
import com.s001.shoppingmall.product.service.ProductService;

@WebMvcTest(controllers = ProductController.class)
class ProductControllerTest {

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
		ResultActions resultActions = mvc.perform(get("/products/" + productId));

		// then
		resultActions
			.andExpect(status().isOk())
			.andDo(print())
			.andExpect(handler().methodName("detail"))
			.andExpect(model().attribute("product", response))
			.andExpect(view().name("product/detail"));

		verify(productService, times(1)).findOne(productId);
	}

	@Test
	@DisplayName("[GET][ERROR] 상품 상세 조회 / 유효하지 않는 상품 번호")
	void detailTest_Fail() throws Exception {
		// test code
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
		ResultActions resultActions = mvc.perform(get("/products"));

		// then
		resultActions
			.andExpect(status().isOk())
			.andDo(print())
			.andExpect(handler().methodName("list"))
			.andExpect(model().attribute("products", result))
			.andExpect(view().name("product/list"));

		verify(productService, times(1)).search(any(Pageable.class), any(ProductSearchCondition.class));
	}

}