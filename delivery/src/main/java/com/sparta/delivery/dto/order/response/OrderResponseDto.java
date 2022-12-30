package com.sparta.delivery.dto.order.response;


import com.sparta.delivery.model.Orders;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderResponseDto {
    private String restaurantName;
    private List<FoodResponseDto> foods;
    private int deliveryFee;
    private int totalPrice;


    public OrderResponseDto(Orders orders, int deliveryFee, List<FoodResponseDto> foods) {
        this.restaurantName = orders.getRestaurantName();
        this.foods = foods;
        this.deliveryFee = deliveryFee;
        this.totalPrice = orders.getTotalPrice();
    }
}