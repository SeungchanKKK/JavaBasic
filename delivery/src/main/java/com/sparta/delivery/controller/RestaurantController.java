package com.sparta.delivery.controller;

import com.sparta.delivery.dto.RestaurantDto;
import com.sparta.delivery.model.Restaurant;
import com.sparta.delivery.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class RestaurantController {

    private final RestaurantService restaurantService;

    //레스토랑 추가
    @PostMapping("/restaurant/register")
    public Restaurant addrestaurant(@RequestBody RestaurantDto requestDto){
        return restaurantService.addRestaurant(requestDto);
    }

    @GetMapping("/restaurants")
    public List<Restaurant> findAllRestaurant() {
        return restaurantService.findAllRestaurant();
    }
}
