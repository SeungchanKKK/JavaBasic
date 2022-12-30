package com.sparta.delivery.dto.order.response;

import com.sparta.delivery.model.OrderItem;
import lombok.Getter;

@Getter
public class FoodResponseDto {
    private String name;
    private int quantity;
    private int price;

    public FoodResponseDto(OrderItem orderItem) {
        this.name = orderItem.getName();
        this.quantity = orderItem.getQuantity();
        this.price = orderItem.getPrice();
    }
}
