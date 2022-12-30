package com.sparta.delivery.dto.order;

import com.sparta.delivery.model.OrderItem;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderDto {
    private Long restaurantId;
    private List<OrderItem> foods;
}