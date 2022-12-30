package com.sparta.delivery.controller;

import com.sparta.delivery.dto.order.OrderDto;
import com.sparta.delivery.dto.order.response.OrderResponseDto;
import com.sparta.delivery.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/order/request")
    public OrderResponseDto orderFood(
            @RequestBody OrderDto orderDto
    ) {
        return orderService.order(orderDto);
    }

    @GetMapping("/orders")
    public List<OrderResponseDto> findAllOrder() {
        return orderService.findAllOrder();
    }
}
