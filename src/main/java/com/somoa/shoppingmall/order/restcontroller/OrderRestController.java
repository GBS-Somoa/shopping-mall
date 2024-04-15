package com.somoa.shoppingmall.order.restcontroller;

import com.somoa.shoppingmall.order.dto.OrderRegisterParam;
import com.somoa.shoppingmall.order.dto.OrderUpdateParam;
import com.somoa.shoppingmall.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Slf4j
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderRestController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderRegisterParam param) {
        Integer orderId = 0;
        try {
            orderId = orderService.save(param);
        } catch (RuntimeException e) {
            ResponseEntity.internalServerError();
        }

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{orderId}")
                .buildAndExpand(orderId)
                .toUri();

        return ResponseEntity.created(location).body(orderId);
    }

    @PatchMapping("/{orderId}")
    public ResponseEntity<?> updateOrderStatus(@PathVariable("orderId") Integer orderId, @RequestBody OrderUpdateParam param) {
        orderService.update(orderId, param);
        return ResponseEntity.ok().build();
    }
}
