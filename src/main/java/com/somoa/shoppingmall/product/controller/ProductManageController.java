package com.somoa.shoppingmall.product.controller;

import com.somoa.shoppingmall.product.dto.ProductRegisterParam;
import com.somoa.shoppingmall.product.dto.ProductSearchCondition;
import com.somoa.shoppingmall.product.exception.DuplicateBarcodeException;
import com.somoa.shoppingmall.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/products/manage")
public class ProductManageController {

    private final ProductService productService;

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("productRegisterForm", new ProductRegisterParam());
        return "product/manage/register";
    }

    @PostMapping
    public String register(@Valid @ModelAttribute("productRegisterForm") ProductRegisterParam param,
                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "product/manage/register";
        }
        Random random = new Random();
        param.setRating(random.nextInt(50) / (double) 10);
        param.setReviewCount(random.nextInt(1000));

        try {
            int productId = productService.save(param);
            return "redirect:/products/manage/" + productId;
        } catch (DuplicateBarcodeException e) {
            bindingResult.rejectValue("barcode", "Duplicate", "이미 등록된 바코드입니다.");
        }
        return "product/manage/register";
    }

    @GetMapping("/{productId}")
    public String detail(@PathVariable("productId") Integer productId,
                         Model model) {
        model.addAttribute("product", productService.findOne(productId));
        return "product/manage/detail";
    }

    @GetMapping
    public String list(@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                       ProductSearchCondition condition,
                       Model model) {
        model.addAttribute("products", productService.search(pageable, condition));
        return "product/manage/list";
    }

    @DeleteMapping("/{productId}")
    public String delete(@PathVariable("productId") Integer productId) {
        productService.delete(productId);
        return "redirect:/products/manage";
    }
}
