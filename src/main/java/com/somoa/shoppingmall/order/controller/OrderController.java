package com.somoa.shoppingmall.order.controller;

import com.somoa.shoppingmall.order.service.OrderService;
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
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/completed/{orderId}")
    public String orderCompleted(@PathVariable("orderId") Integer orderId, Model model) {
        model.addAttribute("order", orderService.findOne(orderId));
        return "order/order-completed";
    }

    @GetMapping("/{orderId}")
    public String detail(@PathVariable("orderId") Integer orderId, Model model) {
        model.addAttribute("order", orderService.findOne(orderId));
        return "order/detail";
    }

    @GetMapping
    public String list(@PageableDefault(sort = "orderDate", direction = Sort.Direction.DESC) Pageable pageable, Model model) {
        model.addAttribute("orders", orderService.findAll(pageable));
        return "order/list";
    }
}
