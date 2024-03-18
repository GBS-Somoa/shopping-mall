package com.s001.shoppingmall.product.controller;

import com.s001.shoppingmall.product.dto.ProductRegisterParam;
import com.s001.shoppingmall.product.dto.ProductSearchCondition;
import com.s001.shoppingmall.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/regist")
    public String registerForm(Model model) {
        model.addAttribute("productRegisterForm", new ProductRegisterParam());
        return "product/regist";
    }

    @PostMapping("/regist")
    public String regist(@ModelAttribute("productRegisterForm") ProductRegisterParam param) {
        Random random = new Random();
        param.setRating(random.nextInt(50) / (double) 10);
        param.setReviewCount(random.nextInt(1000));
        int productId = productService.save(param);
        return "redirect:/products/" + productId;
    }

    @GetMapping("/{productId}")
    public String detail(@PathVariable("productId") Integer productId,
                         Model model) {
        model.addAttribute("product", productService.findOne(productId));
        return "product/detail";
    }

    @GetMapping
    public String list(Pageable pageable,
                       ProductSearchCondition condition,
                       Model model) {
        model.addAttribute("products", productService.search(pageable, condition));
        return "product/list";
    }
}
