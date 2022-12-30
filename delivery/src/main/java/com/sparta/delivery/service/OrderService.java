package com.sparta.delivery.service;

import com.sparta.delivery.dto.order.response.FoodResponseDto;
import com.sparta.delivery.dto.order.OrderDto;
import com.sparta.delivery.dto.order.response.OrderResponseDto;
import com.sparta.delivery.model.Food;
import com.sparta.delivery.model.Orders;
import com.sparta.delivery.model.OrderItem;
import com.sparta.delivery.model.Restaurant;
import com.sparta.delivery.repository.FoodRepository;
import com.sparta.delivery.repository.OrderItemRepository;
import com.sparta.delivery.repository.OrderRepository;
import com.sparta.delivery.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final RestaurantRepository restaurantRepository;

    private final FoodRepository foodRepository;

    private final OrderItemRepository orderItemRepository;

    @Transactional
    public OrderResponseDto order(OrderDto orderDto) {
        Restaurant restaurant = getRestaurant(orderDto);

        int totalPrice = 0;
        List<FoodResponseDto> foodsResponseDtoList = new ArrayList<>();
        List<OrderItem> orderItems = orderDto.getFoods();
        List<OrderItem> orderItemList = new ArrayList<>();
        for (OrderItem tempOrderItem : orderItems) {
            //주문수량체크
            int quantity = tempOrderItem.getQuantity();
            if (quantity < 1 || quantity > 100) {
                throw new IllegalArgumentException("주문수랑은 1개에서 100개까지 입력해주세요");
            }

            Food food = getFood(tempOrderItem);
            //
            OrderItem orderItem = new OrderItem(tempOrderItem,food,quantity);
            orderItemRepository.save(orderItem);

            FoodResponseDto foodResponseDto = new FoodResponseDto(orderItem);
            foodsResponseDtoList.add(foodResponseDto);
            totalPrice += food.getPrice() * quantity;
            orderItemList.add(orderItem);
        }

        if (totalPrice < restaurant.getMinOrderPrice()) {
            throw new IllegalArgumentException("최소금액이상 주문해주세요!");
        }

        int deliveryFee = restaurant.getDeliveryFee();
        totalPrice += deliveryFee;
        Orders orders = new Orders(restaurant, totalPrice,  orderItemList );
        orderRepository.save(orders);
        OrderResponseDto responseDto = new OrderResponseDto(orders, deliveryFee, foodsResponseDtoList);
        return responseDto;
    }


    private Restaurant getRestaurant(OrderDto ordersRequestDto) {
        Restaurant restaurant = restaurantRepository.findById(ordersRequestDto.getRestaurantId())
                .orElseThrow(
                        () -> new NullPointerException("등록되지않은 식당입니다")
                );
        return restaurant;
    }

    private Food getFood(OrderItem tempOrderItem) {
        return foodRepository.findById(tempOrderItem.getId())
                .orElseThrow(() -> new NullPointerException("메뉴에 없는 음식입니다"));
    }

    //주문조회
    @Transactional
    public List<OrderResponseDto> findAllOrder() {
        List<OrderResponseDto> ordersResponseDtoList = new ArrayList<>();

        List<Orders> ordersList = orderRepository.findAll();

        for (Orders orders : ordersList) {
            int deliveryFee = restaurantRepository.findByName(orders.getRestaurantName()).getDeliveryFee();
            List<FoodResponseDto> foodsResponseDtoList = new ArrayList<>();


            List<OrderItem> orderItemsList  = orderItemRepository.findOrderItemsByOrders(orders);
            for (OrderItem orderItem : orderItemsList) {
                FoodResponseDto foodsResponseDto = new FoodResponseDto(orderItem);
                foodsResponseDtoList.add(foodsResponseDto);
            }

            OrderResponseDto ordersResponseDto = new OrderResponseDto(orders, deliveryFee, foodsResponseDtoList);
            ordersResponseDtoList.add(ordersResponseDto);
        }

        return ordersResponseDtoList;
    }
}
