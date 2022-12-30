package com.sparta.delivery.service;

import com.sparta.delivery.dto.FoodDto;
import com.sparta.delivery.model.Food;
import com.sparta.delivery.model.Restaurant;
import com.sparta.delivery.repository.FoodRepository;
import com.sparta.delivery.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class Foodservice {

    private final RestaurantRepository restaurantRepository;

    private final FoodRepository foodRepository;

    @Transactional
    public void addFoods(Long restaurantId, List<FoodDto> requestDtoList) {
        //레스토랑 등록확인
        Restaurant foundRestaurant = restaurantRepository.findById(restaurantId).orElseThrow(() -> new IllegalArgumentException("존재하지않는 식당입니다"));
        //레스토랑 객체만들기
        Restaurant restaurant = foundRestaurant;

        for (FoodDto requestDto : requestDtoList) {
            String foodName = requestDto.getName();
            int foodPrice = requestDto.getPrice();

            //메뉴증복확인
            Optional<Food> same = foodRepository.findFoodByRestaurantAndName(restaurant,foodName);
            if(same.isPresent())
                throw new IllegalArgumentException("이미 존재하는메뉴입니다");

            //메뉴 가격조건
            if (foodPrice < 100)
                throw new IllegalArgumentException("100원이상 기재해주세요");

            if (foodPrice > 1_000_000)
                throw new IllegalArgumentException("1,000,000원이하로 기재해주세요");

            if (foodPrice % 100 > 0)
                throw new IllegalArgumentException("100원단위로 기재해주세요");

            Food food= new Food(requestDto,restaurant);
            foodRepository.save(food);
        }
    }

    public List<Food> findAllRestaurantFoods(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(
                        () -> new NullPointerException("없는식당입니다"));

        return foodRepository.findFoodsByRestaurant(restaurant);
    }
}
