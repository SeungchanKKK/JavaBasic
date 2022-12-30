package com.sparta.delivery.service;

import com.sparta.delivery.dto.RestaurantDto;
import com.sparta.delivery.model.Restaurant;
import com.sparta.delivery.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public Restaurant addRestaurant(RestaurantDto requestDto) {
         int minOrderPrice =requestDto.getMinOrderPrice();
         int deliveryFee = requestDto.getDeliveryFee();

        if(minOrderPrice>100000 || minOrderPrice<999){
                throw new IllegalArgumentException("1000원에서 100000원사이로 입력해주세요");
            }

        if(minOrderPrice%100>0){
                throw new IllegalArgumentException("100원단위로 입력해주세요");
            }

        if(0 > deliveryFee || deliveryFee > 10000){
                throw new IllegalArgumentException("0원에서 10000원까지 입력해주세요");
            }
        if(deliveryFee % 500 > 0){
            throw new IllegalArgumentException("500원단위로 입력해주세요");
        }

        Restaurant restaurant = new Restaurant(requestDto);

        restaurantRepository.save(restaurant);

        return restaurant;
    }

    @Transactional
    public List<Restaurant> findAllRestaurant() {
        return restaurantRepository.findAll();
    }
}
