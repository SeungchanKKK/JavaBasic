package com.sparta.delivery.controller;

import com.sparta.delivery.dto.FoodDto;
import com.sparta.delivery.model.Food;
import com.sparta.delivery.service.Foodservice;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class FoodController {
    private final Foodservice foodservice;

    @PostMapping("/restaurant/{restaurantId}/food/register")
    public void addFood(
            @PathVariable Long restaurantId, @RequestBody List<FoodDto> requestDtoList){
        foodservice.addFoods(restaurantId,requestDtoList);
    }

    @GetMapping("/restaurant/{restaurantId}/foods")
    public List<Food> findAllRestaurantFoods(
            @PathVariable Long restaurantId) {
        return foodservice.findAllRestaurantFoods(restaurantId);
    }
}
