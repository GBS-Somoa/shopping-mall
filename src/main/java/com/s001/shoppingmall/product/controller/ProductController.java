package com.s001.shoppingmall.product.controller;

import com.s001.shoppingmall.product.dto.ProductSearchCondition;
import com.s001.shoppingmall.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/{productId}")
    public String detail(@PathVariable("productId") Integer productId,
                         Model model) {
        model.addAttribute("product", productService.findOne(productId));
        return "product/detail";
    }

    @GetMapping
    public String list(@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                       ProductSearchCondition condition,
                       Model model) {
        model.addAttribute("products", productService.search(pageable, condition));
        return "product/list";
    }
}
